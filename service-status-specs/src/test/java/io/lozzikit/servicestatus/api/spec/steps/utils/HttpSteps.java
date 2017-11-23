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
    private String serviceUUID;
    private NewService modifiedService;


    public HttpSteps(Environment environment) {
        this.environment = environment;
        this.serviceUUID = environment.getServiceUUID();
    }

    @Then("^I receive a (\\d+) status code$")
    public void i_receive_a_status_code(int arg1) throws Throwable {
        assertEquals(arg1, environment.getLastStatusCode());
    }

    @Given("^there is a Service server$")
    public void thereIsAServiceServer() throws Throwable {
        assertNotNull(environment.getApi());
    }

    @And("^I have my Service identifier$")
    public void iHaveMyServiceIdentifier() throws Throwable {
        assertNotNull(serviceUUID);
    }

    @And("^I have added my Service to the server$")
    public void iHaveAddedMyServiceToTheServer() throws Throwable {
        if(environment.getService() == null){
            throw new NullPointerException("Cannot add a service to the server if service is null");
        }else {
            try {
                environment.setLastApiResponse(environment.getApi().addServiceWithHttpInfo(environment.getService()));
                environment.setLastApiCallThrewException(false);
                environment.setLastApiException(null);
                environment.setLastStatusCode(environment.getLastApiResponse().getStatusCode());
                String location = String.valueOf(environment.getLastApiResponse().getHeaders().get("Location"));
                environment.setServiceUUID(location.substring(location.lastIndexOf('/') + 1, location.length() - 1));
            } catch (ApiException e) {
                environment.setLastApiCallThrewException(true);
                environment.setLastApiResponse(null);
                environment.setLastApiException(e);
                environment.setLastStatusCode(environment.getLastApiException().getCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Given("^I have a Service payload$")
    public void iHaveAServicePayload() throws Throwable {
        environment.setService(environment.generateService());
    }

    @Then("^I receive an exception from the server$")
    public void iReceiveAnExceptionFromTheServer() throws Throwable {
        assertTrue(environment.getLastApiCallThrewException());
    }
}
