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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DeletionSteps {


    private Environment environment;
    private ServiceApi api;

    private ApiResponse lastApiResponse;
    private ApiException lastApiException;
    private boolean lastApiCallThrewException;
    private int lastStatusCode;
    private String serviceUUID;

    NewService service;

    public CreationSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
    }

    @Given("^there is a Service server for deletion$")
    public void thereIsAServiceServerForDeletion() throws Throwable {
        assertNotNull(api);
    }

    @And("^I have added my Service to the server$")
    public void iHaveAddedMyServiceToTheServer() throws Throwable {
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

    @And("^I have my Service identifier$")
    public void iHaveMyServiceIdentifier() throws Throwable {
        assertNotNull(serviceUUID);

    }


    @When("^I send a DELETE to the /service/{id} endpoint$")
    public void iSendADELETEToTheServiceIdEndpoint() throws Throwable {
        try {
            lastApiResponse = api.deleteServiceWithHttpInfo(serviceUUID);
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

    @Then("^I receive a (\\d+) status code$")
    public void i_receive_a_status_code(int arg1) throws Throwable {
        assertEquals(204, lastStatusCode);
    }


    @Then("^I receive a (\\d+) error code status code$")
    public void iReceiveAErrorCodeStatusCode(int arg0) throws Throwable {
        assertEquals(404, lastStatusCode);
    }


}
