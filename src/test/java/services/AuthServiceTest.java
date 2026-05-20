package services;

import dao.UtilisateurInterface;
import models.Utilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    private UtilisateurInterface userDAO;
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        userDAO = Mockito.mock(UtilisateurInterface.class);
        authService = new AuthService(userDAO);
    }

    @Test
    public void authenticate_withValidCredentials_returnsUser() {
        Utilisateur mockUser = new Utilisateur();
        mockUser.setLogin("jdoe");
        mockUser.setMotDePasse("secret");
        mockUser.setActif(true);

        when(userDAO.findByLogin("jdoe")).thenReturn(mockUser);

        Utilisateur result = authService.authenticate("jdoe", "secret");

        assertNotNull(result);
        assertEquals("jdoe", result.getLogin());
    }

    @Test
    public void authenticate_withInvalidPassword_returnsNull() {
        Utilisateur mockUser = new Utilisateur();
        mockUser.setLogin("jdoe");
        mockUser.setMotDePasse("secret");
        mockUser.setActif(true);

        when(userDAO.findByLogin("jdoe")).thenReturn(mockUser);

        Utilisateur result = authService.authenticate("jdoe", "wrong");

        assertNull(result);
    }
}
