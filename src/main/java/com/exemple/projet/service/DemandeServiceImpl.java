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
        System.out.println("DEBUG: save called - demande.id=" + demande.getId());
        Demande savedDemande = demandeRepository.save(demande);
        
        // Vérifier si c'est une nouvelle demande après sauvegarde
        if (savedDemande.getId() != null) {
            // Vérifier si les travaux existent déjà
            Optional<Travaux> existingTravaux = travauxRepository.findByDemandeId(savedDemande.getId());
            if (!existingTravaux.isPresent()) {
                // Créer les travaux automatiquement
                System.out.println("DEBUG: Creating travaux for demande " + savedDemande.getId());
                Travaux travaux = new Travaux();
                travaux.setDemande(savedDemande);
                travaux.setTypeStatut(null);
                Travaux savedTravaux = travauxRepository.save(travaux);
                travauxRepository.flush();
                
                // Ajouter le statut initial "En attente"
                Statut statutEnAttente = statutRepository.findById(1).orElse(null);
                if (statutEnAttente != null) {
                    DemandeStatut demandeStatut = new DemandeStatut();
                    demandeStatut.setDemande(savedDemande);
                    demandeStatut.setStatut(statutEnAttente);
                    demandeStatut.setDateChangement(new Date());
                    demandeStatut.setTravaux(savedTravaux);
                    demandeStatutRepository.save(demandeStatut);
                    demandeStatutRepository.flush();
                    System.out.println("DEBUG: Created DemandeStatut with id=" + demandeStatut.getId());
                }
            } else {
                System.out.println("DEBUG: Travaux already exists for demande " + savedDemande.getId());
                // Les travaux existent, vérifier s'il y a un DemandeStatut
                List<DemandeStatut> allStats = demandeStatutRepository.findAll();
                boolean hasStatut = allStats.stream()
                    .anyMatch(s -> s.getDemande() != null && s.getDemande().getId().equals(savedDemande.getId()));
                
                System.out.println("DEBUG: hasStatut for demande " + savedDemande.getId() + " = " + hasStatut);
                
                if (!hasStatut) {
                    System.out.println("DEBUG: Creating DemandeStatut for existing travaux");
                    // Créer le statut initial si aucun n'existe
                    Statut statutEnAttente = statutRepository.findById(1).orElse(null);
                    if (statutEnAttente != null) {
                        DemandeStatut demandeStatut = new DemandeStatut();
                        demandeStatut.setDemande(savedDemande);
                        demandeStatut.setStatut(statutEnAttente);
                        demandeStatut.setDateChangement(new Date());
                        demandeStatut.setTravaux(existingTravaux.get());
                        demandeStatutRepository.save(demandeStatut);
                        demandeStatutRepository.flush();
                        System.out.println("DEBUG: Created DemandeStatut with id=" + demandeStatut.getId());
                    }
                }
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
            List<DemandeStatut> allStats = demandeStatutRepository.findAll();
            final Integer demandeIdFinal = demandeId;
            DemandeStatut lastStatut = allStats.stream()
                .filter(s -> s.getDemande() != null && s.getDemande().getId().equals(demandeIdFinal))
                .sorted((s1, s2) -> s2.getDateChangement().compareTo(s1.getDateChangement()))
                .findFirst()
                .orElse(null);
            if (lastStatut != null) {
                return lastStatut.getStatut();
            }
        } catch (Exception e) {
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
                demandeStatut.setTravaux(travaux);
                demandeStatutRepository.save(demandeStatut);
            }
        }
    }
    
    @Override
    @Transactional
    public void changerStatutAvecObservation(Integer demandeId, Integer nouveauStatutId, String observation) {
        Optional<Travaux> travauxOpt = travauxRepository.findByDemandeId(demandeId);
        if (travauxOpt.isPresent()) {
            Travaux travaux = travauxOpt.get();
            
            Optional<Statut> nouveauStatutOpt = statutRepository.findById(nouveauStatutId);
            if (nouveauStatutOpt.isPresent()) {
                travaux.setTypeStatut(nouveauStatutOpt.get().getTypeStatut());
                travauxRepository.save(travaux);
                
                DemandeStatut demandeStatut = new DemandeStatut();
                demandeStatut.setDemande(travaux.getDemande());
                demandeStatut.setStatut(nouveauStatutOpt.get());
                demandeStatut.setDateChangement(new Date());
                demandeStatut.setObservation(observation);
                demandeStatut.setTravaux(travaux);
                demandeStatutRepository.save(demandeStatut);
            }
        }
    }
    
    @Override
    @Transactional
    public void ajouterObservation(Integer demandeId, String observation) {
        System.out.println("DEBUG: ajouterObservation called - demandeId=" + demandeId + ", observation=" + observation);
        
        if (observation == null || observation.isEmpty()) {
            System.out.println("DEBUG: observation is empty, returning");
            return;
        }
        
        // Trouver la demande
        Optional<Demande> demandeOpt = demandeRepository.findById(demandeId);
        if (!demandeOpt.isPresent()) {
            System.out.println("DEBUG: Demande not found");
            return;
        }
        
        System.out.println("DEBUG: Demande found with id=" + demandeOpt.get().getId());
        
        // Obtenir le dernier statut pour garder le même
        List<DemandeStatut> allStats = demandeStatutRepository.findAll();
        System.out.println("DEBUG: Total DemandeStatut count: " + allStats.size());
        
        List<DemandeStatut> statsForDemande = allStats.stream()
            .filter(s -> s.getDemande() != null && s.getDemande().getId().equals(demandeId))
            .sorted((s1, s2) -> s2.getDateChangement().compareTo(s1.getDateChangement()))
            .collect(java.util.stream.Collectors.toList());
        
        System.out.println("DEBUG: statsForDemande count: " + statsForDemande.size());
        
        // Créer une NOUVELLE ligne avec le dernier statut et la nouvelle observation
        Statut statut = null;
        if (!statsForDemande.isEmpty()) {
            statut = statsForDemande.get(0).getStatut();
            System.out.println("DEBUG: Using existing statut: " + statut.getLibelle());
        } else {
            statut = statutRepository.findById(1).orElse(null); // En attente par défaut
            System.out.println("DEBUG: No existing statut, using default 'En attente'");
        }
        
        // Trouver ou créer les travaux
        List<Travaux> travauxList = travauxRepository.findAll();
        System.out.println("DEBUG: Total travaux count: " + travauxList.size());
        
        Optional<Travaux> travauxOpt = travauxList.stream()
            .filter(t -> t.getDemande() != null && t.getDemande().getId().equals(demandeId))
            .findFirst();
        
        Travaux travaux;
        if (travauxOpt.isPresent()) {
            travaux = travauxOpt.get();
            System.out.println("DEBUG: Using existing travaux id=" + travaux.getId());
        } else {
            System.out.println("DEBUG: Creating new travaux");
            travaux = new Travaux();
            travaux.setDemande(demandeOpt.get());
            travaux.setTypeStatut(null);
            travaux = travauxRepository.save(travaux);
        }
        
        // Créer une NOUVELLE entrée dans l'historique
        if (statut != null) {
            DemandeStatut newStatut = new DemandeStatut();
            newStatut.setDemande(demandeOpt.get());
            newStatut.setStatut(statut);
            newStatut.setDateChangement(new Date());
            newStatut.setObservation(observation);
            newStatut.setTravaux(travaux);
            demandeStatutRepository.save(newStatut);
            demandeStatutRepository.flush();
            System.out.println("DEBUG: Created new DemandeStatut with observation");
        } else {
            System.out.println("DEBUG: statut is null, cannot create DemandeStatut");
        }
    }
}
