package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Notification;
import utils.DBConnection;

/**
 * Implementation of Notification DAO.
 * 
 * @author Major117
 */
public class NotificationDaoImplement implements NotificationInterface {

    @Override
    public void save(Notification n) {
        String sql = "INSERT INTO notification (id_utilisateur, type, titre, message, lu, date_lecture) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, n.getIdUtilisateur());
            stmt.setString(2, n.getType().name().toLowerCase());
            stmt.setString(3, n.getTitre());
            stmt.setString(4, n.getMessage());
            stmt.setBoolean(5, n.isLu());
            stmt.setTimestamp(6, n.getDateLecture());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    n.setIdNotification(rs.getInt(1));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Notification n) {
        String sql = "UPDATE notification SET id_utilisateur = ?, type = ?, titre = ?, message = ?, lu = ?, date_lecture = ? WHERE id_notification = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, n.getIdUtilisateur());
            stmt.setString(2, n.getType().name().toLowerCase());
            stmt.setString(3, n.getTitre());
            stmt.setString(4, n.getMessage());
            stmt.setBoolean(5, n.isLu());
            stmt.setTimestamp(6, n.getDateLecture());
            stmt.setInt(7, n.getIdNotification());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<Notification> getAll() {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notification ORDER BY date_creation DESC";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return notifications;
    }

    @Override
    public void delete(Notification n) {
        String sql = "DELETE FROM notification WHERE id_notification = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, n.getIdNotification());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Notification findById(int id) {
        String sql = "SELECT * FROM notification WHERE id_notification = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNotification(rs);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Notification> findByUser(int idUtilisateur) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notification WHERE id_utilisateur = ? ORDER BY date_creation DESC";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtilisateur);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(mapResultSetToNotification(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return notifications;
    }

    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification n = new Notification();
        n.setIdNotification(rs.getInt("id_notification"));
        n.setIdUtilisateur(rs.getInt("id_utilisateur"));
        String typeStr = rs.getString("type");
        if (typeStr != null) {
            n.setType(Notification.Type.valueOf(typeStr.toUpperCase()));
        }
        n.setTitre(rs.getString("titre"));
        n.setMessage(rs.getString("message"));
        n.setLu(rs.getBoolean("lu"));
        n.setDateCreation(rs.getTimestamp("date_creation"));
        n.setDateLecture(rs.getTimestamp("date_lecture"));
        return n;
    }
}
