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

    @PostConstruct
    public void init(){
        insertInitialData();
    }

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
        public void insertInitialData() {
            System.out.println("initial data is here");

            String sql1 = "INSERT INTO showing (showing_time, theater_number) VALUES (?, ?)";
            String sql2 = "INSERT INTO showing (showing_time, theater_number) VALUES (?, ?)";
            jdbcTemplate.update(sql1, "2024-10-10 18:00:00", 3);

            jdbcTemplate.update(sql2,  "2024-10-11 20:00:00", 4);

        }
    }


