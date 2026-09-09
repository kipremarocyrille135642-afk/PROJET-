package com.example.demo.repository;

import com.example.demo.entity.Faculte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaculteRepository extends JpaRepository<Faculte, Long> {
}