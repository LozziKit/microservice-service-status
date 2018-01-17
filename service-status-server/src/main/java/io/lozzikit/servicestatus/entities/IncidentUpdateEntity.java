package io.lozzikit.servicestatus.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

import io.lozzikit.servicestatus.api.dto.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "incident_update")
public class IncidentUpdateEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", nullable = true)
    private IncidentEntity incidentEntity;


    @Enumerated(EnumType.STRING)
    @Column(name = "incident_type")
    private IncidentType incidentType;

    @Column(name = "message")
    private String message;

    public IncidentUpdateEntity(){}

    public IncidentUpdateEntity(IncidentType incidentType, String message){
        this.incidentType=incidentType;
        this.message=message;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public IncidentEntity getIncidentEntity() {
        return incidentEntity;
    }

    public void setIncidentEntity(IncidentEntity incidentEntity) {
        this.incidentEntity = incidentEntity;
    }

    public IncidentType getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(IncidentType incidentType) {
        this.incidentType = incidentType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
