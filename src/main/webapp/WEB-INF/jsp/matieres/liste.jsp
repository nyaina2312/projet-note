<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des Matières</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid black; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .btn { padding: 5px 10px; background-color: #4CAF50; color: white; text-decoration: none; }
        .btn-modifier { background-color: #FF9800; }
        .btn-supprimer { background-color: #f44336; }
    </style>
</head>
<body>
    <h1>Liste des Matières</h1>
    
    <a href="/matieres/ajouter" class="btn">Ajouter une matière</a>
    <a href="/notes" class="btn" style="background-color: #2196F3;">Retour</a>
    <br><br>
    
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
                        <a href="/matieres/modifier?id=${matiere.idMatiere}" class="btn btn-modifier">Modifier</a>
                        <a href="/matieres/supprimer?id=${matiere.idMatiere}" class="btn btn-supprimer" onclick="return confirm('Voulez-vous vraiment supprimer?')">Supprimer</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
