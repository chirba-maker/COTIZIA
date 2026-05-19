<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="fr">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Adhérents - Cotizia</title>
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
                            <h2>Gestion des Adhérents</h2>
                            <c:if test="${user.role == 'COLLECTEUR'}">
                                <a href="${pageContext.request.contextPath}/adherents?action=create"
                                    class="btn btn-primary"><i class="bi bi-person-plus me-2"></i>
                                    Nouvel Adhérent</a>
                            </c:if>
                        </div>

                        <div class="row mb-4">
                            <div class="col-md-6">
                                <form action="${pageContext.request.contextPath}/adherents" method="get"
                                    class="d-flex shadow-sm rounded overflow-hidden">
                                    <input type="text" name="query" class="form-control border-0 px-3 py-2"
                                        placeholder="Rechercher par nom, ID ou téléphone..." value="${searchQuery}"
                                        style="border-radius: 0;">
                                    <button type="submit" class="btn btn-primary px-4" style="border-radius: 0;">
                                        <i class="bi bi-search"></i>
                                    </button>
                                    <c:if test="${not empty searchQuery}">
                                        <a href="${pageContext.request.contextPath}/adherents"
                                            class="btn btn-outline-secondary px-3" title="Effacer la recherche"
                                            style="border-radius: 0; display: flex; align-items: center;">
                                            <i class="bi bi-x-lg"></i>
                                        </a>
                                    </c:if>
                                </form>
                                <c:if test="${not empty searchQuery}">
                                    <div class="mt-2 small text-muted">
                                        Résultats pour : <strong>"${searchQuery}"</strong>
                                    </div>
                                </c:if>
                            </div>
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
                            
                            <c:if test="${param.error == 'unauthorized'}">
                                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                    <i class="bi bi-shield-lock-fill me-2"></i>
                                    <strong>Accès Refusé :</strong> Vous ne disposez pas des droits requis pour effectuer cette action.
                                    <button type="button" class="btn-close" data-bs-alert="alert"
                                        aria-label="Close"></button>
                                </div>
                            </c:if>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>N° ID</th>
                                            <th>Nom Complet</th>
                                            <th>Téléphone</th>
                                            <th>Profession</th>
                                            <th>Statut</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="a" items="${listAdherent}">
                                            <tr>
                                                <td>${a.numeroIdentification}</td>
                                                <td>${a.nomComplet}</td>
                                                <td>${a.telephone}</td>
                                                <td>${a.profession}</td>
                                                <td>
                                                    <c:set var="statusClass" value="secondary" />
                                                    <c:choose>
                                                        <c:when test="${a.statut == 'ACTIF'}">
                                                            <c:set var="statusClass" value="success" />
                                                        </c:when>
                                                        <c:when test="${a.statut == 'SUSPENDU'}">
                                                            <c:set var="statusClass" value="warning text-dark" />
                                                        </c:when>
                                                        <c:when test="${a.statut == 'RADIE'}">
                                                            <c:set var="statusClass" value="danger" />
                                                        </c:when>
                                                    </c:choose>
                                                    <span class="badge rounded-pill bg-${statusClass} px-3">
                                                        <i class="bi bi-circle-fill me-1 small"></i> ${a.statut}
                                                    </span>
                                                </td>
                                                <td>
                                                    <c:if test="${user.role == 'COLLECTEUR'}">
                                                        <a href="${pageContext.request.contextPath}/adherents?action=edit&id=${a.idAdherent}"
                                                            class="btn btn-sm btn-outline-primary me-1"
                                                            title="Modifier">
                                                            <i class="bi bi-pencil"></i>
                                                        </a>
                                                        <button type="button" class="btn btn-sm btn-outline-danger"
                                                            title="Radier cet adhérent"
                                                            onclick="confirmDelete('${pageContext.request.contextPath}/adherents?action=delete&id=${a.idAdherent}', '${a.nomComplet}', 'Radier cet adhérent', 'bi-person-dash')">
                                                            <i class="bi bi-trash"></i>
                                                        </button>
                                                    </c:if>
                                                    <c:if test="${user.role != 'COLLECTEUR'}">
                                                        <span class="text-muted small"><i class="bi bi-eye me-1"></i> Lecture seule</span>
                                                    </c:if>
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