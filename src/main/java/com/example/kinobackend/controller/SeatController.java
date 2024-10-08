package com.example.kinobackend.controller;

import com.example.kinobackend.model.Seat;
import com.example.kinobackend.model.Showing;
import com.example.kinobackend.service.SeatService;
import com.example.kinobackend.service.ShowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @Autowired
    private ShowingService showingService;

    @PostMapping("/reserve")
    public ResponseEntity<Void> reserveSeats(@RequestBody List<Seat> seats) {
        seatService.reserveSeats(seats);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{seatId}/adjust")
    public ResponseEntity<Void> adjustReservation(@PathVariable int seatId, @RequestParam boolean reserve) {
        seatService.adjustReservation(seatId, reserve);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/available-seats")
    public ResponseEntity<List<Seat>> getAvailableSeats(@PathVariable int id) {
        Showing showing = showingService.getShowingById(id)
                .orElseThrow(() -> new RuntimeException("showing is not found"));

        List<Seat> availableSeats = showing.getAvailableSeats();
        return ResponseEntity.ok(availableSeats);
    }
}
