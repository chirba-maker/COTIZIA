package servlets;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Collecteur;
import models.Utilisateur;
import dao.CollecteurDaoImplement;
import dao.UtilisateurDaoImplement;
import services.AuditService;

/**
 * Controller for Collecteur Management.
 * 
 * @author Major117
 */
public class CollecteurController extends HttpServlet {

    private final CollecteurDaoImplement collecteurDAO = new CollecteurDaoImplement();
    private final UtilisateurDaoImplement userDAO = new UtilisateurDaoImplement();
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
                deleteCollecteur(request, response);
                break;
            default:
                listCollecteurs(request, response);
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
            saveCollecteur(request, response);
        } else if ("update".equals(action)) {
            updateCollecteur(request, response);
        }
    }

    private void listCollecteurs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Collecteur> listCollecteur = collecteurDAO.getAll();
        request.setAttribute("listCollecteur", listCollecteur);
        request.getRequestDispatcher("/WEB-INF/views/listecollecteur.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Utilisateur> listUser = userDAO.getAll();
        request.setAttribute("listUser", listUser);
        request.getRequestDispatcher("/WEB-INF/views/formcollecteur.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Collecteur existingCollecteur = collecteurDAO.findById(id);
        List<Utilisateur> listUser = userDAO.getAll();
        request.setAttribute("listUser", listUser);
        request.setAttribute("collecteurEdit", existingCollecteur);
        request.getRequestDispatcher("/WEB-INF/views/formcollecteur.jsp").forward(request, response);
    }

    private void saveCollecteur(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Collecteur c = mapRequestToCollecteur(request);
            collecteurDAO.save(c);

            Utilisateur currentUser = (Utilisateur) request.getSession().getAttribute("user");
            auditService.logAction(currentUser, "CREATE_COLLECTEUR", "collecteur", c.getIdCollecteur(), null, c,
                    request);

            response.sendRedirect(request.getContextPath() + "/collecteurs");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Erreur lors de la création : " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/collecteurs?action=create");
        }
    }

    private void updateCollecteur(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Collecteur oldC = collecteurDAO.findById(id);

            Collecteur c = mapRequestToCollecteur(request);
            c.setIdCollecteur(id);
            collecteurDAO.update(c);

            Utilisateur currentUser = (Utilisateur) request.getSession().getAttribute("user");
            auditService.logAction(currentUser, "UPDATE_COLLECTEUR", "collecteur", id, oldC, c, request);

            response.sendRedirect(request.getContextPath() + "/collecteurs");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/collecteurs");
        }
    }

    private void deleteCollecteur(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Collecteur c = collecteurDAO.findById(id);
            collecteurDAO.delete(c);

            Utilisateur currentUser = (Utilisateur) request.getSession().getAttribute("user");
            auditService.logAction(currentUser, "DELETE_COLLECTEUR", "collecteur", id, c, null, request);

            response.sendRedirect(request.getContextPath() + "/collecteurs");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/collecteurs");
        }
    }

    private Collecteur mapRequestToCollecteur(HttpServletRequest request) {
        Collecteur c = new Collecteur();
        c.setIdUtilisateur(Integer.parseInt(request.getParameter("id_utilisateur")));
        c.setNom(request.getParameter("nom"));
        c.setPrenom(request.getParameter("prenom"));
        c.setTelephone(request.getParameter("telephone"));
        c.setEmail(request.getParameter("email"));
        c.setZoneCollecte(request.getParameter("zone_collecte"));
        String statut = request.getParameter("statut");
        if (statut != null) {
            c.setStatut(Collecteur.Statut.valueOf(statut.toUpperCase()));
        } else {
            c.setStatut(Collecteur.Statut.ACTIF);
        }
        return c;
    }
}
