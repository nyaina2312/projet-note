package com.exemple.projet.controller;

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

@Controller
public class CandidatController {

    @Autowired
    private CandidatRepository candidatRepository;

    // Liste des candidats
    @GetMapping("/candidats")
    public String listeCandidats(Model model) {
        List<Candidat> candidats = candidatRepository.findAll();
        model.addAttribute("candidats", candidats);
        return "candidats/liste";
    }

    // Formulaire ajout candidat
    @GetMapping("/candidats/ajouter")
    public String ajouterCandidat(Model model) {
        model.addAttribute("candidat", new Candidat());
        return "candidats/formulaire";
    }

    // Sauvegarder candidat
    @PostMapping("/candidats/sauvegarder")
    public String sauvegarderCandidat(@ModelAttribute Candidat candidat) {
        candidatRepository.save(candidat);
        return "redirect:/candidats";
    }

    // Formulaire modifier candidat
    @GetMapping("/candidats/modifier")
    public String modifierCandidat(@RequestParam Integer id, Model model) {
        Candidat candidat = candidatRepository.findById(id).orElse(null);
        model.addAttribute("candidat", candidat);
        return "candidats/formulaire";
    }

    // Supprimer candidat
    @GetMapping("/candidats/supprimer")
    public String supprimerCandidat(@RequestParam Integer id) {
        candidatRepository.deleteById(id);
        return "redirect:/candidats";
    }
}
