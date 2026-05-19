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
public class Cycle {

    public enum Frequence {
        HEBDOMADAIRE, BIMENSUELLE, MENSUELLE
    }

    public enum Statut {
        CREE, OUVERT, ACTIF, SUSPENDU, TERMINE, ANNULE, CLOTURE
    }

    private int idCycle;
    private int idCollecteur;
    private String libelle;
    private String description;
    private BigDecimal montantCotisation;
    private Frequence frequence;
    private Date dateDebut;
    private int nombreTours;
    private Statut statut;
    private Timestamp dateCreation;
    private Timestamp dateModification;

    private boolean supportDemande;

    private Collecteur collecteur;
    private List<Participant> participants;

    public Cycle() {
    }

    public Cycle(int idCycle, int idCollecteur, String libelle, String description, BigDecimal montantCotisation,
            Frequence frequence, Date dateDebut, int nombreTours, Statut statut, Timestamp dateCreation,
            Timestamp dateModification, Collecteur collecteur, List<Participant> participants) {
        this.idCycle = idCycle;
        this.idCollecteur = idCollecteur;
        this.libelle = libelle;
        this.description = description;
        this.montantCotisation = montantCotisation;
        this.frequence = frequence;
        this.dateDebut = dateDebut;
        this.nombreTours = nombreTours;
        this.statut = statut;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.collecteur = collecteur;
        this.participants = participants;
    }

    public Cycle(int idCollecteur, String libelle, String description, BigDecimal montantCotisation,
            Frequence frequence, Date dateDebut, int nombreTours, Statut statut, Timestamp dateCreation,
            Timestamp dateModification, Collecteur collecteur, List<Participant> participants) {
        this.idCollecteur = idCollecteur;
        this.libelle = libelle;
        this.description = description;
        this.montantCotisation = montantCotisation;
        this.frequence = frequence;
        this.dateDebut = dateDebut;
        this.nombreTours = nombreTours;
        this.statut = statut;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.collecteur = collecteur;
        this.participants = participants;
    }

    public int getIdCycle() {
        return idCycle;
    }

    public void setIdCycle(int idCycle) {
        this.idCycle = idCycle;
    }

    public int getIdCollecteur() {
        return idCollecteur;
    }

    public void setIdCollecteur(int idCollecteur) {
        this.idCollecteur = idCollecteur;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMontantCotisation() {
        return montantCotisation;
    }

    public void setMontantCotisation(BigDecimal montantCotisation) {
        this.montantCotisation = montantCotisation;
    }

    public Frequence getFrequence() {
        return frequence;
    }

    public void setFrequence(Frequence frequence) {
        this.frequence = frequence;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public int getNombreTours() {
        return nombreTours;
    }

    public void setNombreTours(int nombreTours) {
        this.nombreTours = nombreTours;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
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

    public boolean isSupportDemande() {
        return supportDemande;
    }

    public void setSupportDemande(boolean supportDemande) {
        this.supportDemande = supportDemande;
    }

    public Collecteur getCollecteur() {
        return collecteur;
    }

    public void setCollecteur(Collecteur collecteur) {
        this.collecteur = collecteur;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "Cycle{" + "idCycle=" + idCycle + ", idCollecteur=" + idCollecteur + ", libelle=" + libelle
                + ", description=" + description + ", montantCotisation=" + montantCotisation + ", frequence="
                + frequence + ", dateDebut=" + dateDebut + ", nombreTours=" + nombreTours + ", statut=" + statut
                + ", dateCreation=" + dateCreation + ", dateModification=" + dateModification + ", collecteur="
                + collecteur + ", participants=" + participants + '}';
    }

    public BigDecimal getCagnotteTour() {
        if (participants == null || participants.isEmpty())
            return BigDecimal.ZERO;
        return montantCotisation.multiply(BigDecimal.valueOf(participants.size()));
    }

    public boolean isActif() {
        return Statut.ACTIF.equals(this.statut);
    }

}
