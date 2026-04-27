<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
            <p><strong>Client:</strong> <c:if test="${demande.client != null}"><a href="${pageContext.request.contextPath}/clients/${demande.client.id}" class="link-primary">${demande.client.nom}</a></c:if></p>
            <p><strong>Contact Client:</strong> <c:if test="${demande.client != null}">${demande.client.contact}</c:if></p>
        </div>
        
        <div class="statut-card">
            <h2>Statut Actuel</h2>
            <p class="statut-actuel">
                <small>Changer le statut :</small>
                <form action="${pageContext.request.contextPath}/demandes/changerStatut" method="post" style="display: inline;">
                    <input type="hidden" name="id" value="${demande.id}" />
                    <select name="statutId" style="width: auto;">
                        <c:forEach var="s" items="${statuts}">
                            <option value="${s.id}">${s.libelle}</option>
                        </c:forEach>
                    </select>
                    <input type="datetime-local" name="dateChangement" style="width: auto;" title="Date du changement (optionnel, sinon heure actuelle)">
                    <button type="submit" class="btn btn-sm btn-primary">Appliquer</button>
                </form>
            </p>
            <p class="statut-actuel">
                <small>Changer le statut :</small>
                <form action="${pageContext.request.contextPath}/demandes/changerStatut" method="post" style="display: inline;">
                    <input type="hidden" name="id" value="${demande.id}" />
                    <select name="statutId" style="width: auto;">
                        <c:forEach var="s" items="${statuts}">
                            <option value="${s.id}">${s.libelle}</option>
                        </c:forEach>
                    </select>
                    <button type="submit" class="btn btn-sm btn-primary">Appliquer</button>
                </form>
            </div>
        </div>

        <c:if test="${not empty alertes}">
            <div class="alert alert-danger" style="margin-top: 20px;">
                <h3>⚠️ Alertes de dépassement de délai</h3>
                <c:forEach var="alerte" items="${alertes}">
                    <div style="margin-bottom: 10px; padding: 10px; background: #fff3cd; border-left: 4px solid #ff9800;">
                        <strong>${alerte.typeAlerte}</strong><br/>
                        ${alerte.message}<br/>
                        <small><fmt:formatDate value="${alerte.dateCreation}" pattern="dd/MM/yyyy HH:mm" /></small>
                        <form action="${pageContext.request.contextPath}/alertes/marquerLue/${alerte.id}" method="post" style="display:inline;">
                            <button type="submit" class="btn btn-sm btn-secondary" style="margin-left: 10px;">Marquer comme lue</button>
                        </form>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <div class="historique-card">
            <h2>Actions sur la Demande</h2>
            <div style="margin-bottom: 20px;">
                <form action="${pageContext.request.contextPath}/demandes/action" method="post" style="display: inline;">
                    <input type="hidden" name="id" value="${demande.id}" />
                    <input type="hidden" name="action" value="confirmer" />
                    <input type="text" name="observation" placeholder="Justification pour confirmation" style="width: 250px;" required />
                    <button type="submit" class="btn btn-sm btn-success">Confirmer</button>
                </form>

                <form action="${pageContext.request.contextPath}/demandes/action" method="post" style="display: inline; margin-left: 10px;">
                    <input type="hidden" name="id" value="${demande.id}" />
                    <input type="hidden" name="action" value="annuler" />
                    <input type="text" name="observation" placeholder="Justification pour annulation" style="width: 250px;" required />
                    <button type="submit" class="btn btn-sm btn-warning">Annuler</button>
                </form>

                <form action="${pageContext.request.contextPath}/demandes/action" method="post" style="display: inline; margin-left: 10px;">
                    <input type="hidden" name="id" value="${demande.id}" />
                    <input type="hidden" name="action" value="supprimer" />
                    <input type="text" name="observation" placeholder="Justification pour suppression" style="width: 250px;" required />
                    <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette demande ?')">Supprimer</button>
                </form>

                <form action="${pageContext.request.contextPath}/demandes/action" method="post" style="display: inline; margin-left: 10px;">
                    <input type="hidden" name="id" value="${demande.id}" />
                    <input type="hidden" name="action" value="modifier" />
                    <input type="text" name="observation" placeholder="Justification pour modification" style="width: 250px;" required />
                    <button type="submit" class="btn btn-sm btn-primary">Modifier</button>
                </form>
            </div>

            <h2>Historique des Statuts</h2>
            <p>Nombre d'entrées: ${historique.size()}</p>
            <table class="table">
                <thead>
                    <tr>
                        <th>Date de changement</th>
                        <th>Statut</th>
                        <th>Observation</th>
                        <th>Durée de changement</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="h" items="${historique}" varStatus="status">
                        <c:set var="dateFormatee">
                            <fmt:formatDate value="${h.dateChangement}" pattern="yyyy-MM-dd'T'HH:mm" />
                        </c:set>
                        <tr>
                            <td><fmt:formatDate value="${h.dateChangement}" pattern="dd/MM/yyyy HH:mm" /></td>
                            <td>${h.statut.libelle}</td>
                            <td>${h.observation}</td>
                            <td>
                                <c:if test="${h.dureeTotaleHeures != null && h.dureeOuvrableHeures != null}">
                                    ${h.dureeTotaleFormatee} (cal) / ${h.dureeOuvrableFormatee} (ouv)
                                </c:if>
                            </td>
                            <td>
                                <button type="button" class="btn btn-sm" onclick="modifierHistorique(${h.id}, '${fn:escapeXml(dateFormatee)}', '${fn:escapeXml(h.observation)}')">Modifier</button>
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
    function modifierHistorique(id, dateChangement, observation) {
        console.log("modifierHistorique appelé:", id, dateChangement, observation);
        document.getElementById('historiqueId').value = id;
        
        // Convertir la date en format datetime-local (YYYY-MM-DDTHH:MM)
        if (dateChangement) {
            try {
                let date = new Date(dateChangement);
                let year = date.getFullYear();
                let month = String(date.getMonth() + 1).padStart(2, '0');
                let day = String(date.getDate()).padStart(2, '0');
                let hours = String(date.getHours()).padStart(2, '0');
                let minutes = String(date.getMinutes()).padStart(2, '0');
                document.getElementById('dateChangementInput').value = `${year}-${month}-${day}T${hours}:${minutes}`;
            } catch (e) {
                console.error("Erreur parsing date:", e);
                document.getElementById('dateChangementInput').value = "";
            }
        }
        
        document.getElementById('observationInput').value = observation || "";
        document.getElementById('modifierModal').style.display = 'block';
    }

    function fermerModal() {
        document.getElementById('modifierModal').style.display = 'none';
    }

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
