package com.example.kinoxpbackend.controller;

import com.example.kinoxpbackend.model.*;
import com.example.kinoxpbackend.repository.*;
import com.example.kinoxpbackend.service.BookingService;
import com.example.kinoxpbackend.service.ShowingServiceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@CrossOrigin(origins="*")
@RestController
@RequestMapping("/showing")

public class ShowingRestController {

    @Autowired
    private ShowingRepository showingRepository;

    @Autowired
    ShowingServiceimpl showingService;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;


    @Autowired
    MovieRepository movieRepository;

    @Autowired
    TheatreRepository theatreRepository;

    @Autowired
    AdminRepository adminRepository;

    @GetMapping("/showings/{movieId}")
    public List<Showing> getShowingsByMovieId(@PathVariable int movieId) {
        return showingService.getShowTimesByMovieId(movieId); // Implement service to fetch showings
    }

    @GetMapping("/theatres")
    public List<Theatre> getAllTheatres(){
        return theatreRepository.findAll();
    }

    @GetMapping("/admins")
    public List<Admin> getAllAdmins(){
        return adminRepository.findAll();
    }

    @PostMapping("/create")
    public ResponseEntity<Showing> createShowing(@RequestBody Showing showtime) {
        // Fetch Movie by movieId from the showtime object
        Movie movie = movieRepository.findMovieByMovieId(showtime.getMovie().getMovieId());
        if (movie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return 404 if movie not found
        }

        // Fetch Admin by adminId from the showtime object
        Admin admin = adminRepository.findById(showtime.getAdmin().getAdminId()).orElse(null);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return 404 if admin not found
        }

        // Fetch Theatre by theatreId from the showtime object
        Theatre theatre = theatreRepository.findById(showtime.getTheatre().getTheatreId()).orElse(null);
        if (theatre == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return 404 if theatre not found
        }

        // Validate the showtime (overlaps, etc.)
        if (!showingService.isShowtimeValid(showtime)) {
            return ResponseEntity.badRequest().body(null); // Return 400 if the showtime is invalid
        }

        // Call the service to create the showing
        Showing createdShowtime = showingService.createShowing(showtime);

        return new ResponseEntity<>(createdShowtime, HttpStatus.CREATED); // Return 201 with the created showing
    }


    @DeleteMapping("/showing/delete/{id}")
    public ResponseEntity<String> deleteShowing(@PathVariable int id) {
        if (!showingRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Showing not found\"}");
        }
        showingRepository.deleteById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"message\": \"Movie deleted successfully\"}");
    }


    @GetMapping("/{showingId}/seats")
    public ResponseEntity<Map<String, Object>> getSeatsForShowing(@PathVariable int showingId) {
        Set<Seat> bookedSeats = bookingService.getBookedSeatsInShowing(showingId);
        Set<Seat> allSeats = bookingService.getAllSeatsInShowing(showingId);
        Theatre theatre = showingRepository.getShowingByShowingId(showingId).getTheatre();



        // Creating the response map
        Map<String, Object> response = new HashMap<>();
        response.put("bookedSeats", bookedSeats);
        response.put("allSeats", allSeats);
        response.put("seatRows", theatre.getSeatRows());  // Include seatRows
        response.put("seatsPerRow", theatre.getSeatsPerRow());// Include seatsPerRow


        return ResponseEntity.ok(response);
    }



    // Gem en booking
    @PostMapping("/booking/{showingId}")
    public ResponseEntity<Booking> saveBooking(
            @PathVariable int showingId,
            @RequestParam String email,
            @RequestBody List<Integer> seatIds) {

        // Find the showing based on showingId
        Showing showing = showingRepository.getShowingByShowingId(showingId);

        // Find seats based on seatIds
        List<Seat> selectedSeats = seatRepository.findAllById(seatIds);

        if (selectedSeats.size() != seatIds.size()) {
            return ResponseEntity.badRequest().build();
        }

        // Create a new booking with the selected seats and user's email
        Booking newBooking = new Booking(new HashSet<>(selectedSeats), showing, email);

        // Save the booking in the database
        Booking savedBooking = bookingRepository.save(newBooking);

        // Return the saved booking as the response
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
    }

}





