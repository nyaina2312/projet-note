-- Sprint 3: Gestion des forages
-- Conversion pour PostgreSQL

-- Table Client
CREATE TABLE IF NOT EXISTS client (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    contact VARCHAR(100) NOT NULL
);

-- Table TypeDevis
CREATE TABLE IF NOT EXISTS type_devis (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL
);

-- Table Demande
CREATE TABLE IF NOT EXISTS demande (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    lieu VARCHAR(200) NOT NULL,
    district VARCHAR(100) NOT NULL,
    client_id INT NOT NULL REFERENCES client(id)
);

-- Table Devis
CREATE TABLE IF NOT EXISTS devis (
    id SERIAL PRIMARY KEY,
    montantTotal DECIMAL(10,2),
    date DATE NOT NULL,
    demande_id INT NOT NULL REFERENCES demande(id),
    type_devis_id INT NOT NULL REFERENCES type_devis(id)
);

-- Table DetailsDevis
CREATE TABLE IF NOT EXISTS details_devis (
    id SERIAL PRIMARY KEY,
    devis_id INT NOT NULL REFERENCES devis(id),
    libelle VARCHAR(200) NOT NULL,
    prixUnitaire DECIMAL(10,2),
    quantite INT,
    montant DECIMAL(10,2) NOT NULL
);

-- Table Statut
CREATE TABLE IF NOT EXISTS statut (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL,
    typeStatut VARCHAR(100)
);

-- Table Travaux
CREATE TABLE IF NOT EXISTS travaux (
    id SERIAL PRIMARY KEY,
    demande_id INT NOT NULL REFERENCES demande(id),
    typeStatut VARCHAR(100)
);

-- Table DemandeStatut (historique des statuts)
CREATE TABLE IF NOT EXISTS demande_statut (
    id SERIAL PRIMARY KEY,
    demande_id INT NOT NULL REFERENCES demande(id),
    statut_id INT NOT NULL REFERENCES statut(id),
    dateChangement DATE NOT NULL
);

-- Insertion des types de devis (sans accents)
INSERT INTO type_devis (libelle) VALUES 
('Etude'), 
('Forage');

-- Insertion des statuts (sans accents)
INSERT INTO statut (libelle, typeStatut) VALUES 
('En attente', 'Initial'),
('Devis etude cree', 'Etude'),
('Devis etude accepte', 'Etude'),
('Devis etude refuse', 'Etude'),
('Etude terrain en cours', 'Etude'),
('Eau trouvee', 'Etude'),
('Eau non trouvee', 'Etude'),
('Devis forage cree', 'Forage'),
('Devis forage accepte', 'Forage'),
('Devis forage refuse', 'Forage'),
('Forage commence', 'Forage'),
('Forage termine', 'Forage'),
('Test sanitaire en cours', 'Test'),
('Test sanitaire termine', 'Test');

-- Insertion de clients de test
INSERT INTO client (nom, contact) VALUES 
('Randriamats', 'randriamats@example.com'),
('Rakoto', 'rakoto@example.com'),
('Rasoa', 'rasoa@example.com')
ON CONFLICT DO NOTHING;
