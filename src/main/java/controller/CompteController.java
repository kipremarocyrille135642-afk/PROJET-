package com.example.demo.controller;

import com.example.demo.entity.Utilisateur;
import com.example.demo.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CompteController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/profil/mot-de-passe")
    public String changerMotDePasse(Authentication authentication,
                                    @RequestParam String ancien,
                                    @RequestParam String nouveau,
                                    @RequestParam String confirmation,
                                    RedirectAttributes redirectAttributes) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (!passwordEncoder.matches(ancien, utilisateur.getMotDePasse())) {
            redirectAttributes.addFlashAttribute("erreurMdp", "Current password is incorrect.");
        } else if (nouveau.length() < 8) {
            redirectAttributes.addFlashAttribute("erreurMdp", "New password must be at least 8 characters.");
        } else if (!nouveau.equals(confirmation)) {
            redirectAttributes.addFlashAttribute("erreurMdp", "The two new passwords do not match.");
        } else if (passwordEncoder.matches(nouveau, utilisateur.getMotDePasse())) {
            redirectAttributes.addFlashAttribute("erreurMdp", "New password must be different from the current one.");
        } else {
            utilisateur.setMotDePasse(passwordEncoder.encode(nouveau));
            utilisateurRepository.save(utilisateur);
            redirectAttributes.addFlashAttribute("succesMdp", "Password updated successfully.");
        }
        return "redirect:/profil";
    }
}