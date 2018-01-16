package io.lozzikit.servicestatus.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "services")
public class ServiceEntity implements Serializable{

    private static final int NAME_LENGTH = 20;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "name", nullable = false, length = NAME_LENGTH)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "port", nullable = false)
    private int port;

    @Column(name = "check_interval", nullable = false)
    private int checkInterval;

    @OneToMany(mappedBy = "serviceEntity", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, targetEntity = StatusEntity.class)
    private List<StatusEntity> statuses;

    @OneToMany(mappedBy = "serviceEntity", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, targetEntity = IncidentEntity.class)
    private Set<IncidentEntity> incidents;

    public ServiceEntity(){}
    public ServiceEntity(String name, String description, String url, int port, int checkInterval){
        this.name=name;
        this.description=description;
        this.url=url;
        this.port=port;
        this.checkInterval=checkInterval;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getInterval() {
        return checkInterval;
    }

    public void setInterval(int interval) {
        this.checkInterval = interval;
    }

    public List<StatusEntity> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<StatusEntity> statuses) {
        this.statuses = statuses;
    }

    public Set<IncidentEntity> getIncidents() {
        return incidents;
    }

    public void setIncidents(Set<IncidentEntity> incidents) {
        this.incidents = incidents;
    }
}
