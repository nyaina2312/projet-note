package com.exemple.projet.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Modèle DemandeStatut - Représente l'historique des statuts d'une demande.
 */
@Entity
@Table(name = "demande_statut")
public class DemandeStatut {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "demande_id")
    private Demande demande;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "statut_id")
    private Statut statut;
    
    @Column(name = "dateChangement")
    private Date dateChangement;
    
    // ========== NOUVELLE RELATION AVEC TRAVAUX ==========
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "travaux_id")
    private Travaux travaux;
    
    public Travaux getTravaux() {
        return travaux;
    }
    
    public void setTravaux(Travaux travaux) {
        this.travaux = travaux;
    }
    
    // ========== GETTERS ET SETTERS ==========
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Demande getDemande() {
        return demande;
    }
    
    public void setDemande(Demande demande) {
        this.demande = demande;
    }
    
    public Statut getStatut() {
        return statut;
    }
    
    public void setStatut(Statut statut) {
        this.statut = statut;
    }
    
    public Date getDateChangement() {
        return dateChangement;
    }
    
    public void setDateChangement(Date dateChangement) {
        this.dateChangement = dateChangement;
    }
}
