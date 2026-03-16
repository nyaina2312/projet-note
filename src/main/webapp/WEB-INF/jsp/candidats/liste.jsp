<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des Candidats</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1>👥 Liste des Candidats</h1>
        
        <div class="nav-links mb-20">
            <a href="/candidats/ajouter" class="btn btn-success">➕ Ajouter un candidat</a>
            <a href="/notes" class="btn btn-secondary">⬅ Retour</a>
        </div>
        
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="candidat" items="${candidats}">
                    <tr>
                        <td>${candidat.idCandidat}</td>
                        <td>${candidat.nom}</td>
                        <td>
                            <a href="/candidats/modifier?id=${candidat.idCandidat}" class="btn btn-warning">✏️ Modifier</a>
                            <a href="/candidats/supprimer?id=${candidat.idCandidat}" class="btn btn-danger" onclick="return confirm('Voulez-vous vraiment supprimer?')">🗑️ Supprimer</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
