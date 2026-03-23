package com.exemple.projet.controller;

/**
 * Contrôleur Matiere - Gère les pages liées aux matières.
 * 
 * Fonctions:
 * - Liste des matières
 * - Formulaire d'ajout
 * - Formulaire de modification
 * - Suppression
 */

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.exemple.projet.model.Matiere;
import com.exemple.projet.repository.MatiereRepository;
import com.exemple.projet.repository.NoteRepository;
import com.exemple.projet.repository.ParametreRepository;

@Controller
public class MatiereController {

    // Repository pour accéder aux données des matières
    @Autowired
    private MatiereRepository matiereRepository;
    
    // Repository pour accéder aux données des notes
    @Autowired
    private NoteRepository noteRepository;
    
    // Repository pour accéder aux données des paramètres
    @Autowired
    private ParametreRepository parametreRepository;

    /**
     * Affiche la liste de toutes les matières.
     * URL: /matieres
     * Vue: matieres/liste.jsp
     */
    @GetMapping("/matieres")
    public String listeMatieres(Model model) {
        // Récupérer toutes les matières depuis la base de données
        List<Matiere> matieres = matiereRepository.findAll();
        // Transmettre à la vue
        model.addAttribute("matieres", matieres);
        return "matieres/liste";
    }

    /**
     * Affiche le formulaire pour ajouter une matière.
     * URL: /matieres/ajouter
     * Vue: matieres/formulaire.jsp
     */
    @GetMapping("/matieres/ajouter")
    public String ajouterMatiere(Model model) {
        // Créer un nouvel objet Matiere vide
        model.addAttribute("matiere", new Matiere());
        return "matieres/formulaire";
    }

    /**
     * Sauvegarde une matière (nouvelle ou modification).
     * URL: /matieres/sauvegarder
     * Méthode: POST
     */
    @PostMapping("/matieres/sauvegarder")
    public String sauvegarderMatiere(@ModelAttribute Matiere matiere) {
        // Sauvegarder dans la base de données
        matiereRepository.save(matiere);
        // Rediriger vers la liste
        return "redirect:/matieres";
    }

    /**
     * Affiche le formulaire pour modifier une matière existante.
     * URL: /matieres/modifier?id=1
     * Vue: matieres/formulaire.jsp
     */
    @GetMapping("/matieres/modifier")
    public String modifierMatiere(@RequestParam Integer id, Model model) {
        // Récupérer la matière par son ID
        Matiere matiere = matiereRepository.findById(id).orElse(null);
        // Transmettre à la vue
        model.addAttribute("matiere", matiere);
        return "matieres/formulaire";
    }

    /**
     * Supprime une matière.
     * URL: /matieres/supprimer?id=1
     * 
     * Supprime d'abord les notes et paramètres associés à la matière
     * pour éviter les erreurs de clé étrangère.
     */
    @GetMapping("/matieres/supprimer")
    public String supprimerMatiere(@RequestParam Integer id) {
        // D'abord supprimer toutes les notes de la matière
        noteRepository.deleteByMatiere_IdMatiere(id);
        
        // Supprimer tous les paramètres de la matière
        parametreRepository.deleteByMatiere_IdMatiere(id);
        
        // Ensuite supprimer la matière
        matiereRepository.deleteById(id);
        
        // Rediriger vers la liste
        return "redirect:/matieres";
    }
}
