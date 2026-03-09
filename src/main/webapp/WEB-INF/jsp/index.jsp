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
        <h1>🎓 Projet Gestion des Notes</h1>
        
        <div class="card">
            <h2>Bienvenue!</h2>
            <p>Cette application permet de gérer les notes des candidats et de calculer la note finale.</p>
        </div>
        
        <div class="nav-links">
            <a href="/notes" class="btn btn-primary">📊 Calculer la note finale</a>
        </div>
    </div>
</body>
</html>
