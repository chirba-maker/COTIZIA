<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
            <!DOCTYPE html>
            <html lang="fr">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Mouchard - Cotizia</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
                <link rel="stylesheet"
                    href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
                <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap"
                    rel="stylesheet">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
            </head>

            <body>

                <%@ include file="/WEB-INF/views/fragments/sidebar.jspf" %>

                    <div class="content">
                        <div class="container-fluid">
                            <div class="d-flex justify-content-between align-items-center mb-4">
                                <h2>Mouchard d'Audit</h2>
                            </div>

                            <div class="card p-4">
                                <div class="table-responsive">
                                    <table class="table table-hover table-sm" style="white-space: nowrap;">
                                        <thead>
                                            <tr>
                                                <th>Date</th>
                                                <th>Utilisateur</th>
                                                <th>Action</th>
                                                <th>Entité</th>
                                                <th>ID Entité</th>
                                                <th>IP</th>
                                                <th>Détails</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="log" items="${listLogs}">
                                                <tr>
                                                    <td>${log.dateAction}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${not empty log.utilisateur}">
                                                                ${log.utilisateur.prenom} ${log.utilisateur.nom}
                                                            </c:when>
                                                            <c:otherwise>
                                                                ID: ${log.idUtilisateur} (Supprimé)
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <c:set var="badgeClass" value="bg-secondary" />
                                                        <c:choose>
                                                            <c:when
                                                                test="${log.action == 'LOGIN' || log.action == 'LOGOUT'}">
                                                                <c:set var="badgeClass" value="bg-primary" />
                                                            </c:when>
                                                            <c:when
                                                                test="${fn:contains(log.action, 'CREATE') || fn:contains(log.action, 'SAVE') || fn:contains(log.action, 'ADD') || fn:contains(log.action, 'GENERATE')}">
                                                                <c:set var="badgeClass" value="bg-success" />
                                                            </c:when>
                                                            <c:when test="${fn:contains(log.action, 'UPDATE')}">
                                                                <c:set var="badgeClass" value="bg-warning text-dark" />
                                                            </c:when>
                                                            <c:when
                                                                test="${fn:contains(log.action, 'DELETE') || fn:contains(log.action, 'REMOVE') || fn:contains(log.action, 'ANNULER') || fn:contains(log.action, 'RETIRE')}">
                                                                <c:set var="badgeClass" value="bg-danger" />
                                                            </c:when>
                                                        </c:choose>
                                                        <span class="badge ${badgeClass}">${log.action}</span>
                                                    </td>
                                                    <td><span class="text-muted small">${log.entite}</span></td>
                                                    <td><code class="text-dark">${log.idEntite}</code></td>
                                                    <td><span class="badge bg-light text-dark border"><i
                                                                class="bi bi-pc-display me-1"></i>${log.adresseIp}</span>
                                                    </td>
                                                    <td>
                                                        <button class="btn btn-sm btn-outline-primary" type="button"
                                                            data-bs-toggle="collapse"
                                                            data-bs-target="#log${log.idMouchard}">
                                                            <i class="bi bi-eye me-1"></i> Détails
                                                        </button>
                                                    </td>
                                                </tr>
                                                <tr class="collapse" id="log${log.idMouchard}">
                                                    <td colspan="7" class="p-0 border-0">
                                                        <div
                                                            class="p-4 bg-light border-start border-4 border-primary mx-3 my-2 rounded-end shadow-sm">
                                                            <div class="row">
                                                                <div class="col-md-6 mb-3 mb-md-0">
                                                                    <h6
                                                                        class="text-uppercase text-muted fw-bold small mb-3">
                                                                        <i class="bi bi-arrow-left-circle me-2"></i>État
                                                                        Précédent (Avant)
                                                                    </h6>
                                                                    <div id="formattedBefore${log.idMouchard}"
                                                                        class="bg-white p-3 rounded border">
                                                                        <c:choose>
                                                                            <c:when test="${not empty log.detailAvant}">
                                                                                <pre class="mb-0 small"
                                                                                    style="white-space: pre-wrap;">${log.detailAvant}</pre>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <div
                                                                                    class="text-center py-2 text-muted small">
                                                                                    <i
                                                                                        class="bi bi-slash-circle me-1"></i>
                                                                                    Aucune donnée disponible
                                                                                </div>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </div>
                                                                </div>
                                                                <div class="col-md-6">
                                                                    <h6
                                                                        class="text-uppercase text-muted fw-bold small mb-3">
                                                                        <i
                                                                            class="bi bi-arrow-right-circle me-2"></i>Nouvel
                                                                        État (Après)
                                                                    </h6>
                                                                    <div id="formattedAfter${log.idMouchard}"
                                                                        class="bg-white p-3 rounded border">
                                                                        <c:choose>
                                                                            <c:when test="${not empty log.detailApres}">
                                                                                <pre class="mb-0 small"
                                                                                    style="white-space: pre-wrap;">${log.detailApres}</pre>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <div
                                                                                    class="text-center py-2 text-muted small">
                                                                                    <i
                                                                                        class="bi bi-slash-circle me-1"></i>
                                                                                    Aucune donnée disponible
                                                                                </div>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>

                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
                    <script>
                        // Auto-format JSON in pre tags on page load
                        document.addEventListener('DOMContentLoaded', function () {
                            document.querySelectorAll('pre').forEach(function (pre) {
                                var text = pre.textContent.trim();
                                if (text && (text.startsWith('{') || text.startsWith('['))) {
                                    try {
                                        var data = JSON.parse(text);
                                        // Build table
                                        var html = '<div class="table-responsive"><table class="table table-sm table-borderless mb-0 small">';
                                        var keys = Object.keys(data);
                                        for (var i = 0; i < keys.length; i++) {
                                            var key = keys[i];
                                            var value = data[key];
                                            var displayValue = value;

                                            if (value === null) {
                                                displayValue = '<span class="text-muted fst-italic">null</span>';
                                            } else if (typeof value === 'boolean') {
                                                displayValue = value ?
                                                    '<span class="text-success fw-bold"><i class="bi bi-check-lg me-1"></i>Oui</span>' :
                                                    '<span class="text-danger fw-bold"><i class="bi bi-x-lg me-1"></i>Non</span>';
                                            }

                                            html += '<tr><th class="text-muted fw-normal" style="width: 40%">' + key + '</th>';
                                            html += '<td class="fw-bold">' + displayValue + '</td></tr>';
                                        }
                                        html += '</table></div>';
                                        pre.outerHTML = html;
                                    } catch (e) {
                                        console.warn('Could not parse JSON:', e);
                                    }
                                }
                            });
                        });
                    </script>
            </body>

            </html>