package com.exemple.projet.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exemple.projet.model.Candidat;
import com.exemple.projet.model.Matiere;
import com.exemple.projet.model.Note;
import com.exemple.projet.model.Operateur;
import com.exemple.projet.model.Parametre;
import com.exemple.projet.model.Resolution;
import com.exemple.projet.repository.CandidatRepository;
import com.exemple.projet.repository.MatiereRepository;
import com.exemple.projet.repository.NoteRepository;
import com.exemple.projet.repository.ParametreRepository;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private CandidatRepository candidatRepository;
    
    @Autowired
    private MatiereRepository matiereRepository;
    
    @Autowired
    private ParametreRepository parametreRepository;

    @Override
    public BigDecimal calculerNoteFinale(Integer idCandidat, Integer idMatiere) {
        // Récupérer toutes les notes
        List<Note> toutesLesNotes = noteRepository.findAll();
        List<BigDecimal> corrections = new ArrayList<>();
        
        // Filtrer les notes pour ce candidat et cette matière
        for (Note note : toutesLesNotes) {
            if (note.getCandidat() != null && note.getMatiere() != null) {
                if (note.getCandidat().getIdCandidat().equals(idCandidat) &&
                    note.getMatiere().getIdMatiere().equals(idMatiere)) {
                    corrections.add(note.getNote());
                }
            }
        }
        
        // Si pas assez de corrections, retourner 0
        if (corrections.size() < 2) {
            return BigDecimal.ZERO;
        }
        
        // Vérifier si toutes les notes sont identiques
        boolean tousIdentiques = true;
        BigDecimal premiere = corrections.get(0);
        for (BigDecimal note : corrections) {
            if (note.compareTo(premiere) != 0) {
                tousIdentiques = false;
                break;
            }
        }
        
        // Si toutes les notes sont identiques, retourner cette note
        if (tousIdentiques) {
            return premiere;
        }
        
        // Si les notes sont différentes, utiliser les paramètres
        List<Parametre> parametres = parametreRepository.findAll();
        Parametre param = null;
        for (Parametre p : parametres) {
            if (p.getMatiere() != null && p.getMatiere().getIdMatiere().equals(idMatiere)) {
                param = p;
                break;
            }
        }
        
        // Si pas de paramètre défini, retourner la moyenne
        if (param == null) {
            return calculerMoyenne(corrections);
        }
        
        // Calculer la différence maximale
        BigDecimal diffMax = BigDecimal.ZERO;
        for (int i = 0; i < corrections.size(); i++) {
            for (int j = i + 1; j < corrections.size(); j++) {
                BigDecimal diff = corrections.get(i).subtract(corrections.get(j)).abs();
                if (diff.compareTo(diffMax) > 0) {
                    diffMax = diff;
                }
            }
        }
        
        BigDecimal seuil = new BigDecimal(param.getDiff());
        
        // Si la différence max est <= au seuil
        if (diffMax.compareTo(seuil) <= 0) {
            // Utiliser la résolution (moyenne)
            return calculerMoyenne(corrections);
        } else {
            // Appliquer l'opérateur avec la différence
            BigDecimal moyenne = calculerMoyenne(corrections);
            String op = param.getOperateur().getOperateur();
            
            switch (op) {
                case "+":
                    return moyenne.add(diffMax);
                case "-":
                    return moyenne.subtract(diffMax);
                case "*":
                    return moyenne.multiply(diffMax);
                case "/":
                    if (diffMax.compareTo(BigDecimal.ZERO) != 0) {
                        return moyenne.divide(diffMax, 2, RoundingMode.HALF_UP);
                    }
                    return moyenne;
                default:
                    return moyenne;
            }
        }
    }
    
    private BigDecimal calculerMoyenne(List<BigDecimal> notes) {
        BigDecimal somme = BigDecimal.ZERO;
        for (BigDecimal note : notes) {
            somme = somme.add(note);
        }
        return somme.divide(new BigDecimal(notes.size()), 2, RoundingMode.HALF_UP);
    }
    
    @Override
    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }
    
    @Override
    public List<Note> findAllNotes() {
        return noteRepository.findAll();
    }
}
