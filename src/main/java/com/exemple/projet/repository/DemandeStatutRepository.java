package com.exemple.projet.repository;

import com.exemple.projet.model.DemandeStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DemandeStatutRepository extends JpaRepository<DemandeStatut, Integer> {
    
    List<DemandeStatut> findByDemandeIdOrderByDateChangementDesc(Integer demandeId);
    
    Optional<DemandeStatut> findFirstByDemandeIdOrderByDateChangementDesc(Integer demandeId);
    
    List<DemandeStatut> findByTravauxIdOrderByDateChangementDesc(Integer travauxId);
    
    Optional<DemandeStatut> findFirstByTravauxIdOrderByDateChangementDesc(Integer travauxId);
    
}
