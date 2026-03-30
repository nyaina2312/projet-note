<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:if test="${not empty devis && devis.id > 0}">Modifier un Devis</c:if><c:if test="${empty devis || empty devis.id}">Ajouter un Devis</c:if></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function() {
            // Changer les détails de la demande quand on sélectionne une demande
            $('#demandeSelect').change(function() {
                var demandeId = $(this).val();
                if (demandeId) {
                    $.get('${pageContext.request.contextPath}/devis/ajax/demande/' + demandeId, function(data) {
                        if (data) {
                            var detailsHtml = '<div class="demande-details" style="display:block;">';
                            detailsHtml += '<p><span class="info-label">ID:</span> ' + data.id + '</p>';
                            detailsHtml += '<p><span class="info-label">Date:</span> ' + data.date + '</p>';
                            detailsHtml += '<p><span class="info-label">Lieu:</span> ' + data.lieu + '</p>';
                            detailsHtml += '<p><span class="info-label">District:</span> ' + data.district + '</p>';
                            detailsHtml += '<p><span class="info-label">Client:</span> ' + (data.client ? data.client.nom : 'N/A') + '</p>';
                            detailsHtml += '</div>';
                            $('#demandeDetails').html(detailsHtml);
                        } else {
                            $('#demandeDetails').html('<div class="error">Aucune information trouvée pour cette demande</div>');
                        }
                    }).fail(function() {
                        $('#demandeDetails').html('<div class="error">Erreur lors de la récupération des données</div>');
                    });
                } else {
                    $('#demandeDetails').html('');
                }
            });
            
            // Ajouter une nouvelle ligne
            $('#detailsTable').on('click', '.btn-ajouter-ligne', function() {
                var newRow = '<tr>' +
                    '<td><input type="text" name="detailLibelle" class="libelle" placeholder="Libellé"></td>' +
                    '<td><input type="text" name="detailPrixUnitaire" class="prix-unitaire" placeholder="Prix unitaire"></td>' +
                    '<td><input type="text" name="detailQuantite" class="quantite" placeholder="Qté"></td>' +
                    '<td><input type="text" name="detailMontant" class="montant" readonly placeholder="Montant"></td>' +
                    '<td><button type="button" class="btn btn-supprimer-ligne">Supprimer</button></td>' +
                    '</tr>';
                $('#detailsTableBody').append(newRow);
            });
            
            // Supprimer une ligne
            $('#detailsTable').on('click', '.btn-supprimer-ligne', function() {
                $(this).closest('tr').remove();
                calculerMontantTotal();
            });
            
            // Calculer le montant quand on change le prix ou la quantité
            $('#detailsTable').on('input', '.prix-unitaire, .quantite', function() {
                var row = $(this).closest('tr');
                var prix = parseFloat(row.find('.prix-unitaire').val().replace(',', '.')) || 0;
                var qte = parseInt(row.find('.quantite').val()) || 0;
                var montant = prix * qte;
                row.find('.montant').val(montant.toFixed(2));
                calculerMontantTotal();
            });
            
            // Calculer le montant total
            function calculerMontantTotal() {
                var total = 0;
                $('#detailsTableBody tr').each(function() {
                    var montant = parseFloat($(this).find('.montant').val()) || 0;
                    total += montant;
                });
                $('#montantTotal').text(total.toFixed(2) + ' Ar');
            }
        });
    </script>
