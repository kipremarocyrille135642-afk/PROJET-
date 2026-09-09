package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "matieres")
public class Matiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @ManyToOne
    @JoinColumn(name = "filiere_id", nullable = false)
    private Filiere filiere;

    @Column(nullable = false)
    private Integer semestre;

    public Matiere() {}

    public Matiere(String nom, Filiere filiere, Integer semestre) {
        this.nom = nom;
        this.filiere = filiere;
        this.semestre = semestre;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Filiere getFiliere() { return filiere; }
    public void setFiliere(Filiere filiere) { this.filiere = filiere; }

    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }
}