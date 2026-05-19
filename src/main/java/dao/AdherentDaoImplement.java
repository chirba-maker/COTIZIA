package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Adherent;
import utils.DBConnection;

/**
 * Implementation of Adherent DAO.
 * 
 * @author Major117
 */
public class AdherentDaoImplement implements AdherentInterface {

    @Override
    public void save(Adherent a) {
        String sql = "INSERT INTO adherent (numero_identification, nom, prenom, date_naissance, adresse, telephone, email, profession, employeur, revenus_estimes, statut) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, a.getNumeroIdentification());
            stmt.setString(2, a.getNom());
            stmt.setString(3, a.getPrenom());
            stmt.setDate(4, a.getDateNaissance());
            stmt.setString(5, a.getAdresse());
            stmt.setString(6, a.getTelephone());
            stmt.setString(7, a.getEmail());
            stmt.setString(8, a.getProfession());
            stmt.setString(9, a.getEmployeur());
            stmt.setBigDecimal(10, a.getRevenusEstimes());
            stmt.setString(11, a.getStatut().name().toLowerCase());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    a.setIdAdherent(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur de base de données lors de l'enregistrement de l'adhérent : " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Adherent a) {
        String sql = "UPDATE adherent SET numero_identification = ?, nom = ?, prenom = ?, date_naissance = ?, adresse = ?, telephone = ?, email = ?, profession = ?, employeur = ?, revenus_estimes = ?, statut = ? WHERE id_adherent = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, a.getNumeroIdentification());
            stmt.setString(2, a.getNom());
            stmt.setString(3, a.getPrenom());
            stmt.setDate(4, a.getDateNaissance());
            stmt.setString(5, a.getAdresse());
            stmt.setString(6, a.getTelephone());
            stmt.setString(7, a.getEmail());
            stmt.setString(8, a.getProfession());
            stmt.setString(9, a.getEmployeur());
            stmt.setBigDecimal(10, a.getRevenusEstimes());
            stmt.setString(11, a.getStatut().name().toLowerCase());
            stmt.setInt(12, a.getIdAdherent());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur de base de données lors de la mise à jour de l'adhérent : " + e.getMessage(), e);
        }
    }

    @Override
    public List<Adherent> getAll() {
        List<Adherent> adherents = new ArrayList<>();
        String sql = "SELECT * FROM adherent ORDER BY numero_identification ASC";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                adherents.add(mapResultSetToAdherent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adherents;
    }

    @Override
    public void delete(Adherent a) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Delete paiements linked to echeances of this adherent's participations
            String sqlPaiements = "DELETE pa FROM paiement pa "
                    + "JOIN echeance e ON pa.id_echeance = e.id_echeance "
                    + "JOIN participant p ON e.id_participant = p.id_participant "
                    + "WHERE p.id_adherent = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlPaiements)) {
                stmt.setInt(1, a.getIdAdherent());
                stmt.executeUpdate();
            }

            // 2. Delete echeances linked to this adherent's participations
            String sqlEcheances = "DELETE e FROM echeance e "
                    + "JOIN participant p ON e.id_participant = p.id_participant "
                    + "WHERE p.id_adherent = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlEcheances)) {
                stmt.setInt(1, a.getIdAdherent());
                stmt.executeUpdate();
            }

            // 3. Delete all participations of this adherent
            String sqlParticipants = "DELETE FROM participant WHERE id_adherent = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlParticipants)) {
                stmt.setInt(1, a.getIdAdherent());
                stmt.executeUpdate();
            }

            // 4. Delete the adherent
            String sqlAdherent = "DELETE FROM adherent WHERE id_adherent = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlAdherent)) {
                stmt.setInt(1, a.getIdAdherent());
                stmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            throw new RuntimeException("Impossible de supprimer l'adhérent : " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Adherent findById(int id) {
        String sql = "SELECT * FROM adherent WHERE id_adherent = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAdherent(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Adherent> search(String query) {
        List<Adherent> adherents = new ArrayList<>();
        String sql = "SELECT * FROM adherent WHERE " +
                "nom LIKE ? OR prenom LIKE ? OR numero_identification LIKE ? OR telephone LIKE ? " +
                "ORDER BY numero_identification ASC";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            String q = "%" + query.trim() + "%";
            stmt.setString(1, q);
            stmt.setString(2, q);
            stmt.setString(3, q);
            stmt.setString(4, q);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    adherents.add(mapResultSetToAdherent(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adherents;
    }

    public Adherent findByNumeroIdentification(String numId) {
        String sql = "SELECT * FROM adherent WHERE numero_identification = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAdherent(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Adherent mapResultSetToAdherent(ResultSet rs) throws SQLException {
        Adherent a = new Adherent();
        a.setIdAdherent(rs.getInt("id_adherent"));
        a.setNumeroIdentification(rs.getString("numero_identification"));
        a.setNom(rs.getString("nom"));
        a.setPrenom(rs.getString("prenom"));
        a.setDateNaissance(rs.getDate("date_naissance"));
        a.setAdresse(rs.getString("adresse"));
        a.setTelephone(rs.getString("telephone"));
        a.setEmail(rs.getString("email"));
        a.setProfession(rs.getString("profession"));
        a.setEmployeur(rs.getString("employeur"));
        a.setRevenusEstimes(rs.getBigDecimal("revenus_estimes"));
        String statutStr = rs.getString("statut");
        if (statutStr != null) {
            a.setStatut(Adherent.Statut.valueOf(statutStr.toUpperCase()));
        }
        a.setDateAdhesion(rs.getTimestamp("date_adhesion"));
        a.setDateModification(rs.getTimestamp("date_modification"));
        return a;
    }
}
