<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="fr">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Cycles de Cotisation - Cotizia</title>
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
                            <h2>Gestion des Cycles</h2>
                            <c:if test="${user.role != 'CONSULTANT'}">
                                <a href="${pageContext.request.contextPath}/cycles?action=create"
                                    class="btn btn-primary"><i class="bi bi-plus-circle me-2"></i>
                                    Nouveau Cycle</a>
                            </c:if>
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
                                            <th>Libellé</th>
                                            <th>Montant</th>
                                            <th>Fréquence</th>
                                            <th>Date Début</th>
                                            <th>Tours</th>
                                            <th>Statut</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="c" items="${listCycle}">
                                            <tr>
                                                <td>${c.libelle}</td>
                                                <td>${c.montantCotisation} GNF</td>
                                                <td><span
                                                        class="text-capitalize">${c.frequence.name().toLowerCase()}</span>
                                                </td>
                                                <td>${c.dateDebut}</td>
                                                <td>${c.nombreTours}</td>
                                                <td>
                                                    <c:set var="statusClass" value="secondary" />
                                                    <c:choose>
                                                        <c:when test="${c.statut == 'ACTIF'}">
                                                            <c:set var="statusClass" value="success" />
                                                        </c:when>
                                                        <c:when test="${c.statut == 'CLOTURE'}">
                                                            <c:set var="statusClass" value="dark" />
                                                        </c:when>
                                                    </c:choose>
                                                    <span
                                                        class="badge rounded-pill bg-${statusClass} px-3 text-capitalize">
                                                        <i class="bi bi-circle-fill me-1 small"></i> ${c.statut}
                                                    </span>
                                                </td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/participant?idCycle=${c.idCycle}"
                                                        class="btn btn-sm btn-outline-info me-1"
                                                        title="Gestion des Participants">
                                                        <i class="bi bi-people"></i>
                                                    </a>
                                                    <c:if test="${user.role != 'CONSULTANT'}">
                                                        <a href="${pageContext.request.contextPath}/cycles?action=edit&id=${c.idCycle}"
                                                            class="btn btn-sm btn-outline-primary me-1"
                                                            title="Modifier">
                                                            <i class="bi bi-pencil"></i>
                                                        </a>
                                                        <button type="button" class="btn btn-sm btn-outline-danger"
                                                            title="Supprimer ce cycle"
                                                            onclick="confirmDelete('${pageContext.request.contextPath}/cycles?action=delete&id=${c.idCycle}', '${c.libelle}', 'Supprimer ce cycle', 'bi-trash')">
                                                            <i class="bi bi-trash"></i>
                                                        </button>
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