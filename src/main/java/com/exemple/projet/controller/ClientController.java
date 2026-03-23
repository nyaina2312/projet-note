package com.exemple.projet.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.exemple.projet.model.Client;
import com.exemple.projet.service.ClientService;

@Controller
public class ClientController {

    @Autowired
    private ClientService clientService;
    
    /**
     * Affiche la liste de tous les clients.
     */
    @GetMapping("/clients")
    public String listeClients(Model model) {
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "clients/liste";
    }
    
    /**
     * Affiche le formulaire pour ajouter un client.
     */
    @GetMapping("/clients/ajouter")
    public String ajouterClient(Model model) {
        model.addAttribute("client", new Client());
        return "clients/formulaire";
    }
    
    /**
     * Sauvegarde un client (nouveau ou modification).
     */
    @PostMapping("/clients/sauvegarder")
    public String sauvegarderClient(@ModelAttribute Client client) {
        clientService.save(client);
        return "redirect:/clients";
    }
    
    /**
     * Affiche le formulaire pour modifier un client existant.
     */
    @GetMapping("/clients/modifier")
    public String modifierClient(@RequestParam Integer id, Model model) {
        Client client = clientService.findById(id).orElse(null);
        model.addAttribute("client", client);
        return "clients/formulaire";
    }
    
    /**
     * Supprime un client.
     */
    @GetMapping("/clients/supprimer")
    public String supprimerClient(@RequestParam Integer id) {
        clientService.deleteById(id);
        return "redirect:/clients";
    }
}
