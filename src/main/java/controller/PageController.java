package com.example.demo.controller;

import com.example.demo.service.InscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    @Autowired
    private InscriptionService inscriptionService;

    @GetMapping("/")
    public String racine() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String pageLogin(@RequestParam(required = false) String erreur,
                            @RequestParam(required = false) String deconnecte,
                            Model model) {
        if (erreur != null) {
            model.addAttribute("erreur", "Email ou mot de passe incorrect.");
        }
        if (deconnecte != null) {
            model.addAttribute("info", "Vous avez été déconnecté.");
        }
        return "login";
    }

    @GetMapping("/inscription")
    public String pageInscription() {
        return "inscription";
    }

    @PostMapping("/inscription")
    public String traiterInscription(@RequestParam String email,
                                     @RequestParam String motDePasse,
                                     Model model) {
        try {
            inscriptionService.inscrire(email, motDePasse);
            return "redirect:/login?inscrit=true";
        } catch (IllegalStateException e) {
            model.addAttribute("erreur", e.getMessage());
            return "inscription";
        }
    }

    @GetMapping("/accueil")
    public String pageAccueil(Authentication authentication, Model model) {
        model.addAttribute("email", authentication.getName());
        return "accueil";
    }

    @GetMapping("/admin")
    public String pageAdmin(Authentication authentication, Model model) {
        model.addAttribute("email", authentication.getName());
        return "admin";
    }
}