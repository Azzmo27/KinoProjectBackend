package com.example.kinobackend.service;

import com.example.kinobackend.model.Seat;
import com.example.kinobackend.model.Showing;
import com.example.kinobackend.repository.SeatRepository;
import com.example.kinobackend.repository.ShowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ShowingRepository showingRepository;


    public void reserveSeats(List<Seat> seats) {
        for (Seat seat : seats) {
            Seat existingSeat = seatRepository.findById(seat.getId())
                    .orElseThrow(() -> new RuntimeException("Seat not found"));


            if (existingSeat.isReserved()) {
                throw new RuntimeException("Seat " + existingSeat.getId() + " is already reserved.");
            }

            // Reserve the seat
            existingSeat.setReserved(true);
            seatRepository.save(existingSeat);
        }
    }


    public void adjustReservation(int seatId, boolean reserve) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        seat.setReserved(reserve);
        seat.setAvailable(!reserve);
        seatRepository.save(seat);
    }


    public List<Seat> getAvailableSeatsForShowing(int showingId) {
        Showing showing = showingRepository.findById(showingId)
                .orElseThrow(() -> new RuntimeException("Showing not found"));


        List<Seat> seats = showing.getSeats();


        return seats.stream()
                .filter(Seat::isAvailable)
                .collect(Collectors.toList());
    }
}
