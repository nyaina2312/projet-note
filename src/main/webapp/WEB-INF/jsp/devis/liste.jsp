<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <%-- Déclare la page JSP avec encodage UTF-8 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>          <%-- Importe JSTL Core (pour <c:forEach>, <c:if>) --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>         <%-- Importe JSTL Format (pour <fmt:formatDate>) --%>
<!DOCTYPE html>                                                            <%-- Déclare le type de document HTML5 --%>
<html>                                                                     <%-- Balise racine HTML --%>
<head>                                                                     <%-- En-tête du document --%>
    <meta charset="UTF-8">                                                 <%-- Encodage UTF-8 --%>
    <title>Liste des Devis</title>                                         <%-- Titre de l'onglet navigateur --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css"> <%-- Lien vers le CSS du projet --%>
</head>
<body>
    <div class="container">                                                <%-- Conteneur principal (fond bleu nuit, coins arrondis) --%>
        <div class="etu-number"><span class="etu-prefix">ETU</span><span class="etu-id">003647</span></div> <%-- Numéro étudiant affiché en grand --%>
        <h1>Liste des Devis</h1>                                           <%-- Titre de la page --%>
        
        <%-- ===== BOUTONS D'ACTION ===== --%>
        <div style="margin-bottom: 20px;">                                 <%-- Conteneur avec marge en bas --%>
            <a href="${pageContext.request.contextPath}/devis/ajouter" class="btn btn-primary">Ajouter un Devis</a> <%-- Lien vers le formulaire d'ajout --%>
            <%-- Bouton vers la page Chiffre d'Affaires Provisionnel --%>
            <%-- Affiche tous les détails de devis avec leur somme totale --%>
            <a href="${pageContext.request.contextPath}/devis/chiffre-affaires" class="btn btn-info">Chiffre d'Affaires</a>
            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">Retour a l'accueil</a> <%-- Lien vers la page d'accueil --%>
        </div>
        
        <%-- ===== TABLEAU DES DEVIS ===== --%>
        <div class="table-container">                                      <%-- Conteneur du tableau (ombre, coins arrondis) --%>
            <table>                                                        <%-- Début du tableau --%>
                <thead>                                                    <%-- En-tête du tableau --%>
                    <tr>
                        <th>ID</th>                                        <%-- Colonne identifiant du devis --%>
                        <th>Date</th>                                      <%-- Colonne date du devis --%>
                        <th>Montant Total</th>                             <%-- Colonne montant total (avec réduction 10% si applicable) --%>
                        <th>Type</th>                                      <%-- Colonne type (Étude ou Forage) --%>
                        <th>Demande</th>                                   <%-- Colonne ID de la demande associée --%>
                        <th>Client</th>                                    <%-- Colonne nom du client --%>
                        <th>Actions</th>                                   <%-- Colonne boutons d'action --%>
                    </tr>
                </thead>
                <tbody>                                                    <%-- Corps du tableau (données) --%>
                    <%-- Boucle sur chaque devis de la liste ${devisList} envoyée par le controller --%>
                    <c:forEach var="devis" items="${devisList}">
                        <tr>                                               <%-- Ligne pour chaque devis --%>
                            <td>${devis.id}</td>                           <%-- Affiche l'ID du devis --%>
                            <td><fmt:formatDate value="${devis.date}" pattern="dd/MM/yyyy" /></td> <%-- Formate la date en jj/mm/aaaa --%>
                            <td>${devis.montantTotal} Ar</td>              <%-- Affiche le montant total en Ariary --%>
                            <td>${devis.typeDevis.libelle}</td>            <%-- Affiche le type (Étude ou Forage) --%>
                            <td>${devis.demande.id}</td>                   <%-- Affiche l'ID de la demande associée --%>
                            <td>${devis.demande.client.nom}</td>           <%-- Affiche le nom du client (via demande → client) --%>
                            <td>                                           <%-- Cellule contenant les boutons d'action --%>
                                <a href="${pageContext.request.contextPath}/devis/voir/${devis.id}" class="btn btn-sm btn-info">Voir</a> <%-- Bouton voir les détails --%>
                                <a href="${pageContext.request.contextPath}/devis/modifier/${devis.id}" class="btn btn-sm btn-warning">Modifier</a> <%-- Bouton modifier --%>
                                <a href="${pageContext.request.contextPath}/devis/supprimer/${devis.id}" class="btn btn-sm btn-danger" onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce devis?')">Supprimer</a> <%-- Bouton supprimer avec confirmation JS --%>
                            </td>
                        </tr>
                    </c:forEach>
                    <%-- Si la liste est vide, affiche un message --%>
                    <c:if test="${empty devisList}">
                        <tr>
                            <td colspan="7" style="text-align: center;">Aucun devis trouvé</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>