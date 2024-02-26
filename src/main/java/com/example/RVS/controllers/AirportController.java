package com.example.RVS.controllers;

import com.example.RVS.dtos.AddScheduleDto;
import com.example.RVS.dtos.BuyTicketDto;
import com.example.RVS.entities.ScheduleEntity;
import com.example.RVS.entities.TicketEntity;
import com.example.RVS.services.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/airport")
@RequiredArgsConstructor
public class AirportController {

    private final AirportService airportService;

    @GetMapping("/{city}")
    public ResponseEntity<?> getAirportInfo(@PathVariable String city) {
        return ResponseEntity.ok(airportService.getAirportInfo(city));
    }

    @GetMapping("/{city}/schedule")
    public ResponseEntity<?> getAirportSchedule(@PathVariable String city) {
        return ResponseEntity.ok(airportService.getAirportSchedule(city));
    }

    @GetMapping("/{city}/schedule/{destination}")
    public ResponseEntity<?> getAirportScheduleByDestination(@PathVariable String city, @PathVariable String destination) {
        return ResponseEntity.ok(airportService.getAirportScheduleByDestination(city, destination));
    }

    @PostMapping("/add/schedule")
    public ResponseEntity<?> addSchedule(@RequestBody AddScheduleDto addScheduleDto) {
        airportService.addSchedule(addScheduleDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/buy/ticket")
    public ResponseEntity<?> buyTicket(@RequestBody BuyTicketDto buyTicketDto) {
        TicketEntity result = airportService.buyTicket(buyTicketDto);

        if(Objects.nonNull(result)) {
            return ResponseEntity.ok(result);
        }

        return ResponseEntity.badRequest().build();
    }


    @GetMapping("/ticket/info/{uuid}")
    public ResponseEntity<?> ticketInfo(@PathVariable String uuid) {
        ScheduleEntity result = airportService.ticketInfo(uuid);

        if(Objects.nonNull(result)) {
            return ResponseEntity.ok(result);
        }

        return ResponseEntity.badRequest().build();
    }

}
