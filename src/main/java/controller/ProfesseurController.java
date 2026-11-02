package com.example.demo.controller;

import com.example.demo.entity.Professeur;
import com.example.demo.repository.ProfesseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Base64;

@Controller
public class ProfesseurController {

    private static final long TAILLE_MAX = 1_000_000L;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @GetMapping("/admin/professeurs")
    public String liste(Authentication authentication, Model model) {
        model.addAttribute("email", authentication.getName());
        model.addAttribute("professeurs", professeurRepository.findAll());
        return "admin-professeurs";
    }

    @PostMapping("/admin/professeurs/{id}/photo")
    public String changerPhoto(@PathVariable Long id,
                               @RequestParam MultipartFile photo,
                               RedirectAttributes redirectAttributes) throws Exception {
        Professeur prof = professeurRepository.findById(id).orElse(null);
        String type = photo.getContentType();
        if (prof == null) {
            redirectAttributes.addFlashAttribute("erreur", "Teacher not found.");
        } else if (photo.isEmpty() || type == null || !type.startsWith("image/")) {
            redirectAttributes.addFlashAttribute("erreur", "Please choose an image file (JPG, PNG, WebP).");
        } else if (photo.getSize() > TAILLE_MAX) {
            redirectAttributes.addFlashAttribute("erreur", "Image too large. Maximum 1 MB.");
        } else {
            String base64 = Base64.getEncoder().encodeToString(photo.getBytes());
            prof.setPhotoBase64("data:" + type + ";base64," + base64);
            professeurRepository.save(prof);
            redirectAttributes.addFlashAttribute("succesMessage", "Photo updated for " + prof.getPrenom() + " " + prof.getNom() + ".");
        }
        return "redirect:/admin/professeurs";
    }

    @PostMapping("/admin/professeurs/{id}/photo/supprimer")
    public String supprimerPhoto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Professeur prof = professeurRepository.findById(id).orElse(null);
        if (prof != null) {
            prof.setPhotoBase64(null);
            professeurRepository.save(prof);
            redirectAttributes.addFlashAttribute("succesMessage", "Photo removed.");
        }
        return "redirect:/admin/professeurs";
    }
}