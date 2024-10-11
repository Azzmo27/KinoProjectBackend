package com.example.backendkino.repository;

import com.example.backendkino.model.Seat;
import com.example.backendkino.model.CinemaHall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface SeatRepository extends JpaRepository<Seat, Integer> {

    Set<Seat> findByBookings_Showing_ShowingId(int showingId);

    Set<Seat> findByCinemaHall(CinemaHall cinemaHall);

}
