<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Details du Devis</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <div class="etu-number"><span class="etu-prefix">ETU</span><span class="etu-id">003647</span></div>
        <h1>Details du Devis #${devis.id}</h1>
        
        <!-- Information generale -->
        <div class="card">
            <h3>Informations Generales</h3>
            <div class="form-group">
                <label>Date:</label> <fmt:formatDate value="${devis.date}" pattern="dd/MM/yyyy" />
            </div>
            <div class="form-group">
                <label>Type:</label> ${devis.typeDevis.libelle}
            </div>
        </div>
        
        <!-- Information sur la demande -->
        <div class="card">
            <h3>Demande Associee</h3>
            <div class="form-group">
                <label>ID Demande:</label> ${devis.demande.id}
            </div>
            <div class="form-group">
                <label>Lieu:</label> ${devis.demande.lieu}
            </div>
            <div class="form-group">
                <label>District:</label> ${devis.demande.district}
            </div>
            <div class="form-group">
                <label>Client:</label> ${devis.demande.client.nom}
            </div>
        </div>
        
        <!-- Details du devis -->
        <h2>Details du Devis</h2>
        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>Libelle</th>
                        <th>Prix Unitaire</th>
                        <th>Quantite</th>
                        <th>Montant</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="detail" items="${devis.detailsDevis}">
                        <tr>
                            <td>${detail.libelle}</td>
                            <td>${detail.prixUnitaire} Ar</td>
                            <td>${detail.quantite}</td>
                            <td>${detail.montant} Ar</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        
        <!-- Montant total -->
        <div class="note-finale-container">
            <div class="note-finale-label">Montant Total</div>
            <div class="note-finale-valeur">${devis.montantTotal} Ar</div>
        </div>
        
        <!-- Boutons d'action -->
        <div class="nav-links">
            <form action="${pageContext.request.contextPath}/devis/changerStatut" method="post" style="display: inline;">
                <input type="hidden" name="devisId" value="${devis.id}" />
                <c:choose>
                    <c:when test="${devis.typeDevis.libelle == 'Etude'}">
                        <button type="submit" name="statutId" value="3" class="btn btn-success">Accepter Devis Etude</button>
                        <button type="submit" name="statutId" value="4" class="btn btn-danger">Refuser Devis Etude</button>
                    </c:when>
                    <c:when test="${devis.typeDevis.libelle == 'Forage'}">
                        <button type="submit" name="statutId" value="9" class="btn btn-success">Accepter Devis Forage</button>
                        <button type="submit" name="statutId" value="10" class="btn btn-danger">Refuser Devis Forage</button>
                    </c:when>
                </c:choose>
            </form>
            <a href="${pageContext.request.contextPath}/devis/liste" class="btn btn-secondary">Retour a la liste</a>
        </div>
    </div>
</body>
</html>