package com.exemple.projet.model;

import javax.persistence.*;

@Entity
@Table(name = "operateur")
public class Operateur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idoperateur")
    private Integer idOperateur;
    
    @Column(name = "operateur", length = 50)
    private String operateur;
    
    // GETTERS ET SETTERS
    
    public Integer getIdOperateur() {
        return idOperateur;
    }
    
    public void setIdOperateur(Integer idOperateur) {
        this.idOperateur = idOperateur;
    }
    
    public String getOperateur() {
        return operateur;
    }
    
    public void setOperateur(String operateur) {
        this.operateur = operateur;
    }
}
