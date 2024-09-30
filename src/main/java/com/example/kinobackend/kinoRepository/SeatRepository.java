package com.example.kinobackend.kinoRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.kinobackend.model.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {

}
