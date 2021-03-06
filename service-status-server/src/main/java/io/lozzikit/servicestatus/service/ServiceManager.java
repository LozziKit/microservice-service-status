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

/**
 * Micro service manager. This class is in of providing an interface with
 * the services's repository.
 */
@org.springframework.stereotype.Service
public class ServiceManager {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    ServiceStatusChecker serviceStatusChecker;

    /**
     * Returns the service entity given by its id
     * @param id The id of the service we look for
     * @return The ServiceEntity matching the given id or null if no such services exists
     */
    public ServiceEntity getService(UUID id) {
        ServiceEntity service = serviceRepository.findOne(id);

        if (service == null) {
            System.err.println("Service not found. Attempting to remove related jobs...");
            try {
                serviceStatusChecker.removeScheduledTask(id);
            } catch (SchedulerException e) {
                System.err.println("No jobs associated with service-"+id);
            }
            throw new EntityNotFoundException(ErrorMessageUtil.buildEntityNotFoundMessage("service"));

        }
        return service;
    }

    /**
     * Get all services from the service repository
     * @return A list of services contained in the service repository
     */
    public List<ServiceEntity> getAllServices() {
        return serviceRepository.findAll();
    }

    /**
     * Creates a service and saves it in the service repository
     * @param service The service to save
     * @return The newly created service
     */
    public ServiceEntity createService(ServiceEntity service) {
        ServiceEntity serviceEntity = serviceRepository.save(service);
        serviceStatusChecker.schedule(service);
        return serviceEntity;
    }

    /**
     * Deletes a service given by its UUID
     * @param uuid The UUID whose service needs to be removed
     */
    public void deleteServiceById(UUID uuid) {

        if (!serviceRepository.exists(uuid)) {
            throw new EntityNotFoundException(ErrorMessageUtil.buildEntityNotFoundMessage("service"));
        }

        //Removing scheduled task associated with removed service
        try {
            serviceStatusChecker.removeScheduledTask(serviceRepository.findOne(uuid));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        serviceRepository.delete(uuid);
    }

    /**
     * Updates a service given by its UUID. All fields are
     * erased and written over. If the new service's interval is different from the previous one,
     * we reschedule the event.
     * @param id The UUID whose service needs to be updated
     * @param service The new service to update data with
     */
    public void updateService(UUID id, ServiceEntity service) {
        ServiceEntity serviceEntity = getService(id);

        serviceEntity.setName(service.getName());
        serviceEntity.setDescription(service.getDescription());
        serviceEntity.setUrl(service.getUrl());
        serviceEntity.setInterval(service.getInterval());

        //If the service interval is different, we notify the scheduler
        if (serviceEntity.getInterval() != service.getInterval() ) {
            try {
                serviceStatusChecker.updateSchedule(serviceEntity, service.getInterval());
            } catch (SchedulerException e) {
                e.printStackTrace();
                return;
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
        ServiceEntity serviceEntity = getService(id);

        serviceEntity.getStatuses().add(status);
        status.setServiceEntity(serviceEntity);
        serviceRepository.save(serviceEntity);
    }

    /**
     * Get the resource location URL
     * @param uuid The UUID of the service that we need to fetch from
     * @return the resource location URL
     */
    public String getLocationUrl(UUID uuid) {
        return "/services/" + uuid.toString();
    }
}
