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

    private ApiResponse lastApiResponse;
    private Service lastReceivedService;
    private List<Service> lastReceivedServiceList;
    private ApiException lastApiException;
    private boolean lastApiCallThrewException;
    private int lastStatusCode;

    private NewService service;
    private String serviceUUID;

    public ServiceReceiveSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
    }

    @Given("^there is a Service server for reception$")
    public void thereIsAServiceServerForReception() throws Throwable {
        assertNotNull(api);
    }

    @And("^I have added my Service to the server for reception$")
    public void iHaveAddedMyServiceToTheServerForReception() throws Throwable {
        service = new NewService();
        try {
            lastApiResponse = api.addServiceWithHttpInfo(service);
            lastApiCallThrewException = false;
            lastApiException = null;
            lastStatusCode = lastApiResponse.getStatusCode();
            String location = String.valueOf(lastApiResponse.getHeaders().get("Location"));
            serviceUUID = location.substring(location.lastIndexOf('/')+1, location.length()-1);
        } catch (ApiException e) {
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            lastStatusCode = lastApiException.getCode();
        }
    }

    @Given("^I have a Service identifier for reception$")
    public void iHaveAServiceIdentifierForReception() throws Throwable {
        assertNotNull(serviceUUID);
    }

    @When("^I send a GET request to the /service/id endpoint$")
    public void iSendAGETRequestToTheServiceIdEndpoint() throws Throwable {
        try {
            lastReceivedService = api.getService(serviceUUID, "history");
            lastApiCallThrewException = false;
            lastApiException = null;
        } catch (ApiException e) {
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            lastStatusCode = lastApiException.getCode();
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
            lastApiCallThrewException = false;
            lastApiException = null;
        } catch (ApiException e) {
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            lastStatusCode = lastApiException.getCode();
        }
    }

    @Then("^I receive a payload containing all Services$")
    public void iReceiveAPayloadContainingAllServices() throws Throwable {
        assertNotNull(lastReceivedServiceList);
    }
}
