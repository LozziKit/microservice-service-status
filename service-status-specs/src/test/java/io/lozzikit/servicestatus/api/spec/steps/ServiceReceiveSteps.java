package io.lozzikit.servicestatus.api.spec.steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.lozzikit.servicestatus.ApiException;
import io.lozzikit.servicestatus.api.ServiceApi;
import io.lozzikit.servicestatus.api.dto.NewService;
import io.lozzikit.servicestatus.api.dto.Service;
import io.lozzikit.servicestatus.api.dto.Status;
import io.lozzikit.servicestatus.api.spec.helpers.Environment;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ServiceReceiveSteps {

    private Environment environment;
    private ServiceApi serviceApi;
    private NewService service;
    private Service lastReceivedService;
    private List<Service> lastReceivedServiceList;
    private List<Status> lastReceivedStatusList;

    public ServiceReceiveSteps(Environment environment) {
        this.environment = environment;
        this.serviceApi = environment.getServiceApi();
        this.service = environment.getService();
    }

    @When("^I send a GET request to the /service/id endpoint$")
    public void iSendAGETRequestToTheServiceIdEndpoint() throws Throwable {
        try {
            lastReceivedService = serviceApi.getService(environment.getServiceUUID());
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

    @Then("^I receive a payload containing the Service$")
    public void iReceiveAPayloadContainingTheService() throws Throwable {
        assertNotNull(lastReceivedService);
    }

    @When("^I send a GET request to the /services endpoint$")
    public void iSendAGETRequestToTheServicesEndpoint() throws Throwable {
        try {
            lastReceivedServiceList = serviceApi.getServices();
            environment.setLastApiCallThrewException(false);
            environment.setLastApiException(null);
        } catch (ApiException e) {
            environment.setLastApiCallThrewException(true);
            environment.setLastApiResponse(null);
            environment.setLastApiException(e);
            environment.setLastStatusCode(environment.getLastApiException().getCode());
        }
    }

    @Then("^I receive a payload containing all Services$")
    public void iReceiveAPayloadContainingAllServices() throws Throwable {
        assertNotNull(lastReceivedServiceList);
        assertTrue(lastReceivedServiceList.size() >= 1);
    }

    @When("^I send a GET request to the /services/id/history$")
    public void iSendAGETRequestToTheServicesIdHistory() throws Throwable {
        try {
            lastReceivedStatusList = serviceApi.getHistory(environment.getServiceUUID());
            environment.setLastApiCallThrewException(false);
            environment.setLastApiException(null);
        } catch (ApiException e) {
            environment.setLastApiCallThrewException(true);
            environment.setLastApiResponse(null);
            environment.setLastApiException(e);
            environment.setLastStatusCode(environment.getLastApiException().getCode());
        }
    }

    @Then("^I receive a payload containing a list of Statuses$")
    public void iReceiveAPayloadContainingAListOfStatuses() throws Throwable {
        assertNotNull(lastReceivedStatusList);
    }

    @And("^the list is sorted chronologically$")
    public void theListIsSortedChronologically() throws Throwable {

        assertTrue(lastReceivedStatusList.stream()
                .sorted(new Comparator<Status>() {
                    @Override
                    public int compare(Status o1, Status o2) {
                        return o1.getUpdateAt().compareTo(o2.getUpdateAt());
                    }
                })
                .collect(Collectors.toList())
                .equals(lastReceivedStatusList));
    }
}
