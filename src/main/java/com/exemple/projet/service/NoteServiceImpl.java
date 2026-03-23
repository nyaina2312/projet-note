package com.exemple.projet.service;

/**
 * Service qui gère le calcul des notes finales.
 * 
 * Fonctionnement :
 * 1. Récupère toutes les notes d'un candidat pour une matière
 * 2. Calcule la différence maximale entre les notes
 * 3. Trouve le paramètre qui correspond le mieux
 * 4. Applique la résolution (min/max/moyenne)
 */

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

@Service  // Indique que c'est un composant Spring de type Service
public class NoteServiceImpl implements NoteService {

    // Logger pour afficher des messages dans la console
    private static final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);

    // Injection automatique des repositories (accès à la base de données)
    @Autowired
    private NoteRepository noteRepository;  // Pour accéder aux notes
    
    @Autowired
    private CandidatRepository candidatRepository;  // Pour accéder aux candidats
    @Autowired
    private MatiereRepository matiereRepository;  // Pour accéder aux matières
    
    @Autowired
    private ParametreRepository parametreRepository;  // Pour accéder aux paramètres

    /**
     * Calcule la note finale d'un candidat pour une matière.
     * 
     * @param idCandidat ID du candidat
     * @param idMatiere ID de la matière
     * @return La note finale calculée
     */
    @Override
    @Transactional  //.transaction ensures data integrity
    public BigDecimal calculerNoteFinale(Integer idCandidat, Integer idMatiere) {
        // Log pour le débogage
        logger.info("Calcul note pour candidat={}, matiere={}", idCandidat, idMatiere);
        
        // Étape 1: Récupérer TOUTES les notes de la base de données
        List<Note> toutesLesNotes = noteRepository.findAll();
        logger.info("Total notes in DB: {}", toutesLesNotes.size());
        
        // Liste qui contiendra les notes du candidat pour la matière sélectionnée
        List<BigDecimal> corrections = new ArrayList<>();
        
        // Étape 2: Filtrer les notes pour garder seulement celles du candidat et de la matière
        for (Note note : toutesLesNotes) {
            if (note.getCandidat() != null && note.getMatiere() != null) {
                // Vérifier si la note appartient au candidat et à la matière demandés
                if (note.getCandidat().getIdCandidat().equals(idCandidat) &&
                    note.getMatiere().getIdMatiere().equals(idMatiere)) {
                    corrections.add(note.getNote());  // Ajouter la note à la liste
                }
            }
        }
        
        // Étape 3: Si moins de 2 notes, impossible de calculer → retourner 0
        if (corrections.size() < 2) {
            return BigDecimal.ZERO;
        }
        
        // Étape 4: Vérifier si toutes les notes sont identiques
        boolean tousIdentiques = true;
        BigDecimal premiere = corrections.get(0);  // Prendre la première note
        for (BigDecimal note : corrections) {
            if (note.compareTo(premiere) != 0) {  // Si différent
                tousIdentiques = false;
                break;
            }
        }
        
        // Si toutes identiques, pas besoin de calculer → retourner cette note
        if (tousIdentiques) {
            return premiere;
        }
        
        // Étape 5: Chercher les paramètres définis pour cette matière
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
        
        // Si aucun paramètre défini pour cette matière → retourner la moyenne
        if (parametresMatiere.isEmpty()) {
            return calculerMoyenne(corrections);
        }
        
        // Étape 6: Calculer la DIFFÉRENCE MAXIMALE entre les notes
        // Exemple: notes = [12, 11, 11] → diffMax = |12-11| = 1
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
        
        // Étape 7: NOUVELLE LOGIQUE - Trouver le paramètre avec la plus petite distance
        // Objectif: Trouver quel paramètre correspond le mieux à la différence observée
        Parametre meilleurParam = null;
        BigDecimal plusPetiteDistance = null;
        
        // Parcourir tous les paramètres de la matière
        for (Parametre p : parametresMatiere) {
            BigDecimal diffParam = new BigDecimal(p.getDiff());  // Seuil du paramètre
            BigDecimal distance = diffMax.subtract(diffParam).abs();  // Distance à diffMax
            
            logger.info("Parametre diff={}, distance={}", p.getDiff(), distance);
            
            // Garder le paramètre avec la plus petite distance
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
        
        // Si pas de paramètre trouvé → retourner la moyenne
        if (meilleurParam == null || meilleurParam.getResolution() == null) {
            return calculerMoyenne(corrections);
        }
        
        // Récupérer l'ID de la résolution
        int idResolution = meilleurParam.getResolution().getIdResolution();
        logger.info("Meilleur param: diff={}, resolution={}", meilleurParam.getDiff(), idResolution);
        
        // Étape 8: Appliquer la résolution trouvée
        // Resolution 1 = Petit → prendre le minimum
        // Resolution 2 = Grande → prendre le maximum  
        // Resolution 3 = Moyenne → calculer la moyenne
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
    
    /**
     * Calcule la moyenne d'une liste de notes.
     * 
     * @param notes Liste des notes
     * @return La moyenne arrondie à 2 décimales
     */
    private BigDecimal calculerMoyenne(List<BigDecimal> notes) {
        BigDecimal somme = BigDecimal.ZERO;
        for (BigDecimal note : notes) {
            somme = somme.add(note);  // Additionner toutes les notes
        }
        // Diviser par le nombre de notes, arrondir à 2 décimales
        return somme.divide(new BigDecimal(notes.size()), 2, RoundingMode.HALF_UP);
    }
    
    /**
     * Sauvegarde une note dans la base de données.
     * 
     * @param note La note à sauvegarder
     * @return La note sauvegardée
     */
    @Override
    @Transactional
    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }
    
    /**
     * Récupère toutes les notes de la base de données.
     * 
     * @return Liste de toutes les notes
     */
    @Override
    @Transactional
    public List<Note> findAllNotes() {
        return noteRepository.findAll();
    }
}
