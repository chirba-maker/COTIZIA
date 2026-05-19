<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="fr">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Collecteurs - Cotizia</title>
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
                            <h2>Gestion des Collecteurs</h2>
                            <a href="${pageContext.request.contextPath}/collecteurs?action=create"
                                class="btn btn-primary"><i class="bi bi-person-plus me-2"></i>
                                Nouveau Collecteur</a>
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
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Nom Complet</th>
                                            <th>Téléphone</th>
                                            <th>Email</th>
                                            <th>Zone de Collecte</th>
                                            <th>Statut</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="c" items="${listCollecteur}">
                                            <tr>
                                                <td>${c.nomComplet}</td>
                                                <td>${c.telephone}</td>
                                                <td>${c.email}</td>
                                                <td>${c.zoneCollecte}</td>
                                                <td>
                                                    <c:set var="statusClass" value="secondary" />
                                                    <c:choose>
                                                        <c:when test="${c.statut == 'ACTIF'}">
                                                            <c:set var="statusClass" value="success" />
                                                        </c:when>
                                                        <c:when test="${c.statut == 'INACTIF'}">
                                                            <c:set var="statusClass" value="danger" />
                                                        </c:when>
                                                    </c:choose>
                                                    <span
                                                        class="badge rounded-pill bg-${statusClass} px-3 text-capitalize">
                                                        <i class="bi bi-circle-fill me-1 small"></i> ${c.statut}
                                                    </span>
                                                </td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/collecteurs?action=edit&id=${c.idCollecteur}"
                                                        class="btn btn-sm btn-outline-primary me-1" title="Modifier">
                                                        <i class="bi bi-pencil"></i>
                                                    </a>
                                                    <button type="button" class="btn btn-sm btn-outline-danger"
                                                        title="Désactiver ce collecteur"
                                                        onclick="confirmDelete('${pageContext.request.contextPath}/collecteurs?action=delete&id=${c.idCollecteur}', '${c.nomComplet}', 'Désactiver le collecteur', 'bi-person-x')">
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