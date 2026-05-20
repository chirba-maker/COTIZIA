<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="fr">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Échéances - ${cycle.libelle}</title>
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
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <c:choose>
                                    <c:when test="${not empty cycle}">
                                        <li class="breadcrumb-item"><a href="cycles">Cycles</a></li>
                                        <li class="breadcrumb-item"><a
                                                href="participant?idCycle=${cycle.idCycle}">Participants</a>
                                        </li>
                                        <li class="breadcrumb-item active">${cycle.libelle}</li>
                                    </c:when>
                                    <c:otherwise>
                                        <li class="breadcrumb-item active">Toutes les Échéances</li>
                                    </c:otherwise>
                                </c:choose>
                            </ol>
                        </nav>

                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <h2>Suivi des Échéances</h2>
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
                                            <th>Tour</th>
                                            <th>Participant</th>
                                            <th>Date Échéance</th>
                                            <th>Montant Dû</th>
                                            <th>Payé</th>
                                            <th>Statut</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="e" items="${listEcheance}">
                                            <tr>
                                                <td>${e.numeroTour}</td>
                                                <td>${e.participant.adherent.nomComplet}</td>
                                                <td>${e.dateEcheance}</td>
                                                <td>${e.montantDu} GNF</td>
                                                <td>${e.montantPaye} GNF</td>
                                                <td>
                                                    <span
                                                        class="badge ${e.statut == 'PAYE' ? 'bg-success' : (e.statut == 'IMPAYE' ? 'bg-danger' : 'bg-warning')} text-capitalize">
                                                        ${e.statut}
                                                    </span>
                                                </td>
                                                <td>
                                                    <c:if test="${e.statut != 'PAYE' && user.role == 'COLLECTEUR' && loggedInCollecteur.idCollecteur == e.cycle.idCollecteur}">
                                                        <a href="paiements?action=pay&idEcheance=${e.idEcheance}"
                                                            class="btn btn-sm btn-primary"><i
                                                                class="bi bi-cash me-1"></i>
                                                            Payer</a>
                                                    </c:if>
                                                    <a href="paiements?idEcheance=${e.idEcheance}"
                                                        class="btn btn-sm btn-outline-info" title="Historique"><i
                                                            class="bi bi-clock-history"></i></a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty listEcheance}">
                                            <tr>
                                                <td colspan="7" class="text-center py-4">Aucune échéance générée.</td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>