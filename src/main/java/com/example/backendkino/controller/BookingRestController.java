package com.example.backendkino.controller;

import com.example.backendkino.model.*;
import com.example.backendkino.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/bookings")
public class BookingRestController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public ResponseEntity<?> fetchAllBookings() {
        try {
            Set<Booking> bookings = bookingService.retrieveAllBookings();
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching the bookings. Please try again later.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> fetchBookingById(@PathVariable int id) {
        try {
            Booking booking = bookingService.fetchBookingById(id);
            if (booking == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
            }
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching the booking. Please try again later.");
        }
    }

    @PostMapping
    public ResponseEntity<?> createNewBooking(
            @RequestParam Set<Seat> seatsToBeBooked,
            @RequestBody Showing showing,
            @RequestParam String email) {
        try {
            Booking newBooking = bookingService.createNewBooking(email, showing, seatsToBeBooked);
            return ResponseEntity.status(HttpStatus.CREATED).body(newBooking);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating the booking. Please try again later.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifyBooking(
            @PathVariable int id,
            @RequestBody Booking updatedBooking) {
        try {
            Booking updated = bookingService.modifyBooking(id, updatedBooking);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the booking. Please try again later.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeBooking(@PathVariable int id) {
        try {
            bookingService.removeBooking(id);
            return ResponseEntity.ok("Booking deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the booking. Please try again later.");
        }
    }
}
