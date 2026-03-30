-- Script de réinitialisation des données et des IDs
-- Pour PostgreSQL

-- Désactiver les contraintes de clé étrangère temporairement
SET CONSTRAINTS ALL DEFERRED;

-- Supprimer les données dans l'ordre inverse des dépendances
TRUNCATE TABLE details_devis CASCADE;
TRUNCATE TABLE demande_statut CASCADE;
TRUNCATE TABLE travaux CASCADE;
TRUNCATE TABLE devis CASCADE;
TRUNCATE TABLE demande CASCADE;
TRUNCATE TABLE client CASCADE;
TRUNCATE TABLE type_devis CASCADE;
TRUNCATE TABLE statut CASCADE;

-- Réinitialiser les séquences (IDs auto-increment) pour PostgreSQL
ALTER SEQUENCE client_id_seq RESTART WITH 1;
ALTER SEQUENCE type_devis_id_seq RESTART WITH 1;
ALTER SEQUENCE demande_id_seq RESTART WITH 1;
ALTER SEQUENCE devis_id_seq RESTART WITH 1;
ALTER SEQUENCE details_devis_id_seq RESTART WITH 1;
ALTER SEQUENCE statut_id_seq RESTART WITH 1;
ALTER SEQUENCE travaux_id_seq RESTART WITH 1;
ALTER SEQUENCE demande_statut_id_seq RESTART WITH 1;

-- Réactiver les contraintes
SET CONSTRAINTS ALL IMMEDIATE;

-- Réinsérer les données de base (types de devis et statuts)
INSERT INTO type_devis (libelle) VALUES 
('Etude'), 
('Forage');

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

-- Réinsérer les clients de test
INSERT INTO client (nom, contact) VALUES 
('Randriamats', 'randriamats@example.com'),
('Rakoto', 'rakoto@example.com'),
('Rasoa', 'rasoa@example.com');
