package io.lozzikit.servicestatus.api.spec.steps;

import cucumber.api.java.en.When;
import io.lozzikit.servicestatus.ApiException;
import io.lozzikit.servicestatus.api.ServiceApi;
import io.lozzikit.servicestatus.api.dto.NewIncident;
import io.lozzikit.servicestatus.api.spec.helpers.Environment;

import static java.lang.Thread.sleep;

public class IncidentCreationSteps {

    private Environment environment;
    private ServiceApi api;
    private NewIncident incident;

    public IncidentCreationSteps(Environment environment){
        this.environment = environment;
        this.api = environment.getApi();
        this.incident = environment.getIncident();
    }

    @When("^I send a POST request to the /service/serviceId/incidents endpoint$")
    public void iSendAPOSTRequestToTheServiceServiceIdIncidentsEndpoint() throws Throwable {
        if(incident == null){
            throw new NullPointerException("Cannot add an incident to a service if incident is null");
        } else {
            try {
                //Api.addServiceToIncident
                //environment.setLastApiResponse(api.
                environment.setLastApiCallThrewException(false);
                environment.setLastApiException(null);
                environment.setLastStatusCode(environment.getLastApiResponse().getStatusCode());
                String location = String.valueOf(environment.getLastApiResponse().getHeaders().get("Location"));
                environment.setServiceUUID(location.substring(location.lastIndexOf('/') + 1, location.length() - 1));
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
