package com.example.kinobackend.repository;

import com.example.kinobackend.model.Showing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ShowingRepository extends JpaRepository <Showing, Integer>  {

}
