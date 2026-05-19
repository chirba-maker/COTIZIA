package servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Adherent;
import models.Utilisateur;
import dao.AdherentDaoImplement;
import services.AuditService;

/**
 * Controller for Adherent Management.
 * 
 * @author Major117
 */
public class AdherentController extends HttpServlet {

    private final AdherentDaoImplement adherentDAO = new AdherentDaoImplement();
    private final AuditService auditService = new AuditService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null)
            action = "list";

        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
        // Authorized for list by filter, but double check actions
        if (("create".equals(action) || "edit".equals(action) || "delete".equals(action))
                && (user == null || user.getRole() != Utilisateur.Role.COLLECTEUR)) {
            response.sendRedirect(request.getContextPath() + "/adherents?error=unauthorized");
            return;
        }

        switch (action) {
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteAdherent(request, response);
                break;
            default:
                listAdherents(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
        if (user == null || user.getRole() != Utilisateur.Role.COLLECTEUR) {
            response.sendRedirect(request.getContextPath() + "/adherents?error=unauthorized");
            return;
        }

        String action = request.getParameter("action");
        if ("save".equals(action)) {
            saveAdherent(request, response);
        } else if ("update".equals(action)) {
            updateAdherent(request, response);
        }
    }

    private void listAdherents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String query = request.getParameter("query");
        List<Adherent> listAdherent;
        if (query != null && !query.trim().isEmpty()) {
            listAdherent = adherentDAO.search(query.trim());
            request.setAttribute("searchQuery", query);
        } else {
            listAdherent = adherentDAO.getAll();
        }
        request.setAttribute("listAdherent", listAdherent);
        request.getRequestDispatcher("/WEB-INF/views/listeadherent.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/formadherent.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Adherent existingAdherent = adherentDAO.findById(id);
        request.setAttribute("adherentEdit", existingAdherent);
        request.getRequestDispatcher("/WEB-INF/views/formadherent.jsp").forward(request, response);
    }

    private void saveAdherent(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Utilisateur currentUser = (Utilisateur) request.getSession().getAttribute("user");
            Adherent a = mapRequestToAdherent(request);
            if (currentUser != null) {
                a.setIdUtilisateur(currentUser.getIdUtilisateur());
            }

            // Check ID uniqueness
            if (adherentDAO.findByNumeroIdentification(a.getNumeroIdentification()) != null) {
                request.getSession().setAttribute("error",
                        "Ce N° d'identification (" + a.getNumeroIdentification() + ") est déjà utilisé.");
                response.sendRedirect(request.getContextPath() + "/adherents?action=create");
                return;
            }

            adherentDAO.save(a);

            auditService.logAction(currentUser, "CREATE_ADHERENT", "adherent", a.getIdAdherent(), null, a, request);

            // Auto-filter by ID after creation
            response.sendRedirect(request.getContextPath() + "/adherents?query=" + a.getNumeroIdentification());
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Erreur lors de la création : " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/adherents?action=create");
        }
    }

    private void updateAdherent(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Utilisateur currentUser = (Utilisateur) request.getSession().getAttribute("user");
            int id = Integer.parseInt(request.getParameter("id"));
            Adherent oldA = adherentDAO.findById(id);

            Adherent a = mapRequestToAdherent(request);
            a.setIdAdherent(id);
            if (oldA != null) {
                a.setIdUtilisateur(oldA.getIdUtilisateur());
            } else if (currentUser != null) {
                a.setIdUtilisateur(currentUser.getIdUtilisateur());
            }

            // Check ID uniqueness if changed
            if (!a.getNumeroIdentification().equals(oldA.getNumeroIdentification())) {
                if (adherentDAO.findByNumeroIdentification(a.getNumeroIdentification()) != null) {
                    request.getSession().setAttribute("error",
                            "Ce N° d'identification (" + a.getNumeroIdentification() + ") est déjà utilisé.");
                    response.sendRedirect(request.getContextPath() + "/adherents?action=edit&id=" + id);
                    return;
                }
            }

            adherentDAO.update(a);

            auditService.logAction(currentUser, "UPDATE_ADHERENT", "adherent", id, oldA, a, request);

            // Auto-filter by ID after update
            response.sendRedirect(request.getContextPath() + "/adherents?query=" + a.getNumeroIdentification());
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/adherents");
        }
    }

    private void deleteAdherent(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Adherent a = adherentDAO.findById(id);
            adherentDAO.delete(a);

            Utilisateur currentUser = (Utilisateur) request.getSession().getAttribute("user");
            auditService.logAction(currentUser, "DELETE_ADHERENT", "adherent", id, a, null, request);

            response.sendRedirect(request.getContextPath() + "/adherents");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/adherents");
        }
    }

    private Adherent mapRequestToAdherent(HttpServletRequest request) {
        Adherent a = new Adherent();
        a.setNumeroIdentification(request.getParameter("numero_identification"));
        a.setNom(request.getParameter("nom"));
        a.setPrenom(request.getParameter("prenom"));
        a.setDateNaissance(Date.valueOf(request.getParameter("date_naissance")));
        a.setAdresse(request.getParameter("adresse"));
        a.setTelephone(request.getParameter("telephone"));
        a.setEmail(request.getParameter("email"));
        a.setProfession(request.getParameter("profession"));
        a.setEmployeur(request.getParameter("employeur"));
        String revenus = request.getParameter("revenus_estimes");
        if (revenus != null && !revenus.isEmpty()) {
            a.setRevenusEstimes(new BigDecimal(revenus));
        }
        String statut = request.getParameter("statut");
        if (statut != null) {
            a.setStatut(Adherent.Statut.valueOf(statut.toUpperCase()));
        } else {
            a.setStatut(Adherent.Statut.ACTIF);
        }
        return a;
    }
}
