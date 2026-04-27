<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Candidat</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <div class="etu-number"><span class="etu-prefix">ETU</span><span class="etu-id">003647</span></div>
        <h1><c:if test="${candidat.idCandidat == null}">➕ Ajouter</c:if><c:if test="${candidat.idCandidat != null}">✏️ Modifier</c:if> un candidat</h1>
        
        <form action="/candidats/sauvegarder" method="post">
            <c:if test="${candidat.idCandidat != null}">
                <input type="hidden" name="idCandidat" value="${candidat.idCandidat}">
            </c:if>
            
            <div class="form-group">
                <label for="nom">Nom:</label>
                <input type="text" name="nom" id="nom" value="${candidat.nom}" required>
            </div>
            
            <div class="nav-links">
                <button type="submit" class="btn btn-success">💾 Sauvegarder</button>
                <a href="/candidats" class="btn btn-secondary">⬅ Retour</a>
            </div>
        </form>
    </div>
</body>
</html>
