package com.exemple.projet.repository;

import com.exemple.projet.model.DetailsDevis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository                                                                // Marque cette interface comme un composant Spring (accès aux données)
public interface DetailsDevisRepository extends JpaRepository<DetailsDevis, Integer> { // Hérite de JpaRepository → CRUD automatique pour DetailsDevis avec PK de type Integer

    List<DetailsDevis> findByDevisId(Integer devisId);                     // Spring Data génère : SELECT * FROM details_devis WHERE devis_id = ?

    // Récupère tous les détails avec leur devis parent (évite le problème N+1 requêtes)
    // JOIN FETCH = fait un seul SELECT avec JOIN au lieu de N+1 requêtes séparées
    @Query("SELECT d FROM DetailsDevis d JOIN FETCH d.devis")             // JPQL : sélectionne les détails et charge le devis en même temps
    List<DetailsDevis> findAllWithDevis();                                 // Retourne la liste de tous les détails avec devis chargé

    // Calcule la somme de tous les montants (= chiffre d'affaires provisionnel)
    // COALESCE(valeur, 0) = retourne 0 si la valeur est NULL (table vide)
    @Query("SELECT COALESCE(SUM(d.montant), 0) FROM DetailsDevis d")      // JPQL : SELECT SUM(montant) FROM details_devis, retourne 0 si vide
    BigDecimal sumAllMontants();                                           // Retourne la somme totale en BigDecimal

}
