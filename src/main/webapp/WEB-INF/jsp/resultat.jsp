<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Résultat</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1>🎯 Résultat du Calcul</h1>
        
        <div class="note-finale-container">
            <div class="note-finale-label">Note Finale</div>
            <div class="note-finale-valeur"><fmt:formatNumber value="${noteFinale}" type="NUMBER" integerOnly="true" /></div>
            <div class="note-details">
                <p><strong>Candidat ID:</strong> ${idCandidat} | <strong>Matière ID:</strong> ${idMatiere}</p>
            </div>
        </div>
        
        <div class="nav-links">
            <a href="/notes" class="btn btn-primary">🔄 Nouveau calcul</a>
            <a href="/admin/reset" class="btn btn-danger" onclick="return confirm('Voulez-vous réinitialiser toutes les données?')">🔄 Réinitialiser les données</a>
            <a href="/candidats" class="btn btn-info">👥 Gestion Candidats</a>
            <a href="/matieres" class="btn btn-info">📖 Gestion Matières</a>
        </div>
    </div>
</body>
</html>
