package io.lozzikit.servicestatus.api.spec.steps.utils;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.lozzikit.servicestatus.api.dto.NewService;
import io.lozzikit.servicestatus.api.spec.helpers.Environment;
import io.lozzkit.servicestatus.ApiException;
import io.lozzkit.servicestatus.ApiResponse;
import io.lozzkit.servicestatus.api.ServiceApi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HttpSteps {

    private Environment environment;
    private ServiceApi api;
    private NewService service;


    public HttpSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
        this.service = environment.getService();
    }

    @Then("^I receive a (\\d+) status code$")
    public void i_receive_a_status_code(int arg1) throws Throwable {
        assertEquals(arg1, environment.getLastStatusCode());
    }

    @Given("^there is a Service server$")
    public void thereIsAServiceServer() throws Throwable {
        assertNotNull(api);
    }

    @And("^I have my Service identifier$")
    public void iHaveMyServiceIdentifier() throws Throwable {
        assertNotNull(environment.getServiceUUID());
    }


    @Given("^I have a Service payload$")
    public void iHaveAServicePayload() throws Throwable {
        environment.setService(environment.generateService());
    }

    @Then("^I receive an exception from the server$")
    public void iReceiveAnExceptionFromTheServer() throws Throwable {
        assertTrue(environment.getLastApiCallThrewException());
    }

    @And("^I receive a (.*) error message$")
    public void iReceiveAnErrorMessage(String message) throws Throwable {
        assertTrue(message.equals(environment.getLastApiException().getMessage()));
    }
}
