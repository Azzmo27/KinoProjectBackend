package com.example.backendkino;

import com.example.backendkino.model.Booking;
import com.example.backendkino.model.Seat;
import com.example.backendkino.model.Showing;
import com.example.backendkino.repository.BookingRepository;
import com.example.backendkino.repository.SeatRepository;
import com.example.backendkino.repository.ShowingRepository;
import com.example.backendkino.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ShowingRepository showingRepository;

    @Mock
    private SeatRepository seatRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllBookings() {
        Set<Booking> bookings = new HashSet<>();
        when(bookingRepository.findAll()).thenReturn(new ArrayList<>(bookings));

        Set<Booking> result = bookingService.retrieveAllBookings();

        assertNotNull(result);
        assertEquals(bookings.size(), result.size());
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void testFetchBookingById() {
        int bookingId = 1;
        Booking booking = new Booking();
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        Booking result = bookingService.fetchBookingById(bookingId);

        assertNotNull(result);
        assertEquals(booking, result);
        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    void testFetchBookingById_NotFound() {
        int bookingId = 1;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        Booking result = bookingService.fetchBookingById(bookingId);

        assertNull(result);
        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    void testModifyBooking_Success() {
        int bookingId = 1;
        Booking existingBooking = new Booking();
        Booking updatedBooking = new Booking();
        updatedBooking.setEmail("newemail@example.com");

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(existingBooking));
        when(bookingRepository.save(existingBooking)).thenReturn(existingBooking);

        Booking result = bookingService.modifyBooking(bookingId, updatedBooking);

        assertNotNull(result);
        assertEquals("newemail@example.com", existingBooking.getEmail());
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(bookingRepository, times(1)).save(existingBooking);
    }

    @Test
    void testModifyBooking_BookingNotFound() {
        int bookingId = 1;
        Booking updatedBooking = new Booking();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.modifyBooking(bookingId, updatedBooking);
        });

        verify(bookingRepository, times(1)).findById(bookingId);
    }

    @Test
    void testRemoveBooking_Success() {
        int bookingId = 1;
        when(bookingRepository.existsById(bookingId)).thenReturn(true);

        bookingService.removeBooking(bookingId);

        verify(bookingRepository, times(1)).existsById(bookingId);
        verify(bookingRepository, times(1)).deleteById(bookingId);
    }

    @Test
    void testRemoveBooking_NotFound() {
        int bookingId = 1;
        when(bookingRepository.existsById(bookingId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.removeBooking(bookingId);
        });

        verify(bookingRepository, times(1)).existsById(bookingId);
        verify(bookingRepository, times(0)).deleteById(bookingId);
    }

    @Test
    void testCreateNewBooking() {
        String email = "test@example.com";
        Showing showing = new Showing();
        Set<Seat> seatsToBeBooked = new HashSet<>();

        Booking booking = new Booking();
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.createNewBooking(email, showing, seatsToBeBooked);

        assertNotNull(result);
        assertEquals(booking, result);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testRetrieveBookedSeatsInShowing() {
        int showingId = 1;
        Set<Seat> seats = new HashSet<>();
        when(seatRepository.findByBookings_Showing_ShowingId(showingId)).thenReturn(seats);

        Set<Seat> result = bookingService.retrieveBookedSeatsInShowing(showingId);

        assertNotNull(result);
        assertEquals(seats.size(), result.size());
        verify(seatRepository, times(1)).findByBookings_Showing_ShowingId(showingId);
    }

    @Test
    void testFetchAllSeatsInShowing() {
        int showingId = 1;
        Showing showing = new Showing();
        Set<Seat> seats = new HashSet<>();
        when(showingRepository.getShowingByShowingId(showingId)).thenReturn(showing);
        when(seatRepository.findByCinemaHall(showing.getCinemaHall())).thenReturn(seats);

        Set<Seat> result = bookingService.fetchAllSeatsInShowing(showingId);

        assertNotNull(result);
        assertEquals(seats.size(), result.size());
        verify(showingRepository, times(1)).getShowingByShowingId(showingId);
        verify(seatRepository, times(1)).findByCinemaHall(showing.getCinemaHall());
    }
}
