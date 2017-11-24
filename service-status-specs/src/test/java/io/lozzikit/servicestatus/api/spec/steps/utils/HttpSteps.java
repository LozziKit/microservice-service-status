package io.lozzikit.servicestatus.api.spec.steps.utils;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.lozzikit.servicestatus.api.spec.helpers.Environment;
import io.lozzkit.servicestatus.api.ServiceApi;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HttpSteps {

    private Environment environment;
    private ServiceApi api;

    public HttpSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
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
        //String temp = environment.getLastApiException().getResponseBody();

        assertTrue(message.equals(environment.getLastApiException().getMessage()));
    }

    @Given("^I have an invalid Service identifier$")
    public void iHaveAWrongServiceIdentifier() throws Throwable {
        environment.setServiceUUID(UUID.randomUUID().toString());
    }


    /*-- Steps for payload validation --*/

    @And("^the Service URL is null$")
    public void theServiceURLIsNull() throws Throwable {
        environment.getService().setUrl(null);
    }

    @And("^the Service Name is null$")
    public void theServiceNameIsNull() throws Throwable {
        environment.getService().setName(null);
    }

    @And("^the Service Description is null$")
    public void theServiceDesciptionIsNull() throws Throwable {
        environment.getService().setDescription(null);
    }

    @And("^the Service port is null$")
    public void theServicePortIsNull() throws Throwable {
        environment.getService().setPort(null);
    }

    @And("^the Service port is negative$")
    public void theServicePortIsNegative() throws Throwable {
        environment.getService().setPort(-1);
    }

    @And("^the Service port is zero$")
    public void theServicePortIsZero() throws Throwable {
        environment.getService().setPort(0);
    }
    @And("^the Service port is too big$")
    public void theServicePortIsToBig() throws Throwable {
        environment.getService().setPort(65536);
    }

    @And("^the Service interval is null$")
    public void theServiceIntervalIsNull() throws Throwable {
        environment.getService().setInterval(null);
    }

    @And("^the Service interval is to small$")
    public void theServiceIntervalIsToSmall() throws Throwable {
        environment.getService().setInterval(4);
    }

    @And("^the Service interval is negative$")
    public void theServiceIntervalIsNegative() throws Throwable {
        environment.getService().setInterval(-1);
    }

}
