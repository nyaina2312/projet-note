package com.exemple.projet.repository;

import com.exemple.projet.model.Devis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DevisRepository extends JpaRepository<Devis, Integer> {
    
    List<Devis> findByDemandeId(Integer demandeId);
    
    @Query("SELECT d FROM Devis d LEFT JOIN FETCH d.detailsDevis WHERE d.id = :id")
    Optional<Devis> findByIdWithDetails(@Param("id") Integer id);
    
    @Query("SELECT d FROM Devis d")
    List<Devis> findAllWithDetails();
    
}
