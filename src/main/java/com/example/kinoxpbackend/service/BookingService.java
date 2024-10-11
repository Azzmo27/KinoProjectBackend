package com.example.kinoxpbackend.service;

import com.example.kinoxpbackend.model.Booking;
import com.example.kinoxpbackend.model.Seat;
import com.example.kinoxpbackend.model.Showing;
import com.example.kinoxpbackend.repository.BookingRepository;
import com.example.kinoxpbackend.repository.SeatRepository;
import com.example.kinoxpbackend.repository.ShowingRepository;
import com.example.kinoxpbackend.repository.TheatreRepository;
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

    @Autowired
    private TheatreRepository theatreRepository;

    public Booking createBooking(String email, Showing showing, Set<Seat> seatsToBeBooked) {
        Booking booking = new Booking();
        booking.setEmail(email);
        booking.setShowing(showing);
        booking.setSeats(seatsToBeBooked);
        return bookingRepository.save(booking);
    }

    public Set<Booking> getAllBookings() {
        return new HashSet<>(bookingRepository.findAll());
    }

    public Booking getBookingById(int id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public Booking updateBooking(int id, Booking updatedBooking) {
        Booking existingBooking = bookingRepository.findById(id).orElse(null);
        if (existingBooking == null) {
            throw new IllegalArgumentException("Booking not found");
        }
        existingBooking.setSeats(updatedBooking.getSeats());
        existingBooking.setShowing(updatedBooking.getShowing());
        existingBooking.setEmail(updatedBooking.getEmail());
        return bookingRepository.save(existingBooking);
    }

    public void deleteBooking(int id) {
        if (!bookingRepository.existsById(id)) {
            throw new IllegalArgumentException("Booking not found");
        }
        bookingRepository.deleteById(id);
    }

    public Set<Seat> getBookedSeatsInShowing(int showingId) {
        return seatRepository.findByBookings_Showing_ShowingId(showingId);
    }

    public Set<Seat> getAllSeatsInShowing(int showingId) {
        Showing showing = showingRepository.getShowingByShowingId(showingId);
        return seatRepository.findByTheatre(showing.getTheatre());
    }
}
