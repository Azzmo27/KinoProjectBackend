package com.example.kinoxpbackend.repository;

import com.example.kinoxpbackend.model.Actor;
import com.example.kinoxpbackend.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Integer> {
    Actor findActorByFullName(String fullName);
}
