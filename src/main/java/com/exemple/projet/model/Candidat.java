package com.exemple.projet.model;

import javax.persistence.*;

@Entity
@Table(name = "candidat")
public class Candidat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcandidat")
    private Integer idCandidat;
    
    @Column(name = "nom", length = 100)
    private String nom;
    
    // GETTERS ET SETTERS
    
    public Integer getIdCandidat() {
        return idCandidat;
    }
    
    public void setIdCandidat(Integer idCandidat) {
        this.idCandidat = idCandidat;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
}
