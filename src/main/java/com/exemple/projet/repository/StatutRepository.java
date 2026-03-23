package com.exemple.projet.repository;

import com.exemple.projet.model.Statut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutRepository extends JpaRepository<Statut, Integer> {
    
}
