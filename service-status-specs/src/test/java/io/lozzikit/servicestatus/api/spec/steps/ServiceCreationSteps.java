package io.lozzikit.servicestatus.api.spec.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import io.lozzikit.servicestatus.api.dto.NewService;
import io.lozzikit.servicestatus.api.spec.helpers.Environment;
import io.lozzkit.servicestatus.ApiException;
import io.lozzkit.servicestatus.api.ServiceApi;

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

    @And("^the Service URL is null$")
    public void theServiceURLIsNull() throws Throwable {
        service.setUrl(null);
    }

    @And("^the Service Name is null$")
    public void theServiceNameIsNull() throws Throwable {
        service.setName(null);
    }

    @And("^the Service Description is null$")
    public void theServiceDesciptionIsNull() throws Throwable {
        service.setDescription(null);
    }

    @And("^the Service port is null$")
    public void theServicePortIsNull() throws Throwable {
        service.setPort(null);
    }

    @And("^the Service port is negative$")
    public void theServicePortIsNegative() throws Throwable {
        service.setPort(-1);
    }


    @And("^the Service port is zero$")
    public void theServicePortIsZero() throws Throwable {
        service.setPort(0);
    }
    @And("^the Service port is to big$")
    public void theServicePortIsToBig() throws Throwable {
        service.setPort(65536);
    }

    @And("^the Service interval is null$")
    public void theServiceIntervalIsNull() throws Throwable {
        service.setInterval(null);
    }

    @And("^the Service interval is to small$")
    public void theServiceIntervalIsToSmall() throws Throwable {
        service.setInterval(4);
    }

    @And("^the Service interval is negative$")
    public void theServiceIntervalIsNegative() throws Throwable {
        service.setInterval(-1);
    }

}