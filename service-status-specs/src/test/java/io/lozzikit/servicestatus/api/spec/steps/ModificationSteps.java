package io.lozzikit.servicestatus.api.spec.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.lozzikit.servicestatus.api.dto.NewService;
import io.lozzikit.servicestatus.api.spec.helpers.Environment;
import io.lozzkit.servicestatus.ApiException;
import io.lozzkit.servicestatus.ApiResponse;
import io.lozzkit.servicestatus.api.ServiceApi;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ModificationSteps {


    private Environment environment;
    private ServiceApi api;

    private ApiResponse lastApiResponse;
    private ApiException lastApiException;
    private Boolean lastApiCallThrewException;
    private Integer lastStatusCode;

    private NewService service;
    private String serviceUUID;

    public ModificationSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
        this.lastApiResponse = environment.getLastApiResponse();
        this.lastApiException = environment.getLastApiException();
        this.serviceUUID = environment.getServiceUUID();
        this.lastApiCallThrewException = environment.getLastApiCallThrewException();
        this.lastStatusCode = environment.getLastStatusCode();
        this.service = environment.getService();
    }

    @Given("^there is a Service server for modification$")
    public void thereIsAServiceServerForModification() throws Throwable {
        assertNotNull(api);
    }

    @And("^I have added my Service to the server for modification$")
    public void iHaveAddedMyServiceToTheServerForModification() throws Throwable {
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

    @And("^I have my Service identifier for modification$")
    public void iHaveMyServiceIdentifierForModification() throws Throwable {
        assertNotNull(serviceUUID);
    }

    @Given("^I have a Service payload for modification$")
    public void iHaveAServicePayloadForModification() throws Throwable {
        modifiedService = new NewService();
        modifiedService.setName("Modified");
    }

    @When("^I send a PUT request to the /service/id endpoint$")
    public void iSendAPUTRequestToTheServiceEndpoint() throws Throwable {
        try {
            lastApiResponse = api.updateServiceWithHttpInfo(serviceUUID, modifiedService);
            lastApiCallThrewException = false;
            lastApiException = null;
            lastStatusCode = lastApiResponse.getStatusCode();
        } catch (ApiException e) {
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            lastStatusCode = lastApiException.getCode();
        }
    }

    @Then("^I receive a (\\d+) status code for my modification$")
    public void iReceiveAStatusCodeForMyModification(int arg0) throws Throwable {
        assertEquals(204, lastStatusCode);
    }

    @When("^I send a PUT request to the /service/id endpoint with an invalid ID$")
    public void iSendAPUTRequestToTheServiceEndpointWithAnInvalidID() throws Throwable {
        try {
            lastApiResponse = api.updateServiceWithHttpInfo(UUID.randomUUID().toString(), modifiedService);
            lastApiCallThrewException = false;
            lastApiException = null;
            lastStatusCode = lastApiResponse.getStatusCode();
        } catch (ApiException e) {
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            lastStatusCode = lastApiException.getCode();
        }
    }

    @Then("^I receive a (\\d+) error code for my modification$")
    public void iReceiveAErrorCodeForMyModification(int arg0) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(405, lastStatusCode);
    }
}
