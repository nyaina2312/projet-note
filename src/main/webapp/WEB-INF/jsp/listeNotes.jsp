<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Calcul de la Note Finale</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .btn { padding: 5px 10px; background-color: #4CAF50; color: white; text-decoration: none; }
        .btn-calculer { background-color: #2196F3; }
        .form-group { margin-bottom: 15px; }
        label { display: inline-block; width: 120px; }
        select { padding: 5px; width: 200px; }
    </style>
</head>
<body>
    <h1>Calcul de la Note Finale</h1>
    
    <p>Sélectionnez un candidat et une matière pour calculer la note finale.</p>
    
    <form action="/notes/calculer" method="get">
        <div class="form-group">
            <label for="candidat">Candidat:</label>
            <select name="idCandidat" id="candidat" required>
                <option value="">-- Choisir un candidat --</option>
                <c:forEach var="candidat" items="${candidats}">
                    <option value="${candidat.idCandidat}">${candidat.idCandidat} - ${candidat.nom}</option>
                </c:forEach>
            </select>
        </div>
        
        <div class="form-group">
            <label for="matiere">Matière:</label>
            <select name="idMatiere" id="matiere" required>
                <option value="">-- Choisir une matière --</option>
                <c:forEach var="matiere" items="${matieres}">
                    <option value="${matiere.idMatiere}">${matiere.idMatiere} - ${matiere.nom}</option>
                </c:forEach>
            </select>
        </div>
        
        <button type="submit" class="btn btn-calculer">Calculer</button>
    </form>
    
    <br>
    <a href="/candidats" class="btn" style="background-color: #9C27B0;">Gestion Candidats</a>
    <a href="/matieres" class="btn" style="background-color: #00BCD4;">Gestion Matières</a>
    <a href="/notes/ajouter" class="btn">Ajouter une note</a>
</body>
</html>
