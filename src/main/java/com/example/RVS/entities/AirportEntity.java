package com.example.RVS.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class AirportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private List<PlaneEntity> planes;

    @ManyToMany
    private List<ScheduleEntity> schedules;

    private String city;
}
