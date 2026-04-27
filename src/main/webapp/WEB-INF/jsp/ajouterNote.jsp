<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ajouter une Note</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <div class="etu-number"><span class="etu-prefix">ETU</span><span class="etu-id">003647</span></div>
        <h1>➕ Ajouter une Note</h1>
        
        <form action="/notes/sauvegarder" method="post">
            <div class="form-group">
                <label for="candidatId">👤 Candidat:</label>
                <select name="candidatId" id="candidatId" required>
                    <option value="">-- Choisir un candidat --</option>
                    <c:forEach var="candidat" items="${candidats}">
                        <option value="${candidat.idCandidat}">${candidat.idCandidat} - ${candidat.nom}}}</option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="form-group">
                <label for="matiereId">📚 Matière:</label>
                <select name="matiereId" id="matiereId" required>
                    <option value="">-- Choisir une matière --</option>
                    <c:forEach var="matiere" items="${matieres}">
                        <option value="${matiere.idMatiere}">${matiere.idMatiere} - ${matiere.nom}}}</option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="form-group">
                <label for="correcteurId">✏️ Correcteur:</label>
                <select name="correcteurId" id="correcteurId" required>
                    <option value="">-- Choisir un correcteur --</option>
                    <c:forEach var="correcteur" items="${correcteurs}">
                        <option value="${correcteur.idCorrecteur}">${correcteur.idCorrecteur} - ${correcteur.nom}</option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="form-group">
                <label for="note">📝 Note:</label>
                <input type="number" name="note" id="note" step="0.01" min="0" max="20" required>
            </div>
            
            <div class="nav-links">
                <button type="submit" class="btn btn-success">💾 Sauvegarder</button>
                <a href="/notes" class="btn btn-secondary">⬅ Retour</a>
            </div>
        </form>
    </div>
</body>
</html>
