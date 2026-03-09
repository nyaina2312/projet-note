-- ============================================
-- SCRIPT DE RÉINITIALISATION DES DONNÉES
-- ============================================

USE projet_note;

-- Supprimer toutes les données dans l'ordre (à cause des clés étrangères)
DELETE FROM note;
DELETE FROM parametre;
DELETE FROM resolution;
DELETE FROM operateur;
DELETE FROM matiere;
DELETE FROM correcteur;
DELETE FROM candidat;

-- Réinitialiser les AUTO_INCREMENT
ALTER TABLE note AUTO_INCREMENT = 1;
ALTER TABLE parametre AUTO_INCREMENT = 1;
ALTER TABLE resolution AUTO_INCREMENT = 1;
ALTER TABLE operateur AUTO_INCREMENT = 1;
ALTER TABLE matiere AUTO_INCREMENT = 1;
ALTER TABLE correcteur AUTO_INCREMENT = 1;
ALTER TABLE candidat AUTO_INCREMENT = 1;

-- ============================================
-- INSÉRER LES DONNÉES DE TEST
-- ============================================

-- Correcteurs
INSERT INTO correcteur (nom) VALUES 
('Correcteur 1'), 
('Correcteur 2'), 
('Correcteur 3');

-- Candidats
INSERT INTO candidat (nom) VALUES 
('Dupont'), 
('Martin'), 
('Bernard'), 
('Petit'), 
('Durand');

-- Matières
INSERT INTO matiere (nom) VALUES 
('Mathématiques'), 
('Physique'), 
('Informatique'), 
('Français'), 
('Anglais');

-- Opérateurs (1=>, 2=<, 3=>=, 4=<=)
INSERT INTO operateur (operateur) VALUES 
('>'), 
('<'), 
('>='), 
('<=');

-- Résolutions (1=Petit/plus petite, 2=Grande/plus grande, 3=Moyenne)
INSERT INTO resolution (nom) VALUES 
('Petit'), 
('Grande'), 
('Moyenne');

-- Paramètres (diff = seuil de différence)
-- idOperateur: 1=> 2=< 3=>= 4=<=
-- idResolution: 1=Petit 2=Grande 3=Moyenne
INSERT INTO parametre (idmatiere, diff, idoperateur, idresolution) VALUES 
(1, 3, 2, 2),   -- Mathématiques: diff=3, op=<, résolution=Grande
(2, 2, 4, 1),   -- Physique: diff=2, op=<=, résolution=Petit
(3, 10, 1, 1),  -- Informatique: diff=10, op=>, résolution=Petit
(4, 5, 3, 3),   -- Français: diff=5, op=>=, résolution=Moyenne
(5, 8, 4, 2);   -- Anglais: diff=8, op=<=, résolution=Grande

-- Notes (3 correcteurs pour chaque candidat dans chaque matière)
INSERT INTO note (note, idcandidat, idmatiere, idcorrecteur) VALUES
-- Dupont (ID 1) - Mathématiques
(12.00, 1, 1, 1), (11.00, 1, 1, 2), (11.00, 1, 1, 3),
-- Dupont - Physique
(7.00, 1, 2, 1), (11.00, 1, 2, 2), (9.00, 1, 2, 3),
-- Martin (ID 2) - Mathématiques
(13.00, 2, 1, 1), (10.00, 2, 1, 2), (11.00, 2, 1, 3),
-- Martin - Physique
(11.00, 2, 2, 1), (16.00, 2, 2, 2), (13.00, 2, 2, 3);


-- ============================================
-- VÉRIFICATION
-- ============================================
SELECT '=== CORRECTEURS ===' as '';
SELECT * FROM correcteur;

SELECT '=== CANDIDATS ===' as '';
SELECT * FROM candidat;

SELECT '=== MATIÈRES ===' as '';
SELECT * FROM matiere;

SELECT '=== PARAMÈTRES ===' as '';
SELECT * FROM parametre;

SELECT '=== NOMBRE DE NOTES ===' as '';
SELECT COUNT(*) as 'Total notes' FROM note;

SELECT '=== NOTES PAR CANDIDAT ===' as '';
SELECT c.nom as 'Candidat', m.nom as 'Matière', n.note as 'Note', cor.nom as 'Correcteur'
FROM note n
JOIN candidat c ON n.idcandidat = c.idcandidat
JOIN matiere m ON n.idmatiere = m.idmatiere
JOIN correcteur cor ON n.idcorrecteur = cor.idcorrecteur
ORDER BY c.nom, m.nom;

SELECT '=== RÉINITIALISATION TERMINÉE ===' as '';
