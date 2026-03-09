# Projet Note - Spring Boot

## Description
Application web pour la gestion des notes d'examens avec correction par 3 correcteurs.

## Fonctionnalités

### CRUD (Create, Read, Update, Delete)
- **Candidat**: Gestion des candidats
- **Matiere**: Gestion des matières
- **Note**: Ajout des notes

### Calcul de la note finale
- Si les 3 correcteurs donnent la même note → cette note
- Sinon → moyenne des notes selon les paramètres

### Base de données
| Table | Type |
|-------|------|
| correcteur | INSERT INTO |
| candidat | CRUD |
| matiere | CRUD |
| operateur | INSERT INTO |
| resolution | INSERT INTO |
| parametre | INSERT INTO |
| note | CRUD |

## Technologies
- Java 17
- Spring Boot 2.7
- MariaDB
- JSP
- Hibernate/JPA

## Installation
1. Créer la base de données MariaDB: `projet_note`
2. Lancer: `mvn spring-boot:run`
3. Accéder à: http://localhost:8081/notes

## Auteurs
- Étudiant: Naina
