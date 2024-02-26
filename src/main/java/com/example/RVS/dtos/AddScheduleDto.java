package com.example.RVS.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddScheduleDto {

    private String airportName;

    private LocalDateTime time;

    private String start;

    private String end;

    private Long planeId;

}
