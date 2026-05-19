/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Major117
 */
public class Participant {

    public enum Statut {
        INSCRIT, ACTIF, RETIRE, SUSPENDU
    }

    private int idParticipant;
    private int idCycle;
    private int idAdherent;
    private int numeroOrdre;
    private Statut statut;
    private BigDecimal montantRecu;
    private Timestamp dateInscription;
    private Cycle cycle;
    private Adherent adherent;
    private List<Echeance> echeances;

    public Participant() {
    }

    public Participant(int idParticipant, int idCycle, int idAdherent, int numeroOrdre, Statut statut,
            BigDecimal montantRecu, Timestamp dateInscription, Cycle cycle, Adherent adherent,
            List<Echeance> echeances) {
        this.idParticipant = idParticipant;
        this.idCycle = idCycle;
        this.idAdherent = idAdherent;
        this.numeroOrdre = numeroOrdre;
        this.statut = statut;
        this.montantRecu = montantRecu;
        this.dateInscription = dateInscription;
        this.cycle = cycle;
        this.adherent = adherent;
        this.echeances = echeances;
    }

    public Participant(int idCycle, int idAdherent, int numeroOrdre, Statut statut, BigDecimal montantRecu,
            Timestamp dateInscription, Cycle cycle, Adherent adherent, List<Echeance> echeances) {
        this.idCycle = idCycle;
        this.idAdherent = idAdherent;
        this.numeroOrdre = numeroOrdre;
        this.statut = statut;
        this.montantRecu = montantRecu;
        this.dateInscription = dateInscription;
        this.cycle = cycle;
        this.adherent = adherent;
        this.echeances = echeances;
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

    public int getIdAdherent() {
        return idAdherent;
    }

    public void setIdAdherent(int idAdherent) {
        this.idAdherent = idAdherent;
    }

    public int getNumeroOrdre() {
        return numeroOrdre;
    }

    public void setNumeroOrdre(int numeroOrdre) {
        this.numeroOrdre = numeroOrdre;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public BigDecimal getMontantRecu() {
        return montantRecu;
    }

    public void setMontantRecu(BigDecimal montantRecu) {
        this.montantRecu = montantRecu;
    }

    public Timestamp getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Timestamp dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Cycle getCycle() {
        return cycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public Adherent getAdherent() {
        return adherent;
    }

    public void setAdherent(Adherent adherent) {
        this.adherent = adherent;
    }

    public List<Echeance> getEcheances() {
        return echeances;
    }

    public void setEcheances(List<Echeance> echeances) {
        this.echeances = echeances;
    }

    @Override
    public String toString() {
        return "Participant{" + "idParticipant=" + idParticipant + ", idCycle=" + idCycle + ", idAdherent=" + idAdherent
                + ", numeroOrdre=" + numeroOrdre + ", statut=" + statut + ", montantRecu=" + montantRecu
                + ", dateInscription=" + dateInscription + ", cycle=" + cycle + ", adherent=" + adherent
                + ", echeances=" + echeances + '}';
    }

    public BigDecimal getTotalDu() {
        if (echeances == null)
            return BigDecimal.ZERO;
        return echeances.stream()
                .map(Echeance::getMontantDu)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalPaye() {
        if (echeances == null)
            return BigDecimal.ZERO;
        return echeances.stream()
                .map(Echeance::getMontantPaye)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getSoldeRestant() {
        return getTotalDu().subtract(getTotalPaye());
    }

    public boolean aDejaRecu() {
        return montantRecu != null && montantRecu.compareTo(BigDecimal.ZERO) > 0;
    }

}
