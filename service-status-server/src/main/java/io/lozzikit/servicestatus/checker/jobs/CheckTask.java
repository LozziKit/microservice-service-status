package io.lozzikit.servicestatus.checker.jobs;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.lozzikit.servicestatus.api.dto.Status;
import io.lozzikit.servicestatus.checker.StatusCodeMatcher;
import io.lozzikit.servicestatus.entities.ServiceEntity;
import io.lozzikit.servicestatus.entities.StatusEntity;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.UUID;

public class CheckTask implements Job {

    public static final String UUID = "UUID";
    public static final String SERVICE = "SERVICE";

    private final OkHttpClient client;

    public CheckTask(){
        this.client = new OkHttpClient();
    }

    /**
     * Check the status of a single service among the one stored in the service service
     * Stores the status of the service as result
     */
    @Override
    public void execute(JobExecutionContext context) {

        UUID uuid = (UUID) context.getJobDetail().getJobDataMap().get(UUID);
        ServiceEntity service = (ServiceEntity)context.getJobDetail().getJobDataMap().get(SERVICE);

        String url = service.getUrl();

        Request request = new Request.Builder().url(url).build();

        Response response;
        int code;
        Status.StateEnum status;

        //Contacting service to get availability
        try{
            response = client.newCall(request).execute();
            code = response.code();
            status = StatusCodeMatcher.match(code);
        }catch (IOException e){
            code = -1;
            status = Status.StateEnum.DOWN;
        }

        //Creating the status from response
        StatusEntity statusToAdd = new StatusEntity(
                new Date(),
                code,
                status,
                service);

        //Serializing statusEntity
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(statusToAdd);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        context.setResult(byteArrayOutputStream.toByteArray());

        System.out.println("Pinged at interval "+service.getInterval());
    }
}