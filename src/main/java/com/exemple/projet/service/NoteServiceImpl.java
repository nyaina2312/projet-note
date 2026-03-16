package com.exemple.projet.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);

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
        logger.info("Calcul note pour candidat={}, matiere={}", idCandidat, idMatiere);
        
        // Récupérer toutes les notes
        List<Note> toutesLesNotes = noteRepository.findAll();
        logger.info("Total notes in DB: {}", toutesLesNotes.size());
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
        
        // Chercher les paramètres pour cette matière
        List<Parametre> parametres = parametreRepository.findAll();
        logger.info("Total parametres: {}", parametres.size());
        Parametre param = null;
        for (Parametre p : parametres) {
            logger.info("Parametre: idMatiere={}, diff={}, idOperateur={}, idResolution={}", 
                p.getMatiere() != null ? p.getMatiere().getIdMatiere() : null,
                p.getDiff(),
                p.getOperateur() != null ? p.getOperateur().getIdOperateur() : null,
                p.getResolution() != null ? p.getResolution().getIdResolution() : null);
            if (p.getMatiere() != null && p.getMatiere().getIdMatiere().equals(idMatiere)) {
                param = p;
                break;
            }
        }
        
        // Si pas de paramètre défini, retourner la moyenne
        if (param == null || param.getOperateur() == null || param.getResolution() == null) {
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
        int idOperateur = param.getOperateur().getIdOperateur();
        int idResolution = param.getResolution().getIdResolution();
        
        // Vérifier la condition selon l'opérateur
        boolean conditionVerifiee = false;
        switch (idOperateur) {
            case 1: // >
                conditionVerifiee = diffMax.compareTo(seuil) > 0;
                break;
            case 2: // <
                conditionVerifiee = diffMax.compareTo(seuil) < 0;
                break;
            case 3: // >=
                conditionVerifiee = diffMax.compareTo(seuil) >= 0;
                break;
            case 4: // <=
                conditionVerifiee = diffMax.compareTo(seuil) <= 0;
                break;
        }
        
        // Appliquer la résolution selon le résultat
        if (conditionVerifiee) {
            // Utiliser la résolution définie
            switch (idResolution) {
                case 1: // Petit - la plus petite note
                    return Collections.min(corrections);
                case 2: // Grande - la plus grande note
                    return Collections.max(corrections);
                case 3: // Moyenne
                    return calculerMoyenne(corrections);
                default:
                    return calculerMoyenne(corrections);
            }
        } else {
            // Condition non vérifiée - retourner la moyenne par défaut
            return calculerMoyenne(corrections);
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
