package com.exemple.projet.model;

import javax.persistence.*;

/**
 * Modèle TypeDevis - Représente le type de devis (Étude ou Forage).
 */
@Entity
@Table(name = "type_devis")
public class TypeDevis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "libelle", length = 100)
    private String libelle;
    
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
}
