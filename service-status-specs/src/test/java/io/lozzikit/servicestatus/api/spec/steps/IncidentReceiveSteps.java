package io.lozzikit.servicestatus.api.spec.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.lozzikit.servicestatus.ApiException;
import io.lozzikit.servicestatus.api.IncidentApi;
import io.lozzikit.servicestatus.api.ServiceApi;
import io.lozzikit.servicestatus.api.dto.Incident;
import io.lozzikit.servicestatus.api.dto.NewIncident;
import io.lozzikit.servicestatus.api.spec.helpers.Environment;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class IncidentReceiveSteps {

    private Environment environment;
    private ServiceApi serviceApi;
    private IncidentApi incidentApi;

    private NewIncident incident;

    private Incident lastReceivedIncident;
    private List<Incident> lastReceivedIncidentList;

    public IncidentReceiveSteps(Environment environment) {
        this.environment = environment;
        this.serviceApi = environment.getServiceApi();
        this.incident = environment.getIncident();
        this.incidentApi = environment.getIncidentApi();
    }

    @When("^I send a GET request to the /services/serviceId/incidents/incidentId endpoint$")
    public void iSendAGETRequestToTheServicesServiceIdIncidentsIncidentIdEndpoint() throws Throwable {
        try {
            lastReceivedIncident = incidentApi.getIncidentDetails(environment.getServiceUUID(), environment.getIncidentUUID());
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

    @Then("^I receive a payload containing the Incident$")
    public void iReceiveAPayloadContainingTheIncident() throws Throwable {
        assertNotNull(lastReceivedIncident);
    }

    @Given("^I have an invalid Incident identifier$")
    public void iHaveAnInvalidIncidentIdentifier() throws Throwable {
        environment.setIncidentUUID(UUID.randomUUID().toString());
    }

    @When("^I send a GET request to the /services/serviceId/incidents endpoint$")
    public void iSendAGETRequestToTheServicesServiceIdIncidentsEndpoint() throws Throwable {
        try {
            lastReceivedIncidentList = incidentApi.getIncidents(environment.getServiceUUID());
            environment.setLastApiCallThrewException(false);
            environment.setLastApiException(null);
        } catch (ApiException e) {
            environment.setLastApiCallThrewException(true);
            environment.setLastApiResponse(null);
            environment.setLastApiException(e);
            environment.setLastStatusCode(environment.getLastApiException().getCode());
        }
    }

    @Then("^I receive a payload containing all Incidents for my Service$")
    public void iReceiveAPayloadContainingAllIncidentsForMyService() throws Throwable {
        assertNotNull(lastReceivedIncidentList);
    }

    @And("^the payload contains multiple Incident$")
    public void thePayloadContainsMultipleIncident() throws Throwable {
        assertTrue(lastReceivedIncidentList.size() > 1);
    }
}
