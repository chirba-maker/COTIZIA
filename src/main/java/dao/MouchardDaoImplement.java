package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Mouchard;
import utils.DBConnection;

/**
 * Implementation of Mouchard DAO for Audit Logs.
 * 
 * @author Major117
 */
public class MouchardDaoImplement implements MouchardInterface {

    @Override
    public void save(Mouchard m) {
        String sql = "INSERT INTO mouchard (id_utilisateur, action, entite, id_entite, detail_avant, detail_apres, adresse_ip, user_agent) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (m.getIdUtilisateur() > 0) {
                stmt.setInt(1, m.getIdUtilisateur());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, m.getAction());
            stmt.setString(3, m.getEntite());
            if (m.getIdEntite() > 0) {
                stmt.setInt(4, m.getIdEntite());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setString(5, m.getDetailAvant());
            stmt.setString(6, m.getDetailApres());
            stmt.setString(7, m.getAdresseIp());
            stmt.setString(8, m.getUserAgent());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    m.setIdMouchard(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Mouchard m) {
        // Audit logs are typically immutable, but implementing for interface
        // completeness
        String sql = "UPDATE mouchard SET id_utilisateur = ?, action = ?, entite = ?, id_entite = ?, detail_avant = ?, detail_apres = ?, adresse_ip = ?, user_agent = ? WHERE id_mouchard = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (m.getIdUtilisateur() > 0) {
                stmt.setInt(1, m.getIdUtilisateur());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, m.getAction());
            stmt.setString(3, m.getEntite());
            if (m.getIdEntite() > 0) {
                stmt.setInt(4, m.getIdEntite());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setString(5, m.getDetailAvant());
            stmt.setString(6, m.getDetailApres());
            stmt.setString(7, m.getAdresseIp());
            stmt.setString(8, m.getUserAgent());
            stmt.setInt(9, m.getIdMouchard());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Mouchard> getAll() {
        List<Mouchard> logs = new ArrayList<>();
        String sql = "SELECT m.*, u.nom, u.prenom, u.role, u.email " +
                "FROM mouchard m " +
                "LEFT JOIN utilisateur u ON m.id_utilisateur = u.id_utilisateur " +
                "ORDER BY m.date_action DESC";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                logs.add(mapResultSetToMouchardWithUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    @Override
    public void delete(Mouchard m) {
        String sql = "DELETE FROM mouchard WHERE id_mouchard = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, m.getIdMouchard());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Mouchard findById(int id) {
        String sql = "SELECT m.*, u.nom, u.prenom, u.role, u.email " +
                "FROM mouchard m " +
                "LEFT JOIN utilisateur u ON m.id_utilisateur = u.id_utilisateur " +
                "WHERE m.id_mouchard = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMouchardWithUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Mouchard mapResultSetToMouchard(ResultSet rs) throws SQLException {
        Mouchard m = new Mouchard();
        m.setIdMouchard(rs.getInt("id_mouchard"));
        m.setIdUtilisateur(rs.getInt("id_utilisateur"));
        m.setAction(rs.getString("action"));
        m.setEntite(rs.getString("entite"));
        m.setIdEntite(rs.getInt("id_entite"));
        m.setDetailAvant(rs.getString("detail_avant"));
        m.setDetailApres(rs.getString("detail_apres"));
        m.setAdresseIp(rs.getString("adresse_ip"));
        m.setUserAgent(rs.getString("user_agent"));
        m.setDateAction(rs.getTimestamp("date_action"));
        return m;
    }

    private Mouchard mapResultSetToMouchardWithUser(ResultSet rs) throws SQLException {
        Mouchard m = mapResultSetToMouchard(rs);

        int userId = rs.getInt("id_utilisateur");
        if (!rs.wasNull()) {
            models.Utilisateur u = new models.Utilisateur();
            u.setIdUtilisateur(userId);
            u.setNom(rs.getString("nom"));
            u.setPrenom(rs.getString("prenom"));
            String roleStr = rs.getString("role");
            if (roleStr != null) {
                u.setRole(models.Utilisateur.Role.valueOf(roleStr.toUpperCase()));
            }
            u.setEmail(rs.getString("email"));
            m.setUtilisateur(u);
        }

        return m;
    }
}
