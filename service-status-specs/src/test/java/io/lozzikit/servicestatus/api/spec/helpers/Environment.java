package io.lozzikit.servicestatus.api.spec.helpers;

import io.lozzikit.servicestatus.api.dto.NewService;
import io.lozzkit.servicestatus.ApiException;
import io.lozzkit.servicestatus.ApiResponse;
import io.lozzkit.servicestatus.api.ServiceApi;

import java.io.IOException;
import java.util.Properties;

public class Environment {
    private static int counter = 0;

    private ServiceApi api = new ServiceApi();
    private ApiResponse lastApiResponse;
    private ApiException lastApiException;
    private Boolean lastApiCallThrewException;
    private Integer lastStatusCode;

    private NewService service;
    private String serviceUUID;

    public Environment() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("environment.properties"));
        String url = properties.getProperty("io.lozzikit.service-status.server.url");
        api.getApiClient().setBasePath(url);

        service = generateService();
    }

    public NewService generateService() {
        NewService result = new NewService();
        result.setName("Test Service number " + ++counter);
        result.setDescription("A service for API testing");
        result.setInterval(5);
        result.setPort(80);
        return result;
    }

    public ServiceApi getApi() {
        return api;
    }

    public void setApi(ServiceApi api) {
        this.api = api;
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

    public Boolean getLastApiCallThrewException() {
        return lastApiCallThrewException;
    }

    public void setLastApiCallThrewException(Boolean lastApiCallThrewException) {
        this.lastApiCallThrewException = lastApiCallThrewException;
    }

    public Integer getLastStatusCode() {
        return lastStatusCode;
    }

    public void setLastStatusCode(Integer lastStatusCode) {
        this.lastStatusCode = lastStatusCode;
    }

    public NewService getService() {
        return service;
    }

    public void setService(NewService service) {
        this.service = service;
    }

    public String getServiceUUID() {
        return serviceUUID;
    }

    public void setServiceUUID(String serviceUUID) {
        this.serviceUUID = serviceUUID;
    }
}