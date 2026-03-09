package com.exemple.projet.model;

import javax.persistence.*;

@Entity
@Table(name = "correcteur")
public class Correcteur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcorrecteur")
    private Integer idCorrecteur;
    
    @Column(name = "nom", length = 100)
    private String nom;
    
    // GETTERS ET SETTERS
    
    public Integer getIdCorrecteur() {
        return idCorrecteur;
    }
    
    public void setIdCorrecteur(Integer idCorrecteur) {
        this.idCorrecteur = idCorrecteur;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
}
