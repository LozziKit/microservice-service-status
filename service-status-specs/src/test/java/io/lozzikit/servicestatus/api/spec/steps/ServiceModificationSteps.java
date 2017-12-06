package io.lozzikit.servicestatus.api.spec.steps;

import com.google.gson.Gson;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import io.lozzikit.servicestatus.api.dto.ApiError;
import io.lozzikit.servicestatus.api.dto.NewService;
import io.lozzikit.servicestatus.api.spec.helpers.Environment;
import io.lozzkit.servicestatus.ApiException;
import io.lozzkit.servicestatus.ApiResponse;
import io.lozzkit.servicestatus.api.ServiceApi;

import static org.junit.Assert.assertNull;


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
        }
    }

    @And("^the new Service description is null$")
    public void theNewServiceDescriptionIsNull() throws Throwable {
        assertNull(service.getDescription());
    }
}
