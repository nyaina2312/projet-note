<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulaire Client</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1><c:if test="${client.id == null}">Ajouter</c:if><c:if test="${client.id != null}">Modifier</c:if> un Client</h1>
        
        <form action="/clients/sauvegarder" method="post">
            <c:if test="${client.id != null}">
                <input type="hidden" name="id" value="${client.id}" />
            </c:if>
            
            <div class="form-group">
                <label for="nom">Nom:</label>
                <input type="text" id="nom" name="nom" value="${client.nom}" required />
            </div>
            
            <div class="form-group">
                <label for="contact">Contact:</label>
                <input type="text" id="contact" name="contact" value="${client.contact}" required />
            </div>
            
            <button type="submit" class="btn btn-primary">Sauvegarder</button>
            <a href="/clients" class="btn btn-secondary">Annuler</a>
        </form>
    </div>
</body>
</html>
