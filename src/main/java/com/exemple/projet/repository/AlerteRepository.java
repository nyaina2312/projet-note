package com.exemple.projet.repository;

import com.exemple.projet.model.Alerte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlerteRepository extends JpaRepository<Alerte, Integer> {
    List<Alerte> findByDemandeIdAndStatut(Integer demandeId, Alerte.StatutAlerte statut);
    List<Alerte> findByStatut(Alerte.StatutAlerte statut);
    long countByStatut(Alerte.StatutAlerte statut);
}
