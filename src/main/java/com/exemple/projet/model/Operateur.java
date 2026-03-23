package com.exemple.projet.model;

/**
 * Modèle Operateur - Représente un opérateur de comparaison.
 * 
 * Opérateurs disponibles:
 * - > (supérieur à)
 * - < (inférieur à)  
 * - >= (supérieur ou égal à)
 * - <= (inférieur ou égal à)
 * 
 * Note: Cet opérateur n'est pas utilisé dans le code actuel.
 * Il était prévu pour une future版本的 logique de calcul.
 */

import javax.persistence.*;

@Entity
@Table(name = "operateur")
public class Operateur {
    
    // ========== CHAMPS ==========
    
    // ID unique de l'opérateur
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idoperateur")
    private Integer idOperateur;
    
    // Le symbole de l'opérateur (>, <, >=, <=)
    @Column(name = "operateur", length = 50)
    private String operateur;
    
    // ========== GETTERS ET SETTERS ==========
    
    public Integer getIdOperateur() {
        return idOperateur;
    }
    
    public void setIdOperateur(Integer idOperateur) {
        this.idOperateur = idOperateur;
    }
    
    /**
     * @return Le symbole de l'opérateur
     */
    public String getOperateur() {
        return operateur;
    }
    
    /**
     * @param operateur Définir l'opérateur
     */
    public void setOperateur(String operateur) {
        this.operateur = operateur;
    }
}
