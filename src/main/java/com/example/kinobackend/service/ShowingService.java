package com.example.kinobackend.service;

import com.example.kinobackend.model.Showing;
import com.example.kinobackend.repository.ShowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShowingService {

    @Autowired
    private ShowingRepository showingRepository;

    public List<Showing> getAllShowings() {
        return showingRepository.findAll();
    }

    public Optional<Showing> getShowingById(int id) {
        return showingRepository.findById(id);
    }

    public Showing createShowing(Showing showing) {
        return showingRepository.save(showing);
    }

    public Showing updateShowing(int id, Showing updatedShowing) {
        if (showingRepository.existsById(id)) {
            updatedShowing.setId(id); // Set the ID of the updated showing
            return showingRepository.save(updatedShowing);
        }
        return null; // Or throw an exception
    }

    public void deleteShowing(int id) {
        if (showingRepository.existsById(id)) {
            showingRepository.deleteById(id);
        }

    }
}
