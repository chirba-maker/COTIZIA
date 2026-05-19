package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MouchardDaoImplement;
import models.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Service for Audit Logging (Mouchard).
 * Converts model objects to flat maps before JSON serialization
 * to avoid circular reference issues.
 * 
 * @author Major117
 */
public class AuditService {

    private final MouchardDaoImplement mouchardDAO;
    private final ObjectMapper objectMapper;

    public AuditService() {
        this.mouchardDAO = new MouchardDaoImplement();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);
    }

    public void logAction(Utilisateur user, String action, String entite, int idEntite, Object avant, Object apres,
            HttpServletRequest request) {
        Mouchard m = new Mouchard();
        if (user != null) {
            m.setIdUtilisateur(user.getIdUtilisateur());
        }
        m.setAction(action);
        m.setEntite(entite);
        m.setIdEntite(idEntite);

        try {
            if (avant != null) {
                Map<String, Object> map = toFlatMap(avant);
                String json = objectMapper.writeValueAsString(map);
                m.setDetailAvant(json);
                System.out.println("[AUDIT DEBUG] avant JSON (" + action + "): " + json);
            }
            if (apres != null) {
                Map<String, Object> map = toFlatMap(apres);
                String json = objectMapper.writeValueAsString(map);
                m.setDetailApres(json);
                System.out.println("[AUDIT DEBUG] apres JSON (" + action + "): " + json);
            }
        } catch (Exception e) {
            System.err.println("[AUDIT ERROR] Serialization failed for " + action + ": " + e.getMessage());
            // Fallback: use toString() if serialization fails
            try {
                if (avant != null && m.getDetailAvant() == null) {
                    m.setDetailAvant("{\"description\": \"" + escapeJson(avant.toString()) + "\"}");
                }
                if (apres != null && m.getDetailApres() == null) {
                    m.setDetailApres("{\"description\": \"" + escapeJson(apres.toString()) + "\"}");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

        if (request != null) {
            m.setAdresseIp(request.getRemoteAddr());
            m.setUserAgent(request.getHeader("User-Agent"));
        }

        System.out.println("[AUDIT DEBUG] Saving mouchard: action=" + m.getAction()
                + " detailAvant="
                + (m.getDetailAvant() != null
                        ? m.getDetailAvant().substring(0, Math.min(100, m.getDetailAvant().length()))
                        : "null")
                + " detailApres="
                + (m.getDetailApres() != null
                        ? m.getDetailApres().substring(0, Math.min(100, m.getDetailApres().length()))
                        : "null"));

        mouchardDAO.save(m);
    }

    /**
     * Converts a model object to a flat map of primitive/string properties,
     * avoiding circular references from nested objects.
     */
    private Map<String, Object> toFlatMap(Object obj) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        if (obj instanceof Utilisateur) {
            Utilisateur u = (Utilisateur) obj;
            map.put("idUtilisateur", u.getIdUtilisateur());
            map.put("nom", u.getNom());
            map.put("prenom", u.getPrenom());
            map.put("login", u.getLogin());
            map.put("email", u.getEmail());
            map.put("role", u.getRole() != null ? u.getRole().name() : null);
            map.put("actif", u.isActif());
            map.put("dateCreation", str(u.getDateCreation()));
        } else if (obj instanceof Paiement) {
            Paiement p = (Paiement) obj;
            map.put("idPaiement", p.getIdPaiement());
            map.put("idEcheance", p.getIdEcheance());
            map.put("idUtilisateur", p.getIdUtilisateur());
            map.put("montant", p.getMontant() != null ? p.getMontant().toPlainString() : null);
            map.put("modePaiement", p.getModePaiement() != null ? p.getModePaiement().name() : null);
            map.put("reference", p.getReference());
            map.put("note", p.getNote());
            map.put("datePaiement", str(p.getDatePaiement()));
        } else if (obj instanceof Participant) {
            Participant p = (Participant) obj;
            map.put("idParticipant", p.getIdParticipant());
            map.put("idCycle", p.getIdCycle());
            map.put("idAdherent", p.getIdAdherent());
            map.put("numeroOrdre", p.getNumeroOrdre());
            map.put("statut", p.getStatut() != null ? p.getStatut().name() : null);
            map.put("montantRecu", p.getMontantRecu() != null ? p.getMontantRecu().toPlainString() : null);
            map.put("dateInscription", str(p.getDateInscription()));
            if (p.getAdherent() != null) {
                map.put("adherent", p.getAdherent().getNomComplet());
            }
        } else if (obj instanceof Cycle) {
            Cycle c = (Cycle) obj;
            map.put("idCycle", c.getIdCycle());
            map.put("libelle", c.getLibelle());
            map.put("description", c.getDescription());
            map.put("montantCotisation",
                    c.getMontantCotisation() != null ? c.getMontantCotisation().toPlainString() : null);
            map.put("frequence", c.getFrequence() != null ? c.getFrequence().name() : null);
            map.put("dateDebut", str(c.getDateDebut()));
            map.put("nombreTours", c.getNombreTours());
            map.put("statut", c.getStatut() != null ? c.getStatut().name() : null);
            map.put("idCollecteur", c.getIdCollecteur());
            if (c.getCollecteur() != null) {
                map.put("collecteur", c.getCollecteur().getNomComplet());
            }
        } else if (obj instanceof Adherent) {
            Adherent a = (Adherent) obj;
            map.put("idAdherent", a.getIdAdherent());
            map.put("numeroIdentification", a.getNumeroIdentification());
            map.put("nom", a.getNom());
            map.put("prenom", a.getPrenom());
            map.put("dateNaissance", str(a.getDateNaissance()));
            map.put("adresse", a.getAdresse());
            map.put("telephone", a.getTelephone());
            map.put("email", a.getEmail());
            map.put("profession", a.getProfession());
            map.put("employeur", a.getEmployeur());
            map.put("revenusEstimes",
                    a.getRevenusEstimes() != null ? a.getRevenusEstimes().toPlainString() : null);
            map.put("statut", a.getStatut() != null ? a.getStatut().name() : null);
            map.put("dateAdhesion", str(a.getDateAdhesion()));
        } else if (obj instanceof Collecteur) {
            Collecteur c = (Collecteur) obj;
            map.put("idCollecteur", c.getIdCollecteur());
            map.put("nom", c.getNom());
            map.put("prenom", c.getPrenom());
            map.put("telephone", c.getTelephone());
            map.put("email", c.getEmail());
            map.put("zoneCollecte", c.getZoneCollecte());
            map.put("statut", c.getStatut() != null ? c.getStatut().name() : null);
            map.put("dateEnregistrement", str(c.getDateEnregistrement()));
        } else if (obj instanceof Echeance) {
            Echeance e = (Echeance) obj;
            map.put("idEcheance", e.getIdEcheance());
            map.put("idParticipant", e.getIdParticipant());
            map.put("idCycle", e.getIdCycle());
            map.put("numeroTour", e.getNumeroTour());
            map.put("dateEcheance", str(e.getDateEcheance()));
            map.put("montantDu", e.getMontantDu() != null ? e.getMontantDu().toPlainString() : null);
            map.put("montantPaye", e.getMontantPaye() != null ? e.getMontantPaye().toPlainString() : null);
            map.put("statut", e.getStatut() != null ? e.getStatut().name() : null);
            map.put("datePaiement", str(e.getDatePaiement()));
        } else {
            // Unknown type: fallback to toString
            map.put("description", obj.toString());
        }

        return map;
    }

    private String str(Object obj) {
        return obj != null ? obj.toString() : null;
    }

    private String escapeJson(String s) {
        if (s == null)
            return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
}
