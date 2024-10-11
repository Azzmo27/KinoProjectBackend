package com.example.kinoxpbackend.config;
import com.example.kinoxpbackend.model.Admin;
import com.example.kinoxpbackend.model.Movie;
import com.example.kinoxpbackend.model.Showing;
import com.example.kinoxpbackend.repository.*;
import com.example.kinoxpbackend.service.ApiServiceGetMovies;
import com.example.kinoxpbackend.service.BookingService;
import com.example.kinoxpbackend.service.ShowingServiceimpl;
import com.example.kinoxpbackend.service.TheatreService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;
import java.util.List;


@Configuration
public class InitDatabase {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private TheatreService theatreService;

    @Autowired
    private ShowingRepository showingRepository;

    @Autowired
    private ShowingServiceimpl showingServiceimpl;

    @Autowired
    private ApiServiceGetMovies apiServiceGetMovies;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private BookingService bookingService;

    @PostConstruct
    public void init() {
        //Fetch movies from OMDB if none exists in our database
        if (movieRepository.count() == 0) {
            List<Movie> movies = apiServiceGetMovies.getMovies();
            movieRepository.saveAll(movies);
        } else {
            System.out.println("Database already populated with movies.");
        }

        //Create root user if no admin exists
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setUsername("root");
            admin.setFullName("root");
            admin.setPassword("root");
            adminRepository.save(admin);
        } else {
            System.out.println("Database already has at least one Admin user.");
        }

        //Create two theatres if none exists
        //Also creates seats
        if (theatreRepository.count() == 0) {
            theatreService.createTheatre(10, 10);
            theatreService.createTheatre(25, 16);
        } else {
            System.out.println("Database already has at least one theatre.");
        }

        //Create two showings, one for each theatre if none exists
        if (showingRepository.count() == 0) {
            Showing showing1 = new Showing(
                    1,
                    LocalDateTime.of(2024, 10, 31, 14, 00),
                    theatreRepository.getTheatresByTheatreId(1),
                    movieRepository.findMovieByMovieId(1),
                    adminRepository.findByUsername("root").get(),
                    LocalDateTime.of(2024, 10, 31, 12, 00)
                    );
            showingServiceimpl.createShowing(showing1);

            Showing showing2 = new Showing(
                    2,
                    LocalDateTime.of(2024, 10, 31, 18, 00),
                    theatreRepository.getTheatresByTheatreId(2),
                    movieRepository.findMovieByMovieId(2),
                    adminRepository.findByUsername("root").get(),
                    LocalDateTime.of(2024, 10, 30, 16, 30)
            );
            showingServiceimpl.createShowing(showing2);
        } else {
            System.out.println("Database already has at least one showing.");
        }
        /*
        if (bookingRepository.count() == 0) {
            Showing showing = showingRepository.getShowingByShowingId(1);
            Seat seat1 = seatRepository.getSeatsBySeatId(1);
            Seat seat2 = seatRepository.getSeatsBySeatId(2);
            Seat seat3 = seatRepository.getSeatsBySeatId(3);
            Seat seat4 = seatRepository.getSeatsBySeatId(4);

            // Explicitly save or re-fetch the seats to ensure they are attached to the current session
            Set<Seat> bookedSeats = Set.of(seat1,seat2,seat3,seat4);

            // Save both bookings to the repository using managed seats
            bookingService.createBooking("tislård@gmail.com", showing, bookedSeats);
        } else {
            System.out.println("Database already has at least one booking.");
        }

    */

    }
}