package services;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import dao.EcheanceDaoImplement;
import models.Cycle;
import models.Echeance;
import models.Participant;

/**
 * Service for managing installments (Echéances).
 * 
 * @author Major117
 */
public class EcheanceService {

    private final EcheanceDaoImplement echeanceDAO;

    public EcheanceService() {
        this.echeanceDAO = new EcheanceDaoImplement();
    }

    /**
     * Generates all installments for a cycle and its participants.
     */
    public void generateEcheancesForCycle(Cycle cycle, List<Participant> participants) {
        if (cycle == null || participants == null || participants.isEmpty()) {
            return;
        }

        BigDecimal montantDu = cycle.getMontantCotisation();
        int nbTours = cycle.getNombreTours();
        Date dateDebut = cycle.getDateDebut();

        for (Participant p : participants) {
            // Avoid duplicate generation for this specific participant
            if (!echeanceDAO.findByParticipant(p.getIdParticipant()).isEmpty()) {
                continue;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateDebut);

            for (int tour = 1; tour <= nbTours; tour++) {
                Echeance e = new Echeance();
                e.setIdParticipant(p.getIdParticipant());
                e.setIdCycle(cycle.getIdCycle());
                e.setNumeroTour(tour);
                e.setMontantDu(montantDu);
                e.setMontantPaye(BigDecimal.ZERO);
                e.setStatut(Echeance.Statut.EN_ATTENTE);

                // Set date based on frequency
                e.setDateEcheance(new Date(calendar.getTimeInMillis()));

                echeanceDAO.save(e);

                // Increment date for next tour
                switch (cycle.getFrequence()) {
                    case HEBDOMADAIRE:
                        calendar.add(Calendar.WEEK_OF_YEAR, 1);
                        break;
                    case BIMENSUELLE:
                        calendar.add(Calendar.DAY_OF_YEAR, 14);
                        break;
                    case MENSUELLE:
                        calendar.add(Calendar.MONTH, 1);
                        break;
                }
            }
        }
    }
}
