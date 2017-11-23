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

public class ServiceDeletionSteps {


    private Environment environment;
    private ServiceApi api;

    private ApiResponse lastApiResponse;
    private ApiException lastApiException;
    private boolean lastApiCallThrewException;
    private int lastStatusCode;
    private String serviceUUID;

    NewService service;

    public ServiceDeletionSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
    }

    @Given("^there is a Service server for deletion$")
    public void thereIsAServiceServerForDeletion() throws Throwable {
        assertNotNull(api);
    }

    @When("^I send a DELETE to the /service/id endpoint$")
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

    @Then("^I receive a (\\d+) status code for deletion$")
    public void i_receive_a_status_code(int arg1) throws Throwable {
        assertEquals(204, lastStatusCode);
    }


    @Then("^I receive a (\\d+) error code status code for deletion$")
    public void iReceiveAErrorCodeStatusCode(int arg0) throws Throwable {
        assertEquals(404, lastStatusCode);
    }


    @Given("^I have my Service identifier for deletion$")
    public void iHaveMyServiceIdentifierForDeletion() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @And("^when I GET /service/id$")
    public void whenIGETServiceId() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
