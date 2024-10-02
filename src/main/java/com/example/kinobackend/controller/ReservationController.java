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

    // Endpoint to reserve tickets
    @PostMapping("/reserve")
    public ResponseEntity<Ticket> reserveTickets(@RequestBody ReservationRequest request) {
        // Validate request input
        if (request.getSeats() == null || request.getSeats().isEmpty() || request.getShowing() == null || request.getCustomer() == null) {
            return ResponseEntity.badRequest().build(); // Return bad request if input is invalid
        }

        // Extract details from the request
        Showing showing = request.getShowing();
        List<Seat> seats = request.getSeats();
        Customer customer = request.getCustomer();

        // Reserve tickets
        Ticket ticket = reservationService.reserveTicket(showing, seats, customer);

        return ResponseEntity.status(HttpStatus.CREATED).body(ticket); // Return the ticket with confirmation details
    }
}
