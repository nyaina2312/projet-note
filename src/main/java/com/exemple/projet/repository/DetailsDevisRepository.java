package com.exemple.projet.repository;

import com.exemple.projet.model.DetailsDevis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailsDevisRepository extends JpaRepository<DetailsDevis, Integer> {
    
    List<DetailsDevis> findByDevisId(Integer devisId);
    
}
