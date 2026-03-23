package com.exemple.projet.model;

import javax.persistence.*;

/**
 * Modèle Travaux - Représente les travaux de forage pour une demande.
 */
@Entity
@Table(name = "travaux")
public class Travaux {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "demande_id")
    private Demande demande;
    
    @Column(name = "typeStatut")
    private String typeStatut;
    
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
    
    public String getTypeStatut() {
        return typeStatut;
    }
    
    public void setTypeStatut(String typeStatut) {
        this.typeStatut = typeStatut;
    }
}
