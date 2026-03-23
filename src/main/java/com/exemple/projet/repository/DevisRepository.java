package com.exemple.projet.repository;

import com.exemple.projet.model.Devis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevisRepository extends JpaRepository<Devis, Integer> {
    
}
