package com.example.demo.repository;

import com.example.demo.entity.Matiere;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatiereRepository extends JpaRepository<Matiere, Long> {
}