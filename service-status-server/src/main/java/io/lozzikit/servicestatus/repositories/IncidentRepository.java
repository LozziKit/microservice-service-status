package io.lozzikit.servicestatus.repositories;

import io.lozzikit.servicestatus.entities.IncidentEntity;
import io.lozzikit.servicestatus.entities.IncidentUpdateEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IncidentRepository extends CrudRepository<IncidentEntity, UUID> {

    IncidentEntity findOneById(UUID idIncident);
    List<IncidentUpdateEntity> findAllByOrderByIncidentUpdatesAsc();
}
