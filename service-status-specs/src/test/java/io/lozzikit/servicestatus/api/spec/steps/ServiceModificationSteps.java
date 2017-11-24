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

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ServiceModificationSteps {


    private Environment environment;
    private ServiceApi api;
    private NewService service;
    private NewService modifiedService;

    public ServiceModificationSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
        this.service = environment.getService();
    }

    @Given("^there is a Service server for modification$")
    public void thereIsAServiceServerForModification() throws Throwable {
        assertNotNull(api);
    }

    @And("^I have added my Service to the server for modification$")
    public void iHaveAddedMyServiceToTheServerForModification() throws Throwable {
        environment.setService(environment.generateService());
        try {
            environment.setLastApiResponse(api.addServiceWithHttpInfo(service));
            environment.setLastApiCallThrewException(false);
            environment.setLastApiException(null);
            environment.setLastStatusCode(environment.getLastApiResponse().getStatusCode());
            String location = String.valueOf(environment.getLastApiResponse().getHeaders().get("Location"));
            environment.setServiceUUID(location.substring(location.lastIndexOf('/')+1, location.length()-1));
        } catch (ApiException e) {
            environment.setLastApiCallThrewException(true);
            environment.setLastApiResponse(null);
            environment.setLastApiException(e);
            environment.setLastStatusCode(environment.getLastApiException().getCode());
        }

    }


    @Given("^I have a Service payload for modification$")
    public void iHaveAServicePayloadForModification() throws Throwable {
        modifiedService = environment.generateService();
    }

    @When("^I send a PUT request to the /service/id endpoint$")
    public void iSendAPUTRequestToTheServiceEndpoint() throws Throwable {
        try {
            environment.setLastApiResponse(api.updateServiceWithHttpInfo(environment.getServiceUUID(), modifiedService));
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

    @When("^I send a PUT request to the /service/id endpoint with an invalid ID$")
    public void iSendAPUTRequestToTheServiceEndpointWithAnInvalidID() throws Throwable {
        try {
            environment.setLastApiResponse(api.updateServiceWithHttpInfo(UUID.randomUUID().toString(), modifiedService));
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

    @And("^the modified payload URL field is null$")
    public void theModifiedPayloadURLFieldIsNull() throws Throwable {
        modifiedService.setUrl(null);
    }

    @And("^the modified payload name field is null$")
    public void theModifiedPayloadNameFieldIsNull() throws Throwable {
        modifiedService.setName(null);
    }


    @And("^the modified payload port field is null$")
    public void theModifiedPayloadPortFieldIsNull() throws Throwable {
        modifiedService.setPort(null);
    }

    @And("^the modified payload interval field is null$")
    public void theModifiedPayloadIntervalFieldIsNull() throws Throwable {
        modifiedService.setInterval(null);
    }
}
