package com.exemple.projet.model;

import javax.persistence.*;

/**
 * Modèle Statut - Représente le statut d'un travail de forage.
 * Peut être un Test Sanitaire (TS) ou non.
 */
@Entity
@Table(name = "statut")
public class Statut {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "libelle", length = 100)
    private String libelle;
    
    @Column(name = "typeStatut", length = 100)
    private String typeStatut;
    
    // ========== GETTERS ET SETTERS ==========
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    
    public String getTypeStatut() {
        return typeStatut;
    }
    
    public void setTypeStatut(String typeStatut) {
        this.typeStatut = typeStatut;
    }
}
