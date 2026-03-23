package com.exemple.projet.model;

/**
 * Modèle Matiere - Représente une matière (ex: JAVA, PHP).
 * 
 * Chaque matière peut avoir plusieurs paramètres de calcul
 * et peut être associée à plusieurs notes.
 */

import javax.persistence.*;

@Entity
@Table(name = "matiere")
public class Matiere {
    
    // ========== CHAMPS ==========
    
    // ID unique de la matière
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmatiere")
    private Integer idMatiere;
    
    // Nom de la matière (ex: "JAVA", "PHP")
    @Column(name = "nom", length = 100)
    private String nom;
    
    // ========== GETTERS ET SETTERS ==========
    
    public Integer getIdMatiere() {
        return idMatiere;
    }
    
    public void setIdMatiere(Integer idMatiere) {
        this.idMatiere = idMatiere;
    }
    
    /**
     * @return Le nom de la matière
     */
    public String getNom() {
        return nom;
    }
    
    /**
     * @param nom Définir le nom de la matière
     */
    public void setNom(String nom) {
        this.nom = nom;
    }
}
