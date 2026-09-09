package com.example.demo.service;

import com.example.demo.entity.Utilisateur;
import com.example.demo.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class InscriptionService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Utilisateur inscrire(String email, String motDePasseEnClair, String nom, String prenom) {
        if (email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalStateException("Invalid email address.");
        }
        if (nom == null || nom.isBlank()) {
            throw new IllegalStateException("Last name is required.");
        }
        if (prenom == null || prenom.isBlank()) {
            throw new IllegalStateException("First name is required.");
        }
        if (motDePasseEnClair == null || motDePasseEnClair.length() < 6) {
            throw new IllegalStateException("Password must be at least 6 characters long.");
        }
        if (utilisateurRepository.existsByEmail(email)) {
            throw new IllegalStateException("An account already exists with this email.");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(email.trim().toLowerCase());
        utilisateur.setMotDePasse(passwordEncoder.encode(motDePasseEnClair));
        utilisateur.setNom(nom.trim());
        utilisateur.setPrenom(prenom.trim());
        return utilisateurRepository.save(utilisateur);
    }
}