package io.lozzikit.servicestatus.service;

import io.lozzikit.servicestatus.api.dto.NewService;
import io.lozzikit.servicestatus.api.dto.Service;
import io.lozzikit.servicestatus.entities.ServiceEntity;
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

    public ServiceEntity createService(NewService service) {
        return serviceRepository.save(toServiceEntity(service));
    }

    public void deleteServiceById(UUID uuid) {
        serviceRepository.delete(uuid);
    }

    public void updateService(UUID id, NewService service) {
        ServiceEntity serviceEntity = serviceRepository.findOne(id);
        serviceEntity.setName(service.getName());
        serviceEntity.setDescription(service.getDescription());
        serviceEntity.setUrl(service.getUrl());
        serviceEntity.setPort(service.getPort());
        serviceEntity.setInterval(service.getInterval());

        serviceRepository.save(serviceEntity);
    }

    public ServiceEntity toServiceEntity(NewService service) {
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setName(service.getName());
        serviceEntity.setDescription(service.getDescription());
        serviceEntity.setUrl(service.getUrl());
        serviceEntity.setPort(service.getPort());
        serviceEntity.setInterval(service.getInterval());

        return serviceEntity;
    }

    public Service toDto(ServiceEntity serviceEntity) {
        Service service = new Service();

        service.setName(serviceEntity.getName());
        service.setDescription(serviceEntity.getDescription());
        service.setUrl(serviceEntity.getUrl());
        service.setPort(serviceEntity.getPort());
        service.setInterval(serviceEntity.getInterval());
        service.setLocation(getLocationUrl(serviceEntity.getId()));
        // TODO: add status support
        service.setStatuses(null);
        service.setLastStatus(null);

        return service;
    }

    public String getLocationUrl(UUID uuid) {
        return "/services/" + uuid.toString();
    }

}
