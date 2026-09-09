package com.example.demo.service;

import com.example.demo.entity.Note;
import com.example.demo.entity.Presence;
import com.example.demo.entity.Utilisateur;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.PresenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private PresenceRepository presenceRepository;

    /**
     * Calcule la moyenne generale sur 20, convertie en equivalent GPA sur 10.
     */
    public double calculerCgpa(Utilisateur eleve) {
        List<Note> notes = noteRepository.findByEleveOrderByDateDesc(eleve);
        if (notes.isEmpty()) return 0.0;

        double moyenneSur20 = notes.stream()
                .mapToDouble(Note::getValeur)
                .average()
                .orElse(0.0);

        double gpa = (moyenneSur20 / 20.0) * 10.0;
        return Math.round(gpa * 100.0) / 100.0;
    }

    /**
     * Calcule la moyenne (SGPA) pour chaque semestre ou l'eleve a des notes.
     * Retourne une Map triee : numero de semestre -> SGPA sur 10.
     */
    public Map<Integer, Double> calculerSgpaParSemestre(Utilisateur eleve) {
        List<Note> notes = noteRepository.findByEleveOrderByDateDesc(eleve);

        Map<Integer, List<Note>> parSemestre = notes.stream()
                .collect(Collectors.groupingBy(n -> n.getMatiere().getSemestre()));

        Map<Integer, Double> resultat = new TreeMap<>();
        for (Map.Entry<Integer, List<Note>> entree : parSemestre.entrySet()) {
            double moyenne20 = entree.getValue().stream()
                    .mapToDouble(Note::getValeur)
                    .average()
                    .orElse(0.0);
            double gpa10 = Math.round((moyenne20 / 20.0) * 10.0 * 100.0) / 100.0;
            resultat.put(entree.getKey(), gpa10);
        }
        return resultat;
    }

    /**
     * Calcule le pourcentage de presence de l'eleve.
     */
    public int calculerPourcentagePresence(Utilisateur eleve) {
        List<Presence> presences = presenceRepository.findByEleve(eleve);
        if (presences.isEmpty()) return 0;

        long nbPresent = presences.stream().filter(Presence::getPresent).count();
        return (int) Math.round((nbPresent * 100.0) / presences.size());
    }
}