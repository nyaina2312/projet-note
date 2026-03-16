-- Créer la base de données
CREATE DATABASE IF NOT EXISTS projet_note;
USE projet_note;

-- Table Correcteur
CREATE TABLE correcteur (
    idcorrecteur INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL
);

-- Table Candidat
CREATE TABLE candidat (
    idcandidat INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL
);

-- Table Matiere
CREATE TABLE matiere (
    idmatiere INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL
);

-- Table Operateur
CREATE TABLE operateur (
    idoperateur INT PRIMARY KEY AUTO_INCREMENT,
    operateur VARCHAR(50) NOT NULL
);

-- Table Resolution
CREATE TABLE resolution (
    idresolution INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL
);


CREATE TABLE parametre (
    idparametre INT PRIMARY KEY AUTO_INCREMENT,
    idmatiere INT,
    diff INT,
    idoperateur INT,
    idresolution INT,
    FOREIGN KEY (idmatiere) REFERENCES matiere(idmatiere),
    FOREIGN KEY (idoperateur) REFERENCES operateur(idoperateur),
    FOREIGN KEY (idresolution) REFERENCES resolution(idresolution)
);


CREATE TABLE note (
    idnote INT PRIMARY KEY AUTO_INCREMENT,
    note DECIMAL(5,2),
    idcandidat INT,
    idmatiere INT,
    idcorrecteur INT,
    FOREIGN KEY (idcandidat) REFERENCES candidat(idcandidat),
    FOREIGN KEY (idmatiere) REFERENCES matiere(idmatiere),
    FOREIGN KEY (idcorrecteur) REFERENCES correcteur(idcorrecteur)
);

-- Insérer des données de test
INSERT IGNORE INTO correcteur (nom) VALUES ('Dupont'), ('Martin'), ('Bernard');

INSERT IGNORE INTO candidat (nom) VALUES ('Dupont'), ('Martin'), ('Bernard'), ('Petit'), ('Durand');

INSERT IGNORE INTO matiere (nom) VALUES ('Mathématiques'), ('Physique'), ('Informatique');

INSERT IGNORE INTO operateur (operateur) VALUES ('>'), ('<'), ('>='), ('<=');

INSERT IGNORE INTO resolution (nom) VALUES ('Petit'), ('Grande'), ('Moyenne');

INSERT IGNORE INTO parametre (idmatiere, diff, idoperateur, idresolution) VALUES 
(1, 3, 2, 2),  -- Mathématiques: diff=3, op=<, résolution=Grande
(2, 2, 4, 1),  -- Physique: diff=2, op=<=, résolution=Petit
(3, 1, 1, 3); -- Informatique: diff=1, op=>, résolution=Moyenne

INSERT IGNORE INTO note (note, idcandidat, idmatiere, idcorrecteur) VALUES
-- Mathématiques (ID 1)
(12.00, 1, 1, 1), (11.00, 1, 1, 2), (11.00, 1, 1, 3),
(13.00, 2, 1, 1), (10.00, 2, 1, 2), (11.00, 2, 1, 3),
(14.00, 3, 1, 1), (14.50, 3, 1, 2), (14.25, 3, 1, 3),
(10.00, 4, 1, 1), (10.00, 4, 1, 2), (10.00, 4, 1, 3),
(17.00, 5, 1, 1), (18.00, 5, 1, 2), (17.50, 5, 1, 3),
-- Physique (ID 2)
(7.00, 1, 2, 1), (11.00, 1, 2, 2), (9.00, 1, 2, 3),
(11.00, 2, 2, 1), (16.00, 2, 2, 2), (13.00, 2, 2, 3),
(18.00, 3, 2, 1), (17.50, 3, 2, 2), (18.25, 3, 2, 3),
(8.00, 4, 2, 1), (9.00, 4, 2, 2), (8.50, 4, 2, 3),
(19.00, 5, 2, 1), (19.50, 5, 2, 2), (19.25, 5, 2, 3),
-- Informatique (ID 3)
(12.00, 1, 3, 1), (11.50, 1, 3, 2), (12.25, 1, 3, 3),
(10.00, 2, 3, 1), (9.50, 2, 3, 2), (10.25, 2, 3, 3),
(11.00, 3, 3, 1), (10.50, 3, 3, 2), (11.25, 3, 3, 3),
(7.00, 4, 3, 1), (7.50, 4, 3, 2), (7.25, 4, 3, 3),
(16.00, 5, 3, 1), (15.50, 5, 3, 2), (16.25, 5, 3, 3);
