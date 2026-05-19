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
public class Adherent {

    public enum Statut {
        ACTIF, SUSPENDU, RADIE
    }

    private int idAdherent;
    private int idUtilisateur;
    private String numeroIdentification;
    private String nom;
    private String prenom;
    private Date dateNaissance;
    private String adresse;
    private String telephone;
    private String email;
    private String profession;
    private String employeur;
    private BigDecimal revenusEstimes;
    private Statut statut;
    private Timestamp dateAdhesion;
    private Timestamp dateModification;

    private List<Participant> participations;

    public Adherent() {
    }

    public Adherent(int idAdherent, String numeroIdentification, String nom, String prenom, Date dateNaissance, String adresse, String telephone, String email, String profession, String employeur, BigDecimal revenusEstimes, Statut statut, Timestamp dateAdhesion, Timestamp dateModification, List<Participant> participations) {
        this.idAdherent = idAdherent;
        this.numeroIdentification = numeroIdentification;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.profession = profession;
        this.employeur = employeur;
        this.revenusEstimes = revenusEstimes;
        this.statut = statut;
        this.dateAdhesion = dateAdhesion;
        this.dateModification = dateModification;
        this.participations = participations;
    }

    public Adherent(String numeroIdentification, String nom, String prenom, Date dateNaissance, String adresse, String telephone, String email, String profession, String employeur, BigDecimal revenusEstimes, Statut statut, Timestamp dateAdhesion, Timestamp dateModification, List<Participant> participations) {
        this.numeroIdentification = numeroIdentification;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.profession = profession;
        this.employeur = employeur;
        this.revenusEstimes = revenusEstimes;
        this.statut = statut;
        this.dateAdhesion = dateAdhesion;
        this.dateModification = dateModification;
        this.participations = participations;
    }

    public int getIdAdherent() {
        return idAdherent;
    }

    public void setIdAdherent(int idAdherent) {
        this.idAdherent = idAdherent;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getNumeroIdentification() {
        return numeroIdentification;
    }

    public void setNumeroIdentification(String numeroIdentification) {
        this.numeroIdentification = numeroIdentification;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getEmployeur() {
        return employeur;
    }

    public void setEmployeur(String employeur) {
        this.employeur = employeur;
    }

    public BigDecimal getRevenusEstimes() {
        return revenusEstimes;
    }

    public void setRevenusEstimes(BigDecimal revenusEstimes) {
        this.revenusEstimes = revenusEstimes;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Timestamp getDateAdhesion() {
        return dateAdhesion;
    }

    public void setDateAdhesion(Timestamp dateAdhesion) {
        this.dateAdhesion = dateAdhesion;
    }

    public Timestamp getDateModification() {
        return dateModification;
    }

    public void setDateModification(Timestamp dateModification) {
        this.dateModification = dateModification;
    }

    public List<Participant> getParticipations() {
        return participations;
    }

    public void setParticipations(List<Participant> participations) {
        this.participations = participations;
    }

    @Override
    public String toString() {
        return "Adherent{" + "idAdherent=" + idAdherent + ", numeroIdentification=" + numeroIdentification + ", nom=" + nom + ", prenom=" + prenom + ", dateNaissance=" + dateNaissance + ", adresse=" + adresse + ", telephone=" + telephone + ", email=" + email + ", profession=" + profession + ", employeur=" + employeur + ", revenusEstimes=" + revenusEstimes + ", statut=" + statut + ", dateAdhesion=" + dateAdhesion + ", dateModification=" + dateModification + ", participations=" + participations + '}';
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    public boolean isActif() {
        return Statut.ACTIF.equals(this.statut);
    }
    
}
