package io.lozzikit.servicestatus.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Entity
public class ServiceEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private String url;

    @NotNull
    private int port;

    @NotNull
    private int checkInterval;

    @OneToMany(mappedBy = "service")
    private List<StatusEntity> statuses;

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
}
