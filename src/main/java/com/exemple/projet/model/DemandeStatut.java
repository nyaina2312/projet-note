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
    
    @Column(name = "datechangement")
    private Date dateChangement;
    
    @Column(name = "observation", length = 500)
    private String observation;

    @Column(name = "duree_totale_heures")
    private Double dureeTotaleHeures;

    @Column(name = "duree_ouvrable_heures")
    private Double dureeOuvrableHeures;

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
    
    public String getObservation() {
        return observation;
    }
    
    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getSource() {
        return travaux != null ? travaux.getTypeStatut() : null;
    }

    public Double getDureeTotaleHeures() {
        return dureeTotaleHeures;
    }

    public void setDureeTotaleHeures(Double dureeTotaleHeures) {
        this.dureeTotaleHeures = dureeTotaleHeures;
    }

    public Double getDureeOuvrableHeures() {
        return dureeOuvrableHeures;
    }

    public void setDureeOuvrableHeures(Double dureeOuvrableHeures) {
        this.dureeOuvrableHeures = dureeOuvrableHeures;
    }

    /**
     * Retourne la durée totale formatée en "Xh YYmin"
     */
    public String getDureeTotaleFormatee() {
        if (dureeTotaleHeures == null) return "";
        int heures = dureeTotaleHeures.intValue();
        int minutes = (int) Math.round((dureeTotaleHeures - heures) * 60);
        return String.format("%dh %02dmin", heures, minutes);
    }

    /**
     * Retourne la durée ouvrable formatée en "Xh YYmin"
     */
    public String getDureeOuvrableFormatee() {
        if (dureeOuvrableHeures == null) return "";
        int heures = dureeOuvrableHeures.intValue();
        int minutes = (int) Math.round((dureeOuvrableHeures - heures) * 60);
        return String.format("%dh %02dmin", heures, minutes);
    }
}
