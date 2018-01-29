package io.lozzikit.servicestatus.service;

import io.lozzikit.servicestatus.api.exceptions.ErrorMessageUtil;
import io.lozzikit.servicestatus.entities.IncidentEntity;
import io.lozzikit.servicestatus.entities.IncidentUpdateEntity;
import io.lozzikit.servicestatus.entities.ServiceEntity;
import io.lozzikit.servicestatus.repositories.IncidentRepository;
import io.lozzikit.servicestatus.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.UUID;

/**
 * Micro service manager. This class is in of providing an interface with
 * the incident's repository.
 */
@org.springframework.stereotype.Service
public class IncidentManager {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    ServiceManager serviceManager;

    @Autowired
    IncidentRepository incidentRepository;

    /**
     * Create an Incident for a Service
     * @param idService the uuid of the service the IncidentEntity will be added
     * @param incidentEntity the incident to be added
     * @param incidentUpdateEntity
     * @return the IncidentEntity
     */
    public IncidentEntity createIncident(UUID idService, IncidentEntity incidentEntity, IncidentUpdateEntity incidentUpdateEntity){

        ServiceEntity serviceEntity = serviceManager.getService(idService);

        serviceEntity.getIncidents().add(incidentEntity);
        incidentEntity.setServiceEntity(serviceEntity);

        incidentEntity.getIncidentUpdates().add(incidentUpdateEntity);
        incidentUpdateEntity.setIncidentEntity(incidentEntity);

        incidentRepository.save(incidentEntity);
        serviceRepository.save(serviceEntity);
        return incidentEntity;

    }

    /**
     * Get all services from the service repository
     *
     * @return A list of services contained in the service repository
     */
    public Set<IncidentEntity> getAllIncidents(UUID serviceId) {
        return serviceManager.getService(serviceId).getIncidents();
    }

    /**
     * Add an IncidentUpdateEntity to an IncidentEntity
     * @param idIncident the if of an IncidentEntity the IncidentUpdateEntity will be added
     * @param incidentUpdateEntity the IncidentUpdateEntity to be added
     */
    public void addIncidentUpdate(UUID idIncident, UUID idService, IncidentUpdateEntity incidentUpdateEntity) {
        IncidentEntity incidentEntity = getIncident(idService,idIncident);

        incidentUpdateEntity.setIncidentEntity(incidentEntity);
        incidentEntity.getIncidentUpdates().add(incidentUpdateEntity);

        incidentRepository.save(incidentEntity);
    }

    /**
     * @param idService the id of a service
     * @param idIncident the id of an Incident
     * @return an IncidentEntity
     */
    public IncidentEntity getIncident(UUID idService, UUID idIncident) {
        ServiceEntity service = serviceManager.getService(idService);

        IncidentEntity incident = incidentRepository.findOneById(idIncident);

        //check if the incident is from this service
        boolean isRelated = false;
        for(IncidentEntity incidentEntity : service.getIncidents()){
            if(incidentEntity.getId().equals(idIncident)){
                isRelated=true;
            }
        }
        if (incident == null || !isRelated) {
            throw new EntityNotFoundException(ErrorMessageUtil.buildEntityNotFoundMessage("incident"));
        }else{
            return incident;
        }
    }

    /**
     * @param incidentEntity an IncidentEntity
     * @return the path of the incident
     */
    public String getLocationUrl( IncidentEntity incidentEntity) {
        return "/services/"+incidentEntity.getServiceEntity().getId().toString() +"/incidents/" + incidentEntity.getId().toString();
    }
}
