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

    private ApiResponse lastApiResponse;
    private ApiException lastApiException;
    private Boolean lastApiCallThrewException;
    private Integer lastStatusCode;

    private NewService service;
    private String serviceUUID;
    private NewService modifiedService;

    public HttpSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
        this.lastApiResponse = environment.getLastApiResponse();
        this.lastApiException = environment.getLastApiException();
        this.serviceUUID = environment.getServiceUUID();
        this.lastApiCallThrewException = environment.getLastApiCallThrewException();
        this.lastStatusCode = environment.getLastStatusCode();
        this.service = environment.getService();
    }

    @Then("^I receive a (\\d+) status code$")
    public void i_receive_a_status_code(int arg1) throws Throwable {
        assertEquals(arg1, lastStatusCode.intValue());
    }

    @Given("^there is a Service server$")
    public void thereIsAServiceServer() throws Throwable {
        assertNotNull(api);
    }

    @And("^I have my Service identifier$")
    public void iHaveMyServiceIdentifier() throws Throwable {
        assertNotNull(serviceUUID);
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

    @Given("^I have a Service payload$")
    public void iHaveAServicePayload() throws Throwable {
        if(service == null){
            service = environment.generateService();
        }
    }

    @Then("^I receive an exception from the server$")
    public void iReceiveAnExceptionFromTheServer() throws Throwable {
        assertTrue(lastApiCallThrewException);
    }
}
