<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="fr">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>${cycleEdit != null ? 'Modifier' : 'Nouveau'} Cycle - Cotizia</title>
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
                            <h2>${cycleEdit != null ? 'Modifier' : 'Nouveau'} Cycle</h2>
                            <a href="${pageContext.request.contextPath}/cycles" class="btn btn-outline-secondary"><i
                                    class="bi bi-arrow-left me-2"></i>
                                Retour</a>
                        </div>

                        <div class="card p-4">
                            <form action="${pageContext.request.contextPath}/cycles" method="post">
                                <input type="hidden" name="action" value="${cycleEdit != null ? 'update' : 'save'}">
                                <input type="hidden" name="csrfToken" value="${csrfToken}">
                                <c:if test="${cycleEdit != null}">
                                    <input type="hidden" name="id" value="${cycleEdit.idCycle}">
                                </c:if>

                                <div class="row g-3">
                                    <div class="col-md-6">
                                        <label for="libelle" class="form-label">Libellé du Cycle</label>
                                        <input type="text" class="form-control" id="libelle" name="libelle"
                                            value="${cycleEdit.libelle}" required
                                            placeholder="Ex: Cycle de Tontine 2024 - Groupe A">
                                    </div>
                                    <div class="col-md-6">
                                        <label for="id_collecteur" class="form-label">Collecteur Responsable</label>
                                        <select class="form-select" id="id_collecteur" name="id_collecteur" required>
                                            <option value="">Sélectionner un collecteur</option>
                                            <c:forEach var="cl" items="${listCollecteur}">
                                                <option value="${cl.idCollecteur}"
                                                    ${cycleEdit.idCollecteur==cl.idCollecteur ? 'selected' : '' }>
                                                    ${cl.nomComplet}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-md-12">
                                        <label for="description" class="form-label">Description</label>
                                        <textarea class="form-control" id="description" name="description"
                                            rows="2">${cycleEdit.description}</textarea>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="montant_cotisation" class="form-label">Montant de Cotisation
                                            (GNF)</label>
                                        <input type="number" step="0.01" class="form-control" id="montant_cotisation"
                                            name="montant_cotisation" value="${cycleEdit.montantCotisation}" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="frequence" class="form-label">Fréquence</label>
                                        <select class="form-select" id="frequence" name="frequence" required>
                                            <option value="hebdomadaire" ${cycleEdit.frequence=='HEBDOMADAIRE'
                                                ? 'selected' : '' }>Hebdomadaire</option>
                                            <option value="bimensuelle" ${cycleEdit.frequence=='BIMENSUELLE'
                                                ? 'selected' : '' }>Bimensuelle (14 jours)</option>
                                            <option value="mensuelle" ${cycleEdit.frequence=='MENSUELLE' ? 'selected'
                                                : '' }>Mensuelle</option>
                                        </select>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="date_debut" class="form-label">Date de Début</label>
                                        <input type="date" class="form-control" id="date_debut" name="date_debut"
                                            value="${cycleEdit.dateDebut}" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="nombre_tours" class="form-label">Nombre de Tours</label>
                                        <input type="number" class="form-control" id="nombre_tours" name="nombre_tours"
                                            value="${cycleEdit.nombreTours}" required min="1">
                                    </div>
                                    <div class="col-md-4">
                                        <label for="statut" class="form-label">Statut</label>
                                        <select class="form-select" id="statut" name="statut" required>
                                            <option value="cree" ${cycleEdit.statut=='CREE' ? 'selected' : '' }>Créé
                                            </option>
                                            <option value="ouvert" ${cycleEdit.statut=='OUVERT' ? 'selected' : '' }>
                                                Ouvert
                                                aux inscr.</option>
                                            <option value="actif" ${cycleEdit.statut=='ACTIF' ? 'selected' : '' }>Actif
                                                / En
                                                cours</option>
                                            <option value="termine" ${cycleEdit.statut=='TERMINE' ? 'selected' : '' }>
                                                Terminé</option>
                                            <option value="annule" ${cycleEdit.statut=='ANNULE' ? 'selected' : '' }>
                                                Annulé
                                            </option>
                                        </select>
                                    </div>
                                </div>

                                <div class="mt-4">
                                    <button type="submit" class="btn btn-primary px-4">Enregistrer</button>
                                    <button type="reset" class="btn btn-light px-4">Réinitialiser</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>