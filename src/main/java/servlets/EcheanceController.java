package servlets;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Echeance;
import models.Cycle;
import models.Participant;
import models.Utilisateur;
import dao.EcheanceDaoImplement;
import dao.CycleDaoImplement;
import dao.ParticipantDaoImplement;
import dao.AdherentDaoImplement;
import services.EcheanceService;
import services.AuditService;

/**
 * Controller for Managing Installments (Echéances).
 * 
 * @author Major117
 */
public class EcheanceController extends HttpServlet {

    private final EcheanceDaoImplement echeanceDAO = new EcheanceDaoImplement();
    private final CycleDaoImplement cycleDAO = new CycleDaoImplement();
    private final ParticipantDaoImplement participantDAO = new ParticipantDaoImplement();
    private final AdherentDaoImplement adherentDAO = new AdherentDaoImplement();
    private final EcheanceService echeanceService = new EcheanceService();
    private final AuditService auditService = new AuditService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");

        if ("generate".equals(action)) {
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/dashboard?error=unauthorized");
                return;
            }
            int idCycle = Integer.parseInt(request.getParameter("idCycle"));
            Cycle cycle = cycleDAO.findById(idCycle);
            if (cycle == null) {
                response.sendRedirect(request.getContextPath() + "/cycles?error=notfound");
                return;
            }

            boolean canGenerate = false;
            if (user.getRole() == Utilisateur.Role.COLLECTEUR) {
                models.Collecteur loggedInCollecteur = new dao.CollecteurDaoImplement().findByUserId(user.getIdUtilisateur());
                if (loggedInCollecteur != null && loggedInCollecteur.getIdCollecteur() == cycle.getIdCollecteur()) {
                    canGenerate = true;
                }
            } else if (user.getRole() == Utilisateur.Role.ADMIN) {
                if (cycle.isSupportDemande()) {
                    canGenerate = true;
                } else {
                    request.getSession().setAttribute("error", "Action réservée au Collecteur responsable du cycle. L'Administrateur ne peut intervenir que si le Collecteur a activé la demande de support.");
                    response.sendRedirect(request.getContextPath() + "/participant?idCycle=" + idCycle);
                    return;
                }
            }

            if (!canGenerate) {
                response.sendRedirect(request.getContextPath() + "/dashboard?error=unauthorized");
                return;
            }
            generateEcheances(request, response);
        } else if (request.getParameter("idCycle") != null && !request.getParameter("idCycle").trim().isEmpty()) {
            listEcheancesByCycle(request, response);
        } else {
            listAllEcheances(request, response);
        }
    }

    private void listAllEcheances(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Echeance> listEcheance = echeanceDAO.getAll();
        
        // Populate display data (participant, adherent and cycle) for each echeance
        for (Echeance e : listEcheance) {
            Participant p = participantDAO.findById(e.getIdParticipant());
            if (p != null) {
                e.setParticipant(p);
                p.setAdherent(adherentDAO.findById(p.getIdAdherent()));
            }
            e.setCycle(cycleDAO.findById(e.getIdCycle()));
        }
        
        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
        if (user != null && user.getRole() == Utilisateur.Role.COLLECTEUR) {
            models.Collecteur loggedInCollecteur = new dao.CollecteurDaoImplement().findByUserId(user.getIdUtilisateur());
            if (loggedInCollecteur != null) {
                request.setAttribute("loggedInCollecteur", loggedInCollecteur);
            }
        }
        
        request.setAttribute("listEcheance", listEcheance);
        request.getRequestDispatcher("/WEB-INF/views/listeecheance.jsp").forward(request, response);
    }

    private void listEcheancesByCycle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idCycle = Integer.parseInt(request.getParameter("idCycle"));
        Cycle cycle = cycleDAO.findById(idCycle);

        List<Echeance> listEcheance = echeanceDAO.findByCycle(idCycle);

        // Populate display data
        for (Echeance e : listEcheance) {
            Participant p = participantDAO.findById(e.getIdParticipant());
            if (p != null) {
                e.setParticipant(p);
                p.setAdherent(adherentDAO.findById(p.getIdAdherent()));
            }
            e.setCycle(cycle);
        }

        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
        if (user != null && user.getRole() == Utilisateur.Role.COLLECTEUR) {
            models.Collecteur loggedInCollecteur = new dao.CollecteurDaoImplement().findByUserId(user.getIdUtilisateur());
            if (loggedInCollecteur != null) {
                request.setAttribute("loggedInCollecteur", loggedInCollecteur);
            }
        }

        request.setAttribute("cycle", cycle);
        request.setAttribute("listEcheance", listEcheance);
        request.getRequestDispatcher("/WEB-INF/views/listeecheance.jsp").forward(request, response);
    }

    private void generateEcheances(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idCycle = Integer.parseInt(request.getParameter("idCycle"));
        try {
            Cycle cycle = cycleDAO.findById(idCycle);
            List<Participant> participants = participantDAO.findByCycle(idCycle);

            echeanceService.generateEcheancesForCycle(cycle, participants);

            Utilisateur currentUser = (Utilisateur) request.getSession().getAttribute("user");
            auditService.logAction(currentUser, "GENERATE_ECHEANCES", "cycle", idCycle, null, null, request);

            response.sendRedirect(request.getContextPath() + "/echeances?idCycle=" + idCycle);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Erreur lors de la génération : " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/echeances?idCycle=" + idCycle);
        }
    }
}
