package com.example.kinobackend;

<<<<<<< HEAD
public class SeatServiceTest {

=======
import com.example.kinobackend.model.Seat;
import com.example.kinobackend.model.Showing;
import com.example.kinobackend.repository.SeatRepository;
import com.example.kinobackend.repository.ShowingRepository;
import com.example.kinobackend.service.SeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private ShowingRepository showingRepository;

    @InjectMocks
    private SeatService seatService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testReserveSeats_Success() {
        // Arrange
        Seat seat1 = new Seat();
        seat1.setId(1);
        seat1.setReserved(false);

        Seat seat2 = new Seat();
        seat2.setId(2);
        seat2.setReserved(false);

        List<Seat> seatsToReserve = List.of(seat1, seat2);

        when(seatRepository.findById(1)).thenReturn(Optional.of(seat1));
        when(seatRepository.findById(2)).thenReturn(Optional.of(seat2));

        // Act
        seatService.reserveSeats(seatsToReserve);

        // Assert
        assertTrue(seat1.isReserved());
        assertTrue(seat2.isReserved());

        verify(seatRepository, times(2)).save(any(Seat.class));
    }

    @Test
    public void testReserveSeats_AlreadyReserved() {
        // Arrange
        Seat seat = new Seat();
        seat.setId(1);
        seat.setReserved(true); // Seat is already reserved

        List<Seat> seatsToReserve = List.of(seat);

        when(seatRepository.findById(1)).thenReturn(Optional.of(seat));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            seatService.reserveSeats(seatsToReserve);
        });
        assertEquals("Seat 1 is already reserved.", exception.getMessage());

        verify(seatRepository, never()).save(any(Seat.class)); // Save should not be called
    }

    @Test
    public void testAdjustReservation_ReserveSeat() {
        // Arrange
        Seat seat = new Seat();
        seat.setId(1);
        seat.setReserved(false);

        when(seatRepository.findById(1)).thenReturn(Optional.of(seat));

        // Act
        seatService.adjustReservation(1, true);

        // Assert
        assertTrue(seat.isReserved());
        assertFalse(seat.isAvailable());

        verify(seatRepository, times(1)).save(seat);
    }

    @Test
    public void testAdjustReservation_UnreserveSeat() {
        // Arrange
        Seat seat = new Seat();
        seat.setId(1);
        seat.setReserved(true);

        when(seatRepository.findById(1)).thenReturn(Optional.of(seat));

        // Act
        seatService.adjustReservation(1, false);

        // Assert
        assertFalse(seat.isReserved());
        assertTrue(seat.isAvailable());

        verify(seatRepository, times(1)).save(seat);
    }

    @Test
    public void testGetAvailableSeatsForShowing() {
        // Arrange
        Seat seat1 = new Seat();
        seat1.setAvailable(true);

        Seat seat2 = new Seat();
        seat2.setAvailable(false);

        Showing showing = new Showing();
        showing.setId(1);
        showing.setSeats(List.of(seat1, seat2));

        when(showingRepository.findById(1)).thenReturn(Optional.of(showing));

        // Act
        List<Seat> availableSeats = seatService.getAvailableSeatsForShowing(1);

        // Assert
        assertEquals(1, availableSeats.size());
        assertTrue(availableSeats.contains(seat1));
        assertFalse(availableSeats.contains(seat2));

        verify(showingRepository, times(1)).findById(1);
    }

    @Test
    public void testGetAvailableSeatsForShowing_ShowingNotFound() {
        // Arrange
        when(showingRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            seatService.getAvailableSeatsForShowing(1);
        });
        assertEquals("Showing not found", exception.getMessage());

        verify(showingRepository, times(1)).findById(1);
    }
>>>>>>> 7d4bf1c412254ba96aeac7af8d4e7efd5a74effc
}
