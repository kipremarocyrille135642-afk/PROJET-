package com.example.demo.controller;

import com.example.demo.entity.Note;
import com.example.demo.entity.Presence;
import com.example.demo.entity.Seance;
import com.example.demo.entity.Utilisateur;
import com.example.demo.repository.AnnonceRepository;
import com.example.demo.repository.PresenceRepository;
import com.example.demo.repository.ProfesseurRepository;
import com.example.demo.repository.SeanceRepository;
import com.example.demo.repository.UtilisateurRepository;
import com.example.demo.service.InscriptionService;
import com.example.demo.service.NoteService;
import com.example.demo.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class PageController {

    @Autowired
    private InscriptionService inscriptionService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PresenceRepository presenceRepository;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private AnnonceRepository annonceRepository;

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private PdfService pdfService;

    private static final String[] PHOTOS_CAMPUS = {
        "https://images.unsplash.com/photo-1541339907198-e08756dedf3f?auto=format&fit=crop&w=1600&q=80",
        "https://images.unsplash.com/photo-1523050854058-8df90110c9f1?auto=format&fit=crop&w=1600&q=80",
        "https://images.unsplash.com/photo-1523240795612-9a054b0db644?auto=format&fit=crop&w=1600&q=80",
        "https://images.unsplash.com/photo-1562774053-701939374585?auto=format&fit=crop&w=1600&q=80",
        "https://images.unsplash.com/photo-1592280771190-3e2e4d571952?auto=format&fit=crop&w=1600&q=80",
        "https://images.unsplash.com/photo-1571260899304-425eee4c7efc?auto=format&fit=crop&w=1600&q=80",
        "https://images.unsplash.com/photo-1498243691581-b145c3f54a5a?auto=format&fit=crop&w=1600&q=80"
    };

    @GetMapping("/")
    public String racine() {
        return "landing";
    }

    @GetMapping("/login")
    public String pageLogin(@RequestParam(required = false) String erreur,
                            @RequestParam(required = false) String deconnecte,
                            Model model) {
        if (erreur != null) {
            model.addAttribute("erreur", "Incorrect email or password.");
        }
        if (deconnecte != null) {
            model.addAttribute("info", "You have been logged out.");
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
                                     @RequestParam String nom,
                                     @RequestParam String prenom,
                                     Model model) {
        try {
            inscriptionService.inscrire(email, motDePasse, nom, prenom);
            return "redirect:/login?inscrit=true";
        } catch (IllegalStateException e) {
            model.addAttribute("erreur", e.getMessage());
            return "inscription";
        }
    }

    @GetMapping("/accueil")
    public String pageAccueil(Authentication authentication,
                              @RequestParam(required = false, defaultValue = "1") Integer semestre,
                              Model model) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        List<Note> notes = noteService.getNotesDeEleve(utilisateur);

        model.addAttribute("email", authentication.getName());
        model.addAttribute("utilisateur", utilisateur);

        double cgpa = notes.stream()
                .mapToDouble(Note::getValeur)
                .average()
                .orElse(0.0) / 2.0;
        model.addAttribute("cgpa", Math.round(cgpa * 100.0) / 100.0);

        Map<Integer, Double> sgpaParSemestre = notes.stream()
                .collect(Collectors.groupingBy(
                        n -> n.getMatiere().getSemestre(),
                        TreeMap::new,
                        Collectors.averagingDouble(n -> n.getValeur() / 2.0)
                ));
        model.addAttribute("sgpaParSemestre", sgpaParSemestre);

        List<Presence> presences = presenceRepository.findByEleve(utilisateur);
        int pourcentagePresence = 0;
        if (!presences.isEmpty()) {
            long nbPresent = presences.stream().filter(Presence::getPresent).count();
            pourcentagePresence = (int) Math.round((nbPresent * 100.0) / presences.size());
        }
        model.addAttribute("pourcentagePresence", pourcentagePresence);

        boolean estAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("estAdmin", estAdmin);

        model.addAttribute("professeurs", professeurRepository.findAll());
        model.addAttribute("annonces", annonceRepository.findAllByOrderByDateDesc());

        Note derniereNote = notes.stream()
                .max(Comparator.comparing(Note::getDate))
                .orElse(null);
        model.addAttribute("derniereNote", derniereNote);

        boolean notificationRecente = derniereNote != null
                && ChronoUnit.DAYS.between(derniereNote.getDate(), LocalDate.now()) <= 3;
        model.addAttribute("notificationRecente", notificationRecente);

        int indexJour = LocalDate.now().getDayOfYear() % PHOTOS_CAMPUS.length;
        model.addAttribute("photoBanniere", PHOTOS_CAMPUS[indexJour]);

        model.addAttribute("seances", seanceRepository.findBySemestreOrderByJourAscHeureDebutAsc(semestre));
        model.addAttribute("semestreActuel", semestre);
        model.addAttribute("semestres", IntStream.rangeClosed(1, 6).boxed().collect(Collectors.toList()));

        return "accueil";
    }

    @GetMapping("/admin")
    public String pageAdmin() {
        return "redirect:/admin/notes";
    }

    @GetMapping("/mes-notes")
    public String pageMesNotes(Authentication authentication, Model model) {
        Utilisateur eleve = utilisateurRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        List<Note> notes = noteService.getNotesDeEleve(eleve);

        model.addAttribute("notes", notes);
        model.addAttribute("email", authentication.getName());
        model.addAttribute("utilisateur", eleve);

        double moyenneGenerale = notes.stream()
                .mapToDouble(Note::getValeur)
                .average()
                .orElse(0.0);
        model.addAttribute("moyenneGenerale", moyenneGenerale);

        Map<Integer, Double> moyennesParSemestre = notes.stream()
                .collect(Collectors.groupingBy(
                        n -> n.getMatiere().getSemestre(),
                        TreeMap::new,
                        Collectors.averagingDouble(Note::getValeur)
                ));
        model.addAttribute("moyennesParSemestre", moyennesParSemestre);

        long nbBonnes = notes.stream().filter(n -> n.getValeur() >= 14).count();
        long nbMoyennes = notes.stream().filter(n -> n.getValeur() >= 10 && n.getValeur() < 14).count();
        long nbFaibles = notes.stream().filter(n -> n.getValeur() < 10).count();
        model.addAttribute("nbBonnes", nbBonnes);
        model.addAttribute("nbMoyennes", nbMoyennes);
        model.addAttribute("nbFaibles", nbFaibles);

        return "mes-notes";
    }

    @GetMapping("/mes-notes/pdf")
    public ResponseEntity<byte[]> telechargerReleveNotes(Authentication authentication) {
        Utilisateur eleve = utilisateurRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        List<Note> notes = noteService.getNotesDeEleve(eleve);

        double moyenneGenerale = notes.stream()
                .mapToDouble(Note::getValeur)
                .average()
                .orElse(0.0);

        byte[] pdf = pdfService.genererReleveNotes(eleve, notes, moyenneGenerale);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "grade-report-" + eleve.getNom() + ".pdf");

        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    @GetMapping("/profil")
    public String pageProfil(Authentication authentication, Model model) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));
        model.addAttribute("utilisateur", utilisateur);
        return "profil";
    }

    @PostMapping("/profil/photo")
    public String uploaderPhoto(Authentication authentication,
                                @RequestParam MultipartFile photo) throws Exception {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (!photo.isEmpty()) {
            String base64 = Base64.getEncoder().encodeToString(photo.getBytes());
            String mimeType = photo.getContentType();
            utilisateur.setPhotoBase64("data:" + mimeType + ";base64," + base64);
            utilisateurRepository.save(utilisateur);
        }

        return "redirect:/profil?succes=true";
    }

    @GetMapping("/emploi-du-temps")
    public String redirectionEmploiDuTemps() {
        return "redirect:/accueil";
    }

    @GetMapping("/admin/notes")
    public String pageAdminNotes(Authentication authentication, Model model) {
        model.addAttribute("email", authentication.getName());
        model.addAttribute("eleves", noteService.getTousLesEleves());
        model.addAttribute("matieres", noteService.getToutesLesMatieres());
        model.addAttribute("notes", noteService.getToutesLesNotes());
        model.addAttribute("moyenneClasse", Math.round(noteService.getMoyenneGeneraleClasse() * 100.0) / 100.0);
        model.addAttribute("topEleves", noteService.getTopEleves(5));
        return "admin-notes";
    }

    @PostMapping("/admin/notes")
    public String ajouterNote(@RequestParam Long eleveId,
                              @RequestParam Long matiereId,
                              @RequestParam Double valeur,
                              @RequestParam String typeExamen,
                              RedirectAttributes redirectAttributes) {
        try {
            noteService.ajouterNote(eleveId, matiereId, valeur, typeExamen);
            return "redirect:/admin/notes?succes=true";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/admin/notes";
        }
    }

    @PostMapping("/admin/notes/{id}/modifier")
    public String modifierNote(@PathVariable Long id,
                               @RequestParam Double valeur,
                               @RequestParam String typeExamen,
                               RedirectAttributes redirectAttributes) {
        try {
            noteService.modifierNote(id, valeur, typeExamen);
            redirectAttributes.addFlashAttribute("succesMessage", "Grade updated successfully.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/admin/notes";
    }

    @PostMapping("/admin/notes/{id}/supprimer")
    public String supprimerNote(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            noteService.supprimerNote(id);
            redirectAttributes.addFlashAttribute("succesMessage", "Grade deleted.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/admin/notes";
    }

    @PostMapping("/admin/eleves/{id}/supprimer")
    public String supprimerEleve(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            noteService.supprimerEleve(id);
            redirectAttributes.addFlashAttribute("succesMessage", "Student deleted.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/admin/notes";
    }
}