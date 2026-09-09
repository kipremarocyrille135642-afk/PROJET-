package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "professeurs")
public class Professeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String specialite;

    @Column(length = 500)
    private String photoUrl;

    public Professeur() {}

    public Professeur(String nom, String prenom, String specialite, String photoUrl) {
        this.nom = nom;
        this.prenom = prenom;
        this.specialite = specialite;
        this.photoUrl = photoUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}