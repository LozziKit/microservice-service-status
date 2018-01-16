package io.lozzikit.servicestatus.service;

import io.lozzikit.servicestatus.entities.IncidentEntity;
import io.lozzikit.servicestatus.entities.IncidentUpdateEntity;
import io.lozzikit.servicestatus.entities.ServiceEntity;
import io.lozzikit.servicestatus.repositories.IncidentRepository;
import io.lozzikit.servicestatus.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Service
public class IncidentManager {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    IncidentRepository incidentRepository;

    public void addIncident(UUID id, IncidentEntity incidentEntity){
        ServiceEntity serviceEntity = serviceRepository.findOne(id);
        serviceEntity.getIncidents().add(incidentEntity);
        incidentEntity.setService(serviceEntity);
        serviceRepository.save(serviceEntity);

    }

    public void addIncidentUpdate(UUID idService, Long idIncident, IncidentUpdateEntity incidentUpdateEntity) {
        IncidentEntity incidentEntity = incidentRepository.findOneById(idIncident);
        incidentEntity.getIncidentUpdates().add(incidentUpdateEntity);
        incidentRepository.save(incidentEntity);

    }

    public Optional<IncidentEntity> getIncident(UUID idService, Long idIncident) {
        ServiceEntity serviceEntity = serviceRepository.findOne(idService);
        return serviceEntity.getIncidents()
                .stream()
                .filter(i -> i.getId().equals(idIncident))
                .findFirst();
    }
}
