package io.lozzikit.servicestatus.api.spec.helpers;

import com.google.gson.Gson;
import io.lozzikit.servicestatus.api.IncidentApi;
import io.lozzikit.servicestatus.api.dto.IncidentType;
import io.lozzikit.servicestatus.api.dto.IncidentUpdate;
import io.lozzikit.servicestatus.api.dto.NewIncident;
import io.lozzikit.servicestatus.api.dto.NewService;
import io.lozzikit.servicestatus.ApiException;
import io.lozzikit.servicestatus.ApiResponse;
import io.lozzikit.servicestatus.api.ServiceApi;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

public class Environment {
    private int counter = 0;

    private ServiceApi serviceApi = new ServiceApi();
    private IncidentApi incidentApi = new IncidentApi();
    private Gson gson = new Gson();

    private ApiResponse lastApiResponse;
    private ApiException lastApiException;
    private boolean lastApiCallThrewException;
    private int lastStatusCode;

    private NewService service;
    private UUID serviceUUID;

    private NewIncident incident;
    private UUID incidentUUID;

    private IncidentUpdate incidentUpdate;

    public Environment() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("environment.properties"));
        String url = properties.getProperty("io.lozzikit.service-status.server.url");
        serviceApi.getApiClient().setBasePath(url);
        incidentApi.getApiClient().setBasePath(url);

        service = generateService();
        incident = generateIncident();
    }

    public NewService generateService() {
        NewService result = new NewService();
        result.setName("Test Service number " + ++counter);
        result.setDescription("A service for API testing");
        result.setInterval(5);
        result.setUrl("http://www.example.com");
        return result;
    }

    public IncidentUpdate generateIncidentUpdate() {
        IncidentUpdate iu = new IncidentUpdate();
        iu.setType(IncidentType.ISSUE);
        iu.setMessage("This is an incident of type " + iu.getType() + ". ");
        return iu;
    }

    public NewIncident generateIncident() {
        NewIncident result = new NewIncident();
        result.setTitle("Test incident number " + ++counter);
        IncidentUpdate temp = generateIncidentUpdate();
        result.setType(temp.getType());
        result.setMessage(temp.getMessage());
        return result;
    }

    public ServiceApi getServiceApi() {
        return serviceApi;
    }

    public void setServiceApi(ServiceApi serviceApi) {
        this.serviceApi = serviceApi;
    }

    public ApiResponse getLastApiResponse() {
        return lastApiResponse;
    }

    public void setLastApiResponse(ApiResponse lastApiResponse) {
        this.lastApiResponse = lastApiResponse;
    }

    public ApiException getLastApiException() {
        return lastApiException;
    }

    public void setLastApiException(ApiException lastApiException) {
        this.lastApiException = lastApiException;
    }

    public boolean getLastApiCallThrewException() {
        return lastApiCallThrewException;
    }

    public void setLastApiCallThrewException(boolean lastApiCallThrewException) {
        this.lastApiCallThrewException = lastApiCallThrewException;
    }

    public int getLastStatusCode() {
        return lastStatusCode;
    }

    public void setLastStatusCode(int lastStatusCode) {
        this.lastStatusCode = lastStatusCode;
    }

    public NewService getService() {
        return service;
    }

    public void setService(NewService service) {
        this.service = service;
    }

    public UUID getServiceUUID() {
        return serviceUUID;
    }

    public void setServiceUUID(UUID serviceUUID) {
        this.serviceUUID = serviceUUID;
    }

    public Gson getGson() {
        return gson;
    }

    public void setIncident(NewIncident incident) {
        this.incident = incident;
    }

    public NewIncident getIncident() {
        return incident;
    }

    public UUID getIncidentUUID() {
        return incidentUUID;
    }

    public void setIncidentUUID(UUID incidentUUID) {
        this.incidentUUID = incidentUUID;
    }

    public IncidentApi getIncidentApi() {
        return incidentApi;
    }

    public IncidentUpdate getIncidentUpdate() {
        return incidentUpdate;
    }

    public void setIncidentUpdate(IncidentUpdate incidentUpdate) {
        this.incidentUpdate = incidentUpdate;
    }
}