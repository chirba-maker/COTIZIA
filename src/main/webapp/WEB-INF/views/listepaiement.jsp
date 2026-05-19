<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="fr">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Historique des Paiements - Cotizia</title>
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
                            <h2>Historique des Paiements</h2>
                            <a href="javascript:history.back()" class="btn btn-outline-secondary"><i
                                    class="bi bi-arrow-left me-2"></i> Retour</a>
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
                                            <th>Date Paiement</th>
                                            <th>Montant</th>
                                            <th>Mode</th>
                                            <th>Référence</th>
                                            <th>Enregistré Par</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="p" items="${listPaiement}">
                                            <tr>
                                                <td>${p.datePaiement}</td>
                                                <td>${p.montant} GNF</td>
                                                <td><span
                                                        class="text-capitalize">${p.modePaiement.name().toLowerCase()}</span>
                                                </td>
                                                <td>${p.reference}</td>
                                                <td>Utilisateur ID: ${p.idUtilisateur}</td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/export/pdf?type=receipt&id=${p.idPaiement}"
                                                        class="btn btn-sm btn-outline-danger"
                                                        title="Exporter Reçu (PDF)">
                                                        <i class="bi bi-file-earmark-pdf"></i>
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty listPaiement}">
                                            <tr>
                                                <td colspan="5" class="text-center py-4">Aucun paiement trouvé.</td>
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