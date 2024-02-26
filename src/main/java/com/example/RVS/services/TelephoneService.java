package com.example.RVS.services;

import com.example.RVS.dtos.AddUserTelephoneDto;
import com.example.RVS.entities.UserTelephoneEntity;
import com.example.RVS.repositories.UserTelephoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TelephoneService {

    private final UserTelephoneRepository userTelephoneRepository;

    public List<UserTelephoneEntity> findByNameOrTelephone(String nameOrTelephone) {
        return userTelephoneRepository.findByTelephoneOrFullName(nameOrTelephone);
    }

    public List<UserTelephoneEntity> findAll() {
        return userTelephoneRepository.findAll();
    }

    public boolean add(AddUserTelephoneDto addUserTelephoneDto) {
        if(Objects.isNull(addUserTelephoneDto.getTelephone()) || Objects.isNull(addUserTelephoneDto.getFullName()))
            return false;

        UserTelephoneEntity user = new UserTelephoneEntity();

        user.setFullName(addUserTelephoneDto.getFullName());
        user.setTelephone(addUserTelephoneDto.getTelephone());

        userTelephoneRepository.save(user);

        return true;
    }
}
