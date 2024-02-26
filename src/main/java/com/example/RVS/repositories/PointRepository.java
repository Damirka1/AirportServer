package com.example.RVS.repositories;

import com.example.RVS.entities.PointEntity;
import com.example.RVS.entities.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointRepository extends JpaRepository<PointEntity, Long> {

    Optional<PointEntity> findByName(String name);

}
