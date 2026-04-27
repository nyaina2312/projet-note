<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des Matières</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <div class="etu-number"><span class="etu-prefix">ETU</span><span class="etu-id">003647</span></div>
        <h1>📚 Liste des Matières</h1>
        
        <div class="nav-links mb-20">
            <a href="/matieres/ajouter" class="btn btn-success">➕ Ajouter une matière</a>
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
                <c:forEach var="matiere" items="${matieres}">
                    <tr>
                        <td>${matiere.idMatiere}</td>
                        <td>${matiere.nom}</td>
                        <td>
                            <a href="/matieres/modifier?id=${matiere.idMatiere}" class="btn btn-warning">✏️ Modifier</a>
                            <a href="/matieres/supprimer?id=${matiere.idMatiere}" class="btn btn-danger" onclick="return confirm('Voulez-vous vraiment supprimer?')">🗑️ Supprimer</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
