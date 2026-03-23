package com.exemple.projet.controller;

/**
 * Contrôleur qui gère les pages liées aux notes.
 * 
 * Un contrôleur reçoit les requêtes HTTP et retourne les vues JSP.
 * C'est le "chef d'orchestre" entre l'utilisateur et la base de données.
 */

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.exemple.projet.model.Candidat;
import com.exemple.projet.model.Matiere;
import com.exemple.projet.model.Correcteur;
import com.exemple.projet.model.Note;
import com.exemple.projet.repository.CandidatRepository;
import com.exemple.projet.repository.MatiereRepository;
import com.exemple.projet.repository.CorrecteurRepository;
import com.exemple.projet.service.NoteService;

@Controller  // Indique que c'est un contrôleur Spring MVC
public class NoteController {

    // Injection du service qui contient la logique métier
    @Autowired
    private NoteService noteService;
    
    // Injection des repositories pour accéder aux données
    @Autowired
    private CandidatRepository candidatRepository;  // Pour les candidats
    
    @Autowired
    private MatiereRepository matiereRepository;  // Pour les matières
    
    @Autowired
    private CorrecteurRepository correcteurRepository;  // Pour les correcteurs

    /**
     * Affiche la page de calcul des notes.
     * 
     * URL: /notes
     * Vue: listeNotes.jsp
     * 
     * @param model Objet qui transmet les données à la vue JSP
     * @return Le nom de la vue JSP à afficher
     */
    @GetMapping("/notes")
    public String listeNotes(Model model) {
        // Récupérer tous les candidats depuis la base de données
        model.addAttribute("candidats", candidatRepository.findAll());
        
        // Récupérer toutes les matières depuis la base de données
        model.addAttribute("matieres", matiereRepository.findAll());
        
        // Retourner la vue listeNotes.jsp
        return "listeNotes";
    }
    
    /**
     * Affiche le formulaire pour ajouter une nouvelle note.
     * 
     * URL: /notes/ajouter
     * Vue: ajouterNote.jsp
     * 
     * @param model Objet qui transmet les données à la vue JSP
     * @return Le nom de la vue JSP à afficher
     */
    @GetMapping("/notes/ajouter")
    public String ajouterNote(Model model) {
        // Créer un nouvel objet Note vide pour le formulaire
        model.addAttribute("note", new Note());
        
        // Transmettre les listes déroulantes
        model.addAttribute("candidats", candidatRepository.findAll());
        model.addAttribute("matieres", matiereRepository.findAll());
        model.addAttribute("correcteurs", correcteurRepository.findAll());
        
        return "ajouterNote";
    }
    
    /**
     * Sauvegarde une nouvelle note dans la base de données.
     * 
     * URL: /notes/savevegarder
     * Méthode: POST
     * 
     * @param candidatId ID du candidat sélectionné
     * @param matiereId ID de la matière sélectionnée
     * @param correcteurId ID du correcteur sélectionné
     * @param note La note attribuée
     * @return Redirection vers la page /notes
     */
    @PostMapping("/notes/sauvegarder")
    public String sauvegarderNote(
            @RequestParam Integer candidatId,    // ID du candidat depuis le formulaire
            @RequestParam Integer matiereId,      // ID de la matière depuis le formulaire
            @RequestParam Integer correcteurId,   // ID du correcteur depuis le formulaire
            @RequestParam BigDecimal note) {      // La note depuis le formulaire
        
        // Étape 1: Récupérer les entités complètes depuis la base de données
        // car le formulaire n'envoie que les IDs
        Candidat candidat = candidatRepository.findById(candidatId).orElse(null);
        Matiere matiere = matiereRepository.findById(matiereId).orElse(null);
        Correcteur correcteur = correcteurRepository.findById(correcteurId).orElse(null);
        
        // Étape 2: Créer un nouvel objet Note
        Note newNote = new Note();
        newNote.setCandidat(candidat);      // Associer le candidat
        newNote.setMatiere(matiere);        // Associer la matière
        newNote.setCorrecteur(correcteur);  // Associer le correcteur
        newNote.setNote(note);               // Enregistrer la note
        
        // Étape 3: Sauvegarder dans la base de données
        noteService.saveNote(newNote);
        
        // Rediriger vers la page de liste
        return "redirect:/notes";
    }
    
    /**
     * Calcule la note finale d'un candidat pour une matière.
     * 
     * URL: /notes/calculer?idCandidat=1&idMatiere=1
     * Vue: resultat.jsp
     * 
     * @param idCandidat ID du candidat
     * @param idMatiere ID de la matière
     * @param model Objet qui transmet les données à la vue JSP
     * @return Le nom de la vue JSP à afficher
     */
    @GetMapping("/notes/calculer")
    public String calculerNote(
            @RequestParam Integer idCandidat,
            @RequestParam Integer idMatiere,
            Model model) {
        
        // Appeler le service pour calculer la note finale
        BigDecimal noteFinale = noteService.calculerNoteFinale(idCandidat, idMatiere);
        
        // Transmettre le résultat à la vue
        model.addAttribute("noteFinale", noteFinale);
        model.addAttribute("idCandidat", idCandidat);
        model.addAttribute("idMatiere", idMatiere);
        
        return "resultat";
    }
}
