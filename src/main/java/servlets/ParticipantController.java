package servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Participant;
import models.Cycle;
import models.Adherent;
import models.Utilisateur;
import dao.ParticipantDaoImplement;
import dao.CycleDaoImplement;
import dao.AdherentDaoImplement;
import services.AuditService;

/**
 * Controller for Managing Participants in a Cycle.
 * 
 * @author Major117
 */
public class ParticipantController extends HttpServlet {

    private final ParticipantDaoImplement participantDAO = new ParticipantDaoImplement();
    private final CycleDaoImplement cycleDAO = new CycleDaoImplement();
    private final AdherentDaoImplement adherentDAO = new AdherentDaoImplement();
    private final AuditService auditService = new AuditService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String idCycleParam = request.getParameter("idCycle");
        if (idCycleParam == null || idCycleParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cycles");
            return;
        }
        int idCycle = Integer.parseInt(idCycleParam);
        Cycle cycle = cycleDAO.findById(idCycle);
        if (cycle == null) {
            response.sendRedirect(request.getContextPath() + "/cycles");
            return;
        }

        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
        models.Collecteur loggedInCollecteur = null;
        if (user != null && user.getRole() == Utilisateur.Role.COLLECTEUR) {
            loggedInCollecteur = new dao.CollecteurDaoImplement().findByUserId(user.getIdUtilisateur());
            if (loggedInCollecteur != null) {
                request.setAttribute("loggedInCollecteur", loggedInCollecteur);
            }
        }

        boolean canManageParticipants = false;
        if (user != null) {
            if (user.getRole() == Utilisateur.Role.COLLECTEUR) {
                if (loggedInCollecteur != null && loggedInCollecteur.getIdCollecteur() == cycle.getIdCollecteur()) {
                    canManageParticipants = true;
                }
            } else if (user.getRole() == Utilisateur.Role.ADMIN) {
                if (cycle.isSupportDemande()) {
                    canManageParticipants = true;
                }
            }
        }
        request.setAttribute("canManageParticipants", canManageParticipants);

        if (("add".equals(action) || "delete".equals(action)) && !canManageParticipants) {
            request.getSession().setAttribute("error", "Accès refusé. Action réservée au Collecteur responsable du cycle (ou à l'Admin en mode support activé).");
            response.sendRedirect(request.getContextPath() + "/participant?idCycle=" + idCycle + "&error=unauthorized");
            return;
        }

