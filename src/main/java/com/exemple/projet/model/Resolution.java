package com.exemple.projet.model;

import javax.persistence.*;

@Entity
@Table(name = "resolution")
public class Resolution {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idresolution")
    private Integer idResolution;
    
    @Column(name = "nom", length = 50)
    private String nom;
    
    // GETTERS ET SETTERS
    
    public Integer getIdResolution() {
        return idResolution;
    }
    
    public void setIdResolution(Integer idResolution) {
        this.idResolution = idResolution;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
}
