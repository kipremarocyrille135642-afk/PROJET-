package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "annonces")
public class Annonce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String contenu;

    @Column(nullable = false)
    private LocalDate date;

    public Annonce() {}

    public Annonce(String contenu) {
        this.contenu = contenu;
        this.date = LocalDate.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}