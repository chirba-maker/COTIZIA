package servlets;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Utilisateur;
import dao.UtilisateurDaoImplement;
import services.AuthService;
import services.AuditService;

/**
 * Controller for User Management.
 * 
 * @author Major117
 */
public class UtilisateurController extends HttpServlet {

    private final UtilisateurDaoImplement userDAO = new UtilisateurDaoImplement();
    private final AuthService authService = new AuthService();
    private final AuditService auditService = new AuditService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
        if (user == null || user.getRole() != Utilisateur.Role.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String action = request.getParameter("action");
        if (action == null)
            action = "list";

        switch (action) {
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteUser(request, response);
                break;
            default:
                listUsers(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
        if (user == null || user.getRole() != Utilisateur.Role.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String action = request.getParameter("action");
        if ("save".equals(action)) {
            saveUser(request, response);
        } else if ("update".equals(action)) {
            updateUser(request, response);
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Utilisateur> listUser = userDAO.getAll();
        request.setAttribute("listUser", listUser);
        request.getRequestDispatcher("/WEB-INF/views/listeutilisateur.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/formutilisateur.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Utilisateur existingUser = userDAO.findById(id);
        request.setAttribute("userEdit", existingUser);
        request.getRequestDispatcher("/WEB-INF/views/formutilisateur.jsp").forward(request, response);
    }

    private void saveUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String nom = request.getParameter("nom");
            String prenom = request.getParameter("prenom");
            String login = request.getParameter("login");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            Utilisateur.Role role = Utilisateur.Role.valueOf(request.getParameter("role").toUpperCase());

            Utilisateur newUser = new Utilisateur();
            newUser.setNom(nom);
            newUser.setPrenom(prenom);
            newUser.setLogin(login);
            newUser.setEmail(email);
            newUser.setMotDePasse(authService.hashPassword(password));
            newUser.setRole(role);
            newUser.setActif(true);

            userDAO.save(newUser);

            Utilisateur currentUser = (Utilisateur) request.getSession().getAttribute("user");
            auditService.logAction(currentUser, "CREATE_USER", "utilisateur", newUser.getIdUtilisateur(), null, newUser,
                    request);

            response.sendRedirect(request.getContextPath() + "/utilisateurs");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Erreur lors de la création : " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/utilisateurs?action=create");
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String nom = request.getParameter("nom");
            String prenom = request.getParameter("prenom");
            String login = request.getParameter("login");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            Utilisateur.Role role = Utilisateur.Role.valueOf(request.getParameter("role").toUpperCase());
            boolean actif = request.getParameter("actif") != null;

            Utilisateur user = userDAO.findById(id);
            Utilisateur oldUser = new Utilisateur(user.getIdUtilisateur(), user.getNom(), user.getPrenom(),
                    user.getLogin(),
                    user.getEmail(), user.getMotDePasse(), user.getRole(), user.isActif(), user.getDateCreation(),
                    user.getDateModification());

            user.setNom(nom);
            user.setPrenom(prenom);
            user.setLogin(login);
            user.setEmail(email);

            // Only update and hash password if a new one is provided
            if (password != null && !password.trim().isEmpty()) {
                user.setMotDePasse(authService.hashPassword(password));
            }

            user.setRole(role);
            user.setActif(actif);

            userDAO.update(user);

            Utilisateur currentUser = (Utilisateur) request.getSession().getAttribute("user");
            auditService.logAction(currentUser, "UPDATE_USER", "utilisateur", user.getIdUtilisateur(), oldUser, user,
                    request);

            response.sendRedirect(request.getContextPath() + "/utilisateurs");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
            response.sendRedirect(
                    request.getContextPath() + "/utilisateurs?action=edit&id=" + request.getParameter("id"));
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Utilisateur user = userDAO.findById(id);
            userDAO.delete(user);

            Utilisateur currentUser = (Utilisateur) request.getSession().getAttribute("user");
            auditService.logAction(currentUser, "DELETE_USER", "utilisateur", id, user, null, request);

            response.sendRedirect(request.getContextPath() + "/utilisateurs");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/utilisateurs");
        }
    }
}
