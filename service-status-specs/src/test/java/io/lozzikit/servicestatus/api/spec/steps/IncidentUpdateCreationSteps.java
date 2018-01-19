package io.lozzikit.servicestatus.api.spec.steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import io.lozzikit.servicestatus.ApiException;
import io.lozzikit.servicestatus.api.IncidentApi;
import io.lozzikit.servicestatus.api.ServiceApi;
import io.lozzikit.servicestatus.api.dto.NewIncident;
import io.lozzikit.servicestatus.api.spec.helpers.Environment;

import static java.lang.Thread.sleep;

public class IncidentUpdateCreationSteps {
    private Environment environment;
    private ServiceApi serviceApi;
    private IncidentApi incidentApi;

    private NewIncident incident;

    public IncidentUpdateCreationSteps(Environment environment){
        this.environment = environment;
        this.serviceApi = environment.getServiceApi();
        this.incident = environment.getIncident();
        this.incidentApi = environment.getIncidentApi();
    }

    @When("^I have added my IncidentUpdate to the Server$")
    public void iHaveAddedMyIncidentUpdateToTheServer() throws Throwable {
        if(incident == null){
            throw new NullPointerException("Cannot add an IncidentUpdate to an incident if incident is null");
        } else {
            try {
                incidentApi.addIncidentUpdateWithHttpInfo(environment.getIncidentUpdate(), environment.getServiceUUID(), environment.getIncidentUUID());
                environment.setLastApiCallThrewException(false);
                environment.setLastApiException(null);
                environment.setLastStatusCode(environment.getLastApiResponse().getStatusCode());
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

    @And("^the IncidentUpdate type is null$")
    public void theIncidentUpdateTypeIsNull() throws Throwable {
        environment.getIncidentUpdate().setIncidentType(null);
    }
}
