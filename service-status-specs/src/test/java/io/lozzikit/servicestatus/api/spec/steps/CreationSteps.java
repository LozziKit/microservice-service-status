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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by Olivier Liechti on 27/07/17.
 */
public class CreationSteps {

    private Environment environment;
    private ServiceApi api;

    private ApiResponse lastApiResponse;
    private ApiException lastApiException;
    private boolean lastApiCallThrewException;
    private int lastStatusCode;

    NewService service;
    String serviceUUID;

    public CreationSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
    }

    @Given("^there is a Service server$")
    public void thereIsAServiceServer() throws Throwable {
        assertNotNull(api);
    }

    @Given("^I have a Service payload$")
    public void iHaveAServicePayload() throws Throwable {
        service = new NewService();
    }

    @When("^I POST it to the /services endpoint$")
    public void iPOSTItToTheServicesEndpoint() throws Throwable {
        try {
            lastApiResponse = api.addServiceWithHttpInfo(service);
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
        assertEquals(201, lastStatusCode);
    }

    @And("^I receive the identifier of my Service$")
    public void iReceiveTheIdentifierOfMyService() throws Throwable {
        String location = String.valueOf(lastApiResponse.getHeaders().get("Location"));
        location.substring(location.lastIndexOf('/'));
    }
}