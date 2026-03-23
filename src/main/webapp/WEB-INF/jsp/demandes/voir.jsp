<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Détails de la Demande</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Détails de la Demande</h1>
        
        <div class="detail-card">
            <p><strong>ID:</strong> ${demande.id}</p>
            <p><strong>Date:</strong> <fmt:formatDate value="${demande.date}" pattern="dd/MM/yyyy" /></p>
            <p><strong>Lieu:</strong> ${demande.lieu}</p>
            <p><strong>District:</strong> ${demande.district}</p>
            <p><strong>Client:</strong> <c:if test="${demande.client != null}">${demande.client.nom}</c:if></p>
            <p><strong>Contact Client:</strong> <c:if test="${demande.client != null}">${demande.client.contact}</c:if></p>
        </div>
        
        <div class="statut-card">
            <h2>Statut Actuel</h2>
            <p class="statut-actuel">
                <c:if test="${statutActuel != null}">
                    ${statutActuel.libelle}
                    <c:if test="${statutActuel.testSanitaire == true}"> (Test Sanitaire)</c:if>
                </c:if>
                <c:if test="${statutActuel == null}">
                    En attente
                </c:if>
            </p>
            
            <h3>Changer le Statut</h3>
            <form action="/demandes/changerStatut" method="post">
                <input type="hidden" name="id" value="${demande.id}" />
                <select name="statutId" required>
                    <option value="">Sélectionner un statut</option>
                    <c:forEach var="statut" items="${statuts}">
                        <option value="${statut.id}">${statut.libelle} <c:if test="${statut.testSanitaire == true}">(TS)</c:if></option>
                    </c:forEach>
                </select>
                <button type="submit" class="btn btn-primary">Changer le Statut</button>
            </form>
        </div>
        
        <div class="historique-card">
            <h2>Historique des Statuts</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Statut</th>
                        <th>Type</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="h" items="${historique}">
                        <tr>
                            <td><fmt:formatDate value="${h.dateChangement}" pattern="dd/MM/yyyy HH:mm" /></td>
                            <td>${h.statut.libelle}</td>
                            <td>
                                <c:if test="${h.statut != null}">
                                    <c:if test="${h.statut.testSanitaire == true}">Test Sanitaire</c:if>
                                    <c:if test="${h.statut.testSanitaire == false}">Standard</c:if>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        
        <a href="/demandes" class="btn btn-secondary">Retour à la liste</a>
    </div>
</body>
</html>
