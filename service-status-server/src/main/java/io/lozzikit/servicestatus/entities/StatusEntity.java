package io.lozzikit.servicestatus.entities;

import io.lozzikit.servicestatus.api.dto.Status;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

public class StatusEntity {
    // checkAt, code, status
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date checkAt;

    private int httpStatus;

    private Status.StatusEnum status;
}
