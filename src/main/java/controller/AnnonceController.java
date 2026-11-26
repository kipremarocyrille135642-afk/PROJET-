
package com.example.demo.controller;

import com.example.demo.entity.Annonce;
import com.example.demo.repository.AnnonceRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/annonces")
public class AnnonceController {

    private final AnnonceRepository annonceRepository;

    public AnnonceController(AnnonceRepository annonceRepository) {
        this.annonceRepository = annonceRepository;
    }

    @GetMapping
    public String afficherAnnonces(Model model,
                                   @RequestParam(required = false) String modifier) {
        model.addAttribute("annonces",
                annonceRepository.findAllByOrderByDateDesc());

        model.addAttribute("nouvelleAnnonce", new Annonce());

        if (modifier != null) {
            annonceRepository.findById(Long.valueOf(modifier))
                    .ifPresent(a -> model.addAttribute("annonceAModifier", a));
        }

        return "admin-annonces";
    }

    @PostMapping("/ajouter")
    public String ajouterAnnonce(@RequestParam String contenu,
                                 RedirectAttributes redirectAttributes) {
        if (contenu == null || contenu.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "erreur", "Le contenu de l'annonce est obligatoire.");
            return "redirect:/admin/annonces";
        }

        annonceRepository.save(new Annonce(contenu.trim()));

        redirectAttributes.addFlashAttribute(
                "succesMessage", "Announcement published successfully!");

        return "redirect:/admin/annonces";
    }

    @PostMapping("/{id}/modifier")
    public String modifierAnnonce(@PathVariable Long id,
                                  @RequestParam String contenu,
                                  RedirectAttributes redirectAttributes) {
        Annonce annonce = annonceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Annonce introuvable."));

        if (contenu == null || contenu.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "erreur", "Content cannot be empty.");
            return "redirect:/admin/annonces";
        }

        annonce.setContenu(contenu.trim());
        annonceRepository.save(annonce);

        redirectAttributes.addFlashAttribute(
                "succesMessage", "Announcement updated successfully!");

        return "redirect:/admin/annonces";
    }

    @PostMapping("/{id}/supprimer")
    public String supprimerAnnonce(@PathVariable Long id,
                                   RedirectAttributes redirectAttributes) {
        if (!annonceRepository.existsById(id)) {
            redirectAttributes.addFlashAttribute(
                    "erreur", "Annonce introuvable.");
            return "redirect:/admin/annonces";
        }

        annonceRepository.deleteById(id);

        redirectAttributes.addFlashAttribute(
                "succesMessage", "Announcement deleted successfully!");

        return "redirect:/admin/annonces";
    }
}