</head>
<body>
    <div class="container">
        <h1><c:if test="${not empty devis && devis.id > 0}">Modifier un Devis</c:if><c:if test="${empty devis || empty devis.id}">Ajouter un Devis</c:if></h1>
        
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/devis/enregistrer" method="post">
            <c:if test="${not empty devis && devis.id > 0}">
                <input type="hidden" name="devisId" value="${devis.id}">
            </c:if>
            
            <!-- Sélection de la demande -->
            <div class="form-group">
                <label for="demandeSelect">Sélectionner une Demande:</label>
                <select id="demandeSelect" name="demandeId" required onchange="loadDemandeDetails(this.value)">
                    <option value="">-- Sélectionner une demande --</option>
                    <c:forEach var="demande" items="${demandes}">
                        <option value="${demande.id}" <c:if test="${not empty devis && devis.demande.id == demande.id}">selected</c:if>>Demande #${demande.id} - ${demande.client.nom} - ${demande.lieu}</option>
                    </c:forEach>
                </select>
            </div>
            
            <!-- Détails de la demande (affichage AJAX) -->
            <div id="demandeDetails"></div>
            
            <!-- Type de devis -->
            <div class="form-group">
                <label for="typeDevisSelect">Type de Devis:</label>
                <select id="typeDevisSelect" name="typeDevisId" required>
                    <option value="">-- Sélectionner le type --</option>
                    <c:forEach var="typeDevis" items="${typeDevisList}">
                        <c:if test="${typeDevis.libelle == 'Etude' || typeDevis.libelle == 'Forage'}">
                            <option value="${typeDevis.id}" <c:if test="${not empty devis && devis.typeDevis.id == typeDevis.id}">selected</c:if>>${typeDevis.libelle}</option>
                        </c:if>
                    </c:forEach>
                </select>
            </div>
            
            <!-- Tableau des détails du devis -->
            <h3>Détails du Devis</h3>
            <table id="detailsTable">
                <thead>
                    <tr>
                        <th>Libellé</th>
                        <th>Prix Unitaire</th>
                        <th>Quantité</th>
                        <th>Montant</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody id="detailsTableBody">
                    <tr>
                        <td><input type="text" name="detailLibelle" class="libelle" placeholder="Libellé"></td>
                        <td><input type="text" name="detailPrixUnitaire" class="prix-unitaire" placeholder="Prix unitaire"></td>
                        <td><input type="text" name="detailQuantite" class="quantite" placeholder="Qté"></td>
                        <td><input type="text" name="detailMontant" class="montant" readonly placeholder="Montant"></td>
                        <td><button type="button" class="btn btn-supprimer-ligne">Supprimer</button></td>
                    </tr>
                </tbody>
            </table>
            
            <button type="button" class="btn btn-ajouter-ligne" id="addLineBtn">+ Ajouter une ligne</button>
            
            <!-- Montant total -->
            <div class="montant-total">
                Montant Total: <span id="montantTotal">0.00 Ar</span>
            </div>
            
            <!-- Boutons -->
            <div style="margin-top: 20px;">
                <button type="submit" class="btn btn-primary">
                    <c:if test="${not empty devis && devis.id > 0}">Modifier le Devis</c:if>
                    <c:if test="${empty devis || empty devis.id}">Enregistrer le Devis</c:if>
                </button>
                <a href="${pageContext.request.contextPath}/devis/liste" class="btn btn-secondary">Annuler</a>
            </div>
        </form>
    </div>
    
    <script>
        // Ajouter une nouvelle ligne au chargement
        document.getElementById('addLineBtn').addEventListener('click', function() {
            var newRow = '<tr>' +
                '<td><input type="text" name="detailLibelle" class="libelle" placeholder="Libellé"></td>' +
                '<td><input type="text" name="detailPrixUnitaire" class="prix-unitaire" placeholder="Prix unitaire"></td>' +
                '<td><input type="text" name="detailQuantite" class="quantite" placeholder="Qté"></td>' +
                '<td><input type="text" name="detailMontant" class="montant" readonly placeholder="Montant"></td>' +
                '<td><button type="button" class="btn btn-supprimer-ligne" onclick="this.closest(\'tr\').remove(); calculerMontantTotal();">Supprimer</button></td>' +
                '</tr>';
            document.getElementById('detailsTableBody').insertRow().innerHTML = newRow;
        });
    </script>
</body>
</html>