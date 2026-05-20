package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Utilisateur;
import utils.DBConnection;

/**
 * Implementation of Utilisateur DAO.
 * 
 * @author Major117
 */
public class UtilisateurDaoImplement implements UtilisateurInterface {

    @Override
    public void save(Utilisateur u) {
        String sql = "INSERT INTO utilisateur (nom, prenom, login, email, mot_de_passe, role, actif, photo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, u.getNom());
            stmt.setString(2, u.getPrenom());
            stmt.setString(3, u.getLogin());
            stmt.setString(4, u.getEmail());
            stmt.setString(5, u.getMotDePasse());
            stmt.setString(6, u.getRole().name().toLowerCase());
            stmt.setBoolean(7, u.isActif());
            stmt.setString(8, u.getPhoto());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    u.setIdUtilisateur(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Utilisateur u) {
        String sql = "UPDATE utilisateur SET nom = ?, prenom = ?, login = ?, email = ?, mot_de_passe = ?, role = ?, actif = ?, photo = ? WHERE id_utilisateur = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getNom());
            stmt.setString(2, u.getPrenom());
            stmt.setString(3, u.getLogin());
            stmt.setString(4, u.getEmail());
            stmt.setString(5, u.getMotDePasse());
            stmt.setString(6, u.getRole().name().toLowerCase());
            stmt.setBoolean(7, u.isActif());
            stmt.setString(8, u.getPhoto());
            stmt.setInt(9, u.getIdUtilisateur());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Utilisateur> getAll() {
        List<Utilisateur> users = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur ORDER BY date_creation DESC";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapResultSetToUtilisateur(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void delete(Utilisateur u) {
        String sql = "DELETE FROM utilisateur WHERE id_utilisateur = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, u.getIdUtilisateur());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Utilisateur findById(int id) {
        if (id <= 0)
            return null;
        String sql = "SELECT * FROM utilisateur WHERE id_utilisateur = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUtilisateur(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by ID: " + id);
            e.printStackTrace();
        }
        return null;
    }

    public Utilisateur findByLogin(String login) {
        String sql = "SELECT * FROM utilisateur WHERE login = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUtilisateur(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Utilisateur mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur u = new Utilisateur();
        u.setIdUtilisateur(rs.getInt("id_utilisateur"));
        u.setNom(rs.getString("nom"));
        u.setPrenom(rs.getString("prenom"));
        u.setLogin(rs.getString("login"));
        u.setEmail(rs.getString("email"));
        u.setMotDePasse(rs.getString("mot_de_passe"));
        String roleStr = rs.getString("role");
        if (roleStr != null) {
            u.setRole(Utilisateur.Role.valueOf(roleStr.toUpperCase()));
        }
        u.setActif(rs.getBoolean("actif"));
        u.setDateCreation(rs.getTimestamp("date_creation"));
        u.setDateModification(rs.getTimestamp("date_modification"));
        u.setPhoto(rs.getString("photo"));
        return u;
    }
}
