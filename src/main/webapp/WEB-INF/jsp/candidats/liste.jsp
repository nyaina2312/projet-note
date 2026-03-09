<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des Candidats</title>
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
    <h1>Liste des Candidats</h1>
    
    <a href="/candidats/ajouter" class="btn">Ajouter un candidat</a>
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
            <c:forEach var="candidat" items="${candidats}">
                <tr>
                    <td>${candidat.idCandidat}</td>
                    <td>${candidat.nom}</td>
                    <td>
                        <a href="/candidats/modifier?id=${candidat.idCandidat}" class="btn btn-modifier">Modifier</a>
                        <a href="/candidats/supprimer?id=${candidat.idCandidat}" class="btn btn-supprimer" onclick="return confirm('Voulez-vous vraiment supprimer?')">Supprimer</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
