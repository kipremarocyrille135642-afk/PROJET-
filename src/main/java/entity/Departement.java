package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "departements")
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @ManyToOne
    @JoinColumn(name = "faculte_id", nullable = false)
    private Faculte faculte;

    public Departement() {}

    public Departement(String nom, Faculte faculte) {
        this.nom = nom;
        this.faculte = faculte;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Faculte getFaculte() { return faculte; }
    public void setFaculte(Faculte faculte) { this.faculte = faculte; }
}
