package com.example.RVS.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private String uuid;

    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    private LocalDateTime updated;

    private LocalDateTime time;

    @ManyToOne
    private PlaneEntity plane;

    @ManyToOne
    private PointEntity start;

    @ManyToOne
    private PointEntity end;

    @OneToMany
    private List<TicketEntity> tickets;

}
