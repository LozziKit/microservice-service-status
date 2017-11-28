package io.lozzikit.servicestatus.service;

import io.lozzikit.servicestatus.entities.ServiceEntity;
import io.lozzikit.servicestatus.entities.StatusEntity;
import io.lozzikit.servicestatus.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Service
public class ServiceService {

    private static final String EXPAND_HISTORY = "HISTORY";

    @Autowired
    ServiceRepository serviceRepository;

    public ServiceEntity getService(UUID id, String expand) {
        ServiceEntity service = serviceRepository.findOne(id);

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
        serviceRepository.delete(uuid);
    }

    public void updateService(UUID id, ServiceEntity service) {
        ServiceEntity serviceEntity = serviceRepository.findOne(id);
        serviceEntity.setName(service.getName());
        serviceEntity.setDescription(service.getDescription());
        serviceEntity.setUrl(service.getUrl());
        serviceEntity.setPort(service.getPort());
        serviceEntity.setInterval(service.getInterval());

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
