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
        <h1>➕ Ajouter une Note</h1>
        
        <form action="/notes/sauvegarder" method="post">
            <div class="form-group">
                <label for="candidat">👤 Candidat:</label>
                <select name="candidat.idCandidat" id="candidat" required>
                    <option value="">-- Choisir un candidat --</option>
                    <c:forEach var="candidat" items="${candidats}">
                        <option value="${candidat.idCandidat}">${candidat.idCandidat} - ${candidat.nom}</option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="form-group">
                <label for="matiere">📚 Matière:</label>
                <select name="matiere.idMatiere" id="matiere" required>
                    <option value="">-- Choisir une matière --</option>
                    <c:forEach var="matiere" items="${matieres}">
                        <option value="${matiere.idMatiere}">${matiere.idMatiere} - ${matiere.nom}</option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="form-group">
                <label for="correcteur">✏️ Correcteur:</label>
                <select name="correcteur.idCorrecteur" id="correcteur" required>
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
