<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <%-- Déclare la page JSP avec encodage UTF-8 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>          <%-- Importe la librairie JSTL Core (pour <c:forEach>, <c:if>) --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>         <%-- Importe la librairie JSTL Format (pour <fmt:formatNumber>) --%>
<!DOCTYPE html>                                                            <%-- Déclare le type de document HTML5 --%>
<html>                                                                     <%-- Balise racine du document HTML --%>
<head>                                                                     <%-- En-tête du document (métadonnées, titre, CSS) --%>
    <meta charset="UTF-8">                                                 <%-- Encodage des caractères en UTF-8 (accents, etc.) --%>
    <title>Chiffre d'Affaires Provisionnel</title>                        <%-- Titre affiché dans l'onglet du navigateur --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css"> <%-- Lien vers le fichier CSS du projet --%>
</head>                                                                    <%-- Fin de l'en-tête --%>
<body>                                                                     <%-- Corps du document (contenu visible) --%>
    <div class="container">                                                <%-- Conteneur principal avec le style bleu nuit --%>

        
        <div class="etu-number"><span class="etu-prefix">ETU</span><span class="etu-id">003647</span></div>


        <h1>Chiffre d'Affaires Previsionnel</h1>                          
        <div class="table-container">                                      
            <table>                                                       
                <thead>                                                   
                    <tr>                                                   
                        <th>ID Detail</th>                                 
                        <th>Devis #</th>                                  
                        <th>Libelle</th>                                   
                        <th>Prix Unitaire</th>                             
                        <th>Quantite</th>                                  
                        <th>Montant</th>                                   
                    </tr>
                </thead>
                <tbody>                                                    
                    <%-- Boucle sur chaque détail de devis envoyé par le controller --%>
                    <%-- ${allDetails} = variable mise dans le Model par DevisController.chiffreAffaires() --%>
                    <c:forEach var="detail" items="${allDetails}">          <%-- Pour chaque détail dans la liste --%>
                        <tr>                                               <%-- Ligne du tableau --%>
                            <td>${detail.id}</td>                          <%-- Affiche l'ID du détail (PK de la table details_devis) --%>
                            <td>${detail.devis.id}</td>                    <%-- Affiche l'ID du devis parent (clé étrangère) --%>
                            <td>${detail.libelle}</td>                     <%-- Affiche le libellé de la ligne (ex: "Tuyau PVC") --%>
                            <%-- Affiche le prix unitaire formaté avec séparateur de milliers --%>
                            <%-- groupingUsed="true" → 1 000 000 au lieu de 1000000 --%>
                            <td><fmt:formatNumber value="${detail.prixUnitaire}" type="number" groupingUsed="true" /> Ar</td>
                            <td>${detail.quantite}</td>                    <%-- Affiche la quantité --%>
                            <%-- Affiche le montant formaté avec séparateur de milliers --%>
                            <td><fmt:formatNumber value="${detail.montant}" type="number" groupingUsed="true" /> Ar</td>
                        </tr>
                    </c:forEach>                                           <%-- Fin de la boucle --%>

                    <%-- Si la liste est vide, affiche un message --%>
                    <c:if test="${empty allDetails}">                       <%-- Test : si la liste est vide ou null --%>
                        <tr>
                            <td colspan="6" style="text-align: center;">Aucun detail trouve</td> <%-- Message sur 6 colonnes --%>
                        </tr>
                    </c:if>
                </tbody>                                                   <%-- Fin du corps du tableau --%>
            </table>                                                       <%-- Fin du tableau --%>
        </div>

        <%-- ===== TOTAL = CHIFFRE D'AFFAIRES PREVISIONNEL ===== --%>
        <%-- ${totalCA} = SUM(montant) de tous les détails, calculé par le controller --%>
        <div class="note-finale-container">                                <%-- Bloc bleu arrondi pour afficher le total --%>
            <div class="note-finale-label">Chiffre d'Affaires Previsionnel</div> <%-- Libellé au-dessus du total --%>
            <div class="note-finale-valeur"><fmt:formatNumber value="${totalCA}" type="number" groupingUsed="true" /> Ar</div> <%-- Valeur du total formatée en grand --%>
        </div>

        <div class="nav-links">                                            
            <a href="${pageContext.request.contextPath}/devis/liste" class="btn btn-secondary">Retour aux devis</a> 
        </div>
    </div>                                                                 
</body>                                                                  
</html>                                                                  
