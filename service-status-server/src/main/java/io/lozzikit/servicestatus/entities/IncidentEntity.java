package io.lozzikit.servicestatus.entities;

import io.lozzikit.servicestatus.api.dto.Service;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "incidents")
public class IncidentEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @OneToMany(targetEntity = IncidentEntity.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, mappedBy = "incident")
    private Set<IncidentUpdateEntity> incidentUpdates;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = true)
    private ServiceEntity service;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<IncidentUpdateEntity> getIncidentUpdates() {
        return incidentUpdates;
    }

    public void setIncidentUpdates(Set<IncidentUpdateEntity> incidentUpdates) {
        this.incidentUpdates = incidentUpdates;
    }

    public ServiceEntity getService() {
        return service;
    }

    public void setService(ServiceEntity service) {
        this.service = service;
    }
}