        if ("add".equals(action)) {
            showAddParticipantForm(idCycle, request, response);
        } else if ("delete".equals(action)) {
            deleteParticipant(request, response);
        } else {
            listParticipants(idCycle, request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
        String idCycleParam = request.getParameter("idCycle");
        if (idCycleParam == null || idCycleParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cycles");
            return;
        }
        int idCycle = Integer.parseInt(idCycleParam);
        Cycle cycle = cycleDAO.findById(idCycle);
        if (cycle == null) {
            response.sendRedirect(request.getContextPath() + "/cycles");
            return;
        }

        boolean canManageParticipants = false;
        if (user != null) {
            if (user.getRole() == Utilisateur.Role.COLLECTEUR) {
                models.Collecteur loggedInCollecteur = new dao.CollecteurDaoImplement().findByUserId(user.getIdUtilisateur());
                if (loggedInCollecteur != null && loggedInCollecteur.getIdCollecteur() == cycle.getIdCollecteur()) {
                    canManageParticipants = true;
                }
            } else if (user.getRole() == Utilisateur.Role.ADMIN) {
                if (cycle.isSupportDemande()) {
                    canManageParticipants = true;
                }
            }
        }

        if (!canManageParticipants) {
            request.getSession().setAttribute("error", "Accès refusé. Action réservée au Collecteur responsable du cycle (ou à l'Admin en mode support activé).");
            response.sendRedirect(request.getContextPath() + "/participant?idCycle=" + idCycle + "&error=unauthorized");
            return;
        }

        String action = request.getParameter("action");
        if ("save".equals(action)) {
            saveParticipant(request, response);
        }
    }

    private void listParticipants(int idCycle, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cycle cycle = cycleDAO.findById(idCycle);
        if (cycle != null) {
            cycle.setCollecteur(new dao.CollecteurDaoImplement().findById(cycle.getIdCollecteur()));
        }
        List<Participant> participants = participantDAO.findByCycle(idCycle);

        // Fetch adherent details for each participant
        for (Participant p : participants) {
            p.setAdherent(adherentDAO.findById(p.getIdAdherent()));
        }

        request.setAttribute("cycle", cycle);
        request.setAttribute("listParticipant", participants);
        request.getRequestDispatcher("/WEB-INF/views/listeparticipant.jsp").forward(request, response);
    }

    private void showAddParticipantForm(int idCycle, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cycle cycle = cycleDAO.findById(idCycle);
        List<Adherent> adherents = adherentDAO.getAll();

        request.setAttribute("cycle", cycle);
        request.setAttribute("listAdherent", adherents);
        request.getRequestDispatcher("/WEB-INF/views/formparticipant.jsp").forward(request, response);
    }

    private void saveParticipant(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idCycle = Integer.parseInt(request.getParameter("idCycle"));
        try {
            int idAdherent = Integer.parseInt(request.getParameter("id_adherent"));
            int ordre = Integer.parseInt(request.getParameter("numero_ordre"));

            // 1. Check if this adherent is already enrolled in this cycle
            List<Participant> participants = participantDAO.findByCycle(idCycle);
            for (Participant existing : participants) {
                if (existing.getIdAdherent() == idAdherent) {
                    request.getSession().setAttribute("error", "Cet adhérent est déjà inscrit à ce cycle.");
                    response.sendRedirect(request.getContextPath() + "/participant?action=add&idCycle=" + idCycle);
                    return;
                }
            }

            // 2. Check if this order number is already taken in this cycle
            for (Participant existing : participants) {
                if (existing.getNumeroOrdre() == ordre) {
                    request.getSession().setAttribute("error", "Le numéro d'ordre " + ordre + " est déjà attribué à un autre participant de ce cycle.");
                    response.sendRedirect(request.getContextPath() + "/participant?action=add&idCycle=" + idCycle);
                    return;
                }
            }

            Participant p = new Participant();
            p.setIdCycle(idCycle);
            p.setIdAdherent(idAdherent);
            p.setNumeroOrdre(ordre);
            p.setStatut(Participant.Statut.INSCRIT);
            p.setMontantRecu(BigDecimal.ZERO);

            participantDAO.save(p);

            Utilisateur currentUser = (Utilisateur) request.getSession().getAttribute("user");
            auditService.logAction(currentUser, "ADD_PARTICIPANT", "participant", p.getIdParticipant(), null, p,
                    request);

            response.sendRedirect(request.getContextPath() + "/participant?idCycle=" + idCycle);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getMessage();
            if (msg != null && msg.contains("Duplicate entry")) {
                if (msg.contains("uq_participant_cycle_adherent")) {
                    msg = "Cet adhérent est déjà inscrit à ce cycle.";
                } else if (msg.contains("uq_participant_ordre")) {
                    msg = "Ce numéro d'ordre est déjà attribué à un autre participant.";
                }
            }
            request.getSession().setAttribute("error", "Erreur lors de l'ajout : " + msg);
            response.sendRedirect(request.getContextPath() + "/participant?action=add&idCycle=" + idCycle);
        }
    }

    private void deleteParticipant(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idCycle = Integer.parseInt(request.getParameter("idCycle"));
        try {
            int id = Integer.parseInt(request.getParameter("id"));

            Participant p = participantDAO.findById(id);
            participantDAO.delete(p);

            Utilisateur currentUser = (Utilisateur) request.getSession().getAttribute("user");
            auditService.logAction(currentUser, "REMOVE_PARTICIPANT", "participant", id, p, null, request);

            response.sendRedirect(request.getContextPath() + "/participant?idCycle=" + idCycle);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/participant?idCycle=" + idCycle);
        }
    }
}
