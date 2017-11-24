package io.lozzikit.servicestatus.api.spec.steps;

import com.google.gson.Gson;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import io.lozzikit.servicestatus.api.dto.ApiError;
import io.lozzikit.servicestatus.api.dto.NewService;
import io.lozzikit.servicestatus.api.spec.helpers.Environment;
import io.lozzkit.servicestatus.ApiException;
import io.lozzkit.servicestatus.ApiResponse;
import io.lozzkit.servicestatus.api.ServiceApi;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ServiceModificationSteps {


    private Environment environment;
    private ServiceApi api;
    private NewService service;

    public ServiceModificationSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
        this.service = environment.getService();
    }

    @When("^I send a PUT request to the /service/id endpoint$")
    public void iSendAPUTRequestToTheServiceEndpoint() throws Throwable {
        try {
            ApiResponse apiResponse = api.updateServiceWithHttpInfo(environment.getServiceUUID(), service);
            environment.setLastApiResponse(apiResponse);
            environment.setLastApiCallThrewException(false);
            environment.setLastApiException(null);
            environment.setLastStatusCode(environment.getLastApiResponse().getStatusCode());
        } catch (ApiException e) {
            environment.setLastApiCallThrewException(true);
            environment.setLastApiResponse(null);
            environment.setLastApiException(e);
            environment.setLastStatusCode(environment.getLastApiException().getCode());
            Gson gson = new Gson();
            ApiError error = gson.fromJson(e.getResponseBody(), ApiError.class);
            String s = "a";
        }
    }

    @And("^the modified payload URL field is null$")
    public void theModifiedPayloadURLFieldIsNull() throws Throwable {
        service.setUrl(null);
    }

    @And("^the modified payload name field is null$")
    public void theModifiedPayloadNameFieldIsNull() throws Throwable {
        service.setName(null);
    }

    @And("^the modified payload port field is null$")
    public void theModifiedPayloadPortFieldIsNull() throws Throwable {
        service.setPort(null);
    }

    @And("^the modified payload interval field is null$")
    public void theModifiedPayloadIntervalFieldIsNull() throws Throwable {
        service.setInterval(null);
    }

    @When("^I have an invalid Service ID$")
    public void iHaveAnInvalidServiceId() throws Throwable {
        environment.setServiceUUID(UUID.randomUUID().toString());
    }
}
