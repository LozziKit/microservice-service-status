package io.lozzikit.servicestatus.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "incidentEntity")
    private List<IncidentUpdateEntity> incidentUpdates;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = true)
    private ServiceEntity serviceEntity;

    public IncidentEntity(){}
    public IncidentEntity(String title, IncidentUpdateEntity incidentUpdate){
        this.title=title;
        this.incidentUpdates= new LinkedList<>();
        this.incidentUpdates.add(incidentUpdate);
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
