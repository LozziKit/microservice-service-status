package io.lozzikit.servicestatus.api.spec.steps;

import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import io.lozzikit.servicestatus.ApiException;
import io.lozzikit.servicestatus.api.IncidentApi;
import io.lozzikit.servicestatus.api.dto.NewIncident;
import io.lozzikit.servicestatus.api.spec.helpers.Environment;

import java.util.UUID;

import static java.lang.Thread.sleep;

public class IncidentCreationSteps {

    private Environment environment;
    private IncidentApi incidentApi;

    private NewIncident incident;

    public IncidentCreationSteps(Environment environment){
        this.environment = environment;
        this.incident = environment.getIncident();
        this.incidentApi = environment.getIncidentApi();
    }

    @And("^the Incident title is null$")
    public void theIncidentTitleIsNull() throws Throwable {
        environment.getIncident().setTitle(null);
    }

    @And("^the Incident type is null")
    public void theIncidentTypeIsNull() throws Throwable {
        environment.getIncident().setType(null);
    }

    @When("^I have added my Incident to the server$")
    public void iHaveAddedMyIncidentToTheServer() throws Throwable {
        if(incident == null){
            throw new NullPointerException("Cannot add an incident to a service if incident is null");
        } else {
            try {
                environment.setLastApiResponse(incidentApi.addIncidentWithHttpInfo(environment.getServiceUUID(), environment.getIncident()));
                environment.setLastApiCallThrewException(false);
                environment.setLastApiException(null);
                environment.setLastStatusCode(environment.getLastApiResponse().getStatusCode());
                String location = String.valueOf(environment.getLastApiResponse().getHeaders().get("Location"));
                environment.setIncidentUUID(UUID.fromString(location.substring(location.lastIndexOf('/') + 1, location.length() - 1)));
                sleep(1000);
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
