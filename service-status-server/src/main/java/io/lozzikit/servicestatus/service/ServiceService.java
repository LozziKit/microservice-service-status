package io.lozzikit.servicestatus.service;

import io.lozzikit.servicestatus.api.exceptions.ErrorMessageUtil;
import io.lozzikit.servicestatus.entities.ServiceEntity;
import io.lozzikit.servicestatus.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Service
public class ServiceService {

    private static final String EXPAND_HISTORY = "HISTORY";

    @Autowired
    ServiceRepository serviceRepository;

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

        serviceRepository.save(serviceEntity);
    }

    public String getLocationUrl(UUID uuid) {
        return "/services/" + uuid.toString();
    }
}
