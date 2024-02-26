package com.example.RVS.services;

import com.example.RVS.dtos.AddScheduleDto;
import com.example.RVS.dtos.BuyTicketDto;
import com.example.RVS.entities.*;
import com.example.RVS.repositories.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirportService {

    private final AirportRepository airportRepository;
    private final PlaneRepository planeRepository;
    private final PointRepository pointRepository;
    private final ScheduleRepository scheduleRepository;
    private final TicketRepository ticketRepository;

    private List<PlaneEntity> createPlanes() {
        List<PlaneEntity> planes = new LinkedList<>();

        PlaneEntity an126 = new PlaneEntity();

        an126.setBrand("Ан");
        an126.setModel("Ан-126");
        an126.setSeatCount(128);

        planes.add(planeRepository.save(an126));

        PlaneEntity boeing777 = new PlaneEntity();

        boeing777.setBrand("Boeing");
        boeing777.setModel("Боинг 777");
        boeing777.setSeatCount(550);

        planes.add(planeRepository.save(boeing777));

        return planes;
    }

    @PostConstruct
    private void init() {
        String city = "Караганда";

        if(Objects.isNull(airportRepository.findByCityIgnoreCase(city).orElse(null))) {
            AirportEntity airportKrg = new AirportEntity();
            airportKrg.setCity(city);

            List<PlaneEntity> planes = createPlanes();

            airportKrg.setPlanes(planes);

            airportRepository.save(airportKrg);
        }

    }

    public AirportEntity getAirportInfo(String city) {
        return airportRepository.findByCityIgnoreCase(city).orElse(null);
    }

    public List<ScheduleEntity> getAirportSchedule(String city) {
        AirportEntity airport = airportRepository.findByCityIgnoreCase(city).orElse(null);

        if(Objects.nonNull(airport))
            return airport.getSchedules();

        return null;
    }

    public List<ScheduleEntity> getAirportScheduleByDestination(String city, String destination) {
        AirportEntity airport = airportRepository.findByCityIgnoreCase(city).orElse(null);

        if(Objects.nonNull(airport))
            return airport.getSchedules()
                    .stream()
                    .filter(scheduleEntity -> scheduleEntity.getEnd().getName().equals(destination))
                    .collect(Collectors.toList());

        return null;
    }

    public void addSchedule(AddScheduleDto addScheduleDto) {
        AirportEntity airport = airportRepository.findByCityIgnoreCase(addScheduleDto.getAirportName()).orElse(null);

        if(Objects.nonNull(airport)) {
            ScheduleEntity schedule = new ScheduleEntity();

            schedule.setTime(addScheduleDto.getTime());

            PointEntity startEntity = pointRepository.findByName(addScheduleDto.getStart()).orElse(null);

            PointEntity endEntity = pointRepository.findByName(addScheduleDto.getEnd()).orElse(null);


            if(Objects.isNull(startEntity)) {
                startEntity = new PointEntity();
                startEntity.setName(addScheduleDto.getStart());

                startEntity = pointRepository.save(startEntity);
            }

            if(Objects.isNull(endEntity)) {
                endEntity = new PointEntity();
                endEntity.setName(addScheduleDto.getEnd());

                endEntity = pointRepository.save(endEntity);
            }

            schedule.setStart(startEntity);
            schedule.setEnd(endEntity);

            schedule =  scheduleRepository.save(schedule);

            airport.getSchedules().add(schedule);
            PlaneEntity plane = airport.getPlanes().stream().findFirst().orElse(null);

            if(Objects.nonNull(plane))
                schedule.setPlane(plane);

            airportRepository.save(airport);
        }

    }

    public TicketEntity buyTicket(BuyTicketDto buyTicketDto) {

        ScheduleEntity schedule = scheduleRepository.findById(buyTicketDto.getScheduleId()).orElse(null);

        if(Objects.nonNull(schedule)) {
            PlaneEntity plane = schedule.getPlane();

            if(Objects.isNull(plane)) {
                AirportEntity airport = airportRepository.findByCityIgnoreCase(schedule.getStart().getName()).orElse(null);

                if (Objects.nonNull(airport)) {
                    plane = airport.getPlanes().stream().findFirst().orElse(null);
                    schedule.setPlane(plane);
                    scheduleRepository.save(schedule);
                }
                else {
                    return null;
                }

                List<TicketEntity> tickets = schedule.getTickets();

                if(tickets.size() + 1 <= plane.getSeatCount()) {
                    TicketEntity ticket = new TicketEntity();

                    ticket.setFullName(buyTicketDto.getFullName());
                    ticket.setIin(buyTicketDto.getIin());
                    ticket.setScheduleUuid(schedule.getUuid());

                    ticket = ticketRepository.save(ticket);

                    schedule.getTickets().add(ticket);
                    scheduleRepository.save(schedule);

                    return ticket;
                }
            }

        }

        return null;
    }

    public ScheduleEntity ticketInfo(String uuid) {
        return scheduleRepository.findByUuid(uuid).orElse(null);
    }
}
