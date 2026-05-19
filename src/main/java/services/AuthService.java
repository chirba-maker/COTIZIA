package services;

import dao.UtilisateurDaoImplement;
import dao.UtilisateurInterface;
import models.Utilisateur;
import org.mindrot.jbcrypt.BCrypt;

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
     * Hashes a password using BCrypt.
     */
    public String hashPassword(String plainTextPassword) {
        if (plainTextPassword == null)
            return null;
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    /**
     * Verifies a password. Supports BCrypt and legacy plain text.
     */
    public boolean checkPassword(String plainTextPassword, String storedPassword) {
        if (storedPassword == null || plainTextPassword == null) {
            return false;
        }

        // Check if stored password is a BCrypt hash
        if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$")
                || storedPassword.startsWith("$2y$")) {
            try {
                return BCrypt.checkpw(plainTextPassword, storedPassword);
            } catch (Exception e) {
                return false;
            }
        }

        // Legacy plain text comparison
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
