package com.exemple.projet.controller;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/admin/reset")
    public String resetDatabase() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Désactiver les clés étrangères
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
            
            // Vider les tables
            stmt.execute("TRUNCATE TABLE note");
            stmt.execute("TRUNCATE TABLE parametre");
            stmt.execute("TRUNCATE TABLE resolution");
            stmt.execute("TRUNCATE TABLE operateur");
            stmt.execute("TRUNCATE TABLE matiere");
            stmt.execute("TRUNCATE TABLE candidat");
            stmt.execute("TRUNCATE TABLE correcteur");
            
            // Réactiver les clés étrangères
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
            
            // Insérer les données
            stmt.execute("INSERT INTO correcteur (nom) VALUES ('Correcteur 1'), ('Correcteur 2'), ('Correcteur 3')");
            stmt.execute("INSERT INTO candidat (nom) VALUES ('Candidat1'), ('Candidat2')");
            stmt.execute("INSERT INTO matiere (nom) VALUES ('JAVA'), ('PHP')");
            stmt.execute("INSERT INTO operateur (operateur) VALUES ('>'), ('<'), ('>='), ('<=')");
            stmt.execute("INSERT INTO resolution (nom) VALUES ('Petit'), ('Grande'), ('Moyenne')");
            stmt.execute("INSERT INTO parametre (idmatiere, diff, idoperateur, idresolution) VALUES (1, 3, 2, 2), (2, 2, 4, 1)");
            
            stmt.execute("INSERT INTO note (note, idcandidat, idmatiere, idcorrecteur) VALUES " +
                "(12, 1, 1, 1), (11, 1, 1, 2), (11, 1, 1, 3), " +
                "(13, 2, 1, 1), (10, 2, 1, 2), (11, 2, 1, 3), " +
                "(14, 3, 1, 1), (14, 3, 1, 2), (14, 3, 1, 3), " +
                "(7, 1, 2, 1), (11, 1, 2, 2), (9, 1, 2, 3), " +
                "(11, 2, 2, 1), (16, 2, 2, 2), (13, 2, 2, 3), " +
                "(18, 3, 2, 1), (17, 3, 2, 2), (18, 3, 2, 3)");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "redirect:/notes?reset=ok";
    }
}
