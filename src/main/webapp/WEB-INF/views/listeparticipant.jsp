<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="fr">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Participants - ${cycle.libelle}</title>
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
                                <li class="breadcrumb-item"><a href="cycles">Cycles</a></li>
                                <li class="breadcrumb-item active">${cycle.libelle}</li>
                            </ol>
                        </nav>

                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <div class="d-flex align-items-center">
                                <h2>Participants du Cycle</h2>
                                <c:if test="${cycle.supportDemande}">
                                    <span class="badge bg-warning text-dark px-3 ms-3 shadow-sm rounded-pill">
                                        <i class="bi bi-shield-fill-check me-1"></i> Support Admin Activé
                                    </span>
                                </c:if>
                            </div>
                            <div>
                                <c:if test="${user.role == 'COLLECTEUR' && loggedInCollecteur.idCollecteur == cycle.idCollecteur}">
                                    <c:choose>
                                        <c:when test="${cycle.supportDemande}">
                                            <a href="cycles?action=toggleSupport&idCycle=${cycle.idCycle}" class="btn btn-outline-danger me-2 shadow-sm">
                                                <i class="bi bi-shield-lock-fill me-1"></i> Retirer Support Admin
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="cycles?action=toggleSupport&idCycle=${cycle.idCycle}" class="btn btn-outline-warning text-dark me-2 shadow-sm" style="background-color: #fff3cd; border-color: #ffc107;">
                                                <i class="bi bi-shield-fill-check me-1"></i> Autoriser Support Admin
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                <c:if test="${canManageParticipants}">
                                    <a href="participant?action=add&idCycle=${cycle.idCycle}" class="btn btn-primary shadow-sm"><i
                                            class="bi bi-plus-lg me-2"></i> Ajouter</a>
                                    <c:if test="${not empty listParticipant}">
                                        <button type="button" class="btn btn-success shadow-sm ms-2" title="Générer les échéances"
                                            onclick="confirmDelete('echeances?action=generate&idCycle=${cycle.idCycle}', '${cycle.libelle}', 'Générer les échéances', 'bi-calendar-check')">
                                            <i class="bi bi-calendar-check me-2"></i> Générer Échéances
                                        </button>
                                    </c:if>
                                </c:if>
                            </div>
                        </div>

                        <c:if test="${user.role == 'ADMIN' && !cycle.supportDemande}">
                            <div class="alert alert-warning d-flex align-items-center shadow-sm border-0 mb-4" role="alert" style="background-color: #fff3cd; border-left: 5px solid #ffc107 !important; color: #664d03;">
                                <i class="bi bi-exclamation-triangle-fill fs-4 me-3 text-warning"></i>
                                <div>
                                    <strong>Mode Consultation Seule :</strong> En tant qu'administrateur, vous ne pouvez pas ajouter ou retirer des participants de ce cycle. 
                                    Le collecteur responsable (<strong>${cycle.collecteur.nomComplet}</strong>) doit activer la demande de support pour vous y autoriser.
                                </div>
                            </div>
                        </c:if>

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
                                            <th>N° Ordre</th>
                                            <th>Adhérent</th>
                                            <th>Téléphone</th>
                                            <th>Statut</th>
                                            <th>Montant Reçu</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="p" items="${listParticipant}">
                                            <tr>
                                                <td>${p.numeroOrdre}</td>
                                                <td>${p.adherent.nomComplet}</td>
                                                <td>${p.adherent.telephone}</td>
                                                <td>
                                                    <c:set var="statusClass" value="secondary" />
                                                    <c:choose>
                                                        <c:when test="${p.statut.name() == 'ACTIF'}">
                                                            <c:set var="statusClass" value="success" />
                                                        </c:when>
                                                        <c:when test="${p.statut.name() == 'SUSPENDU'}">
                                                            <c:set var="statusClass" value="danger" />
                                                        </c:when>
                                                        <c:when test="${p.statut.name() == 'RETIRE'}">
                                                            <c:set var="statusClass" value="warning" />
                                                        </c:when>
                                                        <c:when test="${p.statut.name() == 'INSCRIT'}">
                                                            <c:set var="statusClass" value="info" />
                                                        </c:when>
                                                    </c:choose>
                                                    <span
                                                        class="badge rounded-pill bg-${statusClass} px-3 text-capitalize">${p.statut}</span>
                                                </td>
                                                <td class="fw-bold text-primary">${p.montantRecu} GNF</td>
                                                <td>
                                                    <c:if test="${canManageParticipants}">
                                                        <button type="button" class="btn btn-sm btn-outline-danger"
                                                            title="Retirer ce participant"
                                                            onclick="confirmDelete('participant?action=delete&id=${p.idParticipant}&idCycle=${cycle.idCycle}', '${p.adherent.nomComplet}', 'Retirer du cycle', 'bi-person-dash')">
                                                            <i class="bi bi-person-x"></i>
                                                        </button>
                                                    </c:if>
                                                    <c:if test="${!canManageParticipants}">
                                                        <span class="text-muted small"><i class="bi bi-eye me-1"></i> Lecture seule</span>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty listParticipant}">
                                            <tr>
                                                <td colspan="6" class="text-center py-4 text-muted italic">
                                                    <i class="bi bi-info-circle me-1"></i> Aucun participant pour le
                                                    moment.
                                                </td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>

                <%@ include file="/WEB-INF/views/fragments/footer.jspf" %>
        </body>

        </html>