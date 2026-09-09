package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "facultes")
public class Faculte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    public Faculte() {}

    public Faculte(String nom) {
        this.nom = nom;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}