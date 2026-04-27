package com.exemple.projet.repository;

import com.exemple.projet.model.JourNonTravaille;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JourNonTravailleRepository extends JpaRepository<JourNonTravaille, Integer> {
    List<JourNonTravaille> findByDateJourBetween(LocalDate debut, LocalDate fin);
    boolean existsByDateJour(LocalDate dateJour);
}
