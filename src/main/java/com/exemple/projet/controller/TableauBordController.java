package com.exemple.projet.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.exemple.projet.model.Statut;
import com.exemple.projet.service.ClientService;
import com.exemple.projet.service.DemandeService;
import com.exemple.projet.service.DevisService;

@Controller
public class TableauBordController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private DevisService devisService;

    @GetMapping("/")
    public String afficherTableauDeBord(Model model) {
        Integer nombreClients = clientService.findAll().size();
        Integer nombreDemandes = demandeService.findAll().size();
        Integer nombreDevis = devisService.findAll().size();
        Double chiffreAffaires = devisService.getChiffreAffairesProvisionnel() == null ? 0.0 : devisService.getChiffreAffairesProvisionnel().doubleValue();
        
        Map<Integer, Long> statsParStatut = demandeService.getStatsParStatut();
        
        Map<Integer, String> libellesStatuts = new HashMap<>();
        for (Statut statut : demandeService.getAllStatuts()) {
            libellesStatuts.put(statut.getId(), statut.getLibelle());
        }

        model.addAttribute("nombreClients", nombreClients);
        model.addAttribute("nombreDemandes", nombreDemandes);
        model.addAttribute("nombreDevis", nombreDevis);
        model.addAttribute("chiffreAffaires", chiffreAffaires);
        model.addAttribute("statsParStatutDemandes", statsParStatut);
        model.addAttribute("libellesStatuts", libellesStatuts);

        return "index";
    }
}
