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
        <h1>📊 Résultat du Calcul</h1>
        
        <div class="result-box">
            <h3>Note Finale</h3>
            <div class="note"><fmt:formatNumber value="${noteFinale}" type="NUMBER" integerOnly="true" /></div>
            <p>Candidat ID: ${idCandidat} | Matière ID: ${idMatiere}</p>
        </div>
        
        <div class="nav-links">
            <a href="/notes" class="btn btn-primary">🔄 Nouveau calcul</a>
        </div>
    </div>
</body>
</html>
