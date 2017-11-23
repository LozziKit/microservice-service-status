package io.lozzikit.servicestatus.api.spec.steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.lozzikit.servicestatus.api.dto.NewService;
import io.lozzikit.servicestatus.api.spec.helpers.Environment;
import io.lozzkit.servicestatus.ApiException;
import io.lozzkit.servicestatus.ApiResponse;
import io.lozzkit.servicestatus.api.ServiceApi;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ServiceCreationSteps {

    private Environment environment;
    private ServiceApi api;

    private ApiResponse lastApiResponse;
    private ApiException lastApiException;
    private Boolean lastApiCallThrewException;
    private Integer lastStatusCode;

    NewService service;

    public ServiceCreationSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
        this.lastApiResponse = environment.getLastApiResponse();
        this.lastApiException = environment.getLastApiException();
        this.lastApiCallThrewException = environment.getLastApiCallThrewException();
        this.lastStatusCode = environment.getLastStatusCode();
        this.service = environment.getService();
    }

    @When("^I POST it to the /services endpoint$")
    public void iPOSTItToTheServicesEndpoint() throws Throwable {
        try {
            lastApiResponse = api.addServiceWithHttpInfo(service);
            lastApiCallThrewException = false;
            lastApiException = null;
            lastStatusCode = lastApiResponse.getStatusCode();
        } catch (ApiException e) {
            lastApiCallThrewException = true;
            lastApiResponse = null;
            lastApiException = e;
            lastStatusCode = lastApiException.getCode();
        }
    }



    @And("^I receive the identifier of my Service$")
    public void iReceiveTheIdentifierOfMyService() throws Throwable {
        String location = String.valueOf(lastApiResponse.getHeaders().get("Location"));
        location.substring(location.lastIndexOf('/'));
    }


    @Given("^I have a wrong Service payload$")
    public void iHaveAWrongServicePayload() throws Throwable {
        service = new NewService();
        service.setInterval(3);
    }

}