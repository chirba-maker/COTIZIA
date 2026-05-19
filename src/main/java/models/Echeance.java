/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Major117
 */
public class Echeance {

    public enum Statut {
        EN_ATTENTE, PAYE, PARTIEL, IMPAYE
    }

    private int idEcheance;
    private int idParticipant;
    private int idCycle;
    private int numeroTour;
    private Date dateEcheance;
    private BigDecimal montantDu;
    private BigDecimal montantPaye;
    private Statut statut;
    private Timestamp datePaiement;
    private String commentaire;
    private Timestamp dateCreation;
    private Timestamp dateModification;

    private Participant participant;
    private List<Paiement> paiements;
    private Cycle cycle;

    public Echeance() {
    }

    public Echeance(int idEcheance, int idParticipant, int idCycle, int numeroTour, Date dateEcheance,
            BigDecimal montantDu, BigDecimal montantPaye, Statut statut, Timestamp datePaiement, String commentaire,
            Timestamp dateCreation, Timestamp dateModification, Participant participant, List<Paiement> paiements) {
        this.idEcheance = idEcheance;
        this.idParticipant = idParticipant;
        this.idCycle = idCycle;
        this.numeroTour = numeroTour;
        this.dateEcheance = dateEcheance;
        this.montantDu = montantDu;
        this.montantPaye = montantPaye;
        this.statut = statut;
        this.datePaiement = datePaiement;
        this.commentaire = commentaire;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.participant = participant;
        this.paiements = paiements;
    }

    public Echeance(int idParticipant, int idCycle, int numeroTour, Date dateEcheance, BigDecimal montantDu,
            BigDecimal montantPaye, Statut statut, Timestamp datePaiement, String commentaire, Timestamp dateCreation,
            Timestamp dateModification, Participant participant, List<Paiement> paiements) {
        this.idParticipant = idParticipant;
        this.idCycle = idCycle;
        this.numeroTour = numeroTour;
        this.dateEcheance = dateEcheance;
        this.montantDu = montantDu;
        this.montantPaye = montantPaye;
        this.statut = statut;
        this.datePaiement = datePaiement;
        this.commentaire = commentaire;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.participant = participant;
        this.paiements = paiements;
    }

    public int getIdEcheance() {
        return idEcheance;
    }

    public void setIdEcheance(int idEcheance) {
        this.idEcheance = idEcheance;
    }

    public int getIdParticipant() {
        return idParticipant;
    }

    public void setIdParticipant(int idParticipant) {
        this.idParticipant = idParticipant;
    }

    public int getIdCycle() {
        return idCycle;
    }

    public void setIdCycle(int idCycle) {
        this.idCycle = idCycle;
    }

    public int getNumeroTour() {
        return numeroTour;
    }

    public void setNumeroTour(int numeroTour) {
        this.numeroTour = numeroTour;
    }

    public Date getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(Date dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public BigDecimal getMontantDu() {
        return montantDu;
    }

    public void setMontantDu(BigDecimal montantDu) {
        this.montantDu = montantDu;
    }

    public BigDecimal getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(BigDecimal montantPaye) {
        this.montantPaye = montantPaye;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Timestamp getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Timestamp datePaiement) {
        this.datePaiement = datePaiement;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Timestamp getDateModification() {
        return dateModification;
    }

    public void setDateModification(Timestamp dateModification) {
        this.dateModification = dateModification;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public List<Paiement> getPaiements() {
        return paiements;
    }

    public void setPaiements(List<Paiement> paiements) {
        this.paiements = paiements;
    }

    public Cycle getCycle() {
        return cycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    @Override
    public String toString() {
        return "Echeance{" + "idEcheance=" + idEcheance + ", idParticipant=" + idParticipant + ", idCycle=" + idCycle
                + ", numeroTour=" + numeroTour + ", dateEcheance=" + dateEcheance + ", montantDu=" + montantDu
                + ", montantPaye=" + montantPaye + ", statut=" + statut + ", datePaiement=" + datePaiement
                + ", commentaire=" + commentaire + ", dateCreation=" + dateCreation + ", dateModification="
                + dateModification + ", participant=" + participant + ", paiements=" + paiements + '}';
    }

    public BigDecimal getSoldeRestant() {
        return montantDu.subtract(montantPaye);
    }

    public boolean estPayee() {
        return Statut.PAYE.equals(this.statut);
    }

    public boolean estImpayee() {
        return Statut.IMPAYE.equals(this.statut);
    }

    public void recalculerStatut() {
        if (montantPaye == null)
            montantPaye = BigDecimal.ZERO;
        int cmp = montantPaye.compareTo(montantDu);
        if (cmp >= 0) {
            this.statut = Statut.PAYE;
        } else if (montantPaye.compareTo(BigDecimal.ZERO) > 0) {
            this.statut = Statut.PARTIEL;
        }
    }

}
