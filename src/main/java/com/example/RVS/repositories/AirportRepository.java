package com.example.RVS.repositories;

import com.example.RVS.entities.AirportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirportRepository extends JpaRepository<AirportEntity, Long> {

    Optional<AirportEntity> findByCityIgnoreCase(String city);


}
