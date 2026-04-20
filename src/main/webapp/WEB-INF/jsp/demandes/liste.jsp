<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des Demandes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <div class="etu-number"><span class="etu-prefix">ETU</span><span class="etu-id">003647</span></div>
        <h1>Liste des Demandes de Forage</h1>
        
        <a href="/demandes/ajouter" class="btn btn-primary">Ajouter une Demande</a>
        
        <table class="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Date</th>
                    <th>Lieu</th>
                    <th>District</th>
                    <th>Client</th>
                    <th>Statut</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="demande" items="${demandes}">
                    <tr>
                        <td>${demande.id}</td>
                        <td><fmt:formatDate value="${demande.date}" pattern="dd/MM/yyyy" /></td>
                        <td>${demande.lieu}</td>
                        <td>${demande.district}</td>
                        <td>
                            <c:if test="${demande.client != null}">
                                ${demande.client.nom}
                            </c:if>
                        </td>
                        <td>
                            <c:set var="statut" value="${statutsMap[demande.id]}" />
                            <c:if test="${statut != null}">
                                <span class="badge badge-primary">${statut.libelle}</span>
                            </c:if>
                            <c:if test="${statut == null}">
                                <span class="badge badge-warning">En attente</span>
                            </c:if>
                        </td>
                        <td>
                            <a href="/demandes/voir?id=${demande.id}" class="btn btn-sm">Voir</a>
                            <a href="/demandes/modifier?id=${demande.id}" class="btn btn-sm">Modifier</a>
                            <button type="button" class="btn btn-sm" onclick="changerStatutAvecObservation(${demande.id}, 2, 'Validé')">Confirmer</button>
                            <button type="button" class="btn btn-sm" onclick="changerStatutAvecObservation(${demande.id}, 5, 'Terminé')">Terminé</button>
                            <button type="button" class="btn btn-sm btn-danger" onclick="supprimerDemande(${demande.id})">Supprimer</button>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <a href="/" class="btn btn-secondary">Retour à l'accueil</a>
    </div>
    
    <form id="changerStatutForm" method="POST" action="/demandes/changerStatut" style="display:none;">
        <input type="hidden" name="id" id="demandeId">
        <input type="hidden" name="statutId" id="statutId">
        <input type="hidden" name="observation" id="observation">
    </form>
    
    <form id="supprimerForm" method="POST" action="/demandes/supprimer" style="display:none;">
        <input type="hidden" name="id" id="supprimerDemandeId">
    </form>
    
    <script>
    function changerStatutAvecObservation(id, statutId, actionParDefaut) {
        var observation = prompt(' observation (justification):', actionParDefaut);
        if (observation == null || observation.trim() == '') {
            observation = actionParDefaut;
        }
        document.getElementById('demandeId').value = id;
        document.getElementById('statutId').value = statutId;
        document.getElementById('observation').value = observation;
        console.log("Submitting - id: " + id + ", statutId: " + statutId + ", observation: " + observation);
        document.getElementById('changerStatutForm').submit();
    }
    
    function supprimerDemande(id) {
        if (confirm('Êtes-vous sûr de vouloir supprimer cette demande ?')) {
            document.getElementById('supprimerDemandeId').value = id;
            document.getElementById('supprimerForm').submit();
        }
    }
    </script>
</body>
</html>
