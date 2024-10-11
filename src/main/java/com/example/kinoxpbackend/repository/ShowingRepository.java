package com.example.kinoxpbackend.repository;

import com.example.kinoxpbackend.model.Showing;
import com.example.kinoxpbackend.model.Theatre;
import com.example.kinoxpbackend.model.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface ShowingRepository extends JpaRepository<Showing, Integer> {

    // Find showtimes for a specific theatre and within a date range
    List<Showing> findByTheatreAndDateTime(Theatre theatre, LocalDateTime startDate);

    // Find showtimes by movie ID (this method should exist as well)
    List<Showing> findShowingByMovie_MovieId(int movieId);
    Showing getShowingByShowingId(int showingId);

}