package com.exemple.projet.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Modèle Devis - Représente un devis pour une demande.
 */
@Entity
@Table(name = "devis")
public class Devis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "montantTotal", precision = 10, scale = 2)
    private BigDecimal montantTotal;
    
    @Column(name = "date")
    private Date date;
    
    @ManyToOne
    @JoinColumn(name = "demande_id")
    private Demande demande;
    
    @ManyToOne
    @JoinColumn(name = "type_devis_id")
    private TypeDevis typeDevis;
    
    // ========== GETTERS ET SETTERS ==========
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public BigDecimal getMontantTotal() {
        return montantTotal;
    }
    
    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public Demande getDemande() {
        return demande;
    }
    
    public void setDemande(Demande demande) {
        this.demande = demande;
    }
    
    public TypeDevis getTypeDevis() {
        return typeDevis;
    }
    
    public void setTypeDevis(TypeDevis typeDevis) {
        this.typeDevis = typeDevis;
    }
}
