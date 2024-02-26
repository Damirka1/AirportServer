package com.example.RVS.repositories;

import com.example.RVS.entities.UserTelephoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserTelephoneRepository extends JpaRepository<UserTelephoneEntity, Long> {

    @Query(value = "SELECT * FROM user_telephone_entity where telephone like %?1% or full_name like %?1%", nativeQuery = true)
    List<UserTelephoneEntity> findByTelephoneOrFullName(String telephoneOrFullName);

}
