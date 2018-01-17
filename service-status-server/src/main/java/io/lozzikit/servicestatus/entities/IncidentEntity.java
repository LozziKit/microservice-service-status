package io.lozzikit.servicestatus.entities;

import org.hibernate.annotations.Cascade;
import io.lozzikit.servicestatus.api.dto.IncidentUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "incidents")
public class IncidentEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "title")
    private String title;

    @OneToMany( targetEntity = IncidentUpdateEntity.class)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<IncidentUpdateEntity> incidentUpdates;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = true)
    private ServiceEntity serviceEntity;

    public IncidentEntity(){}
    public IncidentEntity(String title){
        this.title=title;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<IncidentUpdateEntity> getIncidentUpdates() {
        return incidentUpdates;
    }

    public void setIncidentUpdates(List<IncidentUpdateEntity> incidentUpdates) {
        this.incidentUpdates = incidentUpdates;
    }

    public ServiceEntity getServiceEntity() {
        return serviceEntity;
    }

    public void setServiceEntity(ServiceEntity serviceEntity) {
        this.serviceEntity = serviceEntity;
    }
}
