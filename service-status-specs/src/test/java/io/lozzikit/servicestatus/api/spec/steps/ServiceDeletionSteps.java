package io.lozzikit.servicestatus.api.spec.steps;


import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import io.lozzikit.servicestatus.api.dto.NewService;
import io.lozzikit.servicestatus.api.spec.helpers.Environment;
import io.lozzkit.servicestatus.ApiException;
import io.lozzkit.servicestatus.api.ServiceApi;

import java.util.UUID;

public class ServiceDeletionSteps {


    private Environment environment;
    private ServiceApi api;
    NewService service;

    public ServiceDeletionSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
        this.service = environment.getService();
    }

    @When("^I send a DELETE to the /service/id endpoint$")
    public void iSendADELETEToTheServiceIdEndpoint() throws Throwable {
        try {
            environment.setLastApiResponse(api.deleteServiceWithHttpInfo(environment.getServiceUUID()));
            environment.setLastApiCallThrewException(false);
            environment.setLastApiException(null);
            environment.setLastStatusCode(environment.getLastApiResponse().getStatusCode());
        } catch (ApiException e) {
            environment.setLastApiCallThrewException(true);
            environment.setLastApiException(e);
            environment.setLastStatusCode(environment.getLastApiException().getCode());
        }
    }

}
