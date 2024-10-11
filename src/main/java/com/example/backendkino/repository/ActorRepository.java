package com.example.backendkino.repository;

import com.example.backendkino.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Integer> {
    Actor findActorByFullName(String fullName);
}