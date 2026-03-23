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
            travaux.setTestSanitaire(false);
            travauxRepository.save(travaux);
            
            // Ajouter le statut initial "En attente"
            Statut statutEnAttente = statutRepository.findById(1).orElse(null);
            if (statutEnAttente != null) {
                DemandeStatut demandeStatut = new DemandeStatut();
                demandeStatut.setTravaux(travaux);
                demandeStatut.setStatut(statutEnAttente);
                demandeStatut.setDateChangement(new Date());
                demandeStatutRepository.save(demandeStatut);
            }
        }
        
        return savedDemande;
    }
    
    @Override
    public void deleteById(Integer id) {
        demandeRepository.deleteById(id);
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
            
            // Mettre à jour le champ testSanitaire dans Travaux
            Optional<Statut> nouveauStatutOpt = statutRepository.findById(nouveauStatutId);
            if (nouveauStatutOpt.isPresent()) {
                travaux.setTestSanitaire(nouveauStatutOpt.get().getTestSanitaire());
                travauxRepository.save(travaux);
                
                // Ajouter l'historique
                DemandeStatut demandeStatut = new DemandeStatut();
                demandeStatut.setTravaux(travaux);
                demandeStatut.setStatut(nouveauStatutOpt.get());
                demandeStatut.setDateChangement(new Date());
                demandeStatutRepository.save(demandeStatut);
            }
        }
    }
}
