package com.example.kinobackend.controller;

import com.example.kinobackend.model.Showing;
import com.example.kinobackend.service.ShowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/showings")
public class ShowingController {

    @Autowired
    private ShowingService showingService;

    @GetMapping
    public List<Showing> getAllShowings() {
        return showingService.getAllShowings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Showing> getShowingById(@PathVariable int id) {
        return showingService.getShowingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Showing createShowing(@RequestBody Showing showing) {
        return showingService.createShowing(showing);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Showing> updateShowing(@PathVariable int id, @RequestBody Showing updatedShowing) {
        Showing showing = showingService.updateShowing(id, updatedShowing);
        return showing != null ? ResponseEntity.ok(showing) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowing(@PathVariable int id) {
        showingService.deleteShowing(id);
        return ResponseEntity.noContent().build();
    }
}
