package com.example.kinoxpbackend.repository;

import com.example.kinoxpbackend.model.Booking;
import com.example.kinoxpbackend.model.Seat;
import com.example.kinoxpbackend.model.Theatre;
import com.example.kinoxpbackend.model.Booking;
import com.example.kinoxpbackend.model.Seat;
import com.example.kinoxpbackend.model.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface SeatRepository extends JpaRepository<Seat, Integer> {
    Set<Seat> findByTheatre(Theatre theatre);
    Set<Seat> getSeatsByBookings(Set<Booking> bookings);
    List<Seat> findAllBySeatIdIn(List<Integer> seatIds);
    Set<Seat> getSeatsByTheatre(Theatre theatre);
    Seat getSeatsBySeatId(int seatId);
    Set<Seat> findByBookings_Showing_ShowingId(int showingId);
    
}
