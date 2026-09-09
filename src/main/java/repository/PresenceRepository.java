package com.example.demo.repository;

import com.example.demo.entity.Presence;
import com.example.demo.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PresenceRepository extends JpaRepository<Presence, Long> {
    List<Presence> findByEleve(Utilisateur eleve);
}