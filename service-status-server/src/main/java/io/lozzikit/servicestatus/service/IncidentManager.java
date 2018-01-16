package io.lozzikit.servicestatus.service;

import io.lozzikit.servicestatus.entities.IncidentEntity;
import io.lozzikit.servicestatus.entities.ServiceEntity;
import io.lozzikit.servicestatus.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@org.springframework.stereotype.Service
public class IncidentManager {

    @Autowired
    ServiceRepository serviceRepository;

    public void addIncident(UUID id, IncidentEntity incidentEntity){
        ServiceEntity serviceEntity = serviceRepository.findOne(id);
        serviceEntity.getIncidents().add(incidentEntity);
        incidentEntity.setService(serviceEntity);
        serviceRepository.save(serviceEntity);

    }

    public void removeIncident(UUID id, IncidentEntity incidentEntity){
        ServiceEntity serviceEntity = serviceRepository.findOne(id);
        serviceEntity.getIncidents().remove(incidentEntity);
        serviceRepository.save(serviceEntity);
    }

}
