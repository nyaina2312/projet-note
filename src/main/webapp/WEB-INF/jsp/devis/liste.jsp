<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des Devis</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Liste des Devis</h1>
        
        <div style="margin-bottom: 20px;">
            <a href="${pageContext.request.contextPath}/devis/ajouter" class="btn btn-primary">Ajouter un Devis</a>
            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">Retour à l'accueil</a>
        </div>
        
        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Date</th>
                        <th>Montant Total</th>
                        <th>Type</th>
                        <th>Demande</th>
                        <th>Client</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="devis" items="${devisList}">
                        <tr>
                            <td>${devis.id}</td>
                            <td><fmt:formatDate value="${devis.date}" pattern="dd/MM/yyyy" /></td>
                            <td>${devis.montantTotal} Ar</td>
                            <td>${devis.typeDevis.libelle}</td>
                            <td>${devis.demande.id}</td>
                            <td>${devis.demande.client.nom}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/devis/voir/${devis.id}" class="btn btn-sm btn-info">Voir</a>
                                <a href="${pageContext.request.contextPath}/devis/modifier/${devis.id}" class="btn btn-sm btn-warning">Modifier</a>
                                <a href="${pageContext.request.contextPath}/devis/supprimer/${devis.id}" class="btn btn-sm btn-danger" onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce devis?')">Supprimer</a>
                            </td>
                        </tr>
                    </c:forEach>
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