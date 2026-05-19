package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Utilisateur;
import services.AuthService;
import services.AuditService;

/**
 * Controller for handling login and logout.
 * 
 * @author Major117
 */

public class LoginController extends HttpServlet {

    private final AuthService authService = new AuthService();
    private final AuditService auditService = new AuditService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/logout".equals(path)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                Utilisateur user = (Utilisateur) session.getAttribute("user");
                if (user != null) {
                    auditService.logAction(user, "LOGOUT", "utilisateur", user.getIdUtilisateur(), user, null, request);
                }
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/connexion?logout=true");
        } else {
            // Forward to login page
            request.getRequestDispatcher("/WEB-INF/views/connexion.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String login = request.getParameter("login");
        String mdp = request.getParameter("password");

        Utilisateur user = authService.authenticate(login, mdp);

        if (user != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);

            auditService.logAction(user, "LOGIN", "utilisateur", user.getIdUtilisateur(), null, user, request);

            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            request.setAttribute("error", "Identifiants incorrects ou compte inactif");
            request.getRequestDispatcher("/WEB-INF/views/connexion.jsp").forward(request, response);
        }
    }
}
