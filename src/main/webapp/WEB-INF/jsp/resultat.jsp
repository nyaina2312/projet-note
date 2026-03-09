<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Résultat du Calcul</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .resultat { 
            font-size: 24px; 
            padding: 20px; 
            background-color: #e8f5e9; 
            border: 2px solid #4CAF50;
            margin-top: 20px;
        }
        a { color: #2196F3; }
    </style>
</head>
<body>
    <h1>Résultat du Calcul</h1>
    
    <div class="resultat">
        <p><strong>Note finale (Somme des différences):</strong> ${noteFinale}</p>
    </div>
    
    <br>
    <p><a href="/notes">Retour à la liste</a></p>
</body>
</html>
