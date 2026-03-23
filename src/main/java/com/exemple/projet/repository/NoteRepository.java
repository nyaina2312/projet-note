package com.exemple.projet.repository;

import com.exemple.projet.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {
    
    // Supprimer toutes les notes d'un candidat
    void deleteByCandidat_IdCandidat(Integer idCandidat);
    
    // Supprimer toutes les notes d'une matière
    void deleteByMatiere_IdMatiere(Integer idMatiere);
    
    // Supprimer toutes les notes d'un correcteur
    void deleteByCorrecteur_IdCorrecteur(Integer idCorrecteur);
    
    // Trouver toutes les notes d'un candidat
    List<Note> findByCandidat_IdCandidat(Integer idCandidat);
    
    // Trouver toutes les notes d'une matière
    List<Note> findByMatiere_IdMatiere(Integer idMatiere);
    
}
