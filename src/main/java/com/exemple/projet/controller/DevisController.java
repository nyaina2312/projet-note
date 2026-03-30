package com.exemple.projet.controller;

import com.exemple.projet.model.Demande;
import com.exemple.projet.model.Devis;
import com.exemple.projet.model.DetailsDevis;
import com.exemple.projet.model.TypeDevis;
import com.exemple.projet.repository.DemandeRepository;
import com.exemple.projet.repository.TypeDevisRepository;
import com.exemple.projet.service.DevisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller pour la gestion des devis.
 */
@Controller
@RequestMapping("/devis")
public class DevisController {
    
    @Autowired
    private DevisService devisService;
    
    @Autowired
    private DemandeRepository demandeRepository;
    
    @Autowired
    private TypeDevisRepository typeDevisRepository;
    
    /**
     * Afficher la liste des devis (accessible via /devis ou /devis/liste).
     */
    @GetMapping({"/", ""})
    public String index(Model model) {
        List<Devis> devisList = devisService.findAll();
        model.addAttribute("devisList", devisList);
        return "devis/liste";
    }
    
    /**
     * Afficher la liste des devis.
     */
    @GetMapping("/liste")
    public String liste(Model model) {
        List<Devis> devisList = devisService.findAll();
        model.addAttribute("devisList", devisList);
        return "devis/liste";
    }
    
    /**
     * Afficher le formulaire d'ajout de devis.
     */
    @GetMapping("/ajouter")
    public String ajouter(Model model) {
        model.addAttribute("devis", new Devis());
        
        // Liste des demandes pour le dropdown
        List<Demande> demandes = demandeRepository.findAllWithClient();
        model.addAttribute("demandes", demandes);
        
        // Liste des types de devis (Étude et Forage seulement)
        List<TypeDevis> typeDevisList = typeDevisRepository.findAll();
        model.addAttribute("typeDevisList", typeDevisList);
        
        // Initialiser une liste vide pour les détails
        model.addAttribute("detailsList", new ArrayList<DetailsDevis>());
        
        return "devis/formulaire";
    }
    
    /**
     * AJAX: Récupérer les détails d'une demande par son ID.
     */
    @GetMapping("/ajax/demande/{id}")
    @ResponseBody
    public Demande getDemandeDetails(@PathVariable Integer id) {
        Optional<Demande> demandeOpt = demandeRepository.findByIdWithClient(id);
        return demandeOpt.orElse(null);
    }
    
    /**
     * Enregistrer un nouveau devis avec ses détails.
     */
    @PostMapping("/enregistrer")
    public String enregistrer(@ModelAttribute Devis devis,
                              @RequestParam(required = false) Integer demandeId,
                              @RequestParam(required = false) Integer typeDevisId,
                              @RequestParam List<String> detailLibelle,
                              @RequestParam List<String> detailPrixUnitaire,
                              @RequestParam List<String> detailQuantite,
                              Model model) {
        
        try {
            // Récupérer la demande
            Optional<Demande> demandeOpt = demandeRepository.findById(demandeId);
            if (!demandeOpt.isPresent()) {
                model.addAttribute("error", "Demande non trouvée");
                return "devis/formulaire";
            }
            devis.setDemande(demandeOpt.get());
            
            // Récupérer le type de devis
            Optional<TypeDevis> typeDevisOpt = typeDevisRepository.findById(typeDevisId);
            if (typeDevisOpt.isPresent()) {
                devis.setTypeDevis(typeDevisOpt.get());
            }
            
            // Construire la liste des détails
            List<DetailsDevis> detailsList = new ArrayList<>();
            for (int i = 0; i < detailLibelle.size(); i++) {
                if (detailLibelle.get(i) != null && !detailLibelle.get(i).isEmpty()) {
                    DetailsDevis detail = new DetailsDevis();
                    detail.setLibelle(detailLibelle.get(i));
                    
                    if (detailPrixUnitaire.get(i) != null && !detailPrixUnitaire.get(i).isEmpty()) {
                        detail.setPrixUnitaire(new java.math.BigDecimal(detailPrixUnitaire.get(i).replace(",", ".")));
                    }
                    
                    if (detailQuantite.get(i) != null && !detailQuantite.get(i).isEmpty()) {
                        detail.setQuantite(Integer.parseInt(detailQuantite.get(i)));
                    }
                    
                    detailsList.add(detail);
                }
            }
            
            // Sauvegarder en transaction
            devisService.saveDevisWithDetails(devis, detailsList);
            
            return "redirect:/devis/liste";
            
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'enregistrement: " + e.getMessage());
            
            // Repasser les données pour le formulaire
            List<Demande> demandes = demandeRepository.findAllWithClient();
            model.addAttribute("demandes", demandes);
            List<TypeDevis> typeDevisList = typeDevisRepository.findAll();
            model.addAttribute("typeDevisList", typeDevisList);
            
            return "devis/formulaire";
        }
    }
    
    /**
     * Voir les détails d'un devis.
     */
    @GetMapping("/voir/{id}")
    public String voir(@PathVariable Integer id, Model model) {
        Optional<Devis> devisOpt = devisService.findById(id);
        if (devisOpt.isPresent()) {
            model.addAttribute("devis", devisOpt.get());
            return "devis/voir";
        }
        return "redirect:/devis/liste";
    }
    
    /**
     * Supprimer un devis.
     */
    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Integer id) {
        devisService.deleteById(id);
        return "redirect:/devis/liste";
    }
    
    /**
     * Modifier un devis existant (Sprint 4).
     */
    @GetMapping("/modifier/{id}")
    public String modifier(@PathVariable Integer id, Model model) {
        Optional<Devis> devisOpt = devisService.findById(id);
        if (devisOpt.isPresent()) {
            model.addAttribute("devis", devisOpt.get());
            
            // Liste des demandes pour le dropdown
            List<Demande> demandes = demandeRepository.findAllWithClient();
            model.addAttribute("demandes", demandes);
            
            // Liste des types de devis
            List<TypeDevis> typeDevisList = typeDevisRepository.findAll();
            model.addAttribute("typeDevisList", typeDevisList);
            
            // Liste des détails existants
            model.addAttribute("detailsList", devisOpt.get().getDetailsDevis());
            
            return "devis/formulaire";
        }
        return "redirect:/devis/liste";
    }
    
    /**
     * Modifier le statut d'un devis (accepter ou refuser).
     */
    @PostMapping("/changerStatut")
    public String changerStatut(@RequestParam Integer devisId,
                                 @RequestParam Integer statutId) {
        devisService.updateStatut(devisId, statutId);
        return "redirect:/devis/voir/" + devisId;
    }
}