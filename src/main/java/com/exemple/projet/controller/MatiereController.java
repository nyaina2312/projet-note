package com.exemple.projet.controller;

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

@Controller
public class MatiereController {

    @Autowired
    private MatiereRepository matiereRepository;

    // Liste des matières
    @GetMapping("/matieres")
    public String listeMatieres(Model model) {
        List<Matiere> matieres = matiereRepository.findAll();
        model.addAttribute("matieres", matieres);
        return "matieres/liste";
    }

    // Formulaire ajout matière
    @GetMapping("/matieres/ajouter")
    public String ajouterMatiere(Model model) {
        model.addAttribute("matiere", new Matiere());
        return "matieres/formulaire";
    }

    // Sauvegarder matière
    @PostMapping("/matieres/sauvegarder")
    public String sauvegarderMatiere(@ModelAttribute Matiere matiere) {
        matiereRepository.save(matiere);
        return "redirect:/matieres";
    }

    // Formulaire modifier matière
    @GetMapping("/matieres/modifier")
    public String modifierMatiere(@RequestParam Integer id, Model model) {
        Matiere matiere = matiereRepository.findById(id).orElse(null);
        model.addAttribute("matiere", matiere);
        return "matieres/formulaire";
    }

    // Supprimer matière
    @GetMapping("/matieres/supprimer")
    public String supprimerMatiere(@RequestParam Integer id) {
        matiereRepository.deleteById(id);
        return "redirect:/matieres";
    }
}
