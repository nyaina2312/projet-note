<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Matière</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: inline-block; width: 100px; }
        input { padding: 5px; width: 200px; }
        .btn { padding: 5px 15px; background-color: #4CAF50; color: white; border: none; cursor: pointer; }
        .btn-retour { background-color: #757575; }
    </style>
</head>
<body>
    <h1><c:if test="${matiere.idMatiere == null}">Ajouter</c:if><c:if test="${matiere.idMatiere != null}">Modifier</c:if> une matière</h1>
    
    <form action="/matieres/sauvegarder" method="post">
        <c:if test="${matiere.idMatiere != null}">
            <input type="hidden" name="idMatiere" value="${matiere.idMatiere}">
        </c:if>
        
        <div class="form-group">
            <label for="nom">Nom:</label>
            <input type="text" name="nom" id="nom" value="${matiere.nom}" required>
        </div>
        
        <button type="submit" class="btn">Sauvegarder</button>
        <a href="/matieres" class="btn btn-retour">Retour</a>
    </form>
</body>
</html>
