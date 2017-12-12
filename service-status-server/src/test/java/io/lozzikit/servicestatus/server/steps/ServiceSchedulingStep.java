package io.lozzikit.servicestatus.server.steps;

import com.github.tomakehurst.wiremock.WireMockServer;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.lozzikit.servicestatus.checker.ServiceStatusChecker;
import io.lozzikit.servicestatus.entities.ServiceEntity;
import org.quartz.JobKey;
import org.quartz.Trigger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

public class ServiceSchedulingStep {

    private final int PORT = 8080;

    private WireMockServer mockServer;
    private ServiceStatusChecker serviceStatusChecker;
    private List<ServiceEntity> services;
    private Map<ServiceEntity, String> endpoints = new HashMap<>();
    private Map<ServiceEntity, Integer> responses = new HashMap<>();
    private Map<String, Integer> delays = new HashMap<>();

    @Given("^there is a Service server$")
    public void thereIsAServiceServer()  {
        mockServer = new WireMockServer(PORT);
    }

    @And("^the server is up and running$")
    public void theServerIsUpAndRunning() {
        mockServer.start();
    }


    @And("^I have a service checker$")
    public void iHaveAServiceChecker() throws Throwable {
        serviceStatusChecker = new ServiceStatusChecker();
    }

    @And("^I have multiples services:$")
    public void iHaveMultiplesServices(DataTable table) throws Throwable {
        services = new ArrayList<>();
        table.asMaps(String.class, String.class).forEach(map -> {
            ServiceEntity service = new ServiceEntity();
            service.setId(UUID.randomUUID());
            service.setName("Service n. "+map.get("id"));
            service.setDescription(service.getName()+" used for testing");
            service.setInterval(Integer.parseInt(map.get("delay")));
            service.setStatuses(new ArrayList<>());

            String endpoint = map.get("url");

            try {
                URL url = new URL("http","localhost",PORT, endpoint);
                service.setUrl(url.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            services.add(service);
            endpoints.put(service,endpoint);
            responses.put(service, Integer.valueOf(map.get("response")));
            delays.put(service.getName(),service.getInterval());
        });
    }

    @And("^the server is setup to answer accordingly$")
    public void theServerIsSetupToAnswerAccordingly() throws Throwable {
        services.forEach(service -> {
            mockServer
                    .stubFor(get(urlEqualTo(endpoints.get(service)))
                    .willReturn(aResponse().withStatus(responses.get(service))));
        });
    }

    @When("^the service checker scheduled all tasks$")
    public void theServiceCheckerScheduledAllTasks() throws Throwable {
        serviceStatusChecker.scheduleAll(services);
    }

    @Then("^each task shall be scheduled correctly$")
    public void eachTaskShallBeScheduledCorrectly() throws Throwable {
        assertEquals(services.size(),serviceStatusChecker.getScheduledTasks().size());
    }

    @And("^each task shall be executed after their delay$")
    public void eachTaskShallBeExecutedAfterTheirDelay() throws Throwable {
        List<JobKey> tasks = serviceStatusChecker.getScheduledTasks();

        for(JobKey task : tasks){
            List<Trigger> triggers = (List<Trigger>) serviceStatusChecker.getScheduler().getTriggersOfJob(task);
            Date nextFireTime = triggers.get(0).getFireTimeAfter(new Date());
            long diff = nextFireTime.getTime() - System.currentTimeMillis();
            long diffMinutes = (long) (diff / (60.0 * 1000) % 60)+1;

            assertEquals( delays.get(task.getName()).intValue(), diffMinutes);
        }

    }


    @Then("^the server can be shutdown$")
    public void theServerCanBeShutdown() throws Throwable {
        mockServer.stop();
    }
}
