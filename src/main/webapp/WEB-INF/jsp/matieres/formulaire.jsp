<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Matière</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1><c:if test="${matiere.idMatiere == null}">➕ Ajouter</c:if><c:if test="${matiere.idMatiere != null}">✏️ Modifier</c:if> une matière</h1>
        
        <form action="/matieres/sauvegarder" method="post">
            <c:if test="${matiere.idMatiere != null}">
                <input type="hidden" name="idMatiere" value="${matiere.idMatiere}">
            </c:if>
            
            <div class="form-group">
                <label for="nom">Nom:</label>
                <input type="text" name="nom" id="nom" value="${matiere.nom}" required>
            </div>
            
            <div class="nav-links">
                <button type="submit" class="btn btn-success">💾 Sauvegarder</button>
                <a href="/matieres" class="btn btn-secondary">⬅ Retour</a>
            </div>
        </form>
    </div>
</body>
</html>
