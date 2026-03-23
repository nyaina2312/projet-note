package com.exemple.projet.model;

/**
 * Modèle Candidat - Représente un étudiant dans la base de données.
 * 
 * Une classe @Entity indique à Spring que cette classe
 * correspond à une table de la base de données.
 */

import javax.persistence.*;

@Entity  // Indique que cette classe est une entité JPA (table SQL)
@Table(name = "candidat")  // Nom de la table dans la base de données
public class Candidat {
    
    // ========== CHAMPS (COLONNES) ==========
    
    // Clé primaire - ID unique pour chaque candidat
    @Id
    // Auto-incrémentation automatique
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcandidat")  // Nom de la colonne dans la table
    private Integer idCandidat;
    
    // Nom du candidat
    @Column(name = "nom", length = 100)  // VARCHAR(100)
    private String nom;
    
    // ========== GETTERS ET SETTERS ==========
    // Ce sont des méthodes pour accéder et modifier les attributs
    
    /**
     * @return L'ID du candidat
     */
    public Integer getIdCandidat() {
        return idCandidat;
    }
    
    /**
     * @param idCandidat Définir l'ID du candidat
     */
    public void setIdCandidat(Integer idCandidat) {
        this.idCandidat = idCandidat;
    }
    
    /**
     * @return Le nom du candidat
     */
    public String getNom() {
        return nom;
    }
    
    /**
     * @param nom Définir le nom du candidat
     */
    public void setNom(String nom) {
        this.nom = nom;
    }
}
