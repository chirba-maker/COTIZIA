<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="fr">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>${collecteurEdit != null ? 'Modifier' : 'Nouveau'} Collecteur - Cotizia</title>
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
                            <h2>${collecteurEdit != null ? 'Modifier' : 'Nouveau'} Collecteur</h2>
                            <a href="${pageContext.request.contextPath}/collecteurs"
                                class="btn btn-outline-secondary"><i class="bi bi-arrow-left me-2"></i>
                                Retour</a>
                        </div>

                        <div class="card p-4">
                            <form action="${pageContext.request.contextPath}/collecteurs" method="post">
                                <input type="hidden" name="action"
                                    value="${collecteurEdit != null ? 'update' : 'save'}">
                                <input type="hidden" name="csrfToken" value="${csrfToken}">
                                <c:if test="${collecteurEdit != null}">
                                    <input type="hidden" name="id" value="${collecteurEdit.idCollecteur}">
                                </c:if>

                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <label for="id_utilisateur" class="form-label">Compte Utilisateur
                                            Associé</label>
                                        <select class="form-select" id="id_utilisateur" name="id_utilisateur" required>
                                            <option value="">Sélectionner un utilisateur</option>
                                            <c:forEach var="u" items="${listUser}">
                                                <c:if test="${u.role == 'COLLECTEUR'}">
                                                    <option value="${u.idUtilisateur}"
                                                        ${collecteurEdit.idUtilisateur==u.idUtilisateur ? 'selected'
                                                        : '' }>
                                                        ${u.nomComplet} (${u.login})
                                                    </option>
                                                </c:if>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="statut" class="form-label">Statut</label>
                                        <select class="form-select" id="statut" name="statut" required>
                                            <option value="actif" ${collecteurEdit.statut=='ACTIF' ? 'selected' : '' }>
                                                Actif
                                            </option>
                                            <option value="inactif" ${collecteurEdit.statut=='INACTIF' ? 'selected' : ''
                                                }>
                                                Inactif</option>
                                        </select>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="nom" class="form-label">Nom</label>
                                        <input type="text" class="form-control" id="nom" name="nom"
                                            value="${collecteurEdit.nom}" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="prenom" class="form-label">Prénom</label>
                                        <input type="text" class="form-control" id="prenom" name="prenom"
                                            value="${collecteurEdit.prenom}" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="telephone" class="form-label">Téléphone</label>
                                        <input type="text" class="form-control" id="telephone" name="telephone"
                                            value="${collecteurEdit.telephone}" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="email" class="form-label">Email</label>
                                        <input type="email" class="form-control" id="email" name="email"
                                            value="${collecteurEdit.email}">
                                    </div>
                                    <div class="col-md-12">
                                        <label for="zone_collecte" class="form-label">Zone de Collecte</label>
                                        <input type="text" class="form-control" id="zone_collecte" name="zone_collecte"
                                            value="${collecteurEdit.zoneCollecte}"
                                            placeholder="Ex: Marché Madina, Kaloum...">
                                    </div>
                                </div>

                                <div class="mt-4">
                                    <button type="submit" class="btn btn-primary px-4">Enregistrer</button>
                                    <button type="reset" class="btn btn-light px-4">Réinitialiser</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>