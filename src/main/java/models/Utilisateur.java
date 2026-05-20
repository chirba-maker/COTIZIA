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
public class Utilisateur {

    public enum Role {
        ADMIN, COLLECTEUR, CONSULTANT
    }

    private int idUtilisateur;
    private String nom;
    private String prenom;
    private String login;
    private String email;
    private String motDePasse;
    private Role role;
    private boolean actif;
    private Timestamp dateCreation;
    private Timestamp dateModification;
    private String photo;

    public Utilisateur() {
    }

    public Utilisateur(int idUtilisateur, String nom, String prenom, String login, String email, String motDePasse, Role role, boolean actif, Timestamp dateCreation, Timestamp dateModification) {
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
        this.actif = actif;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
    }

    public Utilisateur(String nom, String prenom, String login, String email, String motDePasse, Role role, boolean actif, Timestamp dateCreation, Timestamp dateModification) {
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
        this.actif = actif;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Utilisateur{" + "idUtilisateur=" + idUtilisateur + ", nom=" + nom + ", prenom=" + prenom + ", login=" + login + ", email=" + email + ", motDePasse=" + motDePasse + ", role=" + role + ", actif=" + actif + ", dateCreation=" + dateCreation + ", dateModification=" + dateModification + '}';
    }
    
    
    public String getNomComplet() {
        return prenom + " " + nom;
    }

}
