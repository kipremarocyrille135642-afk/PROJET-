package com.example.demo.repository;

import com.example.demo.entity.Seance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeanceRepository extends JpaRepository<Seance, Long> {
    List<Seance> findBySemestreOrderByJourAscHeureDebutAsc(Integer semestre);
}