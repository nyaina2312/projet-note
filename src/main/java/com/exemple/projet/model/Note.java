package com.exemple.projet.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "note")
public class Note {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idnote")
    private Integer idNote;
    
    @Column(name = "note", precision = 5, scale = 2)
    private BigDecimal note;
    
    // Clés étrangères
    @ManyToOne
    @JoinColumn(name = "idcandidat")
    private Candidat candidat;
    
    @ManyToOne
    @JoinColumn(name = "idmatiere")
    private Matiere matiere;
    
    @ManyToOne
    @JoinColumn(name = "idcorrecteur")
    private Correcteur correcteur;
    
    // GETTERS ET SETTERS
    
    public Integer getIdNote() {
        return idNote;
    }
    
    public void setIdNote(Integer idNote) {
        this.idNote = idNote;
    }
    
    public BigDecimal getNote() {
        return note;
    }
    
    public void setNote(BigDecimal note) {
        this.note = note;
    }
    
    public Candidat getCandidat() {
        return candidat;
    }
    
    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }
    
    public Matiere getMatiere() {
        return matiere;
    }
    
    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }
    
    public Correcteur getCorrecteur() {
        return correcteur;
    }
    
    public void setCorrecteur(Correcteur correcteur) {
        this.correcteur = correcteur;
    }
}
