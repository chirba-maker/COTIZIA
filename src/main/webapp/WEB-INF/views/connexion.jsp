<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="fr">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Connexion - Cotizia</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
            <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap"
                rel="stylesheet">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
        </head>

        <body class="login-page">

            <div class="login-side-image">
                <div class="login-visual-content">
                    <h2>Bienvenue sur Cotizia</h2>
                    <p>La plateforme intelligente pour gérer vos cycles de cotisation en toute confiance et simplicité.
                    </p>
                </div>
            </div>

            <div class="login-side-form">
                <div class="login-form-wrapper">
                    <div class="login-card">
                        <div class="logo">
                            <i class="bi bi-shield-check"></i>
                            <h1>Cotizia</h1>
                        </div>

                        <h2>Connexion</h2>
                        <p class="subtitle">Heureux de vous revoir ! Veuillez vous connecter.</p>

                        <% if (request.getAttribute("error") !=null) { %>
                            <div class="alert alert-danger" role="alert">
                                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                                <%= request.getAttribute("error") %>
                            </div>
                            <% } %>

                                <% if (request.getParameter("logout") !=null) { %>
                                    <div class="alert alert-success" role="alert">
                                        <i class="bi bi-check-circle-fill me-2"></i>
                                        Vous avez été déconnecté avec succès.
                                    </div>
                                    <% } %>

                                        <form action="connexion" method="post">
                                            <input type="hidden" name="csrfToken" value="${csrfToken}">
                                            <div class="mb-4">
                                                <label for="login" class="form-label">Identifiant</label>
                                                <div class="input-group">
                                                    <span class="input-group-text"
                                                        style="background: var(--bg); border-right: none;"><i
                                                            class="bi bi-person"
                                                            style="color: var(--primary);"></i></span>
                                                    <input type="text" class="form-control form-control-lg" id="login"
                                                        name="login" placeholder="Entrez votre identifiant" required
                                                        autofocus style="border-left: none;">
                                                </div>
                                            </div>
                                            <div class="mb-4">
                                                <label for="password" class="form-label">Mot de passe</label>
                                                <div class="input-group">
                                                    <span class="input-group-text"
                                                        style="background: var(--bg); border-right: none;"><i
                                                            class="bi bi-lock"
                                                            style="color: var(--primary);"></i></span>
                                                    <input type="password" class="form-control form-control-lg"
                                                        id="password" name="password" placeholder="••••••••" required
                                                        style="border-left: none;">
                                                </div>
                                            </div>
                                            <div class="d-grid gap-2 mt-5">
                                                <button type="submit" class="btn btn-primary btn-lg">
                                                    <i class="bi bi-box-arrow-in-right me-2"></i>Se connecter
                                                </button>
                                            </div>
                                        </form>

                                        <div class="text-center mt-5">
                                            <p class="text-muted small">&copy; 2026 Cotizia. Tous droits réservés.</p>
                                        </div>
                    </div>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>