-- Script de mise à jour pour Sprint 3
-- Modifier les colonnes testSanitaire en typeStatut sans perdre les données

-- Modifier la table statut
ALTER TABLE statut ADD COLUMN IF NOT EXISTS typeStatut VARCHAR(100);

-- Copier les valeurs: testSanitaire=TRUE devient 'Test', FALSE devient NULL
UPDATE statut SET typeStatut = 'Test' WHERE testSanitaire = TRUE;
UPDATE statut SET typeStatut = NULL WHERE testSanitaire = FALSE;

-- Supprimer l'ancienne colonne
ALTER TABLE statut DROP COLUMN IF EXISTS testSanitaire;

-- Modifier la table travaux
ALTER TABLE travaux ADD COLUMN IF NOT EXISTS typeStatut VARCHAR(100);

-- Copier les valeurs si elles existent
UPDATE travaux SET typeStatut = 'Test' WHERE testSanitaire = TRUE;
UPDATE travaux SET typeStatut = NULL WHERE testSanitaire = FALSE;

-- Supprimer l'ancienne colonne
ALTER TABLE travaux DROP COLUMN IF EXISTS testSanitaire;

-- Mettre à jour les types de statuts existants (si déjà insérés)
UPDATE statut SET typeStatut = 'Initial' WHERE libelle = 'En attente';
UPDATE statut SET typeStatut = 'Etude' WHERE libelle LIKE 'Devis etude%';
UPDATE statut SET typeStatut = 'Etude' WHERE libelle = 'Etude terrain en cours';
UPDATE statut SET typeStatut = 'Etude' WHERE libelle LIKE 'Eau%';
UPDATE statut SET typeStatut = 'Forage' WHERE libelle LIKE 'Devis forage%';
UPDATE statut SET typeStatut = 'Forage' WHERE libelle LIKE 'Forage%';
UPDATE statut SET typeStatut = 'Test' WHERE libelle LIKE 'Test sanitaire%';

-- Ajouter colonne demande_id à la table demande_statut si elle n'existe pas
ALTER TABLE demande_statut ADD COLUMN IF NOT EXISTS demande_id INT;

-- Copier les données de travaux_id vers demande_id si nécessaire
UPDATE demande_statut SET demande_id = (SELECT demande_id FROM travaux WHERE id = demande_statut.travaux_id) WHERE demande_id IS NULL;

-- Rendre la colonne demande_id NOT NULL après la mise à jour
-- ALTER TABLE demande_statut ALTER COLUMN demande_id SET NOT NULL;
