package com.example.RVS.controllers;

import com.example.RVS.dtos.AddUserTelephoneDto;
import com.example.RVS.services.TelephoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lab1")
@RequiredArgsConstructor
public class Lab1Controller {

    private final TelephoneService telephoneService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(telephoneService.findAll());
    }

    @PostMapping("/add")
    public ResponseEntity<?> getAll(@RequestBody AddUserTelephoneDto addUserTelephoneDto) {
        if(telephoneService.add(addUserTelephoneDto))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(defaultValue = "") String query) {
        return ResponseEntity.ok(telephoneService.findByNameOrTelephone(query));
    }
}
