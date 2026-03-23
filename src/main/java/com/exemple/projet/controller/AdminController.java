package com.exemple.projet.controller;

/**
 * Contrôleur Admin - Fonctions d'administration de la base de données.
 * 
 * Ce contrôleur permet de réinitialiser la base de données
 * avec des données de test.
 */

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    // DataSource = connexion à la base de données
    @Autowired
    private DataSource dataSource;

    /**
     * Réinitialise la base de données.
     * 
     * Cette fonction:
     * 1. Vide toutes les tables
     * 2. Réinitialise les IDs auto-incrémentés
     * 3. Insère les données de test
     * 
     * URL: /admin/reset
     * 
     * @return Redirection vers /notes
     */
    @GetMapping("/admin/reset")
    public String resetDatabase() {
        // Utiliser try-with-resources pour fermer automatiquement la connexion
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Étape 1: Désactiver les clés étrangères
            // Nécessaire pour pouvoir supprimer les données sans erreur
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
            
            // Étape 2: Vider toutes les tables (TRUNCATE = supprimer tout le contenu)
            stmt.execute("TRUNCATE TABLE note");
            stmt.execute("TRUNCATE TABLE parametre");
            stmt.execute("TRUNCATE TABLE resolution");
            stmt.execute("TRUNCATE TABLE operateur");
            stmt.execute("TRUNCATE TABLE matiere");
            stmt.execute("TRUNCATE TABLE candidat");
            stmt.execute("TRUNCATE TABLE correcteur");
            
            // Étape 3: Réactiver les clés étrangères
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
            
            // Étape 4: Insérer les correcteurs
            stmt.execute("INSERT INTO correcteur (nom) VALUES ('Correcteur 1'), ('Correcteur 2'), ('Correcteur 3')");
            
            // Étape 5: Insérer les candidats
            stmt.execute("INSERT INTO candidat (nom) VALUES ('Candidat1'), ('Candidat2'), ('Candidat3')");
            
            // Étape 6: Insérer les matières
            stmt.execute("INSERT INTO matiere (nom) VALUES ('JAVA'), ('PHP')");
            
            // Étape 7: Insérer les opérateurs
            stmt.execute("INSERT INTO operateur (operateur) VALUES ('>'), ('<'), ('>='), ('<=')");
            
            // Étape 8: Insérer les résolutions
            stmt.execute("INSERT INTO resolution (nom) VALUES ('Petit'), ('Grande'), ('Moyenne')");
            
            // Étape 9: Insérer les paramètres
            // JAVA: diff=3, resolution=Grande (id=2)
            // PHP: diff=2, resolution=Petit (id=1)
            stmt.execute("INSERT INTO parametre (idmatiere, diff, idoperateur, idresolution) VALUES (1, 3, 2, 2), (2, 2, 4, 1)");
            
            // Étape 10: Insérer les notes de test
            // Pour chaque candidat, chaque matière, 3 correcteurs donnent des notes
            stmt.execute("INSERT INTO note (note, idcandidat, idmatiere, idcorrecteur) VALUES " +
                "(12, 1, 1, 1), (11, 1, 1, 2), (11, 1, 1, 3), " +  // Candidat 1, JAVA
                "(13, 2, 1, 1), (10, 2, 1, 2), (11, 2, 1, 3), " +  // Candidat 2, JAVA
                "(14, 3, 1, 1), (14, 3, 1, 2), (14, 3, 1, 3), " +  // Candidat 3, JAVA
                "(7, 1, 2, 1), (11, 1, 2, 2), (9, 1, 2, 3), " +   // Candidat 1, PHP
                "(11, 2, 2, 1), (16, 2, 2, 2), (13, 2, 2, 3), " +  // Candidat 2, PHP
                "(18, 3, 2, 1), (17, 3, 2, 2), (18, 3, 2, 3)");  // Candidat 3, PHP
            
        } catch (Exception e) {
            // En cas d'erreur, afficher l'erreur dans la console
            e.printStackTrace();
        }
        
        // Rediriger vers la page des notes avec un message
        return "redirect:/notes?reset=ok";
    }
}
