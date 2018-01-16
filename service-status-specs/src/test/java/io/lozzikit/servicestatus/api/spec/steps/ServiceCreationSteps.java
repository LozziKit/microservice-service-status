package io.lozzikit.servicestatus.api.spec.steps;

import cucumber.api.java.en.When;
import io.lozzikit.servicestatus.ApiException;
import io.lozzikit.servicestatus.api.ServiceApi;
import io.lozzikit.servicestatus.api.dto.NewService;
import io.lozzikit.servicestatus.api.spec.helpers.Environment;

public class ServiceCreationSteps {

    private Environment environment;
    private ServiceApi api;
    private NewService service;

    public ServiceCreationSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
        this.service = environment.getService();
    }

    @When("^I have added my Service to the server$")
    public void iHaveAddedMyServiceToTheServer() throws Throwable {
        if(service == null){
            throw new NullPointerException("Cannot add a service to the server if service is null");
        }else {
            try {
                environment.setLastApiResponse(api.addServiceWithHttpInfo(service));
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


}