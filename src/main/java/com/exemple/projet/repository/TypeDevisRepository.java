package com.exemple.projet.repository;

import com.exemple.projet.model.TypeDevis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeDevisRepository extends JpaRepository<TypeDevis, Integer> {
    
}
