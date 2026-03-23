package com.exemple.projet.model;

/**
 * Modèle Note - Représente une note attribuée par un correcteur.
 * 
 * Cette table contient les notes données par chaque correcteur
 * pour chaque candidat dans chaque matière.
 * 
 * Relations:
 * - Un candidat peut avoir plusieurs notes
 * - Une matière peut avoir plusieurs notes  
 * - Un correcteur peut donner plusieurs notes
 */

import javax.persistence.*;
import java.math.BigDecimal;

@Entity  // Entité JPA = table SQL
@Table(name = "note")
public class Note {
    
    // ========== CHAMPS (COLONNES) ==========
    
    // Clé primaire auto-incrémentée
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idnote")
    private Integer idNote;
    
    // La note numérique (ex: 15.50)
    // precision=5 signifie jusqu'à 5 chiffres au total
    // scale=2 signifie 2 chiffres après la virgule
    @Column(name = "note", precision = 5, scale = 2)
    private BigDecimal note;
    
    // ========== CLÉS ÉTRANGÈRES (RELATIONS) ==========
    // Ces annotations définissent les relations entre tables
    
    // Un ManyToOne signifie: PLUSIEURS notes peuvent appartenir à UN candidat
    // C'est comme une clé étrangère SQL
    @ManyToOne(fetch = FetchType.EAGER)  // EAGER = charger immédiatement
    @JoinColumn(name = "idcandidat")    // Colonne de liaison
    private Candidat candidat;  // L'objet Candidat associé
    
    // Plusieurs notes pour une matière
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idmatiere")
    private Matiere matiere;
    
    // Plusieurs notes d'un correcteur
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idcorrecteur")
    private Correcteur correcteur;
    
    // ========== GETTERS ET SETTERS ==========
    
    /**
     * @return ID de la note
     */
    public Integer getIdNote() {
        return idNote;
    }
    
    public void setIdNote(Integer idNote) {
        this.idNote = idNote;
    }
    
    /**
     * @return La valeur de la note
     */
    public BigDecimal getNote() {
        return note;
    }
    
    /**
     * @param note Définir la note
     */
    public void setNote(BigDecimal note) {
        this.note = note;
    }
    
    /**
     * @return Le candidat associé à cette note
     */
    public Candidat getCandidat() {
        return candidat;
    }
    
    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }
    
    /**
     * @return La matière associée à cette note
     */
    public Matiere getMatiere() {
        return matiere;
    }
    
    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }
    
    /**
     * @return Le correcteur qui a donné cette note
     */
    public Correcteur getCorrecteur() {
        return correcteur;
    }
    
    public void setCorrecteur(Correcteur correcteur) {
        this.correcteur = correcteur;
    }
}
