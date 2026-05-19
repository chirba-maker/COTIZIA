package servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Cycle;
import models.Collecteur;
import models.Utilisateur;
import dao.CycleDaoImplement;
import dao.CollecteurDaoImplement;
import services.AuditService;

/**
 * Controller for Cycle Management.
 * 
 * @author Major117
 */
public class CycleController extends HttpServlet {

    private final CycleDaoImplement cycleDAO = new CycleDaoImplement();
    private final CollecteurDaoImplement collecteurDAO = new CollecteurDaoImplement();
    private final dao.ParticipantDaoImplement participantDAO = new dao.ParticipantDaoImplement();
    private final services.EcheanceService echeanceService = new services.EcheanceService();
    private final AuditService auditService = new AuditService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null)
            action = "list";

        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
        // Authorized for list by filter, but check actions
        if (("create".equals(action) || "edit".equals(action) || "delete".equals(action))
                && (user == null || (user.getRole() != Utilisateur.Role.ADMIN
                        && user.getRole() != Utilisateur.Role.COLLECTEUR))) {
            response.sendRedirect(request.getContextPath() + "/cycles?error=unauthorized");
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
                deleteCycle(request, response);
                break;
            case "toggleSupport":
                toggleSupport(request, response);
                break;
            default:
                listCycles(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
        if (user == null
                || (user.getRole() != Utilisateur.Role.ADMIN && user.getRole() != Utilisateur.Role.COLLECTEUR)) {
            response.sendRedirect(request.getContextPath() + "/cycles?error=unauthorized");
            return;
        }

        String action = request.getParameter("action");
        if ("save".equals(action)) {
            saveCycle(request, response);
        } else if ("update".equals(action)) {
            updateCycle(request, response);
        }
    }

    private void listCycles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Cycle> listCycle = cycleDAO.getAll();
        request.setAttribute("listCycle", listCycle);
        request.getRequestDispatcher("/WEB-INF/views/listecycle.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Collecteur> listCollecteur = collecteurDAO.getAll();
        request.setAttribute("listCollecteur", listCollecteur);
        request.getRequestDispatcher("/WEB-INF/views/formcycle.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Cycle existingCycle = cycleDAO.findById(id);
        List<Collecteur> listCollecteur = collecteurDAO.getAll();
        request.setAttribute("listCollecteur", listCollecteur);
        request.setAttribute("cycleEdit", existingCycle);
        request.getRequestDispatcher("/WEB-INF/views/formcycle.jsp").forward(request, response);
    }

    private void saveCycle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Cycle c = mapRequestToCycle(request);
            cycleDAO.save(c);

            Utilisateur currentUser = (Utilisateur) request.getSession().getAttribute("user");
            auditService.logAction(currentUser, "CREATE_CYCLE", "cycle", c.getIdCycle(), null, c, request);

            response.sendRedirect(request.getContextPath() + "/cycles");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Erreur lors de la création : " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/cycles?action=create");
        }
    }

    private void updateCycle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Cycle oldC = cycleDAO.findById(id);

            Cycle c = mapRequestToCycle(request);
            c.setIdCycle(id);
            cycleDAO.update(c);

            // Generate installments if newly activated
            if (oldC.getStatut() != Cycle.Statut.ACTIF && c.getStatut() == Cycle.Statut.ACTIF) {
                List<models.Participant> participants = participantDAO.findByCycle(id);
                echeanceService.generateEcheancesForCycle(c, participants);
            }

            Utilisateur currentUser = (Utilisateur) request.getSession().getAttribute("user");
            auditService.logAction(currentUser, "UPDATE_CYCLE", "cycle", id, oldC, c, request);

            response.sendRedirect(request.getContextPath() + "/cycles");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/cycles");
        }
    }

    private void deleteCycle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Cycle c = cycleDAO.findById(id);
            cycleDAO.delete(c);

            Utilisateur currentUser = (Utilisateur) request.getSession().getAttribute("user");
            auditService.logAction(currentUser, "DELETE_CYCLE", "cycle", id, c, null, request);

            response.sendRedirect(request.getContextPath() + "/cycles");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/cycles");
        }
    }

    private void toggleSupport(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int idCycle = Integer.parseInt(request.getParameter("idCycle"));
            Cycle cycle = cycleDAO.findById(idCycle);
            Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");

            if (cycle != null && user != null && user.getRole() == Utilisateur.Role.COLLECTEUR) {
                Collecteur coll = collecteurDAO.findByUserId(user.getIdUtilisateur());
                if (coll != null && coll.getIdCollecteur() == cycle.getIdCollecteur()) {
                    cycle.setSupportDemande(!cycle.isSupportDemande());
                    cycleDAO.update(cycle);
                    auditService.logAction(user, "TOGGLE_SUPPORT", "cycle", idCycle, null, cycle, request);
                }
            }
            response.sendRedirect(request.getContextPath() + "/participant?idCycle=" + idCycle);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/cycles");
        }
    }

    private Cycle mapRequestToCycle(HttpServletRequest request) {
        Cycle c = new Cycle();
        c.setIdCollecteur(Integer.parseInt(request.getParameter("id_collecteur")));
        c.setLibelle(request.getParameter("libelle"));
        c.setDescription(request.getParameter("description"));
        c.setMontantCotisation(new BigDecimal(request.getParameter("montant_cotisation")));
        c.setFrequence(Cycle.Frequence.valueOf(request.getParameter("frequence").toUpperCase()));
        c.setDateDebut(Date.valueOf(request.getParameter("date_debut")));
        c.setNombreTours(Integer.parseInt(request.getParameter("nombre_tours")));
        String statut = request.getParameter("statut");
        if (statut != null) {
            c.setStatut(Cycle.Statut.valueOf(statut.toUpperCase()));
        } else {
            c.setStatut(Cycle.Statut.CREE);
        }
        return c;
    }
}
