/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Timestamp;

/**
 *
 * @author Major117
 */
public class Mouchard {

    // Constantes d'actions courantes
    public static final String ACTION_LOGIN = "LOGIN";
    public static final String ACTION_LOGOUT = "LOGOUT";
    public static final String ACTION_CREATE_UTILISATEUR = "CREATE_UTILISATEUR";
    public static final String ACTION_UPDATE_UTILISATEUR = "UPDATE_UTILISATEUR";
    public static final String ACTION_DELETE_UTILISATEUR = "DELETE_UTILISATEUR";
    public static final String ACTION_CREATE_CYCLE = "CREATE_CYCLE";
    public static final String ACTION_UPDATE_CYCLE = "UPDATE_CYCLE";
    public static final String ACTION_CLOTURE_CYCLE = "CLOTURE_CYCLE";
    public static final String ACTION_CREATE_PARTICIPANT = "CREATE_PARTICIPANT";
    public static final String ACTION_RETIRE_PARTICIPANT = "RETIRE_PARTICIPANT";
    public static final String ACTION_PAIEMENT = "ENREGISTRER_PAIEMENT";
    public static final String ACTION_ANNULER_PAIEMENT = "ANNULER_PAIEMENT";
    public static final String ACTION_CREATE_ADHERENT = "CREATE_ADHERENT";
    public static final String ACTION_UPDATE_ADHERENT = "UPDATE_ADHERENT";

    private int idMouchard;
    private Integer idUtilisateur;
    private String action;
    private String entite;
    private Integer idEntite;
    private String detailAvant;
    private String detailApres;
    private String adresseIp;
    private String userAgent;
    private Timestamp dateAction;

    private Utilisateur utilisateur;

    public Mouchard() {
    }

    public Mouchard(int idMouchard, Integer idUtilisateur, String action, String entite, Integer idEntite, String detailAvant, String detailApres, String adresseIp, String userAgent, Timestamp dateAction, Utilisateur utilisateur) {
        this.idMouchard = idMouchard;
        this.idUtilisateur = idUtilisateur;
        this.action = action;
        this.entite = entite;
        this.idEntite = idEntite;
        this.detailAvant = detailAvant;
        this.detailApres = detailApres;
        this.adresseIp = adresseIp;
        this.userAgent = userAgent;
        this.dateAction = dateAction;
        this.utilisateur = utilisateur;
    }

    public Mouchard(Integer idUtilisateur, String action, String entite, Integer idEntite, String detailAvant, String detailApres, String adresseIp, String userAgent, Timestamp dateAction, Utilisateur utilisateur) {
        this.idUtilisateur = idUtilisateur;
        this.action = action;
        this.entite = entite;
        this.idEntite = idEntite;
        this.detailAvant = detailAvant;
        this.detailApres = detailApres;
        this.adresseIp = adresseIp;
        this.userAgent = userAgent;
        this.dateAction = dateAction;
        this.utilisateur = utilisateur;
    }

    public int getIdMouchard() {
        return idMouchard;
    }

    public void setIdMouchard(int idMouchard) {
        this.idMouchard = idMouchard;
    }

    public Integer getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Integer idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntite() {
        return entite;
    }

    public void setEntite(String entite) {
        this.entite = entite;
    }

    public Integer getIdEntite() {
        return idEntite;
    }

    public void setIdEntite(Integer idEntite) {
        this.idEntite = idEntite;
    }

    public String getDetailAvant() {
        return detailAvant;
    }

    public void setDetailAvant(String detailAvant) {
        this.detailAvant = detailAvant;
    }

    public String getDetailApres() {
        return detailApres;
    }

    public void setDetailApres(String detailApres) {
        this.detailApres = detailApres;
    }

    public String getAdresseIp() {
        return adresseIp;
    }

    public void setAdresseIp(String adresseIp) {
        this.adresseIp = adresseIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Timestamp getDateAction() {
        return dateAction;
    }

    public void setDateAction(Timestamp dateAction) {
        this.dateAction = dateAction;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public String toString() {
        return "Mouchard{" + "idMouchard=" + idMouchard + ", idUtilisateur=" + idUtilisateur + ", action=" + action + ", entite=" + entite + ", idEntite=" + idEntite + ", detailAvant=" + detailAvant + ", detailApres=" + detailApres + ", adresseIp=" + adresseIp + ", userAgent=" + userAgent + ", dateAction=" + dateAction + ", utilisateur=" + utilisateur + '}';
    }

    
}
