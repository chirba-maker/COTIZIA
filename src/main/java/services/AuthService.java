package services;

import dao.UtilisateurDaoImplement;
import dao.UtilisateurInterface;
import models.Utilisateur;


/**
 * Service for Authentication and Password Security.
 * 
 * @author Major117
 */
public class AuthService {

    private final UtilisateurInterface userDAO;

    public AuthService() {
        this(new UtilisateurDaoImplement());
    }

    public AuthService(UtilisateurInterface userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Retourne le mot de passe tel quel (sans hachage).
     */
    public String hashPassword(String plainTextPassword) {
        return plainTextPassword;
    }

    /**
     * Compare les mots de passe en clair.
     */
    public boolean checkPassword(String plainTextPassword, String storedPassword) {
        if (storedPassword == null || plainTextPassword == null) {
            return false;
        }
        return plainTextPassword.equals(storedPassword);
    }

    /**
     * Authenticates a user.
     */
    public Utilisateur authenticate(String login, String password) {
        Utilisateur user = userDAO.findByLogin(login);
        if (user != null && user.isActif()) {
            if (checkPassword(password, user.getMotDePasse())) {
                return user;
            }
        }
        return null;
    }
}
