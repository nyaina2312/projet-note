package com.exemple.projet.service;

import java.util.List;
import java.util.Optional;
import com.exemple.projet.model.Demande;
import com.exemple.projet.model.Statut;

public interface DemandeService {
    List<Demande> findAll();
    List<Demande> findAllWithClient();
    Optional<Demande> findById(Integer id);
    Demande save(Demande demande);
    void deleteById(Integer id);
    Statut getStatutActuel(Integer demandeId);
    void changerStatut(Integer demandeId, Integer nouveauStatutId);
}
