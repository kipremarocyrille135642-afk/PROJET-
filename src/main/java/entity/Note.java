package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur eleve;

    @ManyToOne
    @JoinColumn(name = "matiere_id", nullable = false)
    private Matiere matiere;

    @Column(nullable = false)
    private Double valeur;

    @Column(nullable = false)
    private String typeExamen;

    @Column(nullable = false)
    private LocalDate date = LocalDate.now();

    public Note() {}

    public Note(Utilisateur eleve, Matiere matiere, Double valeur, String typeExamen) {
        this.eleve = eleve;
        this.matiere = matiere;
        this.valeur = valeur;
        this.typeExamen = typeExamen;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utilisateur getEleve() { return eleve; }
    public void setEleve(Utilisateur eleve) { this.eleve = eleve; }

    public Matiere getMatiere() { return matiere; }
    public void setMatiere(Matiere matiere) { this.matiere = matiere; }

    public Double getValeur() { return valeur; }
    public void setValeur(Double valeur) { this.valeur = valeur; }

    public String getTypeExamen() { return typeExamen; }
    public void setTypeExamen(String typeExamen) { this.typeExamen = typeExamen; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}