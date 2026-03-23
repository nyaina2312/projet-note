package com.exemple.projet.repository;

import com.exemple.projet.model.Travaux;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TravauxRepository extends JpaRepository<Travaux, Integer> {
    
    Optional<Travaux> findByDemandeId(Integer demandeId);
    
}
