package com.exemple.projet.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import com.exemple.projet.model.Demande;
import com.exemple.projet.model.Statut;
import com.exemple.projet.model.Alerte;

public interface DemandeService {
    List<Demande> findAll();
    List<Demande> findAllWithClient();
    Optional<Demande> findById(Integer id);
    Demande save(Demande demande);
    void deleteById(Integer id);
    Statut getStatutActuel(Integer demandeId);
    void changerStatut(Integer demandeId, Integer nouveauStatutId);
    void changerStatutAvecObservation(Integer demandeId, Integer nouveauStatutId, String observation);
    void changerStatutAvecDate(Integer demandeId, Integer nouveauStatutId, Date dateChangement);
    void ajouterObservation(Integer demandeId, String observation);
    java.math.BigDecimal getMontantTotal(Integer demandeId);
    java.util.Map<Integer, Long> getStatsParStatut();
    long count();
    List<Statut> getAllStatuts();
    List<Alerte> getAlertesNonLues();
    long countAlertesNonLues();
    void marquerAlerteLue(Integer alerteId);
    List<Alerte> getAlertesParDemande(Integer demandeId);
    double getSeuilAlerteHeures();
}
