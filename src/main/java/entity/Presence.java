package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "presences")
public class Presence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur eleve;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Boolean present;

    public Presence() {}

    public Presence(Utilisateur eleve, LocalDate date, Boolean present) {
        this.eleve = eleve;
        this.date = date;
        this.present = present;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utilisateur getEleve() { return eleve; }
    public void setEleve(Utilisateur eleve) { this.eleve = eleve; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Boolean getPresent() { return present; }
    public void setPresent(Boolean present) { this.present = present; }
}