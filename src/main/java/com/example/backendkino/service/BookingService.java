package com.example.backendkino.service;

import com.example.backendkino.model.Booking;
import com.example.backendkino.model.Seat;
import com.example.backendkino.model.Showing;
import com.example.backendkino.repository.BookingRepository;
import com.example.backendkino.repository.SeatRepository;
import com.example.backendkino.repository.ShowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowingRepository showingRepository;

    @Autowired
    private SeatRepository seatRepository;

    public Set<Booking> retrieveAllBookings() {
        return new HashSet<>(bookingRepository.findAll());
    }

    public Booking fetchBookingById(int id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public Booking modifyBooking(int id, Booking updatedBooking) {
        Booking existingBooking = bookingRepository.findById(id).orElse(null);
        if (existingBooking == null) {
            throw new IllegalArgumentException("Booking not found");
        }
        existingBooking.setSeats(updatedBooking.getSeats());
        existingBooking.setShowing(updatedBooking.getShowing());
        existingBooking.setEmail(updatedBooking.getEmail());
        return bookingRepository.save(existingBooking);
    }

    public void removeBooking(int id) {
        if (!bookingRepository.existsById(id)) {
            throw new IllegalArgumentException("Booking not found");
        }
        bookingRepository.deleteById(id);
    }

    public Booking createNewBooking(String email, Showing showing, Set<Seat> seatsToBeBooked) {
        Booking booking = new Booking();
        booking.setEmail(email);
        booking.setShowing(showing);
        booking.setSeats(seatsToBeBooked);
        return bookingRepository.save(booking);
    }

    public Set<Seat> retrieveBookedSeatsInShowing(int showingId) {
        return seatRepository.findByBookings_Showing_ShowingId(showingId);
    }

    public Set<Seat> fetchAllSeatsInShowing(int showingId) {
        Showing showing = showingRepository.getShowingByShowingId(showingId);
        return seatRepository.findByCinemaHall(showing.getCinemaHall());
    }
}
