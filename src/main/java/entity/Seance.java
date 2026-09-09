package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "seances")
public class Seance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String jour;

    @Column(nullable = false)
    private String heureDebut;

    @Column(nullable = false)
    private String heureFin;

    @ManyToOne
    @JoinColumn(name = "matiere_id", nullable = false)
    private Matiere matiere;

    @Column(nullable = false)
    private Integer semestre;

    @Column(nullable = false)
    private String salle;

    public Seance() {}

    public Seance(String jour, String heureDebut, String heureFin, Matiere matiere, Integer semestre, String salle) {
        this.jour = jour;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.matiere = matiere;
        this.semestre = semestre;
        this.salle = salle;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getJour() { return jour; }
    public void setJour(String jour) { this.jour = jour; }
    public String getHeureDebut() { return heureDebut; }
    public void setHeureDebut(String heureDebut) { this.heureDebut = heureDebut; }
    public String getHeureFin() { return heureFin; }
    public void setHeureFin(String heureFin) { this.heureFin = heureFin; }
    public Matiere getMatiere() { return matiere; }
    public void setMatiere(Matiere matiere) { this.matiere = matiere; }
    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }
    public String getSalle() { return salle; }
    public void setSalle(String salle) { this.salle = salle; }
}