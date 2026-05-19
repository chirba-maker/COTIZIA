package servlets;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.PaiementDaoImplement;
import dao.EcheanceDaoImplement;
import dao.ParticipantDaoImplement;
import dao.AdherentDaoImplement;
import dao.CycleDaoImplement;
import dao.CollecteurDaoImplement;
import models.Paiement;
import models.Echeance;
import models.Participant;
import models.Cycle;

/**
 * Controller for PDF Generation and Download.
 */
@WebServlet("/export/pdf")
public class PdfController extends HttpServlet {

    private final PaiementDaoImplement paiementDAO = new PaiementDaoImplement();
    private final EcheanceDaoImplement echeanceDAO = new EcheanceDaoImplement();
    private final ParticipantDaoImplement participantDAO = new ParticipantDaoImplement();
    private final AdherentDaoImplement adherentDAO = new AdherentDaoImplement();
    private final CycleDaoImplement cycleDAO = new CycleDaoImplement();
    private final CollecteurDaoImplement collecteurDAO = new CollecteurDaoImplement();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = request.getParameter("type");
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing ID");
            return;
        }

        int id = Integer.parseInt(idParam);

        if ("receipt".equals(type)) {
            Paiement p = hydratePaiement(id);
            if (p != null) {
                request.setAttribute("paiement", p);
                request.getRequestDispatcher("/WEB-INF/views/recu_paiement.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Paiement non trouvé");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid export type");
        }
    }

    private Paiement hydratePaiement(int idPaiement) {
        Paiement p = paiementDAO.findById(idPaiement);
        if (p == null)
            return null;

        Echeance e = echeanceDAO.findById(p.getIdEcheance());
        if (e != null) {
            p.setEcheance(e);

            Cycle c = cycleDAO.findById(e.getIdCycle());
            if (c != null) {
                e.setCycle(c);
                c.setCollecteur(collecteurDAO.findById(c.getIdCollecteur()));
            }

            Participant part = participantDAO.findById(e.getIdParticipant());
            if (part != null) {
                e.setParticipant(part);
                part.setAdherent(adherentDAO.findById(part.getIdAdherent()));
            }
        }
        return p;
    }
}
