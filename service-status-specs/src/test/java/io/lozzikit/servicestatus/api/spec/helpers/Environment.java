package io.lozzikit.servicestatus.api.spec.helpers;

import io.lozzkit.servicestatus.api.ServiceApi;

import java.io.IOException;
import java.util.Properties;

public class Environment {

    private ServiceApi api = new ServiceApi();

    public Environment() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("environment.properties"));
        String url = properties.getProperty("io.lozzikit.service-status.server.url");
        api.getApiClient().setBasePath(url);

    }

    public ServiceApi getApi() {
        return api;
    }


}