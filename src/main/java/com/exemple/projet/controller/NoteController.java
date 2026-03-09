package com.exemple.projet.controller;

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

@Controller
public class NoteController {

    @Autowired
    private NoteService noteService;
    
    @Autowired
    private CandidatRepository candidatRepository;
    
    @Autowired
    private MatiereRepository matiereRepository;
    
    @Autowired
    private CorrecteurRepository correcteurRepository;

    // Afficher la page de calcul
    @GetMapping("/notes")
    public String listeNotes(Model model) {
        model.addAttribute("candidats", candidatRepository.findAll());
        model.addAttribute("matieres", matiereRepository.findAll());
        return "listeNotes";
    }
    
    // Afficher le formulaire d'ajout
    @GetMapping("/notes/ajouter")
    public String ajouterNote(Model model) {
        model.addAttribute("note", new Note());
        model.addAttribute("candidats", candidatRepository.findAll());
        model.addAttribute("matieres", matiereRepository.findAll());
        model.addAttribute("correcteurs", correcteurRepository.findAll());
        return "ajouterNote";
    }
    
    // Sauvegarder la note
    @PostMapping("/notes/sauvegarder")
    public String sauvegarderNote(
            @RequestParam Integer candidatId,
            @RequestParam Integer matiereId,
            @RequestParam Integer correcteurId,
            @RequestParam BigDecimal note) {
        
        // Récupérer les entités depuis la base de données
        Candidat candidat = candidatRepository.findById(candidatId).orElse(null);
        Matiere matiere = matiereRepository.findById(matiereId).orElse(null);
        Correcteur correcteur = correcteurRepository.findById(correcteurId).orElse(null);
        
        // Créer la note
        Note newNote = new Note();
        newNote.setCandidat(candidat);
        newNote.setMatiere(matiere);
        newNote.setCorrecteur(correcteur);
        newNote.setNote(note);
        
        noteService.saveNote(newNote);
        return "redirect:/notes";
    }
    
    // Calculer la note finale
    @GetMapping("/notes/calculer")
    public String calculerNote(
            @RequestParam Integer idCandidat,
            @RequestParam Integer idMatiere,
            Model model) {
        
        BigDecimal noteFinale = noteService.calculerNoteFinale(idCandidat, idMatiere);
        model.addAttribute("noteFinale", noteFinale);
        model.addAttribute("idCandidat", idCandidat);
        model.addAttribute("idMatiere", idMatiere);
        
        return "resultat";
    }
}
