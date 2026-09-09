package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "filieres")
public class Filiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String code;

    @ManyToOne
    @JoinColumn(name = "departement_id", nullable = false)
    private Departement departement;

    public Filiere() {}

    public Filiere(String nom, String code, Departement departement) {
        this.nom = nom;
        this.code = code;
        this.departement = departement;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Departement getDepartement() { return departement; }
    public void setDepartement(Departement departement) { this.departement = departement; }
}