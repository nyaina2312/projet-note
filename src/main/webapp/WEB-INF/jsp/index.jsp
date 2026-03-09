<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Projet Note</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1>🎓 Gestion des Notes</h1>
        
        <div class="card">
            <div class="card-title">Bienvenue!</div>
            <p>Cette application permet de gérer les notes des candidats et de calculer la note finale selon la logique définie.</p>
        </div>
        
        <div class="card">
            <div class="card-title">📋 Fonctionnalités</div>
            <ul style="margin-left: 20px; line-height: 2;">
                <li>👥 Gestion des candidats</li>
                <li>📖 Gestion des matières</li>
                <li>➕ Ajout des notes par correcteur</li>
                <li>📊 Calcul automatique de la note finale</li>
            </ul>
        </div>
        
        <div class="nav-links">
            <a href="/notes" class="btn btn-primary">📊 Calculer la note finale</a>
            <a href="/notes/ajouter" class="btn btn-success">➕ Ajouter une note</a>
            <a href="/candidats" class="btn btn-info">👥 Gestion Candidats</a>
            <a href="/matieres" class="btn btn-info">📖 Gestion Matières</a>
            <a href="/admin/reset" class="btn btn-danger" onclick="return confirm('Voulez-vous réinitialiser toutes les données?')">🔄 Réinitialiser les données</a>
        </div>
        
        <div class="footer">
            <p>Projet Spring Boot - Application de gestion de notes</p>
        </div>
    </div>
</body>
</html>
