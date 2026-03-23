package com.exemple.projet.model;

/**
 * Modèle Resolution - Définit les méthodes de résolution du calcul.
 * 
 * Trois types de résolutions:
 * - Petit (id=1): Prendre la note MINIMUM
 * - Grande (id=2): Prendre la note MAXIMUM  
 * - Moyenne (id=3): Calculer la MOYENNE
 * 
 * Utilisé par la table parametre pour déterminer quelle
 * méthode de calcul appliquer.
 */

import javax.persistence.*;

@Entity
@Table(name = "resolution")
public class Resolution {
    
    // ========== CHAMPS ==========
    
    // ID unique de la résolution
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idresolution")
    private Integer idResolution;
    
    // Nom de la résolution (Petit, Grande, Moyenne)
    @Column(name = "nom", length = 50)
    private String nom;
    
    // ========== GETTERS ET SETTERS ==========
    
    public Integer getIdResolution() {
        return idResolution;
    }
    
    public void setIdResolution(Integer idResolution) {
        this.idResolution = idResolution;
    }
    
    /**
     * @return Le nom de la résolution (Petit/Grande/Moyenne)
     */
    public String getNom() {
        return nom;
    }
    
    /**
     * @param nom Définir le nom de la résolution
     */
    public void setNom(String nom) {
        this.nom = nom;
    }
}
