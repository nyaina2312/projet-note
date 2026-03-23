<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des Clients</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Liste des Clients</h1>
        
        <a href="/clients/ajouter" class="btn btn-primary">Ajouter un Client</a>
        
        <table class="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Contact</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="client" items="${clients}">
                    <tr>
                        <td>${client.id}</td>
                        <td>${client.nom}</td>
                        <td>${client.contact}</td>
                        <td>
                            <a href="/clients/modifier?id=${client.id}" class="btn btn-sm">Modifier</a>
                            <a href="/clients/supprimer?id=${client.id}" class="btn btn-sm btn-danger" onclick="return confirm('Voulez-vous vraiment supprimer ce client?')">Supprimer</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <a href="/" class="btn btn-secondary">Retour à l'accueil</a>
    </div>
</body>
</html>
