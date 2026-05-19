package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Echeance;
import utils.DBConnection;

/**
 * Implementation of Echeance DAO.
 * 
 * @author Major117
 */
public class EcheanceDaoImplement implements EcheanceInterface {

    @Override
    public void save(Echeance e) {
        String sql = "INSERT INTO echeance (id_participant, id_cycle, numero_tour, date_echeance, montant_du, montant_paye, statut, date_paiement, commentaire) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, e.getIdParticipant());
            stmt.setInt(2, e.getIdCycle());
            stmt.setInt(3, e.getNumeroTour());
            stmt.setDate(4, e.getDateEcheance());
            stmt.setBigDecimal(5, e.getMontantDu());
            stmt.setBigDecimal(6, e.getMontantPaye());
            stmt.setString(7, e.getStatut().name().toLowerCase());
            stmt.setTimestamp(8, e.getDatePaiement());
            stmt.setString(9, e.getCommentaire());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    e.setIdEcheance(rs.getInt(1));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Echeance e) {
        String sql = "UPDATE echeance SET id_participant = ?, id_cycle = ?, numero_tour = ?, date_echeance = ?, montant_du = ?, montant_paye = ?, statut = ?, date_paiement = ?, commentaire = ? WHERE id_echeance = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, e.getIdParticipant());
            stmt.setInt(2, e.getIdCycle());
            stmt.setInt(3, e.getNumeroTour());
            stmt.setDate(4, e.getDateEcheance());
            stmt.setBigDecimal(5, e.getMontantDu());
            stmt.setBigDecimal(6, e.getMontantPaye());
            stmt.setString(7, e.getStatut().name().toLowerCase());
            stmt.setTimestamp(8, e.getDatePaiement());
            stmt.setString(9, e.getCommentaire());
            stmt.setInt(10, e.getIdEcheance());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<Echeance> getAll() {
        List<Echeance> echeances = new ArrayList<>();
        String sql = "SELECT * FROM echeance ORDER BY date_creation DESC";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                echeances.add(mapResultSetToEcheance(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return echeances;
    }

    @Override
    public void delete(Echeance e) {
        String sql = "DELETE FROM echeance WHERE id_echeance = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, e.getIdEcheance());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Echeance findById(int id) {
        String sql = "SELECT * FROM echeance WHERE id_echeance = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEcheance(rs);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Echeance> findByCycle(int idCycle) {
        List<Echeance> echeances = new ArrayList<>();
        String sql = "SELECT * FROM echeance WHERE id_cycle = ? ORDER BY date_echeance, numero_tour";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCycle);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    echeances.add(mapResultSetToEcheance(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return echeances;
    }

    public List<Echeance> findByParticipant(int idParticipant) {
        List<Echeance> echeances = new ArrayList<>();
        String sql = "SELECT * FROM echeance WHERE id_participant = ? ORDER BY numero_tour";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idParticipant);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    echeances.add(mapResultSetToEcheance(rs));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return echeances;
    }

    private Echeance mapResultSetToEcheance(ResultSet rs) throws SQLException {
        Echeance e = new Echeance();
        e.setIdEcheance(rs.getInt("id_echeance"));
        e.setIdParticipant(rs.getInt("id_participant"));
        e.setIdCycle(rs.getInt("id_cycle"));
        e.setNumeroTour(rs.getInt("numero_tour"));
        e.setDateEcheance(rs.getDate("date_echeance"));
        e.setMontantDu(rs.getBigDecimal("montant_du"));
        e.setMontantPaye(rs.getBigDecimal("montant_paye"));
        String statutStr = rs.getString("statut");
        if (statutStr != null) {
            e.setStatut(Echeance.Statut.valueOf(statutStr.toUpperCase()));
        }
        e.setDatePaiement(rs.getTimestamp("date_paiement"));
        e.setCommentaire(rs.getString("commentaire"));
        e.setDateCreation(rs.getTimestamp("date_creation"));
        e.setDateModification(rs.getTimestamp("date_modification"));
        return e;
    }
}
