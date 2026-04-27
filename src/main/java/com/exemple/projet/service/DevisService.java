package com.exemple.projet.service;

import com.exemple.projet.model.Devis;
import com.exemple.projet.model.DetailsDevis;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des devis.
 */
public interface DevisService {
    
    /**
     * Sauvegarder un devis avec ses détails en transaction.
     * @param devis Le devis à sauvegarder
     * @param details Liste des détails du devis
     * @return Le devis sauvegardé avec son ID
     */
    Devis saveDevisWithDetails(Devis devis, List<DetailsDevis> details);
    
    /**
     * Trouver tous les devis avec leurs détails.
     * @return Liste des devis
     */
    List<Devis> findAll();
    
    /**
     * Trouver un devis par son ID.
     * @param id ID du devis
     * @return Le devis trouvé
     */
    Optional<Devis> findById(Integer id);
    
    /**
     * Trouver tous les devis pour une demande.
     * @param demandeId ID de la demande
     * @return Liste des devis
     */
    List<Devis> findByDemandeId(Integer demandeId);
    
    /**
     * Supprimer un devis par son ID.
     * @param id ID du devis
     */
    void deleteById(Integer id);
    
    /**
     * Mettre à jour le statut d'un devis.
     * @param devisId ID du devis
     * @param statutId Nouveau statut ID
     */
    void updateStatut(Integer devisId, Integer statutId);                  // Change le statut d'un devis (accepter/refuser)

    List<DetailsDevis> findAllDetails();                                   // Récupère tous les détails de devis (pour la page CA)

    BigDecimal getChiffreAffairesProvisionnel();                           // Calcule SUM de tous les montants (= chiffre d'affaires)
}