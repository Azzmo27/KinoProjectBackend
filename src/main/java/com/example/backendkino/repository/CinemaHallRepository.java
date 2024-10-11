package com.example.backendkino.repository;

import com.example.backendkino.model.CinemaHall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CinemaHallRepository extends JpaRepository<CinemaHall, Integer> {

    CinemaHall getCinemaHallsByCinemaHallId(int cinemaHallId);

    Optional<CinemaHall> findById(Integer cinemaHallId);

}
