package com.exemple.projet.model;

/**
 * Modèle Parametre - Définit les règles de calcul pour chaque matière.
 * 
 * C'est le cœur du système de calcul des notes finales.
 * 
 * Exemple:
 * - Matière: JAVA
 * - Diff: 3 (seuil de différence)
 * - Opérateur: < (inférieur à)
 * - Résolution: Grande (prendre le max)
 * 
 * Cela signifie:
 * "Si la différence entre les notes est < 3, prendre la note la plus grande"
 */

import javax.persistence.*;

@Entity
@Table(name = "parametre")
public class Parametre {
    
    // ========== CHAMPS ==========
    
    // ID unique du paramètre
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idparametre")
    private Integer idParametre;
    
    // Le "seuil de différence" - la différence maximale acceptable
    // Ex: diff=3 signifie "si la différence est proche de 3"
    @Column(name = "diff")
    private Integer diff;
    
    // ========== CLÉS ÉTRANGÈRES (RELATIONS) ==========
    
    // Le paramètre est lié à une matière
    // Une matière peut avoir PLUSIEURS paramètres
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idmatiere")
    private Matiere matiere;
    
    // Le paramètre utilise un opérateur (> < >= <=)
    // Le champ operateur n'est pas utilisé dans le code actuel
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idoperateur")
    private Operateur operateur;
    
    // Le paramètre utilise une méthode de résolution
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idresolution")
    private Resolution resolution;
    
    // ========== GETTERS ET SETTERS ==========
    
    public Integer getIdParametre() {
        return idParametre;
    }
    
    public void setIdParametre(Integer idParametre) {
        this.idParametre = idParametre;
    }
    
    /**
     * @return Le seuil de différence (ex: 3)
     */
    public Integer getDiff() {
        return diff;
    }
    
    /**
     * @param diff Définir le seuil de différence
     */
    public void setDiff(Integer diff) {
        this.diff = diff;
    }
    
    /**
     * @return La matière associée à ce paramètre
     */
    public Matiere getMatiere() {
        return matiere;
    }
    
    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }
    
    /**
     * @return L'opérateur de comparaison (> < >= <=)
     */
    public Operateur getOperateur() {
        return operateur;
    }
    
    public void setOperateur(Operateur operateur) {
        this.operateur = operateur;
    }
    
    /**
     * @return La méthode de résolution (Petit/Grande/Moyenne)
     */
    public Resolution getResolution() {
        return resolution;
    }
    
    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }
}
