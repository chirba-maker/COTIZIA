<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="fr">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Ajouter Participant - ${cycle.libelle}</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
            <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap"
                rel="stylesheet">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
        </head>

        <body>

            <%@ include file="/WEB-INF/views/fragments/sidebar.jspf" %>

                <div class="content">
                    <div class="container-fluid">
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <h2>Gérer les Participants</h2>
                            <a href="${pageContext.request.contextPath}/cycles" class="btn btn-outline-secondary"><i
                                    class="bi bi-arrow-left me-2"></i> Retour aux Cycles</a>
                        </div>

                        <div class="card p-4">
                            <form action="${pageContext.request.contextPath}/participant" method="post">
                                <input type="hidden" name="action" value="save">
                                <input type="hidden" name="csrfToken" value="${csrfToken}">
                                <input type="hidden" name="idCycle" value="${cycle.idCycle}">

                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <label for="id_adherent" class="form-label">Sélectionner l'Adhérent</label>
                                        <select class="form-select select2" id="id_adherent" name="id_adherent"
                                            required>
                                            <option value="">Chercher un adhérent...</option>
                                            <c:forEach var="a" items="${listAdherent}">
                                                <option value="${a.idAdherent}">${a.nomComplet}
                                                    (${a.numeroIdentification})
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="numero_ordre" class="form-label">Numéro d'Ordre (Tour de
                                            réception)</label>
                                        <input type="number" class="form-control" id="numero_ordre" name="numero_ordre"
                                            required min="1" max="${cycle.nombreTours}">
                                    </div>
                                </div>

                                <div class="mt-4">
                                    <button type="submit" class="btn btn-primary px-4">Ajouter au Cycle</button>
                                    <button type="reset" class="btn btn-light px-4">Réinitialiser</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>