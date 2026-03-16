package com.exemple.projet.model;

import javax.persistence.*;

@Entity
@Table(name = "parametre")
public class Parametre {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idparametre")
    private Integer idParametre;
    
    @Column(name = "diff")
    private Integer diff;
    
    // Clés étrangères
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idmatiere")
    private Matiere matiere;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idoperateur")
    private Operateur operateur;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idresolution")
    private Resolution resolution;
    
    // GETTERS ET SETTERS
    
    public Integer getIdParametre() {
        return idParametre;
    }
    
    public void setIdParametre(Integer idParametre) {
        this.idParametre = idParametre;
    }
    
    public Integer getDiff() {
        return diff;
    }
    
    public void setDiff(Integer diff) {
        this.diff = diff;
    }
    
    public Matiere getMatiere() {
        return matiere;
    }
    
    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }
    
    public Operateur getOperateur() {
        return operateur;
    }
    
    public void setOperateur(Operateur operateur) {
        this.operateur = operateur;
    }
    
    public Resolution getResolution() {
        return resolution;
    }
    
    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }
}
