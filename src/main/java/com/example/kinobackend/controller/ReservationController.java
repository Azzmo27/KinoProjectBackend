package com.example.kinobackend.controller;

import com.example.kinobackend.dto.ReservationRequest;
import com.example.kinobackend.model.Customer;
import com.example.kinobackend.model.Seat;
import com.example.kinobackend.model.Showing;
import com.example.kinobackend.model.Ticket;
import com.example.kinobackend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/reserve")
    public ResponseEntity<Ticket> reserveTickets(@RequestBody ReservationRequest request) {

        if (request.getSeats() == null || request.getSeats().isEmpty() || request.getShowing() == null || request.getCustomer() == null) {
            return ResponseEntity.badRequest().build();
        }

        Showing showing = request.getShowing();
        List<Seat> seats = request.getSeats();
        Customer customer = request.getCustomer();

        Ticket ticket = reservationService.reserveTicket(showing, seats, customer);

        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }
}
