package com.exemple.projet.model;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Jours non travaillés (jours fériés, congés, weekends si besoin)
 */
@Entity
@Table(name = "jour_non_travaille", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"date_jour"})
})
public class JourNonTravaille {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "date_jour", nullable = false, unique = true)
    private LocalDate dateJour;

    @Column(name = "motif", length = 200)
    private String motif;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDateJour() {
        return dateJour;
    }

    public void setDateJour(LocalDate dateJour) {
        this.dateJour = dateJour;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }
}
