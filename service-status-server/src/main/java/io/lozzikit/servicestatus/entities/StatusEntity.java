package io.lozzikit.servicestatus.entities;

import io.lozzikit.servicestatus.api.dto.Status;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "status")
public class StatusEntity implements Serializable{
    // checkAt, code, status
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "check_at")
    private Date checkAt;

    @Column(name = "http_status")
    private int httpStatus;

    @Column(name = "status")
    private Status.StatusEnum status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL})
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

    public StatusEntity(){}

    public StatusEntity(@NotNull Date lastCheck,
                        @NotNull int httpStatus,
                        @NotNull Status.StatusEnum status,
                        @NotNull ServiceEntity serviceEntity){
        setCheckAt(lastCheck);
        setHttpStatus(httpStatus);
        setStatus(status);
        setService(serviceEntity);

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCheckAt() {
        return checkAt;
    }

    public void setCheckAt(Date checkAt) {
        this.checkAt = checkAt;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Status.StatusEnum getStatus() {
        return status;
    }

    public void setStatus(Status.StatusEnum status) {
        this.status = status;
    }

    public ServiceEntity getService() {
        return service;
    }

    public void setService(ServiceEntity service) {
        this.service = service;
    }
}
