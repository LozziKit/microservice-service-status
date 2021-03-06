package io.lozzikit.servicestatus.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.net.URL;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "services")
public class ServiceEntity implements Serializable{

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "url", nullable = false)
    private URL url;

    @Column(name = "check_interval", nullable = false)
    private int checkInterval;

    @OneToMany(mappedBy = "serviceEntity", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})

    @OrderBy("checkAt ASC")
    private List<StatusEntity> statuses;

    @OneToMany(mappedBy = "serviceEntity", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private Set<IncidentEntity> incidents;

    public ServiceEntity(){}
    public ServiceEntity(String name, String description, URL url, int checkInterval){
        this.name=name;
        this.description=description;
        this.url=url;
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

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
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
