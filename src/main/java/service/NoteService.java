package com.example.demo.service;

import com.example.demo.entity.Matiere;
import com.example.demo.entity.Note;
import com.example.demo.entity.Utilisateur;
import com.example.demo.repository.MatiereRepository;
import com.example.demo.repository.NoteRepository;
import com.example.demo.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private MatiereRepository matiereRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Note> getNotesDeEleve(Utilisateur eleve) {
        return noteRepository.findByEleveOrderByDateDesc(eleve);
    }

    public List<Utilisateur> getTousLesEleves() {
        return utilisateurRepository.findAll();
    }

    public List<Matiere> getToutesLesMatieres() {
        return matiereRepository.findAll();
    }

    public List<Note> getToutesLesNotes() {
        return noteRepository.findAll();
    }

    public void ajouterNote(Long eleveId, Long matiereId, Double valeur, String typeExamen) {
        if (valeur == null || valeur < 0 || valeur > 20) {
            throw new IllegalStateException("The grade must be between 0 and 20.");
        }
        if (typeExamen == null || typeExamen.isBlank()) {
            throw new IllegalStateException("Exam type is required.");
        }

        Utilisateur eleve = utilisateurRepository.findById(eleveId)
                .orElseThrow(() -> new IllegalStateException("Student not found"));
        Matiere matiere = matiereRepository.findById(matiereId)
                .orElseThrow(() -> new IllegalStateException("Subject not found"));

        Note note = new Note(eleve, matiere, valeur, typeExamen.trim());
        noteRepository.save(note);
    }

    public void modifierNote(Long id, Double valeur, String typeExamen) {
        if (valeur == null || valeur < 0 || valeur > 20) {
            throw new IllegalStateException("The grade must be between 0 and 20.");
        }
        if (typeExamen == null || typeExamen.isBlank()) {
            throw new IllegalStateException("Exam type is required.");
        }
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Grade not found"));
        note.setValeur(valeur);
        note.setTypeExamen(typeExamen.trim());
        noteRepository.save(note);
    }

    public void supprimerNote(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new IllegalStateException("Grade not found");
        }
        noteRepository.deleteById(id);
    }

    public void supprimerEleve(Long id) {
        Utilisateur eleve = utilisateurRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Student not found"));
        if ("ROLE_ADMIN".equals(eleve.getRole())) {
            throw new IllegalStateException("Cannot delete an administrator account.");
        }
        noteRepository.deleteByEleve(eleve);
        utilisateurRepository.delete(eleve);
    }

    public double getMoyenneGeneraleClasse() {
        List<Note> toutesLesNotes = noteRepository.findAll();
        return toutesLesNotes.stream()
                .mapToDouble(Note::getValeur)
                .average()
                .orElse(0.0);
    }

    public List<Map.Entry<Utilisateur, Double>> getTopEleves(int limite) {
        List<Note> toutesLesNotes = noteRepository.findAll();
        Map<Utilisateur, Double> moyennesParEleve = toutesLesNotes.stream()
                .collect(Collectors.groupingBy(
                        Note::getEleve,
                        Collectors.averagingDouble(Note::getValeur)
                ));
        return moyennesParEleve.entrySet().stream()
                .sorted(Map.Entry.<Utilisateur, Double>comparingByValue().reversed())
                .limit(limite)
                .collect(Collectors.toList());
    }
}