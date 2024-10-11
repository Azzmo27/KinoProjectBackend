package com.example.backendkino.controller;

import com.example.backendkino.model.*;
import com.example.backendkino.repository.*;
import com.example.backendkino.service.BookingService;
import com.example.backendkino.service.ShowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/showing")
public class ShowingRestController {

    @Autowired
    private ShowingRepository showingRepository;

    @Autowired
    private ShowingService showingService;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CinemaHallRepository cinemaHallRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/showings/{movieId}")
    public List<Showing> fetchShowingsByMovieId(@PathVariable int movieId) {
        return showingService.retrieveShowTimesByMovieId(movieId);
    }

    @GetMapping("/cinema-halls")
    public List<CinemaHall> fetchAllCinemaHalls() {
        return cinemaHallRepository.findAll();
    }

    @GetMapping("/admins")
    public List<Employee> fetchAllAdmins() {
        return employeeRepository.findAll();
    }

    @PostMapping("/create")
    public ResponseEntity<Showing> createNewShowing(@RequestBody Showing showtime) {
        Movie movie = movieRepository.findMovieByMovieId(showtime.getMovie().getMovieId());
        if (movie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Employee employee = employeeRepository.findById(showtime.getEmployee().getEmployeeId()).orElse(null);
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        CinemaHall theatre = cinemaHallRepository.findById(showtime.getCinemaHall().getCinemaHallId()).orElse(null);
        if (theatre == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (!showingService.validateShowtime(showtime)) {
            return ResponseEntity.badRequest().body(null);
        }

        Showing createdShowtime = showingService.scheduleNewShowing(showtime);
        return new ResponseEntity<>(createdShowtime, HttpStatus.CREATED);
    }

    @DeleteMapping("/showing/delete/{id}")
    public ResponseEntity<String> removeShowing(@PathVariable int id) {
        if (!showingRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Showing not found\"}");
        }
        showingRepository.deleteById(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"message\": \"Showing deleted successfully\"}");
    }

    @GetMapping("/{showingId}/seats")
    public ResponseEntity<Map<String, Object>> fetchSeatsForShowing(@PathVariable int showingId) {
        Set<Seat> bookedSeats = bookingService.retrieveBookedSeatsInShowing(showingId);
        Set<Seat> allSeats = bookingService.fetchAllSeatsInShowing(showingId);
        CinemaHall cinemaHall = showingRepository.getShowingByShowingId(showingId).getCinemaHall();

        Map<String, Object> response = new HashMap<>();
        response.put("bookedSeats", bookedSeats);
        response.put("allSeats", allSeats);
        response.put("seatRows", cinemaHall.getSeatRows());
        response.put("seatsPerRow", cinemaHall.getSeatsPerRow());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/booking/{showingId}")
    public ResponseEntity<Booking> saveNewBooking(
            @PathVariable int showingId,
            @RequestParam String email,
            @RequestBody List<Integer> seatIds) {

        Showing showing = showingRepository.getShowingByShowingId(showingId);
        List<Seat> selectedSeats = seatRepository.findAllById(seatIds);

        if (selectedSeats.size() != seatIds.size()) {
            return ResponseEntity.badRequest().build();
        }

        Booking newBooking = new Booking(new HashSet<>(selectedSeats), showing, email);
        Booking savedBooking = bookingRepository.save(newBooking);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
    }
}
