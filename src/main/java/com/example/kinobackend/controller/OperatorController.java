package com.example.kinobackend.controller;

import com.example.kinobackend.model.Showing;
import com.example.kinobackend.service.ShowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/operator")
public class OperatorController {


    @Autowired
    private ShowingService showingService;

    @GetMapping("/schedule")
    public ResponseEntity<List<Showing>> getSchedule() {
        List<Showing> showings = showingService.getAllShowings();
        return ResponseEntity.ok(showings);
    }
}
