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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ServiceCreationSteps {

    private Environment environment;
    private ServiceApi api;
    NewService service;

    public ServiceCreationSteps(Environment environment) {
        this.environment = environment;
        this.api = environment.getApi();
        this.service = environment.getService();
    }

    @And("^I receive the identifier of my Service$")
    public void iReceiveTheIdentifierOfMyService() throws Throwable {
        String location = String.valueOf(environment.getLastApiResponse().getHeaders().get("Location"));
        location.substring(location.lastIndexOf('/'));
    }

    @And("^URL is null$")
    public void urlIsNull() throws Throwable {
        environment.getService().setUrl(null);
    }
}