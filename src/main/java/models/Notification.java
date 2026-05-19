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
public class Notification {

    public enum Type {
        ECHEANCE_PROCHE, IMPAYE, SYSTEME, INFO
    }

    private int idNotification;
    private int idUtilisateur;
    private Type type;
    private String titre;
    private String message;
    private boolean lu;
    private Timestamp dateCreation;
    private Timestamp dateLecture;

    private Utilisateur utilisateur;

    public Notification() {
    }

    public Notification(int idNotification, int idUtilisateur, Type type, String titre, String message, boolean lu, Timestamp dateCreation, Timestamp dateLecture, Utilisateur utilisateur) {
        this.idNotification = idNotification;
        this.idUtilisateur = idUtilisateur;
        this.type = type;
        this.titre = titre;
        this.message = message;
        this.lu = lu;
        this.dateCreation = dateCreation;
        this.dateLecture = dateLecture;
        this.utilisateur = utilisateur;
    }

    public Notification(int idUtilisateur, Type type, String titre, String message, boolean lu, Timestamp dateCreation, Timestamp dateLecture, Utilisateur utilisateur) {
        this.idUtilisateur = idUtilisateur;
        this.type = type;
        this.titre = titre;
        this.message = message;
        this.lu = lu;
        this.dateCreation = dateCreation;
        this.dateLecture = dateLecture;
        this.utilisateur = utilisateur;
    }

    public int getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(int idNotification) {
        this.idNotification = idNotification;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isLu() {
        return lu;
    }

    public void setLu(boolean lu) {
        this.lu = lu;
    }

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Timestamp getDateLecture() {
        return dateLecture;
    }

    public void setDateLecture(Timestamp dateLecture) {
        this.dateLecture = dateLecture;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public String toString() {
        return "Notification{" + "idNotification=" + idNotification + ", idUtilisateur=" + idUtilisateur + ", type=" + type + ", titre=" + titre + ", message=" + message + ", lu=" + lu + ", dateCreation=" + dateCreation + ", dateLecture=" + dateLecture + ", utilisateur=" + utilisateur + '}';
    }

}
