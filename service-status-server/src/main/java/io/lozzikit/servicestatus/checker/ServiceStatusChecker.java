package io.lozzikit.servicestatus.checker;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.lozzikit.servicestatus.api.dto.Status;
import io.lozzikit.servicestatus.entities.ServiceEntity;
import io.lozzikit.servicestatus.entities.StatusEntity;
import io.lozzikit.servicestatus.service.ServiceService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Class in charge of checking services' status
 */
public class ServiceStatusChecker  {

    private static final String UUID = "UUID";
    private static final String DEFAULT_EXPAND= "HISTORY";

    @Autowired
    ServiceService serviceService;

    private OkHttpClient httpClient = new OkHttpClient();
    private final Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

    public ServiceStatusChecker() throws SchedulerException {
    }

    /**
     * Starts the main event loop of the service status checker
     */
    public void start(){

        List<ServiceEntity> services = serviceService.getAllServices(DEFAULT_EXPAND);g

        services.forEach(s -> {

            JobDetail job = newJob(CheckTask.class)
                    .withIdentity(s.getName())
                    .build();

            job.getJobDataMap().put(UUID,s.getId());

            Trigger trigger = newTrigger()
                    .withIdentity("trigger")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInMinutes(s.getInterval())
                            .repeatForever())
                    .build();

            try {
                //Tries to schedule the setup job
                scheduler.scheduleJob(job, trigger);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        });

    }

    private class CheckTask implements Job{


        /**
         * Check the status of a single service among the one stored in the service service
         * Stores the status of the service as result
         */
        @Override
        public void execute(JobExecutionContext context) {

            UUID uuid = (UUID) context.getJobDetail().getJobDataMap().get(UUID);

            ServiceEntity service = serviceService.getService(uuid , DEFAULT_EXPAND);

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

            context.setResult(statusToAdd);
        }
    }

}
