package com.exemple.projet.service;

import com.exemple.projet.model.Demande;
import com.exemple.projet.model.DemandeStatut;
import com.exemple.projet.model.Devis;
import com.exemple.projet.model.DetailsDevis;
import com.exemple.projet.model.Statut;
import com.exemple.projet.repository.DemandeRepository;
import com.exemple.projet.repository.DemandeStatutRepository;
import com.exemple.projet.repository.DevisRepository;
import com.exemple.projet.repository.DetailsDevisRepository;
import com.exemple.projet.repository.StatutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service pour la gestion des devis.
 */
@Service
public class DevisServiceImpl implements DevisService {
    
    @Autowired
    private DevisRepository devisRepository;
    
    @Autowired
    private DetailsDevisRepository detailsDevisRepository;
    
    @Autowired
    private DemandeRepository demandeRepository;
    
    @Autowired
    private DemandeStatutRepository demandeStatutRepository;
    
    @Autowired
    private StatutRepository statutRepository;
    
    @Override                                                              // Indique que cette méthode redéfinit une méthode de l'interface DevisService
    @Transactional                                                         // Toute la méthode s'exécute dans une transaction BD (si erreur, tout est annulé)
    public Devis saveDevisWithDetails(Devis devis, List<DetailsDevis> details) { // Reçoit le devis + liste de ses détails en paramètre
        // === ÉTAPE 1 : Calculer le montant total du devis ===
        BigDecimal montantTotal = BigDecimal.ZERO;                         // Initialise le total à 0 (BigDecimal pour la précision monétaire)
        BigDecimal seuilReduction = new BigDecimal("1000000");             // Définit le seuil de réduction à 1 000 000 Ariary

        for (DetailsDevis detail : details) {                              // Boucle sur chaque ligne de détail du devis
            if (detail.getPrixUnitaire() != null && detail.getQuantite() != null) { // Vérifie que le prix et la quantité sont renseignés
                // Règle métier : réduction de 10% si prixUnitaire >= 1 000 000 Ar
                // Exemple : 1 500 000 → 1 350 000 (1 500 000 × 0.9)
                BigDecimal prixEffectif = detail.getPrixUnitaire();        // Copie le prix unitaire saisi dans une variable locale

                if (prixEffectif.compareTo(seuilReduction) >= 0) {         // compareTo >= 0 signifie "supérieur ou égal à" 1 000 000
                    prixEffectif = prixEffectif.multiply(new BigDecimal("0.9")); // Multiplie par 0.9 = réduction de 10%
                    detail.setPrixUnitaire(prixEffectif);                  // Enregistre le prix réduit dans l'objet détail (sera sauvegardé en BD)
                }

                BigDecimal montant = prixEffectif.multiply(new BigDecimal(detail.getQuantite())); // Montant = prix effectif × quantité
                detail.setMontant(montant);                                // Enregistre le montant calculé dans l'objet détail
                montantTotal = montantTotal.add(montant);                  // Ajoute ce montant au total du devis
            }
        }
        devis.setMontantTotal(montantTotal);                               // Enregistre le montant total calculé dans l'objet devis
        devis.setDate(new Date());                                         // Enregistre la date actuelle comme date du devis

        // === ÉTAPE 2 : Sauvegarder le devis en base de données (table "devis") ===
        Devis savedDevis = devisRepository.save(devis);                    // INSERT en BD, Hibernate retourne l'objet avec l'ID auto-généré

        // === ÉTAPE 3 : Sauvegarder chaque détail avec la référence au devis parent ===
        for (DetailsDevis detail : details) {                              // Boucle sur chaque détail
            detail.setDevis(savedDevis);                                   // Lie le détail au devis parent (clé étrangère devis_id)
            detailsDevisRepository.save(detail);                           // INSERT en BD dans la table "details_devis"
        }

        // === ÉTAPE 4 : Créer une entrée d'historique de statut pour ce devis ===
        final String libelleStatut;                                        // Variable finale (obligatoire pour l'utiliser dans le lambda plus bas)
        if (savedDevis.getTypeDevis() != null                              // Vérifie si le type de devis existe
                && savedDevis.getTypeDevis().getLibelle().equalsIgnoreCase("Forage")) { // Vérifie si c'est un devis de type "Forage"
            libelleStatut = "Devis forage créé";                           // Si Forage → statut "Devis forage créé"
        } else {
            libelleStatut = "Devis étude créé";                            // Sinon → statut "Devis étude créé"
        }

        Optional<Statut> statutOpt = statutRepository.findAll().stream()   // Récupère tous les statuts et les convertit en Stream
                .filter(s -> s.getLibelle().equalsIgnoreCase(libelleStatut)) // Filtre pour trouver le statut avec le bon libellé
                .findFirst();                                              // Prend le premier résultat (ou vide si aucun)

        if (statutOpt.isPresent()) {                                       // Si le statut a été trouvé dans la BD
            DemandeStatut demandeStatut = new DemandeStatut();             // Crée un nouvel objet d'historique de statut
            demandeStatut.setDemande(savedDevis.getDemande());             // Associe la demande concernée
            demandeStatut.setStatut(statutOpt.get());                      // Associe le statut trouvé
            demandeStatut.setDateChangement(new Date());                   // Enregistre la date/heure du changement
            demandeStatutRepository.save(demandeStatut);                   // Sauvegarde en BD dans "demande_statut"
        }

        return savedDevis;                                                 // Retourne le devis sauvegardé (avec son ID)
    }
    
