-- ============================================================
-- SCHEMA DE LA BASE DE DONNÉES - PROJET FORAGE
-- PostgreSQL
-- ============================================================

-- ==================== TABLES ====================

CREATE TABLE IF NOT EXISTS client (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    contact VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS type_devis (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS demande (
    id SERIAL PRIMARY KEY,
    date TIMESTAMP NOT NULL,
    lieu VARCHAR(200) NOT NULL,
    district VARCHAR(100) NOT NULL,
    client_id INTEGER NOT NULL,
    FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS devis (
    id SERIAL PRIMARY KEY,
    montantTotal DECIMAL(10,2),
    date TIMESTAMP NOT NULL,
    demande_id INTEGER NOT NULL,
    type_devis_id INTEGER NOT NULL,
    FOREIGN KEY (demande_id) REFERENCES demande(id) ON DELETE CASCADE,
    FOREIGN KEY (type_devis_id) REFERENCES type_devis(id)
);

CREATE TABLE IF NOT EXISTS details_devis (
    id SERIAL PRIMARY KEY,
    devis_id INTEGER NOT NULL,
    libelle VARCHAR(200) NOT NULL,
    prix_unitaire DECIMAL(10,2) NOT NULL,
    quantite INTEGER NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (devis_id) REFERENCES devis(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS statut (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL,
    type_statut VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS travaux (
    id SERIAL PRIMARY KEY,
    demande_id INTEGER NOT NULL,
    type_statut VARCHAR(100),
    FOREIGN KEY (demande_id) REFERENCES demande(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS demande_statut (
    id SERIAL PRIMARY KEY,
    demande_id INTEGER NOT NULL,
    statut_id INTEGER NOT NULL,
    datechangement TIMESTAMP NOT NULL,
    observation VARCHAR(500),
    travaux_id INTEGER,
    duree_totale_heures DOUBLE PRECISION,  -- Durée totale en heures (approche 1: toutes les heures comptées)
    duree_ouvrable_heures DOUBLE PRECISION, -- Durée ouvrable en heures (approche 2: 8h-17h, lun-ven uniquement)
    FOREIGN KEY (demande_id) REFERENCES demande(id) ON DELETE CASCADE,
    FOREIGN KEY (statut_id) REFERENCES statut(id),
    FOREIGN KEY (travaux_id) REFERENCES travaux(id) ON DELETE CASCADE
);

-- ==================== HORAIRE DE TRAVAIL ====================

CREATE TABLE IF NOT EXISTS horaire_travail (
    id SERIAL PRIMARY KEY,
    heure_debut TIME NOT NULL,
    heure_fin TIME NOT NULL,
    description VARCHAR(200)
);

-- ==================== JOURS NON TRAVAILLÉS ====================

CREATE TABLE IF NOT EXISTS jour_non_travaille (
    id SERIAL PRIMARY KEY,
    date_jour DATE NOT NULL UNIQUE,
    motif VARCHAR(200)
);

-- ==================== ALERTES ====================

CREATE TABLE IF NOT EXISTS alerte (
    id SERIAL PRIMARY KEY,
    demande_id INTEGER NOT NULL,
    type_alerte VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    date_creation TIMESTAMP NOT NULL,
    statut VARCHAR(20) NOT NULL DEFAULT 'NON_LUE',
    FOREIGN KEY (demande_id) REFERENCES demande(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_alerte_demande_id ON alerte(demande_id);

-- ==================== ALTER TABLE POUR AJOUTER LES COLONNES MANQUANTES ====================

-- Ajout des colonnes de durée à demande_statut (si la table existe déjà)
ALTER TABLE demande_statut ADD COLUMN IF NOT EXISTS duree_totale_heures DOUBLE PRECISION;
ALTER TABLE demande_statut ADD COLUMN IF NOT EXISTS duree_ouvrable_heures DOUBLE PRECISION;

-- ==================== DONNÉES INITIALES HORAIRE TRAVAIL ====================

INSERT INTO horaire_travail (heure_debut, heure_fin, description)
VALUES ('08:00:00', '17:00:00', 'Horaire standard 9h-17h')
ON CONFLICT DO NOTHING;
INSERT INTO statut (id, libelle, type_statut) VALUES
(1, 'En attente', 'Initial'),
(2, 'Confirme', 'CONFIRME'),
(3, 'Devis etude cree', 'DEVIS'),
(4, 'Annule', 'ANNULE'),
(5, 'Devis forage cree', 'DEVIS'),
(6, 'Accepter Devis', 'ACCEPTE'),
(7, 'Refuser Devis', 'REFUSE')
ON CONFLICT (id) DO NOTHING;

-- Réinitialiser la séquence pour les futurs insertions
SELECT setval('statut_id_seq', (SELECT MAX(id) FROM statut));

-- Insertion des types de devis
INSERT INTO type_devis (libelle) VALUES ('Etude') ON CONFLICT DO NOTHING;
INSERT INTO type_devis (libelle) VALUES ('Forage') ON CONFLICT DO NOTHING;
