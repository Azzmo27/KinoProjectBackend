package com.example.kinobackend;

import com.example.kinobackend.model.Showing;
import com.example.kinobackend.repository.ShowingRepository;
import com.example.kinobackend.service.ShowingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testng.AssertJUnit.*;

public class ShowingServiceTest {

    @Mock
    private ShowingRepository showingRepository;

    @InjectMocks
    private ShowingService showingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllShowings() {
        // Arrange
        List<Showing> expectedShowings = Arrays.asList(new Showing(), new Showing());
        when(showingRepository.findAll()).thenReturn(expectedShowings);

        // Act
        List<Showing> actualShowings = showingService.getAllShowings();

        // Assert
        assertEquals(expectedShowings, actualShowings);
        verify(showingRepository, times(1)).findAll();
    }

    @Test
    public void testGetShowingById_Found() {
        // Arrange
        Showing expectedShowing = new Showing();
        when(showingRepository.findById(1)).thenReturn(Optional.of(expectedShowing));

        // Act
        Optional<Showing> actualShowing = showingService.getShowingById(1);

        // Assert
        assertTrue(actualShowing.isPresent());
        assertEquals(expectedShowing, actualShowing.get());
        verify(showingRepository, times(1)).findById(1);
    }

    @Test
    public void testGetShowingById_NotFound() {
        // Arrange
        when(showingRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        Optional<Showing> actualShowing = showingService.getShowingById(1);

        // Assert
        assertFalse(actualShowing.isPresent());
        verify(showingRepository, times(1)).findById(1);
    }

    @Test
    public void testCreateShowing() {
        // Arrange
        Showing newShowing = new Showing();
        when(showingRepository.save(newShowing)).thenReturn(newShowing);

        // Act
        Showing createdShowing = showingService.createShowing(newShowing);

        // Assert
        assertEquals(newShowing, createdShowing);
        verify(showingRepository, times(1)).save(newShowing);
    }

    @Test
    public void testUpdateShowing_Found() {
        // Arrange
        int id = 1;
        Showing updatedShowing = new Showing();
        when(showingRepository.existsById(id)).thenReturn(true);
        when(showingRepository.save(updatedShowing)).thenReturn(updatedShowing);

        // Act
        Showing result = showingService.updateShowing(id, updatedShowing);

        // Assert
        assertNotNull(result);
        assertEquals(updatedShowing, result);
        verify(showingRepository, times(1)).existsById(id);
        verify(showingRepository, times(1)).save(updatedShowing);
    }

    @Test
    public void testUpdateShowing_NotFound() {
        // Arrange
        int id = 1;
        Showing updatedShowing = new Showing();
        when(showingRepository.existsById(id)).thenReturn(false);

        // Act
        Showing result = showingService.updateShowing(id, updatedShowing);

        // Assert
        assertNull(result);
        verify(showingRepository, times(1)).existsById(id);
        verify(showingRepository, times(0)).save(updatedShowing);
    }

    @Test
    public void testDeleteShowing_Found() {
        // Arrange
        int id = 1;
        when(showingRepository.existsById(id)).thenReturn(true);

        // Act
        showingService.deleteShowing(id);

        // Assert
        verify(showingRepository, times(1)).existsById(id);
        verify(showingRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteShowing_NotFound() {
        // Arrange
        int id = 1;
        when(showingRepository.existsById(id)).thenReturn(false);

        // Act
        showingService.deleteShowing(id);

        // Assert
        verify(showingRepository, times(1)).existsById(id);
        verify(showingRepository, times(0)).deleteById(id);
    }
}


