package com.example.demo.repository;

import com.example.demo.entity.NouvelEleve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NouvelEleveRepository extends JpaRepository<NouvelEleve, Long> {
    List<NouvelEleve> findAllByOrderByDateInscriptionDescIdDesc();
    boolean existsByEmail(String email);
}