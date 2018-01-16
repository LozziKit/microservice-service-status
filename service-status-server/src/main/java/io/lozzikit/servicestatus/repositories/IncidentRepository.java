package io.lozzikit.servicestatus.repositories;

import io.lozzikit.servicestatus.entities.IncidentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentRepository extends CrudRepository<IncidentEntity, Long> {



}
