<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="fr">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Enregistrer Paiement - Cotizia</title>
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
                            <h2>Enregistrer un Paiement</h2>
                            <a href="${pageContext.request.contextPath}/echeances?idCycle=${echeance.idCycle}"
                                class="btn btn-outline-secondary"><i class="bi bi-arrow-left me-2"></i> Retour</a>
                        </div>

                        <div class="card p-4">
                            <div class="alert alert-info border-0 shadow-sm">
                                <strong>Détails Échéance:</strong> Tour ${echeance.numeroTour} - Montant Dû:
                                ${echeance.montantDu} GNF (Reste à payer: ${echeance.montantDu - echeance.montantPaye}
                                GNF)
                            </div>

                            <form action="${pageContext.request.contextPath}/paiements" method="post">
                                <input type="hidden" name="action" value="save">
                                <input type="hidden" name="csrfToken" value="${csrfToken}">
                                <input type="hidden" name="idEcheance" value="${echeance.idEcheance}">

                                <div class="row g-3 mt-2">
                                    <div class="col-md-6">
                                        <label for="montant" class="form-label">Montant Versé (GNF)</label>
                                        <input type="number" step="0.01" class="form-control" id="montant"
                                            name="montant" value="${echeance.montantDu - echeance.montantPaye}"
                                            required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="mode_paiement" class="form-label">Mode de Paiement</label>
                                        <select class="form-select" id="mode_paiement" name="mode_paiement" required>
                                            <option value="especes">Espèces</option>
                                            <option value="orange_money">Orange Money</option>
                                            <option value="mobi_cash">MobiCash</option>
                                            <option value="virement">Virement Bancaire</option>
                                        </select>
                                    </div>
                                    <div class="col-md-12">
                                        <label for="reference" class="form-label">Référence (N° Transaction,
                                            Reçu...)</label>
                                        <input type="text" class="form-control" id="reference" name="reference">
                                    </div>
                                    <div class="col-md-12">
                                        <label for="note" class="form-label">Note / Commentaire</label>
                                        <textarea class="form-control" id="note" name="note" rows="2"></textarea>
                                    </div>
                                </div>

                                <div class="mt-4">
                                    <button type="submit" class="btn btn-primary px-4">Valider le Paiement</button>
                                    <button type="reset" class="btn btn-light px-4">Réinitialiser</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>