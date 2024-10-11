package com.example.kinoxpbackend.service;

import com.example.kinoxpbackend.model.Admin;
import com.example.kinoxpbackend.model.Movie;
import com.example.kinoxpbackend.model.Showing;
import com.example.kinoxpbackend.model.Theatre;
import com.example.kinoxpbackend.repository.AdminRepository;
import com.example.kinoxpbackend.repository.MovieRepository;
import com.example.kinoxpbackend.repository.ShowingRepository;
import com.example.kinoxpbackend.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShowingServiceimpl implements ApiServicegetShowing {

    @Autowired
    private ShowingRepository showingRepository;

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private AdminRepository adminRepository;



    @Override
    public Showing createShowing(Showing showing) {
        // Hent film og teater fra databasen baseret på de sendte ID'er
        Movie movie = movieRepository.findById(showing.getMovie().getMovieId()).orElseThrow(
                () -> new RuntimeException("Movie not found"));
        Theatre theater = theatreRepository.findById(showing.getTheatre().getTheatreId()).orElseThrow(
                () -> new RuntimeException("Theater not found"));
        Admin admin = adminRepository.findById(showing.getAdmin().getAdminId()).orElseThrow(
                () -> new RuntimeException("Admin not found")
        );

        LocalDateTime endTime;
        String[] runTime = movie.getRuntime().split(" ");
        int runTimeMinutes =  Integer.valueOf(runTime[0]);
        endTime = showing.getDateTime().plusHours(1);
        endTime = endTime.plusMinutes(runTimeMinutes);
        showing.setEndTime(endTime);

        // Sæt de hentede film og teater ind i showtime
        showing.setMovie(movie);
        showing.setTheatre(theater);
        showing.setAdmin(admin);

        // Gem showtime
        return showingRepository.save(showing);
    }

    public List<Showing> getShowTimesByMovieId(int movieId) {
        return showingRepository.findShowingByMovie_MovieId(movieId);
    }

    public boolean isShowtimeValid(Showing newShowtime) {
        // Assuming that you want to check for showtimes on the same date and that the new showtime has a valid end time
        LocalDateTime startDateTime = newShowtime.getDateTime();
        Movie movie = movieRepository.findById(newShowtime.getMovie().getMovieId()).orElseThrow(
                () -> new RuntimeException("Movie not found"));

        List<Showing> existingShowtimes = showingRepository.findByTheatreAndDateTime(
                newShowtime.getTheatre(),
                startDateTime

        );
        String[] runTime =movie.getRuntime().split(" ");
        int runTimeMinutes =  Integer.valueOf(runTime[0]);
        for (Showing eShowtime : existingShowtimes) {
            LocalDateTime cleaning = eShowtime.getEndTime().plusMinutes(60+runTimeMinutes);

            // Check for overlaps
            if (startDateTime.isBefore(cleaning)) {
                return false; // Overlap detected
            }
        }
        return true; // No overlap
    }


//    public Showing createShowing(Theatre theatre, Movie movie, LocalDateTime startTime, Admin admin, LocalDateTime endTime) {
//        Showing showing = new Showing();
//        showing.setTheatre(theatre);
//        showing.setMovie(movie);
//        showing.setDateTime(startTime);
//        showing.setEndTime(endTime); // Set the end time as well
//
//        return showingRepository.save(showing);
//    }
//
//
//    // Fetch all showings for a specific movie by movieId
//    public List<Showing> getShowingsByMovieId(int movieId) {
//        // Assuming your ShowingRepository has a method to find showings by movie ID
//        return showingRepository.findByMovie_MovieId(movieId);
//
//    }
}
