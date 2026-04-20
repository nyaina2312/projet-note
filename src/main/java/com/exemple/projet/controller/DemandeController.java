package com.exemple.projet.controller;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.exemple.projet.model.Client;
import com.exemple.projet.model.Demande;
import com.exemple.projet.model.DemandeStatut;
import com.exemple.projet.model.Statut;
import com.exemple.projet.model.Travaux;
import com.exemple.projet.repository.ClientRepository;
import com.exemple.projet.repository.DemandeRepository;
import com.exemple.projet.repository.DemandeStatutRepository;
import com.exemple.projet.repository.StatutRepository;
import com.exemple.projet.repository.TravauxRepository;
import com.exemple.projet.service.DemandeService;

@Controller
public class DemandeController {

    @Autowired
    private DemandeService demandeService;
    
    @Autowired
    private DemandeRepository demandeRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private StatutRepository statutRepository;
    
    @Autowired
    private TravauxRepository travauxRepository;
    
    @Autowired
    private DemandeStatutRepository demandeStatutRepository;
    
    /**
     * Affiche la liste de toutes les demandes.
     */
    @GetMapping("/demandes")
    public String listeDemandes(Model model) {
        System.out.println("=== DEBUG: listeDemandes called ===");
        try {
            List<Demande> demandes = demandeService.findAllWithClient();
            System.out.println("=== DEBUG: Found " + demandes.size() + " demandes ===");
            
            // Créer une map des statuts actuels pour chaque demande
            java.util.Map<Integer, Statut> statutsMap = new java.util.HashMap<>();
            for (Demande d : demandes) {
                try {
                    Statut s = demandeService.getStatutActuel(d.getId());
                    if (s != null) {
                        statutsMap.put(d.getId(), s);
                    }
                } catch (Exception e) {
                    System.out.println("=== DEBUG: Error getting statut for demande " + d.getId() + ": " + e.getMessage());
                }
            }
            model.addAttribute("demandes", demandes);
            model.addAttribute("statutsMap", statutsMap);
            return "demandes/liste";
        } catch (Exception e) {
            System.out.println("=== DEBUG: Error in listeDemandes: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("demandes", new java.util.ArrayList<>());
            model.addAttribute("statutsMap", new java.util.HashMap<>());
            return "demandes/liste";
        }
    }
    
    /**
     * Affiche le formulaire pour ajouter une demande.
     */
    @GetMapping("/demandes/ajouter")
    public String ajouterDemande(Model model) {
        model.addAttribute("demande", new Demande());
        model.addAttribute("clients", clientRepository.findAll());
        return "demandes/formulaire";
    }
    
    /**
     * Sauvegarde une demande (nouvelle ou modification).
     */
    @PostMapping("/demandes/sauvegarder")
    public String sauvegarderDemande(@ModelAttribute Demande demande, @RequestParam("client.id") Integer clientId, 
            @RequestParam(required = false) String observation, Model model) {
        // Récupérer le client depuis la base de données
        Client client = clientRepository.findById(clientId).orElse(null);
        demande.setClient(client);
        
        // Sauvegarder la demande
        Demande savedDemande = demandeService.save(demande);
        
        // Si observation et ID existant, ajouter dans l'historique
        if (observation != null && !observation.isEmpty() && savedDemande.getId() != null) {
            demandeService.ajouterObservation(savedDemande.getId(), observation);
        }
        
        return "redirect:/demandes";
    }
    
    /**
     * Affiche le formulaire pour modifier une demande existante.
     */
    @GetMapping("/demandes/modifier")
    public String modifierDemande(@RequestParam Integer id, Model model) {
        try {
            Demande demande = demandeService.findById(id).orElse(null);
            model.addAttribute("demande", demande);
            model.addAttribute("clients", clientRepository.findAll());
            if (demande != null && demande.getClient() != null) {
                model.addAttribute("clientId", demande.getClient().getId());
            }
            return "demandes/formulaire";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/demandes";
        }
    }
    
    /**
     * Supprime une demande.
     */
    @GetMapping("/demandes/supprimer")
    public String supprimerDemandeGet(@RequestParam Integer id) {
        demandeService.deleteById(id);
        return "redirect:/demandes";
    }
    
    /**
     * Supprime une demande (POST).
     */
    @PostMapping("/demandes/supprimer")
    public String supprimerDemande(@RequestParam Integer id) {
        demandeService.deleteById(id);
        return "redirect:/demandes";
    }
    
    /**
     * Affiche les détails d'une demande avec son statut actuel.
     */
    @GetMapping("/demandes/voir")
    public String voirDemande(@RequestParam Integer id, Model model) {
        System.out.println("=== DEBUG: voirDemande called with id=" + id);
        
        // Utiliser le service qui charge maintenant avec le client grâce à FetchType.EAGER
        Demande demande = demandeService.findById(id).orElse(null);
        System.out.println("=== DEBUG: demande = " + demande);
        
        if (demande == null) {
            model.addAttribute("errorMessage", "Demande non trouvée");
            return "redirect:/demandes";
        }
        
        Statut statutActuel = null;
        try {
            statutActuel = demandeService.getStatutActuel(id);
        } catch (Exception e) {
            System.out.println("=== DEBUG: Error getting statut: " + e.getMessage());
        }
        
        model.addAttribute("demande", demande);
        model.addAttribute("statutActuel", statutActuel);
        model.addAttribute("statuts", statutRepository.findAll());
        
        // Récupérer l'historique des statuts
        try {
            // Obtenir tous les DemandeStatut et filtrer par demandeId
            List<DemandeStatut> allStats = demandeStatutRepository.findAll();
            System.out.println("=== DEBUG: Total DemandeStatut count: " + allStats.size());
            
            final Integer demandeIdFinal = id;
            List<DemandeStatut> historique = allStats.stream()
                .filter(s -> s.getDemande() != null && s.getDemande().getId() != null && s.getDemande().getId().equals(demandeIdFinal))
                .sorted((s1, s2) -> s2.getDateChangement().compareTo(s1.getDateChangement()))
                .collect(java.util.stream.Collectors.toList());
            System.out.println("=== DEBUG: Found " + historique.size() + " history entries for demande " + id);
            
            // Debug: afficher les ids des demandes associées
            for (DemandeStatut ds : allStats) {
                if (ds.getDemande() != null) {
                    System.out.println("=== DEBUG: DemandeStatut " + ds.getId() + " has demandeId=" + ds.getDemande().getId());
                }
            }
            
            model.addAttribute("historique", historique);
        } catch (Exception e) {
            System.out.println("=== DEBUG: Error getting historique: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("historique", new java.util.ArrayList<>());
        }
        
        return "demandes/voir";
    
    }
    
    /**
     * Change le statut d'une demande.
     */
    @PostMapping("/demandes/changerStatut")
    public String changerStatut(@RequestParam Integer id, @RequestParam Integer statutId, @RequestParam(required = false) String observation) {
        System.out.println("DEBUG: id=" + id + ", statutId=" + statutId + ", observation='" + observation + "'");
        if (observation != null && !observation.trim().isEmpty()) {
            demandeService.changerStatutAvecObservation(id, statutId, observation);
        } else {
            String defaultObs = "Statut modifié";
            demandeService.changerStatutAvecObservation(id, statutId, defaultObs);
        }
        return "redirect:/demandes";
    }
    
    @PostMapping("/demandes/modifierHistorique")
    public String modifierHistorique(@RequestParam Integer id, @RequestParam(required = false) String dateChangement, 
            @RequestParam(required = false) String observation, @RequestParam Integer demandeId) {
        System.out.println("DEBUG modifierHistorique: id=" + id + ", dateChangement='" + dateChangement + "', observation='" + observation + "'");
        try {
            Optional<DemandeStatut> optDs = demandeStatutRepository.findById(id);
            if (optDs.isPresent()) {
                DemandeStatut ds = optDs.get();
                
                // Vérifier que cette entrée appartient bien à la demande
                if (ds.getDemande() == null || !ds.getDemande().getId().equals(demandeId)) {
                    System.out.println("DEBUG: Cette entrée d'historique n'appartient pas à cette demande");
                    return "redirect:/demandes/voir?id=" + demandeId;
                }
                
                // Mise à jour de la date (autorisée sur toutes les lignes)
                if (dateChangement != null && !dateChangement.isEmpty()) {
                    try {
                        java.time.LocalDateTime ldt = java.time.LocalDateTime.parse(dateChangement);
                        java.sql.Timestamp ts = java.sql.Timestamp.valueOf(ldt);
                        ds.setDateChangement(new java.util.Date(ts.getTime()));
                    } catch (Exception e) {
                        System.out.println("DEBUG: Erreur parsing date: " + e.getMessage());
                    }
                }
                // Mise à jour de l'observation (autorisée sur toutes les lignes)
                if (observation != null) {
                    ds.setObservation(observation);
                }
                
                demandeStatutRepository.save(ds);
                System.out.println("DEBUG: Historique modifié avec succès");
            } else {
                System.out.println("DEBUG: Pas de demande_statut trouvé avec id=" + id);
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Erreur - " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/demandes/voir?id=" + demandeId;
    }
}