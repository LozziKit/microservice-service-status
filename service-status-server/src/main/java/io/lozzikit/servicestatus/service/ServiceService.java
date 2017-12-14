package io.lozzikit.servicestatus.service;

import io.lozzikit.servicestatus.api.exceptions.ErrorMessageUtil;
import io.lozzikit.servicestatus.checker.ServiceStatusChecker;
import io.lozzikit.servicestatus.entities.ServiceEntity;
import io.lozzikit.servicestatus.entities.StatusEntity;
import io.lozzikit.servicestatus.repositories.ServiceRepository;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Service
public class ServiceService {

    private static final String EXPAND_HISTORY = "HISTORY";

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    ServiceStatusChecker serviceStatusChecker;

    public ServiceEntity getService(UUID id, String expand) {
        ServiceEntity service = serviceRepository.findOne(id);

        if (service == null) {
            throw new EntityNotFoundException(ErrorMessageUtil.buildEntityNotFoundMessage("service"));
        }

        if (expand.equals(EXPAND_HISTORY)) {
            service.setStatuses(null);
        }

        return service;
    }

    public List<ServiceEntity> getAllServices(String expand) {
        List<ServiceEntity> serviceEntities = serviceRepository.findAll();

        if (expand.equals(EXPAND_HISTORY)) {
            serviceEntities.stream().map(service -> {
                service.setStatuses(null);
                return service;
            });
        }

        return serviceEntities;
    }

    public ServiceEntity createService(ServiceEntity service) {
        return serviceRepository.save(service);
    }

    public void deleteServiceById(UUID uuid) {
        if (!serviceRepository.exists(uuid)) {
            throw new EntityNotFoundException(ErrorMessageUtil.buildEntityNotFoundMessage("service"));
        }

        serviceRepository.delete(uuid);
    }

    public void updateService(UUID id, ServiceEntity service) {
        ServiceEntity serviceEntity = serviceRepository.findOne(id);

        if (serviceEntity == null) {
            throw new EntityNotFoundException(ErrorMessageUtil.buildEntityNotFoundMessage("service"));
        }

        serviceEntity.setName(service.getName());
        serviceEntity.setDescription(service.getDescription());
        serviceEntity.setUrl(service.getUrl());
        serviceEntity.setPort(service.getPort());
        serviceEntity.setInterval(service.getInterval());

        //If the service interval is different, we notifiy the scheduler
        if (serviceEntity.getInterval() != service.getInterval() ) {
            try {
                serviceStatusChecker.updateSchedule(service.getName(), service.getInterval());
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }

        serviceRepository.save(serviceEntity);
    }

    /**
     * Add a status to the list of statuses tracked within a service
     * @param id The service's id
     * @param status The status to add to the service
     */
    public void addStatus(UUID id, StatusEntity status){

        ServiceEntity serviceEntity = serviceRepository.findOne(id);

        List<StatusEntity> tempStatuses = serviceEntity.getStatuses();
        tempStatuses.add(status);
        serviceEntity.setStatuses(tempStatuses);
    }

    public String getLocationUrl(UUID uuid) {
        return "/services/" + uuid.toString();
    }
}
