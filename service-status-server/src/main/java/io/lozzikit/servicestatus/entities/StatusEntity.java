package io.lozzikit.servicestatus.entities;

import io.lozzikit.servicestatus.api.dto.Status;
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
    private Status.StateEnum status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL})
    @JoinColumn(name = "service_id")
    private ServiceEntity serviceEntity;

    public StatusEntity(){}

    public StatusEntity(@NotNull Date lastCheck,
                        @NotNull int httpStatus,
                        @NotNull Status.StateEnum status,
                        @NotNull ServiceEntity serviceEntity){
        setCheckAt(lastCheck);
        setHttpStatus(httpStatus);
        setStatus(status);
        setServiceEntity(serviceEntity);

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

    public Status.StateEnum getStatus() {
        return status;
    }

    public void setStatus(Status.StateEnum status) {
        this.status = status;
    }

    public ServiceEntity getServiceEntity() {
        return serviceEntity;
    }

    public void setServiceEntity(ServiceEntity serviceEntity) {
        this.serviceEntity = serviceEntity;
    }

    @Override
    public String toString() {
        return "id : "+getId()+"\n"+
                "date : "+getCheckAt()+"\n"+
                "status : "+getStatus()+"\n"+
                "httpStatus : "+getHttpStatus();
    }
}
