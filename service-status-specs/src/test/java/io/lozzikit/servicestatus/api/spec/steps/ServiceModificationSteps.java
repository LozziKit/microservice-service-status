package io.lozzikit.servicestatus.api.spec.steps;

import cucumber.api.java.en.When;
import io.lozzikit.servicestatus.api.dto.NewService;
import io.lozzikit.servicestatus.api.spec.helpers.Environment;
import io.lozzkit.servicestatus.ApiException;
import io.lozzkit.servicestatus.api.ServiceApi;


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
            environment.setLastApiResponse(api.updateServiceWithHttpInfo(environment.getServiceUUID(), service));
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

}
