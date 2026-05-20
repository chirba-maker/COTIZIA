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
            <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login-premium.css">
        </head>

        <body class="premium-login-page">

            <div class="login-bg-shapes">
                <div class="shape shape-1"></div>
                <div class="shape shape-2"></div>
                <div class="shape shape-3"></div>
            </div>

            <div class="login-container">
                <div class="login-glass-card">
                    <div class="logo-container">
                        <div class="logo-icon">
                            <i class="bi bi-shield-lock"></i>
                        </div>
                        <h1>Cotizia</h1>
                    </div>

                    <h2>Bienvenue</h2>
                    <p class="subtitle">Connectez-vous pour accéder à votre espace de gestion premium.</p>

                    <% if (request.getAttribute("error") != null) { %>
                        <div class="custom-alert">
                            <i class="bi bi-exclamation-triangle-fill me-2" style="font-size: 1.2rem;"></i>
                            <div><%= request.getAttribute("error") %></div>
                        </div>
                    <% } %>

                    <% if (request.getParameter("logout") != null) { %>
                        <div class="custom-alert alert-success">
                            <i class="bi bi-check-circle-fill me-2" style="font-size: 1.2rem;"></i>
                            <div>Vous avez été déconnecté avec succès.</div>
                        </div>
                    <% } %>

                    <form action="connexion" method="post" class="login-form">
                        <input type="hidden" name="csrfToken" value="${csrfToken}">
                        
                        <div class="form-group mb-4">
                            <label for="login" class="form-label">Identifiant</label>
                            <div class="input-glass-wrapper">
                                <i class="bi bi-person input-icon"></i>
                                <input type="text" class="form-control glass-input" id="login"
                                    name="login" placeholder="Entrez votre identifiant" required
                                    autofocus autocomplete="off">
                            </div>
                        </div>
                        
                        <div class="form-group mb-5">
                            <label for="password" class="form-label">Mot de passe</label>
                            <div class="input-glass-wrapper">
                                <i class="bi bi-key input-icon"></i>
                                <input type="password" class="form-control glass-input"
                                    id="password" name="password" placeholder="••••••••" required>
                            </div>
                        </div>
                        
                        <button type="submit" class="btn-premium w-100">
                            <span>Se connecter</span>
                            <i class="bi bi-arrow-right-short"></i>
                        </button>
                    </form>

                    <div class="login-footer">
                        <p>&copy; 2026 Cotizia. Solutions Financières Premium.</p>
                    </div>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>