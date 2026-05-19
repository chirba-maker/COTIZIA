/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Major117
 */
public class Collecteur {

    public enum Statut {
        ACTIF, SUSPENDU, INACTIF
    }

    private int idCollecteur;
    private int idUtilisateur;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String zoneCollecte;
    private Statut statut;
    private Timestamp dateEnregistrement;
    private Timestamp dateModification;

    private Utilisateur utilisateur;
    private List<Cycle> cycles;

    public Collecteur() {
    }

    public Collecteur(int idCollecteur, int idUtilisateur, String nom, String prenom, String telephone, String email, String zoneCollecte, Statut statut, Timestamp dateEnregistrement, Timestamp dateModification, Utilisateur utilisateur, List<Cycle> cycles) {
        this.idCollecteur = idCollecteur;
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.zoneCollecte = zoneCollecte;
        this.statut = statut;
        this.dateEnregistrement = dateEnregistrement;
        this.dateModification = dateModification;
        this.utilisateur = utilisateur;
        this.cycles = cycles;
    }

    public Collecteur(int idUtilisateur, String nom, String prenom, String telephone, String email, String zoneCollecte, Statut statut, Timestamp dateEnregistrement, Timestamp dateModification, Utilisateur utilisateur, List<Cycle> cycles) {
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.zoneCollecte = zoneCollecte;
        this.statut = statut;
        this.dateEnregistrement = dateEnregistrement;
        this.dateModification = dateModification;
        this.utilisateur = utilisateur;
        this.cycles = cycles;
    }

    public int getIdCollecteur() {
        return idCollecteur;
    }

    public void setIdCollecteur(int idCollecteur) {
        this.idCollecteur = idCollecteur;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
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

    public String getZoneCollecte() {
        return zoneCollecte;
    }

    public void setZoneCollecte(String zoneCollecte) {
        this.zoneCollecte = zoneCollecte;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Timestamp getDateEnregistrement() {
        return dateEnregistrement;
    }

    public void setDateEnregistrement(Timestamp dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public Timestamp getDateModification() {
        return dateModification;
    }

    public void setDateModification(Timestamp dateModification) {
        this.dateModification = dateModification;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public List<Cycle> getCycles() {
        return cycles;
    }

    public void setCycles(List<Cycle> cycles) {
        this.cycles = cycles;
    }

    @Override
    public String toString() {
        return "Collecteur{" + "idCollecteur=" + idCollecteur + ", idUtilisateur=" + idUtilisateur + ", nom=" + nom + ", prenom=" + prenom + ", telephone=" + telephone + ", email=" + email + ", zoneCollecte=" + zoneCollecte + ", statut=" + statut + ", dateEnregistrement=" + dateEnregistrement + ", dateModification=" + dateModification + ", utilisateur=" + utilisateur + ", cycles=" + cycles + '}';
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    public boolean isActif() {
        return Statut.ACTIF.equals(this.statut);
    }
    
}
