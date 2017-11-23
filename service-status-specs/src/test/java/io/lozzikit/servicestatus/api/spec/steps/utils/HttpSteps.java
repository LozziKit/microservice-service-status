package io.lozzikit.servicestatus.api.spec.steps.utils;

import cucumber.api.java.en.Then;
import io.lozzikit.servicestatus.api.dto.NewService;
import io.lozzikit.servicestatus.api.spec.helpers.Environment;
import io.lozzkit.servicestatus.ApiException;
import io.lozzkit.servicestatus.ApiResponse;
import io.lozzkit.servicestatus.api.ServiceApi;

import static org.junit.Assert.assertEquals;

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

}
