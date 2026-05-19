<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
            <!DOCTYPE html>
            <html lang="fr">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Tableau de Bord - Cotizia</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
                <link rel="stylesheet"
                    href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
                <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap"
                    rel="stylesheet">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
                <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
            </head>

            <body>

                <%@ include file="/WEB-INF/views/fragments/sidebar.jspf" %>

                    <div class="content">
                        <div class="container-fluid">
                            <div class="d-flex justify-content-between align-items-center mb-4 flex-wrap">
                                <div>
                                    <h2 class="mb-1">Tableau de Bord</h2>
                                    <p class="text-muted mb-0" style="font-size: 0.9rem;">Vue d'ensemble de votre
                                        activité
                                    </p>
                                </div>
                                <div class="d-flex align-items-center gap-3">
                                    <span class="badge bg-light text-dark px-3 py-2" style="font-size: 0.85rem;">
                                        <i class="bi bi-calendar3 me-2"></i>
                                        <%= new java.text.SimpleDateFormat("dd MMMM yyyy").format(new java.util.Date())
                                            %>
                                    </span>
                                </div>
                            </div>

                            <!-- KPI Cards -->
                            <div class="row g-4 mb-5">
                                <div class="col-12 col-sm-6 col-xl-3">
                                    <div class="card stat-card h-100 border-start border-4"
                                        style="border-color: var(--primary) !important;">
                                        <div class="kpi-label">Adhérents Actifs</div>
                                        <div class="kpi-value" style="color: var(--primary);">${stats.totalAdherents}
                                        </div>
                                        <i class="bi bi-people" style="color: var(--primary);"></i>
                                    </div>
                                </div>
                                <div class="col-12 col-sm-6 col-xl-3">
                                    <div class="card stat-card h-100 border-start border-4"
                                        style="border-color: #16a34a !important;">
                                        <div class="kpi-label">Collecte Totale</div>
                                        <div class="kpi-value" style="color: #16a34a;">${stats.totalCollecte} <small
                                                style="font-size: 0.6em; font-weight: 400;">GNF</small></div>
                                        <i class="bi bi-cash-stack" style="color: #16a34a;"></i>
                                    </div>
                                </div>
                                <div class="col-12 col-sm-6 col-xl-3">
                                    <div class="card stat-card h-100 border-start border-4"
                                        style="border-color: var(--accent) !important;">
                                        <div class="kpi-label">Taux Recouvrement</div>
                                        <div class="kpi-value" style="color: var(--accent);">
                                            ${stats.tauxRecouvrement}<small style="font-size: 0.6em;">%</small></div>
                                        <i class="bi bi-graph-up-arrow" style="color: var(--accent);"></i>
                                    </div>
                                </div>
                                <div class="col-12 col-sm-6 col-xl-3">
                                    <div class="card stat-card h-100 border-start border-4"
                                        style="border-color: #dc2626 !important;">
                                        <div class="kpi-label">Impayés</div>
                                        <div class="kpi-value" style="color: #dc2626;">${stats.totalImpayes}</div>
                                        <i class="bi bi-exclamation-triangle" style="color: #dc2626;"></i>
                                    </div>
                                </div>
                            </div>

                            <!-- Charts Row -->
                            <div class="row g-4 mb-5">
                                <div class="col-12 col-lg-8">
                                    <div class="card p-4">
                                        <div class="d-flex justify-content-between align-items-center mb-4">
                                            <h5 class="card-title mb-0">Évolution de la Collecte</h5>
                                            <span class="badge bg-light text-muted">6 derniers mois</span>
                                        </div>
                                        <div class="chart-container">
                                            <canvas id="trendChart"></canvas>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-12 col-lg-4">
                                    <div class="card p-4">
                                        <h5 class="card-title mb-4">Statut des Échéances</h5>
                                        <div class="chart-container">
                                            <canvas id="statusChart"></canvas>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Quick Actions -->
                            <div class="mb-5">
                                <h5 class="mb-3" style="font-weight: 600;">Raccourcis Rapides</h5>
                                <div class="row g-3">
                                    <c:if test="${user.role != 'CONSULTANT'}">
                                        <div class="col-6 col-md-3">
                                            <a href="${pageContext.request.contextPath}/cycles?action=create"
                                                class="btn btn-outline-primary w-100 p-3 text-center transition-up shadow-sm">
                                                <i class="bi bi-plus-circle d-block fs-3 mb-2"></i>
                                                <span style="font-size: 0.85rem; font-weight: 500;">Nouveau Cycle</span>
                                            </a>
                                        </div>
                                    </c:if>
                                    <c:if test="${user.role != 'CONSULTANT'}">
                                        <div class="col-6 col-md-3">
                                            <a href="${pageContext.request.contextPath}/adherents?action=create"
                                                class="btn btn-outline-secondary w-100 p-3 text-center transition-up shadow-sm">
                                                <i class="bi bi-person-plus d-block fs-3 mb-2"></i>
                                                <span style="font-size: 0.85rem; font-weight: 500;">Nouvel
                                                    Adhérent</span>
                                            </a>
                                        </div>
                                    </c:if>
                                    <div class="col-6 col-md-3">
                                        <a href="${pageContext.request.contextPath}/paiements"
                                            class="btn btn-outline-success w-100 p-3 text-center transition-up shadow-sm">
                                            <i class="bi bi-wallet2 d-block fs-3 mb-2"></i>
                                            <span style="font-size: 0.85rem; font-weight: 500;">Voir Paiements</span>
                                        </a>
                                    </div>
                                    <c:if test="${user.role == 'ADMIN'}">
                                        <div class="col-6 col-md-3">
                                            <a href="${pageContext.request.contextPath}/utilisateurs"
                                                class="btn btn-outline-dark w-100 p-3 text-center transition-up shadow-sm">
                                                <i class="bi bi-people d-block fs-3 mb-2"></i>
                                                <span style="font-size: 0.85rem; font-weight: 500;">Équipe</span>
                                            </a>
                                        </div>
                                    </c:if>
                                </div>
                            </div>

                            <!-- Secondary Row: Payments & Activities -->
                            <div class="row g-4 mb-5">
                                <!-- Latest Payments -->
                                <div class="col-12 col-xl-7">
                                    <div class="card h-100 shadow-sm border-0">
                                        <div
                                            class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                                            <h5 class="card-title mb-0" style="font-weight: 600;">
                                                <i class="bi bi-receipt text-success me-2"></i>Derniers Paiements
                                            </h5>
                                            <a href="paiements" class="btn btn-sm btn-light text-primary fw-500">Voir
                                                tout</a>
                                        </div>
                                        <div class="card-body p-0">
                                            <div class="table-responsive">
                                                <table class="table table-hover align-middle mb-0">
                                                    <thead class="bg-light text-muted small text-uppercase">
                                                        <tr>
                                                            <th class="ps-4">Adhérent</th>
                                                            <th>Date</th>
                                                            <th class="text-end pe-4">Montant</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach var="pay" items="${stats.latestPayments}">
                                                            <tr>
                                                                <td class="ps-4">
                                                                    <div class="d-flex align-items-center">
                                                                        <div
                                                                            class="avatar-sm bg-light-success text-success me-3">
                                                                            ${pay.adherent.substring(0, 1)}
                                                                        </div>
                                                                        <span class="fw-500">${pay.adherent}</span>
                                                                    </div>
                                                                </td>
                                                                <td class="text-muted">
                                                                    <fmt:formatDate value="${pay.date}"
                                                                        pattern="dd/MM/yyyy HH:mm" />
                                                                </td>
                                                                <td class="text-end pe-4">
                                                                    <span class="fw-bold text-success">
                                                                        <fmt:formatNumber value="${pay.montant}"
                                                                            type="number" /> GNF
                                                                    </span>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                        <c:if test="${empty stats.latestPayments}">
                                                            <tr>
                                                                <td colspan="3"
                                                                    class="text-center py-4 text-muted italic">
                                                                    <i class="bi bi-info-circle me-1"></i> Aucun
                                                                    paiement
                                                                    récent.
                                                                </td>
                                                            </tr>
                                                        </c:if>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- Recent Activities -->
                                <div class="col-12 col-xl-5">
                                    <div class="card h-100 shadow-sm border-0">
                                        <div class="card-header bg-white py-3">
                                            <h5 class="card-title mb-0" style="font-weight: 600;">
                                                <i class="bi bi-activity text-primary me-2"></i>Activités Récentes
                                            </h5>
                                        </div>
                                        <div class="card-body">
                                            <div class="timeline">
                                                <c:forEach var="act" items="${stats.recentActivities}">
                                                    <div
                                                        class="timeline-item pb-3 border-start ps-4 position-relative ml-2">
                                                        <div class="timeline-marker"></div>
                                                        <div class="small text-muted mb-1">
                                                            <fmt:formatDate value="${act.date}" pattern="dd/MM HH:mm" />
                                                            <span class="mx-2">•</span>
                                                            <span class="fw-500 text-dark">${act.user}</span>
                                                        </div>
                                                        <p class="mb-0 small">
                                                            <span
                                                                class="badge bg-light text-primary text-uppercase me-2"
                                                                style="font-size: 0.65rem;">
                                                                ${act.action}
                                                            </span>
                                                            <span class="text-muted">sur</span>
                                                            <span class="fw-500">${act.entite}</span>
                                                        </p>
                                                    </div>
                                                </c:forEach>
                                                <c:if test="${empty stats.recentActivities}">
                                                    <div class="text-center py-4 text-muted italic">
                                                        <i class="bi bi-info-circle me-1"></i> Aucune activité récente.
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
                    <script>
                        // Trend Chart — Premium Styling
                        const trendCtx = document.getElementById('trendChart').getContext('2d');
                        const gradient = trendCtx.createLinearGradient(0, 0, 0, 300);
                        gradient.addColorStop(0, 'rgba(30, 60, 114, 0.15)');
                        gradient.addColorStop(1, 'rgba(30, 60, 114, 0.01)');

                        new Chart(trendCtx, {
                            type: 'line',
                            data: {
                                labels: [
                                    <c:forEach var="label" items="${stats.historyLabels}" varStatus="loop">
                                        '${label}'${!loop.last ? ',' : ''}
                                    </c:forEach>
                                ],
                                datasets: [{
                                    label: 'Montant Collecté (GNF)',
                                    data: [
                                        <c:forEach var="val" items="${stats.historyValues}" varStatus="loop">
                                            ${val}${!loop.last ? ',' : ''}
                                        </c:forEach>
                                    ],
                                    borderColor: '#1e3c72',
                                    backgroundColor: gradient,
                                    fill: true,
                                    tension: 0.4,
                                    borderWidth: 2.5,
                                    pointBackgroundColor: '#1e3c72',
                                    pointBorderColor: '#fff',
                                    pointBorderWidth: 2,
                                    pointRadius: 5,
                                    pointHoverRadius: 7
                                }]
                            },
                            options: {
                                responsive: true,
                                maintainAspectRatio: false,
                                plugins: {
                                    legend: { display: false },
                                    tooltip: {
                                        backgroundColor: '#1e3c72',
                                        titleColor: '#fff',
                                        bodyColor: '#fff',
                                        padding: 12,
                                        cornerRadius: 8,
                                        displayColors: false
                                    }
                                },
                                scales: {
                                    y: {
                                        beginAtZero: true,
                                        grid: { color: 'rgba(30, 60, 114, 0.05)' },
                                        ticks: { color: '#6b7a99', font: { size: 11 } }
                                    },
                                    x: {
                                        grid: { display: false },
                                        ticks: { color: '#6b7a99', font: { size: 11 } }
                                    }
                                }
                            }
                        });

                        // Status Chart — Premium Doughnut
                        const statusCtx = document.getElementById('statusChart').getContext('2d');
                        new Chart(statusCtx, {
                            type: 'doughnut',
                            data: {
                                labels: [
                                    <c:forEach var="label" items="${stats.statusDistLabels}" varStatus="loop">
                                        '${label}'${!loop.last ? ',' : ''}
                                    </c:forEach>
                                ],
                                datasets: [{
                                    data: [
                                        <c:forEach var="val" items="${stats.statusDistValues}" varStatus="loop">
                                            ${val}${!loop.last ? ',' : ''}
                                        </c:forEach>
                                    ],
                                    backgroundColor: ['#16a34a', '#ffb800', '#dc2626', '#94a3b8'],
                                    borderWidth: 0,
                                    hoverOffset: 6
                                }]
                            },
                            options: {
                                responsive: true,
                                maintainAspectRatio: false,
                                cutout: '70%',
                                plugins: {
                                    legend: {
                                        position: 'bottom',
                                        labels: {
                                            padding: 16,
                                            usePointStyle: true,
                                            pointStyle: 'circle',
                                            font: { size: 12 }
                                        }
                                    },
                                    tooltip: {
                                        backgroundColor: '#1e3c72',
                                        padding: 12,
                                        cornerRadius: 8
                                    }
                                }
                            }
                        });
                    </script>
            </body>

            </html>