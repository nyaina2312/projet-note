package com.exemple.projet.controller;

/**
 * Contrôleur Candidat - Gère les pages liées aux candidats.
 * 
 * Fonctions:
 * - Liste des candidats
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

import com.exemple.projet.model.Candidat;
import com.exemple.projet.repository.CandidatRepository;
import com.exemple.projet.repository.NoteRepository;

@Controller
public class CandidatController {

    // Repository pour accéder aux données des candidats
    @Autowired
    private CandidatRepository candidatRepository;
    
    // Repository pour accéder aux données des notes
    @Autowired
    private NoteRepository noteRepository;

    /**
     * Affiche la liste de tous les candidats.
     * URL: /candidats
     * Vue: candidats/liste.jsp
     */
    @GetMapping("/candidats")
    public String listeCandidats(Model model) {
        // Récupérer tous les candidats depuis la base de données
        List<Candidat> candidats = candidatRepository.findAll();
        // Transmettre à la vue
        model.addAttribute("candidats", candidats);
        return "candidats/liste";
    }

    /**
     * Affiche le formulaire pour ajouter un candidat.
     * URL: /candidats/ajouter
     * Vue: candidats/formulaire.jsp
     */
    @GetMapping("/candidats/ajouter")
    public String ajouterCandidat(Model model) {
        // Créer un nouvel objet Candidat vide
        model.addAttribute("candidat", new Candidat());
        return "candidats/formulaire";
    }

    /**
     * Sauvegarde un candidat (nouveau ou modification).
     * URL: /candidats/sauvegarder
     * Méthode: POST
     */
    @PostMapping("/candidats/sauvegarder")
    public String sauvegarderCandidat(@ModelAttribute Candidat candidat) {
        // Sauvegarder dans la base de données
        candidatRepository.save(candidat);
        // Rediriger vers la liste
        return "redirect:/candidats";
    }

    /**
     * Affiche le formulaire pour modifier un candidat existant.
     * URL: /candidats/modifier?id=1
     * Vue: candidats/formulaire.jsp
     */
    @GetMapping("/candidats/modifier")
    public String modifierCandidat(@RequestParam Integer id, Model model) {
        // Récupérer le candidat par son ID
        Candidat candidat = candidatRepository.findById(id).orElse(null);
        // Transmettre à la vue
        model.addAttribute("candidat", candidat);
        return "candidats/formulaire";
    }

    /**
     * Supprime un candidat.
     * URL: /candidats/supprimer?id=1
     * 
     * Supprime d'abord toutes les notes associées au candidat
     * pour éviter les erreurs de clé étrangère.
     */
    @GetMapping("/candidats/supprimer")
    public String supprimerCandidat(@RequestParam Integer id) {
        // D'abord supprimer toutes les notes du candidat
        noteRepository.deleteByCandidat_IdCandidat(id);
        
        // Ensuite supprimer le candidat
        candidatRepository.deleteById(id);
        
        // Rediriger vers la liste
        return "redirect:/candidats";
    }
}
