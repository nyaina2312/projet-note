package com.exemple.projet.repository;

import com.exemple.projet.model.DetailsDevis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailsDevisRepository extends JpaRepository<DetailsDevis, Integer> {
    
}
