package io.lozzikit.servicestatus.checker.jobs;

import io.lozzikit.servicestatus.entities.StatusEntity;
import io.lozzikit.servicestatus.service.ServiceManager;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

@Service
public class JobListener implements org.quartz.JobListener {

    public static final String DEFAULT_LISTENER = "DEFAULT_LISTENER";

    @Autowired
    ServiceManager serviceManager;

    @Override
    public String getName() {
        return DEFAULT_LISTENER;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {}

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {}

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {

        byte[] statusEntitySerialized = (byte[]) context.getResult();
        StatusEntity statusEntity = null;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(statusEntitySerialized);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            statusEntity = (StatusEntity) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert statusEntity != null;

        serviceManager.addStatus(statusEntity.getServiceEntity().getId(), statusEntity);
    }
}
