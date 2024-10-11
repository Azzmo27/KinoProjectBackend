package com.example.backendkino.service;

import com.example.backendkino.model.CinemaHall;
import com.example.backendkino.model.Seat;
import com.example.backendkino.repository.CinemaHallRepository;
import com.example.backendkino.repository.SeatRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CinemaHallService {

    @Autowired
    private CinemaHallRepository cinemaHallRepository;

    @Autowired
    private SeatRepository seatRepository;

    public CinemaHall initializeCinemaHall(int seatRows, int seatsPerRow) {
        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setSeatRows(seatRows);
        cinemaHall.setSeatsPerRow(seatsPerRow);
        CinemaHall savedCinemaHall = cinemaHallRepository.save(cinemaHall);


        for (int row = 1; row <= seatRows; row++) {
            for (int seatNum = 1; seatNum <= seatsPerRow; seatNum++) {
                Seat seat = new Seat();
                seat.setRowNumber(row);
                seat.setSeatNumber(seatNum);
                seat.setCinemaHall(savedCinemaHall);
                seatRepository.save(seat);
            }
        }

        return savedCinemaHall;
    }

}