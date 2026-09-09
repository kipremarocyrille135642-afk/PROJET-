package com.example.demo;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private FaculteRepository faculteRepository;

    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private FiliereRepository filiereRepository;

    @Autowired
    private MatiereRepository matiereRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private AnnonceRepository annonceRepository;

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String[] JOURS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private static final String[] SALLES = {"Room A101", "Room A102", "Room B201", "Room B202", "Computer Lab 1"};

    private static final String[][] ELEVES_EXEMPLE = {
        {"exemple.kouassi@demo.ci", "Kouassi", "Aya"},
        {"exemple.traore@demo.ci", "Traore", "Ibrahim"},
        {"exemple.kone@demo.ci", "Kone", "Fatou"},
        {"exemple.yao@demo.ci", "Yao", "Michel"},
        {"exemple.coulibaly@demo.ci", "Coulibaly", "Awa"},
        {"exemple.bamba@demo.ci", "Bamba", "Sekou"},
        {"exemple.ouattara@demo.ci", "Ouattara", "Mariam"},
        {"exemple.kouame@demo.ci", "Kouame", "Jean"},
        {"exemple.diabate@demo.ci", "Diabate", "Aminata"},
        {"exemple.toure@demo.ci", "Toure", "Moussa"},
        {"exemple.nguessan@demo.ci", "Nguessan", "Adjoa"},
        {"exemple.diallo@demo.ci", "Diallo", "Fatoumata"},
        {"exemple.kra@demo.ci", "Kra", "Yves"},
        {"exemple.sanogo@demo.ci", "Sanogo", "Bintou"},
        {"exemple.kouadio@demo.ci", "Kouadio", "Serge"},
        {"exemple.camara@demo.ci", "Camara", "Aissatou"},
        {"exemple.bakayoko@demo.ci", "Bakayoko", "Karim"},
        {"exemple.konan@demo.ci", "Konan", "Sylvie"},
        {"exemple.doumbia@demo.ci", "Doumbia", "Issouf"},
        {"exemple.ndri@demo.ci", "Ndri", "Nadege"}
    };

    @Override
    public void run(String... args) throws Exception {
        if (faculteRepository.count() == 0) {
            creerStructureAcademique();

            for (String[] eleve : ELEVES_EXEMPLE) {
                if (!utilisateurRepository.existsByEmail(eleve[0])) {
                    creerEleveExemple(eleve[0], eleve[1], eleve[2]);
                }
            }

            if (!utilisateurRepository.existsByEmail("admin@demo.ci")) {
                creerAdmin();
            }
        }

        if (professeurRepository.count() == 0) {
            creerProfesseurs();
        }

        if (annonceRepository.count() == 0) {
            annonceRepository.save(new Annonce("Welcome to EduTrack! Please check your grades and attendance regularly."));
        }

        if (seanceRepository.count() == 0) {
            creerEmploiDuTemps();
        }
    }

    private void creerStructureAcademique() {
        Faculte facInfo = faculteRepository.save(new Faculte("Faculty of Computer Science"));
        Departement deptAppli = departementRepository.save(new Departement("Department of Computer Applications", facInfo));
        Filiere bca = filiereRepository.save(new Filiere("Bachelor of Computer Applications", "BCA", deptAppli));

        matiereRepository.save(new Matiere("Fundamentals of Computer", bca, 1));
        matiereRepository.save(new Matiere("Programming in C", bca, 1));
        matiereRepository.save(new Matiere("Mathematics-I", bca, 1));
        matiereRepository.save(new Matiere("Communication Skills", bca, 1));
        matiereRepository.save(new Matiere("Digital Electronics", bca, 1));

        matiereRepository.save(new Matiere("Data Structures", bca, 2));
        matiereRepository.save(new Matiere("Java Programming", bca, 2));
        matiereRepository.save(new Matiere("Database Management System", bca, 2));
        matiereRepository.save(new Matiere("Operating Systems", bca, 2));
        matiereRepository.save(new Matiere("Environmental Studies", bca, 2));

        matiereRepository.save(new Matiere("Object Oriented Programming", bca, 3));
        matiereRepository.save(new Matiere("Web Development", bca, 3));
        matiereRepository.save(new Matiere("Computer Networks", bca, 3));
        matiereRepository.save(new Matiere("Software Engineering", bca, 3));
        matiereRepository.save(new Matiere("Python Programming", bca, 3));

        matiereRepository.save(new Matiere("Advanced Java", bca, 4));
        matiereRepository.save(new Matiere("PHP Programming", bca, 4));
        matiereRepository.save(new Matiere("Data Analytics", bca, 4));
        matiereRepository.save(new Matiere("Mobile Application Development", bca, 4));
        matiereRepository.save(new Matiere("Artificial Intelligence", bca, 4));

        matiereRepository.save(new Matiere("Cloud Computing", bca, 5));
        matiereRepository.save(new Matiere("Cyber Security", bca, 5));
        matiereRepository.save(new Matiere("Machine Learning", bca, 5));
        matiereRepository.save(new Matiere("Project Management", bca, 5));
        matiereRepository.save(new Matiere("Internship", bca, 5));

        matiereRepository.save(new Matiere("Major Project", bca, 6));
        matiereRepository.save(new Matiere("Seminar", bca, 6));
        matiereRepository.save(new Matiere("Blockchain Technology", bca, 6));
        matiereRepository.save(new Matiere("Internet of Things", bca, 6));
        matiereRepository.save(new Matiere("Professional Ethics", bca, 6));
    }

    private void creerEleveExemple(String email, String nom, String prenom) {
        Utilisateur eleve = new Utilisateur();
        eleve.setEmail(email);
        eleve.setMotDePasse(passwordEncoder.encode("exemple123"));
        eleve.setNom(nom);
        eleve.setPrenom(prenom);
        utilisateurRepository.save(eleve);
    }

    private void creerAdmin() {
        Utilisateur admin = new Utilisateur();
        admin.setEmail("admin@demo.ci");
        admin.setMotDePasse(passwordEncoder.encode("admin123"));
        admin.setNom("Admin");
        admin.setPrenom("Principal");
        admin.setRole("ROLE_ADMIN");
        utilisateurRepository.save(admin);
    }

    private void creerProfesseurs() {
        professeurRepository.save(new Professeur("Kouassi", "Jean-Marc", "Programming & Algorithms", "https://randomuser.me/api/portraits/men/32.jpg"));
        professeurRepository.save(new Professeur("Traore", "Fatou", "Databases", "https://randomuser.me/api/portraits/women/44.jpg"));
        professeurRepository.save(new Professeur("Diarra", "Souleymane", "Networks & Cybersecurity", "https://randomuser.me/api/portraits/men/56.jpg"));
        professeurRepository.save(new Professeur("Bamba", "Aminata", "Artificial Intelligence", "https://randomuser.me/api/portraits/women/68.jpg"));
        professeurRepository.save(new Professeur("Konate", "Ibrahim", "Web Development", "https://randomuser.me/api/portraits/men/12.jpg"));
        professeurRepository.save(new Professeur("Yao", "Marie-Claire", "Project Management", "https://randomuser.me/api/portraits/women/21.jpg"));
    }

    private void creerEmploiDuTemps() {
        for (int semestre = 1; semestre <= 6; semestre++) {
            final int semestreCourant = semestre;
            List<Matiere> matieresDuSemestre = matiereRepository.findAll().stream()
                    .filter(m -> m.getSemestre() == semestreCourant)
                    .collect(Collectors.toList());

            String heureDebut = "08:00";
            String heureFin = "10:00";

            for (int i = 0; i < matieresDuSemestre.size() && i < JOURS.length; i++) {
                seanceRepository.save(new Seance(
                        JOURS[i],
                        heureDebut,
                        heureFin,
                        matieresDuSemestre.get(i),
                        semestreCourant,
                        SALLES[i % SALLES.length]
                ));
            }
        }
    }
}