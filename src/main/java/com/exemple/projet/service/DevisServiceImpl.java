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
    
    @Override
    @Transactional
    public Devis saveDevisWithDetails(Devis devis, List<DetailsDevis> details) {
        // 1. Calculer le montant total
        BigDecimal montantTotal = BigDecimal.ZERO;
        for (DetailsDevis detail : details) {
            if (detail.getPrixUnitaire() != null && detail.getQuantite() != null) {
                BigDecimal montant = detail.getPrixUnitaire().multiply(new BigDecimal(detail.getQuantite()));
                detail.setMontant(montant);
                montantTotal = montantTotal.add(montant);
            }
        }
        devis.setMontantTotal(montantTotal);
        devis.setDate(new Date());
        
        // 2. Sauvegarder le devis (table principale)
        Devis savedDevis = devisRepository.save(devis);
        
        // 3. Sauvegarder les détails avec l'ID du devis
        for (DetailsDevis detail : details) {
            detail.setDevis(savedDevis);
            detailsDevisRepository.save(detail);
        }
        
        // 4. Créer le statut du devis (Devis étude créé ou Devis forage créé)
        // Le type de devis 1 = Etude, 2 = Forage
        final String libelleStatut;
        if (savedDevis.getTypeDevis() != null && savedDevis.getTypeDevis().getLibelle().equalsIgnoreCase("Forage")) {
            libelleStatut = "Devis forage créé";
        } else {
            libelleStatut = "Devis étude créé";
        }
        
        Optional<Statut> statutOpt = statutRepository.findAll().stream()
                .filter(s -> s.getLibelle().equalsIgnoreCase(libelleStatut))
                .findFirst();
        
        if (statutOpt.isPresent()) {
            DemandeStatut demandeStatut = new DemandeStatut();
            demandeStatut.setDemande(savedDevis.getDemande());
            demandeStatut.setStatut(statutOpt.get());
            demandeStatut.setDateChangement(new Date());
            demandeStatutRepository.save(demandeStatut);
        }
        
        return savedDevis;
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