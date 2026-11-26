package com.example.demo.controller;

import com.example.demo.entity.NouvelEleve;
import com.example.demo.repository.NouvelEleveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class NouvelEleveController {

    @Autowired
    private NouvelEleveRepository repository;

    @GetMapping("/admin/nouveaux-eleves")
    public String liste(@RequestParam(required = false) String q,
                        Authentication authentication, Model model) {
        List<NouvelEleve> eleves = repository.findAllByOrderByDateInscriptionDescIdDesc();
        if (q != null && !q.isBlank()) {
            String m = q.toLowerCase(Locale.ROOT);
            eleves = eleves.stream().filter(e ->
                    (e.getNom() + " " + e.getPrenom() + " " + e.getEmail()).toLowerCase(Locale.ROOT).contains(m))
                    .collect(Collectors.toList());
        }
        model.addAttribute("eleves", eleves);
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("email", authentication.getName());
        return "admin-nouveaux-eleves";
    }

    @PostMapping("/admin/nouveaux-eleves")
    public String ajouter(@RequestParam String nom,
                          @RequestParam String prenom,
                          @RequestParam String email,
                          @RequestParam(required = false) String telephone,
                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateNaissance,
                          @RequestParam(required = false) String filiere,
                          @RequestParam(required = false) Integer semestre,
                          RedirectAttributes ra) {
        String mail = email.trim().toLowerCase(Locale.ROOT);
        if (repository.existsByEmail(mail)) {
            ra.addFlashAttribute("erreur", "This email is already registered.");
            return "redirect:/admin/nouveaux-eleves";
        }
        NouvelEleve e = new NouvelEleve();
        e.setNom(nom.trim());
        e.setPrenom(prenom.trim());
        e.setEmail(mail);
        e.setTelephone(telephone);
        e.setDateNaissance(dateNaissance);
        e.setFiliere(filiere);
        e.setSemestre(semestre);
        repository.save(e);
        ra.addFlashAttribute("succesMessage", prenom + " " + nom + " registered.");
        return "redirect:/admin/nouveaux-eleves";
    }

    @PostMapping("/admin/nouveaux-eleves/{id}/statut")
    public String statut(@PathVariable Long id, @RequestParam String statut) {
        repository.findById(id).ifPresent(e -> {
            e.setStatut(statut);
            repository.save(e);
        });
        return "redirect:/admin/nouveaux-eleves";
    }

    @PostMapping("/admin/nouveaux-eleves/{id}/supprimer")
    public String supprimer(@PathVariable Long id, RedirectAttributes ra) {
        repository.deleteById(id);
        ra.addFlashAttribute("succesMessage", "Record deleted.");
        return "redirect:/admin/nouveaux-eleves";
    }
}