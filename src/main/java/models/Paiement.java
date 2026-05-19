/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 *
 * @author Major117
 */
public class Paiement {

    public enum ModePaiement {
        ESPECES, MOBILE_MONEY, VIREMENT, CHEQUE, ORANGE_MONEY, MOBI_CASH
    }

    private int idPaiement;
    private int idEcheance;
    private int idUtilisateur;
    private BigDecimal montant;
    private ModePaiement modePaiement;
    private String reference;
    private String note;
    private Timestamp datePaiement;

    private Echeance echeance;
    private Utilisateur utilisateur;

    public Paiement() {
    }

    public Paiement(int idPaiement, int idEcheance, int idUtilisateur, BigDecimal montant, ModePaiement modePaiement,
            String reference, String note, Timestamp datePaiement, Echeance echeance, Utilisateur utilisateur) {
        this.idPaiement = idPaiement;
        this.idEcheance = idEcheance;
        this.idUtilisateur = idUtilisateur;
        this.montant = montant;
        this.modePaiement = modePaiement;
        this.reference = reference;
        this.note = note;
        this.datePaiement = datePaiement;
        this.echeance = echeance;
        this.utilisateur = utilisateur;
    }

    public Paiement(int idEcheance, int idUtilisateur, BigDecimal montant, ModePaiement modePaiement, String reference,
            String note, Timestamp datePaiement, Echeance echeance, Utilisateur utilisateur) {
        this.idEcheance = idEcheance;
        this.idUtilisateur = idUtilisateur;
        this.montant = montant;
        this.modePaiement = modePaiement;
        this.reference = reference;
        this.note = note;
        this.datePaiement = datePaiement;
        this.echeance = echeance;
        this.utilisateur = utilisateur;
    }

    public int getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(int idPaiement) {
        this.idPaiement = idPaiement;
    }

    public int getIdEcheance() {
        return idEcheance;
    }

    public void setIdEcheance(int idEcheance) {
        this.idEcheance = idEcheance;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public ModePaiement getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Timestamp datePaiement) {
        this.datePaiement = datePaiement;
    }

    public Echeance getEcheance() {
        return echeance;
    }

    public void setEcheance(Echeance echeance) {
        this.echeance = echeance;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public String toString() {
        return "Paiement{" + "idPaiement=" + idPaiement + ", idEcheance=" + idEcheance + ", idUtilisateur="
                + idUtilisateur + ", montant=" + montant + ", modePaiement=" + modePaiement + ", reference=" + reference
                + ", note=" + note + ", datePaiement=" + datePaiement + ", echeance=" + echeance + ", utilisateur="
                + utilisateur + '}';
    }

}
