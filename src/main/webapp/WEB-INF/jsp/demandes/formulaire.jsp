<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulaire Demande</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <div class="etu-number"><span class="etu-prefix">ETU</span><span class="etu-id">003647</span></div>
        <h1><c:if test="${demande.id == null}">Ajouter</c:if><c:if test="${demande.id != null}">Modifier</c:if> une Demande</h1>
        
        <form action="/demandes/sauvegarder" method="post">
            <c:if test="${demande.id != null}">
                <input type="hidden" name="id" value="${demande.id}" />
            </c:if>
            
            <div class="form-group">
                <label for="date">Date:</label>
                <fmt:formatDate value="${demande.date}" pattern="yyyy-MM-dd" var="formattedDate" />
                <input type="date" id="date" name="date" value="${formattedDate}" required />
            </div>
            
            <div class="form-group">
                <label for="client.id">Client:</label>
                <select id="client.id" name="client.id" required>
                    <option value="">Sélectionner un client</option>
                    <c:forEach var="client" items="${clients}">
                        <option value="${client.id}" ${clientId == client.id ? 'selected' : ''}>${client.nom}</option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="form-group">
                <label for="lieu">Lieu:</label>
                <input type="text" id="lieu" name="lieu" value="${demande.lieu}" required />
            </div>
            
            <div class="form-group">
                <label for="district">District:</label>
                <input type="text" id="district" name="district" value="${demande.district}" required />
            </div>
            
            <c:if test="${demande.id != null}">
            <div class="form-group">
                <label for="observation">Observation (optionnel):</label>
                <input type="text" id="observation" name="observation" placeholder="Justification de la modification" />
            </div>
            </c:if>
            
            <button type="submit" class="btn btn-primary">Sauvegarder</button>
            <a href="/demandes" class="btn btn-secondary">Annuler</a>
        </form>
    </div>
</body>
</html>
