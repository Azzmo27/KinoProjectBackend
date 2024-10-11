package com.example.backendkino.controller;

import com.example.backendkino.model.CinemaHall;
import com.example.backendkino.service.CinemaHallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cinemaHall")
@CrossOrigin
public class CinemaHallRestController {

    @Autowired
    private CinemaHallService cinemaHallService;

    @PostMapping("/create")
    public ResponseEntity<?> addCinemaHall(@RequestBody CinemaHall cinemaHall) {
        try {
            CinemaHall createdCinemaHall = cinemaHallService.initializeCinemaHall(cinemaHall.getSeatRows(), cinemaHall.getSeatsPerRow());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCinemaHall);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to create cinema hall. Please try again later.");
        }
    }
}
