package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.InputStream;
import java.util.Base64;

import models.Utilisateur;
import dao.UtilisateurDaoImplement;
import services.AuthService;
import services.AuditService;

/**
 * Controller for User Profile Management.
 * Allows any logged-in user to update their own info and password.
 */
public class ProfileController extends HttpServlet {

    private final UtilisateurDaoImplement userDAO = new UtilisateurDaoImplement();
    private final AuthService authService = new AuthService();
    private final AuditService auditService = new AuditService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        // Refresh user data from DB to ensure it's up to date
        Utilisateur updatedUser = userDAO.findById(user.getIdUtilisateur());
        request.getSession().setAttribute("user", updatedUser);

        request.getRequestDispatcher("/WEB-INF/views/monprofil.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Utilisateur sessionUser = (Utilisateur) request.getSession().getAttribute("user");
        if (sessionUser == null) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        try {
            String nom = request.getParameter("nom");
            String prenom = request.getParameter("prenom");
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            // Fetch current user from DB
            Utilisateur user = userDAO.findById(sessionUser.getIdUtilisateur());

            // Backup for audit
            Utilisateur oldUser = new Utilisateur(user.getIdUtilisateur(), user.getNom(), user.getPrenom(),
                    user.getLogin(), user.getEmail(), user.getMotDePasse(), user.getRole(),
                    user.isActif(), user.getDateCreation(), user.getDateModification());

            // Update fields
            user.setNom(nom);
            user.setPrenom(prenom);
            user.setEmail(email);

            // Update password if provided
            if (password != null && !password.trim().isEmpty()) {
                user.setMotDePasse(authService.hashPassword(password));
            }

            // Handle file upload
            Part filePart = request.getPart("photo");
            if (filePart != null && filePart.getSize() > 0) {
                try (InputStream inputStream = filePart.getInputStream()) {
                    byte[] imageBytes = inputStream.readAllBytes();
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    String mimeType = filePart.getContentType();
                    if (mimeType != null && mimeType.startsWith("image/")) {
                        user.setPhoto("data:" + mimeType + ";base64," + base64Image);
                    } else {
                        request.getSession().setAttribute("error", "Le fichier importé n'est pas une image valide.");
                        response.sendRedirect(request.getContextPath() + "/profile");
                        return;
                    }
                }
            }

            userDAO.update(user);

            // Update session user
            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("success", "Votre profil a été mis à jour avec succès !");

            // Audit Log
            auditService.logAction(user, "UPDATE_PROFILE", "utilisateur", user.getIdUtilisateur(), oldUser, user,
                    request);

            response.sendRedirect(request.getContextPath() + "/profile");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/profile");
        }
    }
}
