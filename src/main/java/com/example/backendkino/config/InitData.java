package com.example.backendkino.config;

import com.example.backendkino.model.*;
import com.example.backendkino.repository.*;
import com.example.backendkino.service.MovieService;
import com.example.backendkino.service.ShowingService;
import com.example.backendkino.service.CinemaHallService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class InitData {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CinemaHallRepository cinemaHallRepository;

    @Autowired
    private CinemaHallService cinemaHallService;

    @Autowired
    private ShowingRepository showingRepository;

    @Autowired
    private ShowingService showingService;

    @Autowired
    private MovieService movieService;

    @PostConstruct
    public void initializeData() {
        setupMovies();
        setupEmployee();
        setupCinemas();
        setupShowings();
    }

    private void setupMovies() {
        if (movieRepository.count() == 0) {
            List<Movie> movies = movieService.fetchAllMovies();
            movieRepository.saveAll(movies);
        } else {
            System.out.println("Movies is available in the database.");
        }
    }

    private void setupEmployee() {
        if (employeeRepository.count() == 0) {
            Employee employee = new Employee();
            employee.setUsername("employee");
            employee.setFullName("employee");
            employee.setPassword("1234");
            employeeRepository.save(employee);
        } else {
            System.out.println("Employee user already exists in the database.");
        }
    }

    private void setupCinemas() {
        if (cinemaHallRepository.count() == 0) {
            cinemaHallService.initializeCinemaHall(10, 10);
            cinemaHallService.initializeCinemaHall(25, 16);
        } else {
            System.out.println("Cinema Halls are already set up in the database.");
        }
    }

    private void setupShowings() {
        if (showingRepository.count() == 0) {
            Showing showing1 = new Showing(
                    1,
                    LocalDateTime.of(2024, 11, 5, 16, 30),
                    cinemaHallRepository.getCinemaHallsByCinemaHallId(1),
                    movieRepository.findMovieByMovieId(1),
                    employeeRepository.findByUsername("employee").get(),
                    LocalDateTime.of(2024, 11, 5, 14, 00)
            );
            showingService.scheduleNewShowing(showing1);

            Showing showing2 = new Showing(
                    2,
                    LocalDateTime.of(2024, 11, 6, 19, 00),
                    cinemaHallRepository.getCinemaHallsByCinemaHallId(2),
                    movieRepository.findMovieByMovieId(2),
                    employeeRepository.findByUsername("employee").get(),
                    LocalDateTime.of(2024, 11, 5, 16, 30)
            );
            showingService.scheduleNewShowing(showing2);
        } else {
            System.out.println("Showings are already configured in the database.");
        }
    }
}
