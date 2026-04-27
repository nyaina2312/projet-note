package com.exemple.projet.repository;

import com.exemple.projet.model.HoraireTravail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoraireTravailRepository extends JpaRepository<HoraireTravail, Integer> {
    // Une seule ligne attendue
    HoraireTravail findFirstBy();
}
