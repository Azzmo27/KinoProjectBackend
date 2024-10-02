package com.example.kinobackend.controller;

import com.example.kinobackend.model.Showing;
import com.example.kinobackend.service.ShowingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/operator")
public class OperatorController {

    @GetMapping("/schedule")
    public ResponseEntity<List<Showing>> getSchedule() {
        // Movie operator kan se tidsplanen
        return ResponseEntity.ok(ShowingService.getSchedule());
    }
}
