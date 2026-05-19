<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="fr">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Utilisateurs - Cotizia</title>
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
                            <h2>Gestion des Utilisateurs</h2>
                            <a href="utilisateurs?action=create" class="btn btn-primary"><i
                                    class="bi bi-plus-lg me-2"></i>
                                Nouvel Utilisateur</a>
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
                            <c:if test="${not empty sessionScope.message}">
                                <div class="alert alert-success alert-dismissible fade show" role="alert">
                                    <i class="bi bi-check-circle-fill me-2"></i>
                                    ${sessionScope.message}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert"
                                        aria-label="Close"></button>
                                </div>
                                <c:remove var="message" scope="session" />
                            </c:if>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Nom Complet</th>
                                            <th>Login</th>
                                            <th>Email</th>
                                            <th>Rôle</th>
                                            <th>Statut</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="u" items="${listUser}">
                                            <tr>
                                                <td>${u.nomComplet}</td>
                                                <td>${u.login}</td>
                                                <td>${u.email}</td>
                                                <td>
                                                    <span
                                                        class="badge badge-${u.role.name().toLowerCase()} text-capitalize">
                                                        ${u.role.name().toLowerCase()}
                                                    </span>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${u.actif}">
                                                            <span class="text-success"><i
                                                                    class="bi bi-check-circle-fill me-1"></i>
                                                                Actif</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-danger"><i
                                                                    class="bi bi-x-circle-fill me-1"></i>
                                                                Inactif</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <a href="utilisateurs?action=edit&id=${u.idUtilisateur}"
                                                        class="btn btn-sm btn-outline-primary me-1" title="Modifier">
                                                        <i class="bi bi-pencil"></i>
                                                    </a>
                                                    <button type="button" class="btn btn-sm btn-outline-danger"
                                                        title="Supprimer"
                                                        onclick="confirmDelete('utilisateurs?action=delete&id=${u.idUtilisateur}', '${u.nomComplet}', 'Supprimer définitivement', 'bi-person-x')">
                                                        <i class="bi bi-trash"></i>
                                                    </button>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
        </body>

        </html>