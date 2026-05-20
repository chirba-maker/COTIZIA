<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="fr">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Mon Profil - Cotizia</title>
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
                            <h2><i class="bi bi-person-badge me-2 text-primary"></i>Mon Profil</h2>
                            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-secondary">
                                <i class="bi bi-arrow-left me-2"></i>Retour au Dashboard
                            </a>
                        </div>

                        <form action="${pageContext.request.contextPath}/profile" method="post" enctype="multipart/form-data">
                            <input type="hidden" name="csrfToken" value="${csrfToken}">
                            <div class="row">
                            <div class="col-lg-8">
                                <div class="card p-4 border-0 shadow-sm">
                                    <c:if test="${not empty sessionScope.error}">
                                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                            <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                            ${sessionScope.error}
                                            <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                aria-label="Close"></button>
                                        </div>
                                        <c:remove var="error" scope="session" />
                                    </c:if>

                                    <c:if test="${not empty sessionScope.success}">
                                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                                            <i class="bi bi-check-circle-fill me-2"></i>
                                            ${sessionScope.success}
                                            <button type="button" class="btn-close" data-bs-dismiss="alert"
                                                aria-label="Close"></button>
                                        </div>
                                        <c:remove var="success" scope="session" />
                                    </c:if>

                                        <div class="row g-3">
                                            <div class="col-md-6">
                                                <label for="nom" class="form-label">Nom</label>
                                                <input type="text" class="form-control" id="nom" name="nom"
                                                    value="${user.nom}" required>
                                            </div>
                                            <div class="col-md-6">
                                                <label for="prenom" class="form-label">Prénom</label>
                                                <input type="text" class="form-control" id="prenom" name="prenom"
                                                    value="${user.prenom}" required>
                                            </div>
                                            <div class="col-md-6">
                                                <label for="login" class="form-label">Login (Identifiant)</label>
                                                <input type="text" class="form-control bg-light" id="login"
                                                    value="${user.login}" readonly disabled>
                                                <div class="form-text">L'identifiant ne peut pas être modifié.</div>
                                            </div>
                                            <div class="col-md-6">
                                                <label for="email" class="form-label">Email</label>
                                                <input type="email" class="form-control" id="email" name="email"
                                                    value="${user.email}" required>
                                            </div>

                                            <hr class="my-4">
                                            <h5 class="mb-3 text-secondary"><i
                                                    class="bi bi-shield-lock me-2"></i>Sécurité</h5>

                                            <div class="col-md-12">
                                                <label for="password" class="form-label">Nouveau Mot de Passe</label>
                                                <input type="password" class="form-control" id="password"
                                                    name="password" placeholder="Laissez vide pour conserver l'actuel">
                                                <div class="form-text">Utilisez un mot de passe fort pour protéger votre
                                                    compte.</div>
                                            </div>
                                        </div>

                                        <div class="mt-4">
                                            <button type="submit" class="btn btn-primary px-5 py-2">
                                                <i class="bi bi-save me-2"></i>Mettre à jour mon profil
                                            </button>
                                        </div>
                                </div>
                            </div>

                            <div class="col-lg-4 mt-4 mt-lg-0">
                                <div class="card border-0 shadow-sm card-accent-top p-4 text-center">
                                    <div class="mb-3 position-relative d-inline-block">
                                        <c:choose>
                                            <c:when test="${not empty user.photo}">
                                                <img src="${user.photo}" alt="Photo de profil" class="rounded-circle object-fit-cover shadow-sm" style="width: 130px; height: 130px; border: 4px solid var(--white);" id="profilePreview">
                                            </c:when>
                                            <c:otherwise>
                                                <div class="d-flex align-items-center justify-content-center rounded-circle bg-light text-primary mx-auto shadow-sm" style="width: 130px; height: 130px; border: 4px solid var(--white);" id="profilePreviewPlaceholder">
                                                    <i class="bi bi-person-fill" style="font-size: 4.5rem;"></i>
                                                </div>
                                                <img src="" alt="Preview" class="rounded-circle object-fit-cover shadow-sm d-none" style="width: 130px; height: 130px; border: 4px solid var(--white);" id="profilePreview">
                                            </c:otherwise>
                                        </c:choose>
                                        
                                        <label for="photoUpload" class="position-absolute bottom-0 end-0 bg-primary text-white rounded-circle d-flex align-items-center justify-content-center shadow" style="width: 38px; height: 38px; cursor: pointer; transition: transform 0.2s; right: 5px !important;" onmouseover="this.style.transform='scale(1.1)'" onmouseout="this.style.transform='scale(1)'" title="Changer la photo">
                                            <i class="bi bi-camera-fill"></i>
                                        </label>
                                    </div>
                                    
                                    <div class="mb-4">
                                        <label for="photoUpload" class="btn btn-sm btn-outline-primary rounded-pill px-4">
                                            <i class="bi bi-upload me-1"></i> Importer une image
                                        </label>
                                        <input type="file" id="photoUpload" name="photo" class="d-none" accept="image/*" onchange="previewImage(event)">
                                    </div>
                                    <h4 class="mb-1">${user.nomComplet}</h4>
                                    <p class="text-muted mb-3"><span
                                            class="badge bg-info text-white">${user.role}</span></p>
                                    <hr>
                                    <div class="text-start">
                                        <p class="small text-muted mb-1">Dernière modification :</p>
                                        <p class="small fw-medium">${user.dateModification}</p>
                                        <p class="small text-muted mb-1">Compte créé le :</p>
                                        <p class="small fw-medium">${user.dateCreation}</p>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>

                <script>
                    function previewImage(event) {
                        const reader = new FileReader();
                        reader.onload = function(){
                            const output = document.getElementById('profilePreview');
                            output.src = reader.result;
                            output.classList.remove('d-none');
                            
                            const placeholder = document.getElementById('profilePreviewPlaceholder');
                            if(placeholder) {
                                placeholder.classList.add('d-none');
                            }
                        };
                        reader.readAsDataURL(event.target.files[0]);
                    }
                </script>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>