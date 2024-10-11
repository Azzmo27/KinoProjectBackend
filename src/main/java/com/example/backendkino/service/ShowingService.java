package com.example.backendkino.service;

import com.example.backendkino.model.Employee;
import com.example.backendkino.model.Movie;
import com.example.backendkino.model.Showing;
import com.example.backendkino.model.CinemaHall;
import com.example.backendkino.repository.EmployeeRepository;
import com.example.backendkino.repository.MovieRepository;
import com.example.backendkino.repository.ShowingRepository;
import com.example.backendkino.repository.CinemaHallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShowingService {

    @Autowired
    private ShowingRepository showingRepository;

    @Autowired
    private CinemaHallRepository cinemaHallRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Showing> retrieveShowTimesByMovieId(int movieId) {
        return showingRepository.findShowingByMovie_MovieId(movieId);
    }

    public Showing scheduleNewShowing(Showing showing) {
        Movie movie = movieRepository.findById(showing.getMovie().getMovieId()).orElseThrow(
                () -> new RuntimeException("Movie not found"));
        CinemaHall cinemaHall = cinemaHallRepository.findById(showing.getCinemaHall().getCinemaHallId()).orElseThrow(
                () -> new RuntimeException("Cinema Hall not found"));
        Employee employee = employeeRepository.findById(showing.getEmployee().getEmployeeId()).orElseThrow(
                () -> new RuntimeException("Employee not found"));

        LocalDateTime endTime;
        String[] runTime = movie.getRuntime().split(" ");
        int runTimeMinutes = Integer.parseInt(runTime[0]);
        endTime = showing.getDateTime().plusHours(1);
        endTime = endTime.plusMinutes(runTimeMinutes);
        showing.setEndTime(endTime);

        showing.setMovie(movie);
        showing.setCinemaHall(cinemaHall);
        showing.setEmployee(employee);

        return showingRepository.save(showing);
    }

    public boolean validateShowtime(Showing newShowtime) {
        LocalDateTime startDateTime = newShowtime.getDateTime();
        Movie movie = movieRepository.findById(newShowtime.getMovie().getMovieId()).orElseThrow(
                () -> new RuntimeException("Movie not found"));

        List<Showing> existingShowtimes = showingRepository.findByCinemaHallAndDateTime(
                newShowtime.getCinemaHall(),
                startDateTime
        );

        String[] runTime = movie.getRuntime().split(" ");
        int runTimeMinutes = Integer.parseInt(runTime[0]);
        for (Showing eShowtime : existingShowtimes) {
            LocalDateTime cleaningTime = eShowtime.getEndTime().plusMinutes(60 + runTimeMinutes);

            if (startDateTime.isBefore(cleaningTime)) {
                return false;
            }
        }
        return true;
    }
}