    @Override
    public List<Devis> findAll() {
        return devisRepository.findAllWithDetails();
    }
    
    @Override
    public Optional<Devis> findById(Integer id) {
        return devisRepository.findByIdWithDetails(id);
    }
    
    @Override
    public List<Devis> findByDemandeId(Integer demandeId) {
        return devisRepository.findByDemandeId(demandeId);
    }
    
    @Override
    @Transactional
    public void deleteById(Integer id) {
        // Supprimer d'abord les détails
        List<DetailsDevis> details = detailsDevisRepository.findByDevisId(id);
        detailsDevisRepository.deleteAll(details);
        
        // Puis supprimer le devis
        devisRepository.deleteById(id);
    }
    
    // Récupère tous les détails de devis avec leur devis parent
    // Utilisé par la page Chiffre d'Affaires Provisionnel
    @Override                                                              // Redéfinit la méthode de l'interface DevisService
    public List<DetailsDevis> findAllDetails() {                           // Retourne une liste de tous les détails de devis
        return detailsDevisRepository.findAllWithDevis();                  // Appelle le repository qui fait un JOIN FETCH avec le devis parent
    }

    // Calcule la somme de tous les montants de détails
    // Équivaut au chiffre d'affaires provisionnel (= SUM(montant) dans la BD)
    @Override                                                              // Redéfinit la méthode de l'interface DevisService
    public BigDecimal getChiffreAffairesProvisionnel() {                   // Retourne un BigDecimal (la somme totale)
        return detailsDevisRepository.sumAllMontants();                    // Appelle le repository qui fait SELECT SUM(montant) FROM details_devis
    }
    
    @Override
    @Transactional
    public void updateStatut(Integer devisId, Integer statutId) {
        Optional<Devis> devisOpt = devisRepository.findById(devisId);
        if (devisOpt.isPresent()) {
            Devis devis = devisOpt.get();
            Optional<Statut> statutOpt = statutRepository.findById(statutId);
            
            if (statutOpt.isPresent()) {
                // Créer une nouvelle entrée dans demande_statut
                DemandeStatut demandeStatut = new DemandeStatut();
                demandeStatut.setDemande(devis.getDemande());
                demandeStatut.setStatut(statutOpt.get());
                demandeStatut.setDateChangement(new Date());
                demandeStatutRepository.save(demandeStatut);
            }
        }
    }
}