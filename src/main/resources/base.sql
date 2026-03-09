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
INSERT IGNORE INTO correcteur (nom) VALUES ('Correcteur 1'), ('Correcteur 2'), ('Correcteur 3');

INSERT IGNORE INTO candidat (nom) VALUES ('Ny aina'), ('Balita'), ('Bema');

INSERT IGNORE INTO matiere (nom) VALUES ('Mathématiques'), ('Physique'), ('Informatique');

INSERT IGNORE INTO operateur (operateur) VALUES ('+'), ('-'), ('*'), ('/');

INSERT IGNORE INTO resolution (nom) VALUES ('Addition'), ('Soustraction'), ('Multiplication');

INSERT IGNORE INTO parametre (idmatiere, diff, idoperateur, idresolution) VALUES 
(1, 10, 1, 1),
(2, 15, 2, 2),
(3, 20, 3, 3);

INSERT IGNORE INTO note (note, idcandidat, idmatiere, idcorrecteur) VALUES
-- Mathématiques (ID 1)
(15.50, 1, 1, 1), (16.00, 1, 1, 2), (15.75, 1, 1, 3),
(12.00, 2, 1, 1), (13.50, 2, 1, 2), (12.25, 2, 1, 3),
(14.00, 3, 1, 1), (14.50, 3, 1, 2), (14.25, 3, 1, 3),
-- Physique (ID 2)
(18.00, 1, 2, 1), (17.50, 1, 2, 2), (18.25, 1, 2, 3),
(16.00, 2, 2, 1), (15.50, 2, 2, 2), (16.25, 2, 2, 3),
(18.00, 3, 2, 1), (17.50, 3, 2, 2), (18.25, 3, 2, 3),
-- Informatique (ID 3)
(10.00, 1, 3, 1), (11.00, 1, 3, 2), (10.50, 1, 3, 3),
(8.00, 2, 3, 1), (9.00, 2, 3, 2), (8.50, 2, 3, 3),
(12.00, 3, 3, 1), (13.00, 3, 3, 2), (12.50, 3, 3, 3);
