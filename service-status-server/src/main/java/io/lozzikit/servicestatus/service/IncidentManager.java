package io.lozzikit.servicestatus.service;

import io.lozzikit.servicestatus.entities.IncidentEntity;
import io.lozzikit.servicestatus.entities.IncidentUpdateEntity;
import io.lozzikit.servicestatus.entities.ServiceEntity;
import io.lozzikit.servicestatus.repositories.IncidentRepository;
import io.lozzikit.servicestatus.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@org.springframework.stereotype.Service
public class IncidentManager {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    IncidentRepository incidentRepository;

    public IncidentEntity createIncident(UUID id, IncidentEntity incidentEntity){
        ServiceEntity serviceEntity = serviceRepository.findOne(id);
        serviceEntity.getIncidents().add(incidentEntity);
        incidentEntity.setServiceEntity(serviceEntity);
        incidentRepository.save(incidentEntity);
        serviceRepository.save(serviceEntity);
        return incidentEntity;
    }

    /**
     * Get all services from the service repository
     * @return A list of services contained in the service repository
     */
    public Set<IncidentEntity> getAllIncidents(UUID serviceId) {
        ServiceEntity serviceEntity = serviceRepository.findOne(serviceId);
        return serviceEntity.getIncidents();
    }

    public void addIncidentUpdate(UUID idService, UUID idIncident, IncidentUpdateEntity incidentUpdateEntity) {
        IncidentEntity incidentEntity = incidentRepository.findOneById(idIncident);
        incidentEntity.getIncidentUpdates().add(incidentUpdateEntity);
        incidentRepository.save(incidentEntity);

    }

    public Optional<IncidentEntity> getIncident(UUID idService, UUID idIncident) {
        ServiceEntity serviceEntity = serviceRepository.findOne(idService);
        return serviceEntity.getIncidents()
                .stream()
                .filter(i -> i.getId().equals(idIncident))
                .findFirst();
    }
}
