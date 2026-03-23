<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des Demandes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Liste des Demandes de Forage</h1>
        
        <a href="/demandes/ajouter" class="btn btn-primary">Ajouter une Demande</a>
        
        <table class="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Date</th>
                    <th>Lieu</th>
                    <th>District</th>
                    <th>Client</th>
                    <th>Statut</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="demande" items="${demandes}">
                    <tr>
                        <td>${demande.id}</td>
                        <td><fmt:formatDate value="${demande.date}" pattern="dd/MM/yyyy" /></td>
                        <td>${demande.lieu}</td>
                        <td>${demande.district}</td>
                        <td>
                            <c:if test="${demande.client != null}">
                                ${demande.client.nom}
                            </c:if>
                        </td>
                        <td>
                            <c:set var="statut" value="${statutsMap[demande.id]}" />
                            <c:if test="${statut != null}">
                                <span class="badge badge-primary">${statut.libelle}</span>
                            </c:if>
                            <c:if test="${statut == null}">
                                <span class="badge badge-warning">En attente</span>
                            </c:if>
                        </td>
                        <td>
                            <a href="/demandes/voir?id=${demande.id}" class="btn btn-sm">Voir</a>
                            <a href="/demandes/modifier?id=${demande.id}" class="btn btn-sm">Modifier</a>
                            <a href="/demandes/supprimer?id=${demande.id}" class="btn btn-sm btn-danger" onclick="return confirm('Voulez-vous vraiment supprimer cette demande?')">Supprimer</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <a href="/" class="btn btn-secondary">Retour à l'accueil</a>
    </div>
</body>
</html>
