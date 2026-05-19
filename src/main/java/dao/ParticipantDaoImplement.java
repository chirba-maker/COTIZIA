package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Participant;
import utils.DBConnection;

/**
 * Implementation of Participant DAO.
 * 
 * @author Major117
 */
public class ParticipantDaoImplement implements ParticipantInterface {

    @Override
    public void save(Participant p) {
        String sql = "INSERT INTO participant (id_cycle, id_adherent, numero_ordre, statut, montant_recu) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, p.getIdCycle());
            stmt.setInt(2, p.getIdAdherent());
            stmt.setInt(3, p.getNumeroOrdre());
            stmt.setString(4, p.getStatut().name().toLowerCase());
            stmt.setBigDecimal(5, p.getMontantRecu());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setIdParticipant(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'enregistrement du participant : " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Participant p) {
        String sql = "UPDATE participant SET id_cycle = ?, id_adherent = ?, numero_ordre = ?, statut = ?, montant_recu = ? WHERE id_participant = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, p.getIdCycle());
            stmt.setInt(2, p.getIdAdherent());
            stmt.setInt(3, p.getNumeroOrdre());
            stmt.setString(4, p.getStatut().name().toLowerCase());
            stmt.setBigDecimal(5, p.getMontantRecu());
            stmt.setInt(6, p.getIdParticipant());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour du participant : " + e.getMessage(), e);
        }
    }

    @Override
    public List<Participant> getAll() {
        List<Participant> participants = new ArrayList<>();
        String sql = "SELECT * FROM participant ORDER BY date_inscription DESC";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                participants.add(mapResultSetToParticipant(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participants;
    }

    @Override
    public void delete(Participant p) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Delete paiements via echeances of this participant
            String sqlPaiements = "DELETE pa FROM paiement pa "
                    + "JOIN echeance e ON pa.id_echeance = e.id_echeance "
                    + "WHERE e.id_participant = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlPaiements)) {
                stmt.setInt(1, p.getIdParticipant());
                stmt.executeUpdate();
            }

            // 2. Delete echeances of this participant
            String sqlEcheances = "DELETE FROM echeance WHERE id_participant = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlEcheances)) {
                stmt.setInt(1, p.getIdParticipant());
                stmt.executeUpdate();
            }

            // 3. Delete the participant
            String sqlParticipant = "DELETE FROM participant WHERE id_participant = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlParticipant)) {
                stmt.setInt(1, p.getIdParticipant());
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
            throw new RuntimeException("Impossible de supprimer le participant : " + e.getMessage(), e);
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
    public Participant findById(int id) {
        String sql = "SELECT * FROM participant WHERE id_participant = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToParticipant(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Participant> findByCycle(int idCycle) {
        List<Participant> participants = new ArrayList<>();
        String sql = "SELECT * FROM participant WHERE id_cycle = ? ORDER BY numero_ordre";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCycle);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    participants.add(mapResultSetToParticipant(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participants;
    }

    private Participant mapResultSetToParticipant(ResultSet rs) throws SQLException {
        Participant p = new Participant();
        p.setIdParticipant(rs.getInt("id_participant"));
        p.setIdCycle(rs.getInt("id_cycle"));
        p.setIdAdherent(rs.getInt("id_adherent"));
        p.setNumeroOrdre(rs.getInt("numero_ordre"));
        String statutStr = rs.getString("statut");
        if (statutStr != null) {
            p.setStatut(Participant.Statut.valueOf(statutStr.toUpperCase()));
        }
        p.setMontantRecu(rs.getBigDecimal("montant_recu"));
        p.setDateInscription(rs.getTimestamp("date_inscription"));
        return p;
    }
}
