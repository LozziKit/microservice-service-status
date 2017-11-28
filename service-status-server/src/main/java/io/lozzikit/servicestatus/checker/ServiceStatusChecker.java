package io.lozzikit.servicestatus.checker;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.lozzikit.servicestatus.api.dto.Status;
import io.lozzikit.servicestatus.entities.ServiceEntity;
import io.lozzikit.servicestatus.entities.StatusEntity;
import io.lozzikit.servicestatus.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Class in charge of checking services' status
 */
public class ServiceStatusChecker {

    private static final String DEFAULT_EXPAND= "HISTORY";

    @Autowired
    ServiceService serviceService;

    private OkHttpClient httpClient = new OkHttpClient();
    private final ConcurrentTaskScheduler scheduler = new ConcurrentTaskScheduler();

    /**
     * Check the status of a single service among the one stored in the service service
     * @return The status of the service
     */
    public StatusEntity checkStatus(UUID uuid){

        ServiceEntity service = serviceService.getService(uuid,DEFAULT_EXPAND);

        String url = service.getUrl();

        Request request = new Request.Builder().url(url).build();

        Response response;
        int code;
        Status.StatusEnum status;

        //Contacting service to get availability
        try{
            response = httpClient.newCall(request).execute();
            code = response.code();
            status = StatusCodeMatcher.match(code);
        }catch (IOException e){
            code = 0;
            status = Status.StatusEnum.DOWN;
        }

        //Creating the status from response
        StatusEntity statusToAdd = new StatusEntity(
                new Date(),
                code,
                status,
                service);

        serviceService.addStatus(uuid,statusToAdd);

        return statusToAdd;
    }

    /**
     * Starts the main event loop of the service status checker
     */
    public void start(){

        List<ServiceEntity> services = serviceService.getAllServices(DEFAULT_EXPAND);

        services.forEach(s -> {
            scheduler.schedule(
                    ()->checkStatus(s.getId()),
                    triggerContext -> {

                    }
            );
        });

    }

}
