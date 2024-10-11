package com.example.backendkino.repository;

import com.example.backendkino.model.Showing;
import com.example.backendkino.model.CinemaHall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowingRepository extends JpaRepository<Showing, Integer> {

    Showing getShowingByShowingId(int showingId);

    List<Showing> findShowingByMovie_MovieId(int movieId);

    List<Showing> findByCinemaHallAndDateTime(CinemaHall cinemaHall, LocalDateTime startDate);

}
