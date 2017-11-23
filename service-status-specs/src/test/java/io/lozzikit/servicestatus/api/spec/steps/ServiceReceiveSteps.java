package io.lozzikit.servicestatus.api.spec.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.lozzikit.servicestatus.api.dto.NewService;
import io.lozzikit.servicestatus.api.dto.Service;
import io.lozzikit.servicestatus.api.spec.helpers.Environment;
import io.lozzkit.servicestatus.ApiException;
import io.lozzkit.servicestatus.ApiResponse;
import io.lozzkit.servicestatus.api.ServiceApi;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class ServiceReceiveSteps {

    private Environment environment;
    private ServiceApi api;

    private Service lastReceivedService;
    private List<Service> lastReceivedServiceList;

    public ServiceReceiveSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
    }


    @When("^I send a GET request to the /service/id endpoint$")
    public void iSendAGETRequestToTheServiceIdEndpoint() throws Throwable {
        try {
            lastReceivedService = api.getService(environment.getServiceUUID(), "history");
            environment.setLastApiCallThrewException(false);
            environment.setLastApiException(null);
            environment.setLastStatusCode(environment.getLastApiResponse().getStatusCode());
            String location = String.valueOf(environment.getLastApiResponse().getHeaders().get("Location"));
            environment.setServiceUUID(location.substring(location.lastIndexOf('/')+1, location.length()-1));
        } catch (ApiException e) {
            environment.setLastApiCallThrewException(true);
            environment.setLastApiResponse(null);
            environment.setLastApiException(e);
            environment.setLastStatusCode(environment.getLastApiException().getCode());
        }

    }


    @Then("^I receive a payload containing the Service$")
    public void iReceiveAPayloadContainingTheService() throws Throwable {
        assertNotNull(lastReceivedService);
    }

    @When("^I send a GET request to the /services endpoint$")
    public void iSendAGETRequestToTheServicesEndpoint() throws Throwable {
        try {
            lastReceivedServiceList = api.getServices("history");
            environment.setLastApiCallThrewException(false);
            environment.setLastApiException(null);
        } catch (ApiException e) {
            environment.setLastApiCallThrewException(true);
            environment.setLastApiResponse(null);
            environment.setLastApiException(e);
            environment.setLastStatusCode(environment.getLastApiException().getCode());
        }
    }

    @Then("^I receive a payload containing all Services$")
    public void iReceiveAPayloadContainingAllServices() throws Throwable {
        assertNotNull(lastReceivedServiceList);
    }
}
