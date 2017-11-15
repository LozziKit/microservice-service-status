package io.lozzikit.servicestatus.service;

import io.lozzikit.servicestatus.api.dto.NewService;
import io.lozzikit.servicestatus.entities.ServiceEntity;
import io.lozzikit.servicestatus.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceService {

    @Autowired
    ServiceRepository serviceRepository;

    public ServiceEntity createService(NewService service) {
        return serviceRepository.save(toServiceEntity(service));
    }

    private ServiceEntity toServiceEntity(NewService service) {
        ServiceEntity entity = new ServiceEntity();
        return null;
    }
}
