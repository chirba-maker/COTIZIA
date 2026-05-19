<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="fr">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>${adherentEdit != null ? 'Modifier' : 'Nouvel'} Adhérent - Cotizia</title>
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
                            <h2>${adherentEdit != null ? 'Modifier' : 'Nouvel'} Adhérent</h2>
                            <a href="${pageContext.request.contextPath}/adherents" class="btn btn-outline-secondary"><i
                                    class="bi bi-arrow-left me-2"></i>
                                Retour</a>
                        </div>

                        <div class="card p-4">
                            <c:if test="${not empty sessionScope.error}">
                                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                    ${sessionScope.error}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert"
                                        aria-label="Close"></button>
                                </div>
                                <c:remove var="error" scope="session" />
                            </c:if>

                            <form action="${pageContext.request.contextPath}/adherents" method="post">
                                <input type="hidden" name="action" value="${adherentEdit != null ? 'update' : 'save'}">
                                <input type="hidden" name="csrfToken" value="${csrfToken}">
                                <c:if test="${adherentEdit != null}">
                                    <input type="hidden" name="id" value="${adherentEdit.idAdherent}">
                                </c:if>

                                <div class="row g-3">
                                    <div class="col-md-4">
                                        <label for="numero_identification" class="form-label">N° Identification</label>
                                        <input type="text" class="form-control" id="numero_identification"
                                            name="numero_identification" value="${adherentEdit.numeroIdentification}"
                                            required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="nom" class="form-label">Nom</label>
                                        <input type="text" class="form-control" id="nom" name="nom"
                                            value="${adherentEdit.nom}" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="prenom" class="form-label">Prénom</label>
                                        <input type="text" class="form-control" id="prenom" name="prenom"
                                            value="${adherentEdit.prenom}" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="date_naissance" class="form-label">Date de Naissance</label>
                                        <input type="date" class="form-control" id="date_naissance"
                                            name="date_naissance" value="${adherentEdit.dateNaissance}" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="telephone" class="form-label">Téléphone</label>
                                        <input type="text" class="form-control" id="telephone" name="telephone"
                                            value="${adherentEdit.telephone}" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="email" class="form-label">Email</label>
                                        <input type="email" class="form-control" id="email" name="email"
                                            value="${adherentEdit.email}">
                                    </div>
                                    <div class="col-md-12">
                                        <label for="adresse" class="form-label">Adresse</label>
                                        <textarea class="form-control" id="adresse" name="adresse"
                                            rows="2">${adherentEdit.adresse}</textarea>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="profession" class="form-label">Profession</label>
                                        <input type="text" class="form-control" id="profession" name="profession"
                                            value="${adherentEdit.profession}">
                                    </div>
                                    <div class="col-md-6">
                                        <label for="employeur" class="form-label">Employeur</label>
                                        <input type="text" class="form-control" id="employeur" name="employeur"
                                            value="${adherentEdit.employeur}">
                                    </div>
                                    <div class="col-md-6">
                                        <label for="revenus_estimes" class="form-label">Revenus Estimés (GNF)</label>
                                        <input type="number" step="0.01" class="form-control" id="revenus_estimes"
                                            name="revenus_estimes" value="${adherentEdit.revenusEstimes}">
                                    </div>
                                    <div class="col-md-6">
                                        <label for="statut" class="form-label">Statut</label>
                                        <select class="form-select" id="statut" name="statut" required>
                                            <option value="actif" ${adherentEdit.statut=='ACTIF' ? 'selected' : '' }>
                                                Actif
                                            </option>
                                            <option value="suspendu" ${adherentEdit.statut=='SUSPENDU' ? 'selected' : ''
                                                }>
                                                Suspendu</option>
                                            <option value="radie" ${adherentEdit.statut=='RADIE' ? 'selected' : '' }>
                                                Radié
                                            </option>
                                        </select>
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