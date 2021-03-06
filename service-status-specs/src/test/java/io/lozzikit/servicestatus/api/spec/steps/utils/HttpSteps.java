package io.lozzikit.servicestatus.api.spec.steps.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.lozzikit.servicestatus.api.ServiceApi;
import io.lozzikit.servicestatus.api.dto.ApiValidationError;
import io.lozzikit.servicestatus.api.spec.helpers.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class HttpSteps {

    private Environment environment;
    private ServiceApi api;
    private Gson gson;

    public HttpSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getServiceApi();
        this.gson = environment.getGson();
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

    @And("^I receive a (.*) validation error message$")
    public void iReceiveAValidationErrorMessage(String message) throws Throwable {
        List<ApiValidationError> errors = gson.fromJson(environment.getLastApiException().getResponseBody(), new TypeToken<ArrayList<ApiValidationError>>(){}.getType());
        boolean contains = false;
        for(ApiValidationError e : errors){
            if(message.equals(e.getField()+ " " + e.getError())){
                contains = true;
                break;
            }
        }
        assertTrue(contains);

    }

    @Given("^I have an invalid Service identifier$")
    public void iHaveAWrongServiceIdentifier() throws Throwable {
        environment.setServiceUUID(UUID.randomUUID());
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

    @And("^the URL contains a valid port$")
    public void theURLContainsAValidPort() throws Throwable {
        environment.getService().setUrl(environment.getService().getUrl()+":89");
    }

    @And("^the Service port is negative$")
    public void theServicePortIsNegative() throws Throwable {
        environment.getService().setUrl(environment.getService().getUrl()+":-5");
    }

    @And("^the Service port is zero$")
    public void theServicePortIsZero() throws Throwable {
        environment.getService().setUrl(environment.getService().getUrl()+":0");
    }
    @And("^the Service port is too big$")
    public void theServicePortIsToBig() throws Throwable {
        environment.getService().setUrl(environment.getService().getUrl()+":65536");
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

    @Given("^I have an Incident payload$")
    public void iHaveAnIncidentPayload() throws Throwable {
        environment.setIncident(environment.generateIncident());
    }

    @And("^I have my Incident identifier$")
    public void iHaveMyIncidentIdentifier() throws Throwable {
        assertNotNull(environment.getIncidentUUID());
    }

    @And("^I have an IncidentUpdate payload$")
    public void iHaveAnIncidentUpdatePayload() throws Throwable {
        environment.setIncidentUpdate(environment.generateIncidentUpdate());
    }
}
