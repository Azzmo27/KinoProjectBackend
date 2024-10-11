package com.example.kinobackend.service;

import com.example.kinobackend.model.Showing;
import com.example.kinobackend.repository.ShowingRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShowingService {

    @Autowired
    private ShowingRepository showingRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;



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
            updatedShowing.setId(id);
            return showingRepository.save(updatedShowing);
        }
        return null;
    }

    public void deleteShowing(int id) {
        if (showingRepository.existsById(id)) {
            showingRepository.deleteById(id);
        }
    }

    }


