package io.lozzikit.servicestatus.server.steps.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.lozzikit.servicestatus.api.ServiceApi;
import io.lozzikit.servicestatus.api.dto.NewService;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.lozzikit.servicestatus.server.steps.integration.Utils.ping;
import static org.junit.Assert.assertTrue;

public class ServiceSchedulingStep {

    private final String SERVICE_SERVER_URL = "http://localhost:8080/api";

    private ServiceApi serviceApi;
    private WireMockServer wireMockServer;

    private List<NewService> services = new ArrayList<>();

    public ServiceSchedulingStep(){
        serviceApi = new ServiceApi();
        serviceApi.getApiClient().setBasePath(SERVICE_SERVER_URL);

        if(wireMockServer!=null)
            wireMockServer.stop();

        wireMockServer = new WireMockServer(wireMockConfig().port(Utils.getPortFromURL(SERVICE_SERVER_URL)+1));
        wireMockServer.start();
    }

    @Given("^I have a service Server running")
    public void iHaveAServiceServer() throws Throwable {
        assertTrue(ping(serviceApi.getApiClient().getHttpClient(),SERVICE_SERVER_URL));
    }


    @Given("^I have the following services:$")
    public void iHaveTheFollowingServices(DataTable table) throws Throwable {
        table.asMaps(String.class, String.class).forEach(map->{
            NewService service = new NewService();
            service.setName(map.get("name"));
            service.setDescription(map.get("description"));
            service.setInterval(Integer.valueOf(map.get("interval")));
            service.setUrl(map.get("url"));
            service.setPort(Utils.getPortFromURL(map.get("url")));
            services.add(service);
        });

        services.forEach(s -> {
            String url = s.getUrl();
            wireMockServer.stubFor(
                    get(urlEqualTo("/"+url.split("/")[3]))
                    .willReturn(
                            aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "text/plain")
                    )
            );
        });
    }

    @And("^they're added to the server for tracking$")
    public void theyReAddedToTheServerForTracking() throws Throwable {
        for (NewService s : services) {
            serviceApi.addService(s);
        }
    }

    @Then("^if I wait the correct amount of time$")
    public void ifIWaitTheCorrectAmountOfTime() throws Throwable {
        int max = services.stream()
                .mapToInt(NewService::getInterval)
                .reduce(Integer::max)
                .orElse(0);

        Thread.sleep(max*1500);
    }

    @Then("^they're supposed to have been contacted$")
    public void theyReSupposedToHaveBeenContacted() throws Throwable {
        services.forEach(s ->
                wireMockServer.verify(
                        getRequestedFor(
                                urlEqualTo("/"+s.getUrl().split("/")[3]))
                                .withHeader("Accept",equalTo("application/json"))));
    }
}
