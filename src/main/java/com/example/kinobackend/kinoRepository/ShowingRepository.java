package com.example.kinobackend.kinoRepository;

import com.example.kinobackend.model.Showing;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ShowingRepository extends JpaRepository <Showing, Integer>  {

}
