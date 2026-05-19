package dao;

import models.Utilisateur;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UtilisateurDaoImplementTest {

    @Test
    public void testFindByLogin_returnsNullWhenNotFound() {
        UtilisateurDaoImplement dao = new UtilisateurDaoImplement();
        // This test assumes no running DB; ensure method handles absence gracefully
        Utilisateur u = dao.findByLogin("nonexistent_login_for_test");
        assertNull(u);
    }
}
