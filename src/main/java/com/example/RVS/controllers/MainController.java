package com.example.RVS.controllers;

import com.example.RVS.entities.PointEntity;
import com.example.RVS.entities.ScheduleEntity;
import com.example.RVS.services.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final AirportService airportService;

    @GetMapping("/")
    public String index(Model model) {

        return "index";
    }

    @GetMapping("/{airport:[^.]+}")
    public String airport(Model model, @PathVariable String airport) {

        airport = airport.toUpperCase(Locale.ROOT);

        model.addAttribute("airport", airport);

        var l = airportService.getAirportSchedule(airport);

        List<ScheduleEntity> schedules = new LinkedList<>();

        for(int i = 0; i < l.size(); i++) {
            int finalI = i;
            if(schedules.stream().noneMatch(scheduleEntity -> scheduleEntity.getEnd().getName().equals(l.get(finalI).getEnd().getName())))
                schedules.add(l.get(i));
        }

        model.addAttribute("schedules", schedules);

        LocalDate time = LocalDateTime.now().toLocalDate();

        model.addAttribute("timeStart", time);

        model.addAttribute("timeEnd", time.plusMonths(1));


        return "airport";
    }

//    @GetMapping("/books/")
//    public String airport(Model model) {
//        return "book";
//    }
}
