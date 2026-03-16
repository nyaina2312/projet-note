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
import com.exemple.projet.repository.OperateurRepository;
import com.exemple.projet.repository.ResolutionRepository;

import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
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
        
        // Filtrer les paramètres pour cette matière
        List<Parametre> parametresMatiere = new ArrayList<>();
        for (Parametre p : parametres) {
            if (p.getMatiere() != null && p.getMatiere().getIdMatiere().equals(idMatiere)) {
                parametresMatiere.add(p);
            }
        }
        
        logger.info("Parametres pour matiere {}: {}", idMatiere, parametresMatiere.size());
        
        // Si pas de paramètre défini, retourner la moyenne
        if (parametresMatiere.isEmpty()) {
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
        
        logger.info("DiffMax calculé: {}", diffMax);
        
        // Nouvelle logique : trouver le paramètre avec la plus petite distance à diffMax
        Parametre meilleurParam = null;
        BigDecimal plusPetiteDistance = null;
        
        for (Parametre p : parametresMatiere) {
            BigDecimal diffParam = new BigDecimal(p.getDiff());
            BigDecimal distance = diffMax.subtract(diffParam).abs();
            
            logger.info("Parametre diff={}, distance={}", p.getDiff(), distance);
            
            if (plusPetiteDistance == null || distance.compareTo(plusPetiteDistance) < 0) {
                plusPetiteDistance = distance;
                meilleurParam = p;
            } else if (distance.compareTo(plusPetiteDistance) == 0) {
                // En cas d'égalité, prendre le plus petit diff
                if (meilleurParam != null && diffParam.compareTo(new BigDecimal(meilleurParam.getDiff())) < 0) {
                    meilleurParam = p;
                }
            }
        }
        
        if (meilleurParam == null || meilleurParam.getResolution() == null) {
            return calculerMoyenne(corrections);
        }
        
        int idResolution = meilleurParam.getResolution().getIdResolution();
        logger.info("Meilleur param: diff={}, resolution={}", meilleurParam.getDiff(), idResolution);
        
        // Appliquer la résolution trouvée
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
    }
    
    private BigDecimal calculerMoyenne(List<BigDecimal> notes) {
        BigDecimal somme = BigDecimal.ZERO;
        for (BigDecimal note : notes) {
            somme = somme.add(note);
        }
        return somme.divide(new BigDecimal(notes.size()), 2, RoundingMode.HALF_UP);
    }
    
    @Override
    @Transactional
    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }
    
    @Override
    @Transactional
    public List<Note> findAllNotes() {
        return noteRepository.findAll();
    }
}
