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

INSERT IGNORE INTO candidat (nom) VALUES ('Candidat1'), ('Candidat2');

INSERT IGNORE INTO matiere (nom) VALUES ('JAVA'), ('PHP');

INSERT IGNORE INTO operateur (operateur) VALUES ('>'), ('<'), ('>='), ('<=');

INSERT IGNORE INTO resolution (nom) VALUES ('Petit'), ('Grande'), ('Moyenne');

INSERT IGNORE INTO parametre (idmatiere, diff, idoperateur, idresolution) VALUES 
(1, 3, 3, 1),  -- Mathématiques: diff=3, op=>=, résolution=Petit (note la plus petite)
(2, 5, 2, 1);  -- PHP: diff=5, op=<=, résolution=Petit

INSERT IGNORE INTO note (note, idcandidat, idmatiere, idcorrecteur) VALUES
-- JAVA (ID 1) - Exemple: 9 et 14, diff=5, param: diff=3, op=3(>=), res=1(Petit) -> 9
(9.00, 1, 1, 1), (14.00, 1, 1, 2), (11.00, 1, 1, 3),
(12.00, 2, 1, 1), (13.00, 2, 1, 2), (12.00, 2, 1, 3),
(14.00, 3, 1, 1), (14.00, 3, 1, 2), (14.00, 3, 1, 3),
-- PHP (ID 2)
(18.00, 1, 2, 1), (17.00, 1, 2, 2), (18.00, 1, 2, 3),
(16.00, 2, 2, 1), (15.00, 2, 2, 2), (16.00, 2, 2, 3),
(18.00, 3, 2, 1), (17.00, 3, 2, 2), (18.00, 3, 2, 3);
