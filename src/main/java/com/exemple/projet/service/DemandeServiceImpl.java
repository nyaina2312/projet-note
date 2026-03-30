package com.exemple.projet.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.exemple.projet.model.Demande;
import com.exemple.projet.model.DemandeStatut;
import com.exemple.projet.model.Statut;
import com.exemple.projet.model.Travaux;
import com.exemple.projet.repository.DemandeRepository;
import com.exemple.projet.repository.DemandeStatutRepository;
import com.exemple.projet.repository.StatutRepository;
import com.exemple.projet.repository.TravauxRepository;
import com.exemple.projet.repository.DevisRepository;

@Service
public class DemandeServiceImpl implements DemandeService {

    @Autowired
    private DemandeRepository demandeRepository;
    
    @Autowired
    private TravauxRepository travauxRepository;
    
    @Autowired
    private DemandeStatutRepository demandeStatutRepository;
    
    @Autowired
    private StatutRepository statutRepository;
    
    @Autowired
    private DevisRepository devisRepository;
    
    @Override
    public List<Demande> findAll() {
        return demandeRepository.findAll();
    }
    
    @Override
    public List<Demande> findAllWithClient() {
        return demandeRepository.findAllWithClient();
    }
    
    @Override
    public Optional<Demande> findById(Integer id) {
        return demandeRepository.findById(id);
    }
    
    @Override
    public Demande save(Demande demande) {
        Demande savedDemande = demandeRepository.save(demande);
        
        // Vérifier si c'est une nouvelle demande (pas d'ID avant sauvegarde)
        if (demande.getId() == null) {
            // Créer les travaux automatiquement
            Travaux travaux = new Travaux();
            travaux.setDemande(savedDemande);
            travaux.setTypeStatut(null);
            travauxRepository.save(travaux);
            
            // Ajouter le statut initial "En attente"
            Statut statutEnAttente = statutRepository.findById(1).orElse(null);
            if (statutEnAttente != null) {
                DemandeStatut demandeStatut = new DemandeStatut();
                demandeStatut.setDemande(savedDemande);
                demandeStatut.setStatut(statutEnAttente);
                demandeStatut.setDateChangement(new Date());
                demandeStatutRepository.save(demandeStatut);
            }
        }
        
        return savedDemande;
    }
    
    @Override
    @Transactional
    public void deleteById(Integer id) {
        try {
            // Supprimer d'abord les Devisassocis  cette demande
            List<com.exemple.projet.model.Devis> devisList = devisRepository.findByDemandeId(id);
            for (com.exemple.projet.model.Devis devis : devisList) {
                // Supprimer les details du devis
                if (devis.getDetailsDevis() != null) {
                    for (com.exemple.projet.model.DetailsDevis detail : devis.getDetailsDevis()) {
                        // Supprimer via le repository
                    }
                }
                // Supprimer le devis
                devisRepository.delete(devis);
            }
            
            // Supprimer les travaux associs
            Optional<Travaux> travauxOpt = travauxRepository.findByDemandeId(id);
            if (travauxOpt.isPresent()) {
                Travaux travaux = travauxOpt.get();
                // Supprimer les demandes_statut associs aux travaux
                List<DemandeStatut> historique = demandeStatutRepository.findByTravauxIdOrderByDateChangementDesc(travaux.getId());
                for (DemandeStatut ds : historique) {
                    demandeStatutRepository.delete(ds);
                }
                // Supprimer les travaux
                travauxRepository.delete(travaux);
            }
            
            // Finally, supprimer la demande
            demandeRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression: " + e.getMessage());
        }
    }
    
    @Override
    public Statut getStatutActuel(Integer demandeId) {
        try {
            Optional<Travaux> travauxOpt = travauxRepository.findByDemandeId(demandeId);
            if (travauxOpt.isPresent()) {
                Optional<DemandeStatut> demandeStatutOpt = demandeStatutRepository
                        .findFirstByTravauxIdOrderByDateChangementDesc(travauxOpt.get().getId());
                if (demandeStatutOpt.isPresent()) {
                    return demandeStatutOpt.get().getStatut();
                }
            }
        } catch (Exception e) {
            // En cas d'erreur, retourner null
            return null;
        }
        return null;
    }
    
    @Override
    @Transactional
    public void changerStatut(Integer demandeId, Integer nouveauStatutId) {
        Optional<Travaux> travauxOpt = travauxRepository.findByDemandeId(demandeId);
        if (travauxOpt.isPresent()) {
            Travaux travaux = travauxOpt.get();
            
            // Mettre à jour le champ typeStatut dans Travaux
            Optional<Statut> nouveauStatutOpt = statutRepository.findById(nouveauStatutId);
            if (nouveauStatutOpt.isPresent()) {
                travaux.setTypeStatut(nouveauStatutOpt.get().getTypeStatut());
                travauxRepository.save(travaux);
                
                // Ajouter l'historique
                DemandeStatut demandeStatut = new DemandeStatut();
                demandeStatut.setDemande(travaux.getDemande());
                demandeStatut.setStatut(nouveauStatutOpt.get());
                demandeStatut.setDateChangement(new Date());
                demandeStatutRepository.save(demandeStatut);
            }
        }
    }
}
