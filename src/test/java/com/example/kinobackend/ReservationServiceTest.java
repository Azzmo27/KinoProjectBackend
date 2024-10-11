package com.example.kinobackend;

import com.example.kinobackend.model.Customer;
import com.example.kinobackend.model.Movie;
import com.example.kinobackend.model.Seat;
import com.example.kinobackend.model.Showing;
import com.example.kinobackend.model.Ticket;
import com.example.kinobackend.repository.TicketRepository;
import com.example.kinobackend.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testReserveTicket_Success() {
        // Arrange
        Customer customer = new Customer();
        Movie movie = new Movie();
        movie.set3D(false);
        movie.setDuration(120);

        Showing showing = new Showing();
        showing.setMovie(movie);

        Seat seat1 = new Seat();
        seat1.setAvailable(true);
        seat1.setCowboy(false);
        seat1.setSofa(false);

        Seat seat2 = new Seat();
        seat2.setAvailable(true);
        seat2.setCowboy(false);
        seat2.setSofa(true);

        List<Seat> seats = List.of(seat1, seat2);

        Ticket savedTicket = new Ticket();
        when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);

        // Act
        Ticket ticket = reservationService.reserveTicket(showing, seats, customer);

        // Assert
        assertNotNull(ticket);
        assertEquals(27.00, ticket.getPrice()); // 10.00 for seat1, 12.00 for seat2, + 5.00 booking fee
        assertNotNull(ticket.getBookingReference());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    public void testReserveTicket_NoSeatsSelected() {
        // Arrange
        Customer customer = new Customer();
        Showing showing = new Showing();
        List<Seat> seats = new ArrayList<>();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.reserveTicket(showing, seats, customer);
        });
        assertEquals("No seats selected for reservation", exception.getMessage());

        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    public void testReserveTicket_SeatAlreadyReserved() {
        // Arrange
        Customer customer = new Customer();
        Movie movie = new Movie();
        Showing showing = new Showing();
        showing.setMovie(movie);

        Seat seat1 = new Seat();
        seat1.setAvailable(false); // Seat is already reserved
        seat1.setCowboy(false);
        seat1.setSofa(false);

        List<Seat> seats = List.of(seat1);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            reservationService.reserveTicket(showing, seats, customer);
        });
        assertEquals("En eller flere er allerede reserveret", exception.getMessage());

        verify(ticketRepository, never()).save(any(Ticket.class));
    }

    @Test
    public void testReserveTicket_With3DAndLongFilm() {
        // Arrange
        Customer customer = new Customer();
        Movie movie = new Movie();
        movie.set3D(true); // 3D movie
        movie.setDuration(180); // Long film

        Showing showing = new Showing();
        showing.setMovie(movie);

        Seat seat1 = new Seat();
        seat1.setAvailable(true);
        seat1.setCowboy(false);
        seat1.setSofa(false);

        List<Seat> seats = List.of(seat1);

        Ticket savedTicket = new Ticket();
        when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);

        // Act
        Ticket ticket = reservationService.reserveTicket(showing, seats, customer);

        // Assert
        assertNotNull(ticket);
        assertEquals(14.50, ticket.getPrice()); // 10.00 base + 1.50 (3D) + 2.00 (long film) + 5.00 (booking fee)
        assertTrue(ticket.is3D());
        assertTrue(ticket.isLongFilm());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    public void testReserveTicket_GroupDiscount() {
        // Arrange
        Customer customer = new Customer();
        Movie movie = new Movie();
        Showing showing = new Showing();
        showing.setMovie(movie);

        List<Seat> seats = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            Seat seat = new Seat();
            seat.setAvailable(true);
            seat.setCowboy(false);
            seat.setSofa(false);
            seats.add(seat);
        }

        Ticket savedTicket = new Ticket();
        when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);

        // Act
        Ticket ticket = reservationService.reserveTicket(showing, seats, customer);

        // Assert
        assertNotNull(ticket);
        double expectedPrice = (10.00 * 11) * 0.93; // Base price for 11 seats with 7% group discount
        assertEquals(expectedPrice, ticket.getPrice());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }
}
