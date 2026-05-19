<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="fr">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>${userEdit != null ? 'Modifier' : 'Nouvel'} Utilisateur - Cotizia</title>
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
                            <h2>${userEdit != null ? 'Modifier' : 'Nouvel'} Utilisateur</h2>
                            <a href="${pageContext.request.contextPath}/utilisateurs"
                                class="btn btn-outline-secondary"><i class="bi bi-arrow-left me-2"></i>
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
                            <form action="${pageContext.request.contextPath}/utilisateurs" method="post">
                                <input type="hidden" name="action" value="${userEdit != null ? 'update' : 'save'}">
                                <input type="hidden" name="csrfToken" value="${csrfToken}">
                                <c:if test="${userEdit != null}">
                                    <input type="hidden" name="id" value="${userEdit.idUtilisateur}">
                                </c:if>

                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <label for="nom" class="form-label">Nom</label>
                                        <input type="text" class="form-control" id="nom" name="nom"
                                            value="${userEdit.nom}" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="prenom" class="form-label">Prénom</label>
                                        <input type="text" class="form-control" id="prenom" name="prenom"
                                            value="${userEdit.prenom}" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="login" class="form-label">Login</label>
                                        <input type="text" class="form-control" id="login" name="login"
                                            value="${userEdit.login}" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="email" class="form-label">Email</label>
                                        <input type="email" class="form-control" id="email" name="email"
                                            value="${userEdit.email}" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="password" class="form-label">Mot de passe ${userEdit != null ?
                                            '(laisser
                                            vide pour ne pas changer)' : ''}</label>
                                        <input type="password" class="form-control" id="password" name="password"
                                            ${userEdit !=null ? '' : 'required' }>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="role" class="form-label">Rôle</label>
                                        <select class="form-select" id="role" name="role" required>
                                            <option value="admin" ${userEdit.role=='ADMIN' ? 'selected' : '' }>
                                                Administrateur</option>
                                            <option value="collecteur" ${userEdit.role=='COLLECTEUR' ? 'selected' : ''
                                                }>
                                                Collecteur</option>
                                            <option value="consultant" ${userEdit.role=='CONSULTANT' ? 'selected' : ''
                                                }>
                                                Consultant</option>
                                        </select>
                                    </div>
                                    <c:if test="${userEdit != null}">
                                        <div class="col-md-6 d-flex align-items-end">
                                            <div class="form-check form-switch mb-2">
                                                <input class="form-check-input" type="checkbox" id="actif" name="actif"
                                                    ${userEdit.actif ? 'checked' : '' }>
                                                <label class="form-check-input-label" for="actif">Compte actif</label>
                                            </div>
                                        </div>
                                    </c:if>
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