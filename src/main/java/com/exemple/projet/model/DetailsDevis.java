package com.exemple.projet.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Modèle DetailsDevis - Représente le détail d'un devis.
 */
@Entity
@Table(name = "details_devis")
public class DetailsDevis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "devis_id")
    private Devis devis;
    
    @Column(name = "libelle", length = 200)
    private String libelle;
    
    @Column(name = "prixUnitaire", precision = 10, scale = 2)
    private BigDecimal prixUnitaire;
    
    @Column(name = "quantite")
    private Integer quantite;
    
    @Column(name = "montant", precision = 10, scale = 2)
    private BigDecimal montant;
    
    // ========== GETTERS ET SETTERS ==========
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Devis getDevis() {
        return devis;
    }
    
    public void setDevis(Devis devis) {
        this.devis = devis;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    
    public BigDecimal getMontant() {
        return montant;
    }
    
    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }
    
    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }
    
    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
    
    public Integer getQuantite() {
        return quantite;
    }
    
    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }
}
