package com.exemple.projet.model;

import javax.persistence.*;

/**
 * Modèle Client - Représente un client qui fait une demande de forage.
 */
@Entity
@Table(name = "client")
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "nom", length = 100)
    private String nom;
    
    @Column(name = "contact", length = 100)
    private String contact;
    
    // ========== GETTERS ET SETTERS ==========
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getContact() {
        return contact;
    }
    
    public void setContact(String contact) {
        this.contact = contact;
    }
}
