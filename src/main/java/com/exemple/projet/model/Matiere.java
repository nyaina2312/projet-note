package com.exemple.projet.model;

import javax.persistence.*;

@Entity
@Table(name = "matiere")
public class Matiere {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmatiere")
    private Integer idMatiere;
    
    @Column(name = "nom", length = 100)
    private String nom;
    
    // GETTERS ET SETTERS
    
    public Integer getIdMatiere() {
        return idMatiere;
    }
    
    public void setIdMatiere(Integer idMatiere) {
        this.idMatiere = idMatiere;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
}
