package servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Paiement;
import models.Echeance;
import models.Utilisateur;
import models.Cycle;
import models.Collecteur;
import dao.PaiementDaoImplement;
import dao.EcheanceDaoImplement;
import dao.CycleDaoImplement;
import dao.CollecteurDaoImplement;
import services.AuditService;

/**
 * Controller for Managing Payments.
 * 
 * @author Major117
 */
public class PaiementController extends HttpServlet {

    private final PaiementDaoImplement paiementDAO = new PaiementDaoImplement();
    private final EcheanceDaoImplement echeanceDAO = new EcheanceDaoImplement();
    private final CycleDaoImplement cycleDAO = new CycleDaoImplement();
    private final CollecteurDaoImplement collecteurDAO = new CollecteurDaoImplement();
    private final AuditService auditService = new AuditService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");

        if ("pay".equals(action)) {
            if (user == null || user.getRole() != Utilisateur.Role.COLLECTEUR) {
                request.getSession().setAttribute("error", "Accès refusé. L'enregistrement des paiements est la responsabilité exclusive des collecteurs.");
                response.sendRedirect(request.getContextPath() + "/paiements?error=unauthorized");
                return;
            }
            int idEcheance = Integer.parseInt(request.getParameter("idEcheance"));
            Echeance echeance = echeanceDAO.findById(idEcheance);
            if (echeance == null) {
                response.sendRedirect(request.getContextPath() + "/paiements?error=notfound");
                return;
            }
            Cycle cycle = cycleDAO.findById(echeance.getIdCycle());
            Collecteur collecteur = collecteurDAO.findByUserId(user.getIdUtilisateur());
            if (cycle == null || collecteur == null || collecteur.getIdCollecteur() != cycle.getIdCollecteur()) {
                request.getSession().setAttribute("error", "Accès refusé. Vous ne pouvez enregistrer des paiements que pour les zones/cycles dont vous êtes responsable.");
                response.sendRedirect(request.getContextPath() + "/echeances?idCycle=" + echeance.getIdCycle() + "&error=unauthorized");
                return;
            }
            showPaymentForm(request, response);
        } else if (request.getParameter("idEcheance") != null && !request.getParameter("idEcheance").trim().isEmpty()) {
            listPaiementsByEcheance(request, response);
        } else {
            listAllPaiements(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
        if (user == null || user.getRole() != Utilisateur.Role.COLLECTEUR) {
            request.getSession().setAttribute("error", "Accès refusé. L'enregistrement des paiements est la responsabilité exclusive des collecteurs.");
            response.sendRedirect(request.getContextPath() + "/paiements?error=unauthorized");
            return;
        }

        String action = request.getParameter("action");
        if ("save".equals(action)) {
            int idEcheance = Integer.parseInt(request.getParameter("idEcheance"));
            Echeance echeance = echeanceDAO.findById(idEcheance);
            if (echeance == null) {
                response.sendRedirect(request.getContextPath() + "/paiements?error=notfound");
                return;
            }
            Cycle cycle = cycleDAO.findById(echeance.getIdCycle());
            Collecteur collecteur = collecteurDAO.findByUserId(user.getIdUtilisateur());
            if (cycle == null || collecteur == null || collecteur.getIdCollecteur() != cycle.getIdCollecteur()) {
                request.getSession().setAttribute("error", "Accès refusé. Vous ne pouvez enregistrer des paiements que pour les zones/cycles dont vous êtes responsable.");
                response.sendRedirect(request.getContextPath() + "/echeances?idCycle=" + echeance.getIdCycle() + "&error=unauthorized");
                return;
            }
            savePaiement(request, response);
        }
    }

    private void listAllPaiements(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Paiement> listPaiement = paiementDAO.getAll();
        request.setAttribute("listPaiement", listPaiement);
        request.getRequestDispatcher("/WEB-INF/views/listepaiement.jsp").forward(request, response);
    }

    private void listPaiementsByEcheance(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idEcheance = Integer.parseInt(request.getParameter("idEcheance"));
        List<Paiement> listPaiement = paiementDAO.findByEcheance(idEcheance);
        request.setAttribute("listPaiement", listPaiement);
        request.getRequestDispatcher("/WEB-INF/views/listepaiement.jsp").forward(request, response);
    }

    private void showPaymentForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idEcheance = Integer.parseInt(request.getParameter("idEcheance"));
        Echeance echeance = echeanceDAO.findById(idEcheance);
        request.setAttribute("echeance", echeance);
        request.getRequestDispatcher("/WEB-INF/views/formpaiement.jsp").forward(request, response);
    }

    private void savePaiement(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int idEcheance = Integer.parseInt(request.getParameter("idEcheance"));
            BigDecimal montant = new BigDecimal(request.getParameter("montant"));
            Paiement.ModePaiement mode = Paiement.ModePaiement
                    .valueOf(request.getParameter("mode_paiement").toUpperCase());
            String reference = request.getParameter("reference");
            String note = request.getParameter("note");

            Utilisateur currentUser = (Utilisateur) request.getSession().getAttribute("user");

            Paiement p = new Paiement();
            p.setIdEcheance(idEcheance);
            p.setIdUtilisateur(currentUser.getIdUtilisateur());
            p.setMontant(montant);
            p.setModePaiement(mode);
            p.setReference(reference);
            p.setNote(note);

            paiementDAO.save(p);

            // Update Echeance status and paid amount
            Echeance e = echeanceDAO.findById(idEcheance);
            e.setMontantPaye(e.getMontantPaye().add(montant));
            if (e.getMontantPaye().compareTo(e.getMontantDu()) >= 0) {
                e.setStatut(Echeance.Statut.PAYE);
                e.setDatePaiement(new Timestamp(System.currentTimeMillis()));
            } else {
                e.setStatut(Echeance.Statut.PARTIEL);
            }
            echeanceDAO.update(e);

            auditService.logAction(currentUser, "SAVE_PAIEMENT", "paiement", p.getIdPaiement(), null, p, request);

            response.sendRedirect(request.getContextPath() + "/echeances?idCycle=" + e.getIdCycle());
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Erreur lors du paiement : " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/paiements");
        }
    }
}
