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
        <div class="etu-number"><span class="etu-prefix">ETU</span><span class="etu-id">003647</span></div>
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
                    <c:if test="${statutActuel.typeStatut != null}"> (${statutActuel.typeStatut})</c:if>
                </c:if>
                <c:if test="${statutActuel == null}">
                    En attente
                </c:if>
            </p>
        </div>
        
        <div class="historique-card">
            <h2>Historique des Statuts</h2>
            <p>Nombre d'entrées: ${historique.size()}</p>
            <form action="/demandes/changerStatut" method="post" style="margin-bottom: 10px;">
                <input type="hidden" name="id" value="${demande.id}" />
                <label style="display:inline; margin-right: 5px;"><strong>Changer Statut:</strong></label>
                <select name="statutId" required style="width: 150px;">
                    <option value="">Sélectionner</option>
                    <c:forEach var="statut" items="${statuts}">
                        <option value="${statut.id}">${statut.id} - ${statut.libelle}</option>
                    </c:forEach>
                </select>
                <input type="text" name="observation" placeholder="Observation" style="width: 200px;" />
                <button type="submit" class="btn btn-sm btn-primary">OK</button>
            </form>
            <table class="table">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Statut</th>
                        <th>Observation</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="h" items="${historique}" varStatus="status">
                        <tr>
                            <td><fmt:formatDate value="${h.dateChangement}" pattern="dd/MM/yyyy HH:mm" /></td>
                            <td>${h.statut.libelle} (ID=${h.statut.id})</td>
                            <td>${h.observation}</td>
                            <td>
                                <button type="button" class="btn btn-sm" onclick="modifierHistorique(${h.id}, '${h.dateChangement}', '${h.observation}')">Modifier</button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        
        <div id="modifierModal" style="display:none;">
            <div class="modal-content">
                <h3>Modifier l'historique</h3>
                <form action="/demandes/modifierHistorique" method="post">
                    <input type="hidden" name="id" id="historiqueId">
                    <input type="hidden" name="demandeId" value="${demande.id}">
                    <label>Date:</label>
                    <input type="datetime-local" name="dateChangement" id="dateChangementInput">
                    <label>Observation:</label>
                    <input type="text" name="observation" id="observationInput">
                    <button type="submit" class="btn btn-primary">Mettre à jour</button>
                    <button type="button" class="btn btn-secondary" onclick="fermerModal()">Annuler</button>
                </form>
            </div>
        </div>
        
        <a href="/demandes" class="btn btn-secondary">Retour à la liste</a>
    </div>
    
    <script>
    function modifierHistorique(id, date, observation) {
        document.getElementById('modifierModal').style.display = 'block';
        document.getElementById('historiqueId').value = id;
        
        // Formater la date pour datetime-local (yyyy-MM-ddTHH:mm)
        var dateStr = '';
        if (date) {
            var d = new Date(date);
            var year = d.getFullYear();
            var month = ('0' + (d.getMonth() + 1)).slice(-2);
            var day = ('0' + d.getDate()).slice(-2);
            var hours = ('0' + d.getHours()).slice(-2);
            var minutes = ('0' + d.getMinutes()).slice(-2);
            dateStr = year + '-' + month + '-' + day + 'T' + hours + ':' + minutes;
        }
        document.getElementById('dateChangementInput').value = dateStr;
        document.getElementById('observationInput').value = observation ? observation : '';
    }
    
    function fermerModal() {
        document.getElementById('modifierModal').style.display = 'none';
    }
    </script>
</body>
</html>
