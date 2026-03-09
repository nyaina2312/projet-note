<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ajouter une Note</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        form { max-width: 400px; }
        label { display: block; margin-top: 10px; }
        input, select { width: 100%; padding: 8px; margin-top: 5px; }
        button { margin-top: 15px; padding: 10px 20px; background-color: #4CAF50; color: white; border: none; }
    </style>
</head>
<body>
    <h1>Ajouter une Note</h1>
    
    <form action="/notes/sauvegarder" method="post">
        <label>Candidat:</label>
        <select name="candidat.idCandidat" required>
            <option value="">Sélectionner un candidat</option>
            <c:forEach var="candidat" items="${candidats}">
                <option value="${candidat.idCandidat}">${candidat.nom}</option>
            </c:forEach>
        </select>
        
        <label>Matière:</label>
        <select name="matiere.idMatiere" required>
            <option value="">Sélectionner une matière</option>
            <c:forEach var="matiere" items="${matieres}">
                <option value="${matiere.idMatiere}">${matiere.nom}</option>
            </c:forEach>
        </select>
        
        <label>Correcteur:</label>
        <select name="correcteur.idCorrecteur" required>
            <option value="">Sélectionner un correcteur</option>
            <c:forEach var="correcteur" items="${correcteurs}">
                <option value="${correcteur.idCorrecteur}">${correcteur.nom}</option>
            </c:forEach>
        </select>
        
        <label>Note:</label>
        <input type="number" step="0.01" name="note" required>
        
        <button type="submit">Sauvegarder</button>
    </form>
    
    <br>
    <a href="/notes">Retour à la liste</a>
</body>
</html>
