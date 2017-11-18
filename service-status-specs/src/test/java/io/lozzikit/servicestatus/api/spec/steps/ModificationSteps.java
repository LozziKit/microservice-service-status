package io.lozzikit.servicestatus.api.spec.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import io.lozzikit.servicestatus.api.dto.NewService;
import io.lozzikit.servicestatus.api.spec.helpers.Environment;
import io.lozzkit.servicestatus.ApiException;
import io.lozzkit.servicestatus.ApiResponse;
import io.lozzkit.servicestatus.api.ServiceApi;

import static org.junit.Assert.assertNotNull;

public class ModificationSteps {


    private Environment environment;
    private ServiceApi api;

    private ApiResponse lastApiResponse;
    private ApiException lastApiException;
    private boolean lastApiCallThrewException;
    private int lastStatusCode;

    NewService service;
    String serviceUUID;

    public ModificationSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
    }

    @And("^I have added my Service to the server$")
    public void iHaveAddedMyServiceToTheServer() throws Throwable {
        service = new NewService();
        try {
            lastApiResponse = api.addServiceWithHttpInfo(service);
            lastApiCallThrewException = false;
            lastApiException = null;
            lastStatusCode = lastApiResponse.getStatusCode();
            String location = (String)lastApiResponse.getHeaders().get("location");
            serviceUUID = location.substring(location.lastIndexOf('/'));
        } catch (ApiException e) {
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            lastStatusCode = lastApiException.getCode();
        }
    }

    @Given("^I have my Service identifier$")
    public void iHaveMyServiceIdentifier() throws Throwable {
        assertNotNull(serviceUUID);
    }

    @When("^I send a PUT request to the /service endpoint$")
    public void iSendAPUTRequestToTheServiceEndpoint() throws Throwable {
        try {
            lastApiResponse = api.updateServiceWithHttpInfo(serviceUUID, service);
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

}
