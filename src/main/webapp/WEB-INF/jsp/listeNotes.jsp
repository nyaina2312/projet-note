<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Calcul de la Note Finale</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1>📊 Calcul de la Note Finale</h1>
        
        <p class="text-center">Sélectionnez un candidat et une matière pour calculer la note finale.</p>
        
        <form action="/notes/calculer" method="get">
            <div class="form-group">
                <label for="candidat">👤 Candidat:</label>
                <select name="idCandidat" id="candidat" required>
                    <option value="">-- Choisir un candidat --</option>
                    <c:forEach var="candidat" items="${candidats}">
                        <option value="${candidat.idCandidat}">${candidat.idCandidat} - ${candidat.nom}}}</option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="form-group">
                <label for="matiere">📚 Matière:</label>
                <select name="idMatiere" id="matiere" required>
                    <option value="">-- Choisir une matière --</option>
                    <c:forEach var="matiere" items="${matieres}">
                        <option value="${matiere.idMatiere}">${matiere.idMatiere} - ${matiere.nom}}}</option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="nav-links">
                <button type="submit" class="btn btn-primary">📈 Calculer</button>
            </div>
        </form>
        
        <div class="nav-links">
            <a href="/admin/reset" class="btn btn-danger" onclick="return confirm('Voulez-vous réinitialiser toutes les données?')">🔄 Réinitialiser les données</a>
            <a href="/candidats" class="btn btn-info">👥 Gestion Candidats</a>
            <a href="/matieres" class="btn btn-info">📖 Gestion Matières</a>
            <a href="/notes/ajouter" class="btn btn-success">➕ Ajouter une note</a>
        </div>
    </div>
</body>
</html>
