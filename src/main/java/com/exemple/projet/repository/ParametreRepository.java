package com.exemple.projet.repository;

import com.exemple.projet.model.Parametre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParametreRepository extends JpaRepository<Parametre, Integer> {
    
    // Supprimer tous les paramètres d'une matière
    void deleteByMatiere_IdMatiere(Integer idMatiere);
    
    // Trouver tous les paramètres d'une matière
    List<Parametre> findByMatiere_IdMatiere(Integer idMatiere);
    
}
