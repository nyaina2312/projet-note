package com.exemple.projet.model;

/**
 * Modèle Correcteur - Représente un correcteur de copies.
 * 
 * Chaque correcteur peut donner plusieurs notes
 * pour différents candidats et différentes matières.
 */

import javax.persistence.*;

@Entity
@Table(name = "correcteur")
public class Correcteur {
    
    // ========== CHAMPS ==========
    
    // ID unique du correcteur
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcorrecteur")
    private Integer idCorrecteur;
    
    // Nom du correcteur
    @Column(name = "nom", length = 100)
    private String nom;
    
    // ========== GETTERS ET SETTERS ==========
    
    public Integer getIdCorrecteur() {
        return idCorrecteur;
    }
    
    public void setIdCorrecteur(Integer idCorrecteur) {
        this.idCorrecteur = idCorrecteur;
    }
    
    /**
     * @return Le nom du correcteur
     */
    public String getNom() {
        return nom;
    }
    
    /**
     * @param nom Définir le nom du correcteur
     */
    public void setNom(String nom) {
        this.nom = nom;
    }
}
