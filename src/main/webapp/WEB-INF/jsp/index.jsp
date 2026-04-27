<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion des Forages</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <div class="etu-number">
            <span class="etu-prefix">ETU</span>
            <span class="etu-id">003647</span>
        </div>

        <h1>Gestion des Forages</h1>
        
        <div class="card">
            <div class="card-title">Bienvenue!</div>
            <p>Cette application permet de gérer les demandes de forage et leur statut.</p>
        </div>
        
        <div class="card">
            <div class="card-title">Fonctionnalites</div>
            <ul style="margin-left: 20px; line-height: 2;">
                <li>Gestion des clients</li>
                <li>Gestion des demandes de forage</li>
                <li>Suivi du statut des travaux</li>
                <li>Historique des statuts</li>
            </ul>
        </div>
        
        <div class="nav-links">
            <a href="/clients" class="btn btn-primary">Gestion Clients</a>
            <a href="/demandes" class="btn btn-success">Gestion Demandes</a>
            <a href="/devis" class="btn btn-primary">Gestion Devis</a>
        </div>
        
        <div class="footer">
            <p>Projet Spring Boot - Application de gestion des forages</p>
        </div>
    </div>
</body>
</html>
