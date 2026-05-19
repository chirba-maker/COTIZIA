package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Paiement;
import utils.DBConnection;

/**
 * Implementation of Paiement DAO.
 * 
 * @author Major117
 */
public class PaiementDaoImplement implements PaiementInterface {

    @Override
    public void save(Paiement p) {
        String sql = "INSERT INTO paiement (id_echeance, id_utilisateur, montant, mode_paiement, reference, note) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, p.getIdEcheance());
            stmt.setInt(2, p.getIdUtilisateur());
            stmt.setBigDecimal(3, p.getMontant());
            stmt.setString(4, p.getModePaiement().name().toLowerCase());
            stmt.setString(5, p.getReference());
            stmt.setString(6, p.getNote());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setIdPaiement(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Paiement p) {
        String sql = "UPDATE paiement SET id_echeance = ?, id_utilisateur = ?, montant = ?, mode_paiement = ?, reference = ?, note = ? WHERE id_paiement = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, p.getIdEcheance());
            stmt.setInt(2, p.getIdUtilisateur());
            stmt.setBigDecimal(3, p.getMontant());
            stmt.setString(4, p.getModePaiement().name().toLowerCase());
            stmt.setString(5, p.getReference());
            stmt.setString(6, p.getNote());
            stmt.setInt(7, p.getIdPaiement());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Paiement> getAll() {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement ORDER BY date_paiement DESC";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                paiements.add(mapResultSetToPaiement(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paiements;
    }

    @Override
    public void delete(Paiement p) {
        String sql = "DELETE FROM paiement WHERE id_paiement = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, p.getIdPaiement());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Paiement findById(int id) {
        String sql = "SELECT * FROM paiement WHERE id_paiement = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPaiement(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Paiement> findByEcheance(int idEcheance) {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement WHERE id_echeance = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEcheance);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    paiements.add(mapResultSetToPaiement(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paiements;
    }

    private Paiement mapResultSetToPaiement(ResultSet rs) throws SQLException {
        Paiement p = new Paiement();
        p.setIdPaiement(rs.getInt("id_paiement"));
        p.setIdEcheance(rs.getInt("id_echeance"));
        p.setIdUtilisateur(rs.getInt("id_utilisateur"));
        p.setMontant(rs.getBigDecimal("montant"));
        String modeStr = rs.getString("mode_paiement");
        if (modeStr != null) {
            p.setModePaiement(Paiement.ModePaiement.valueOf(modeStr.toUpperCase()));
        }
        p.setReference(rs.getString("reference"));
        p.setNote(rs.getString("note"));
        p.setDatePaiement(rs.getTimestamp("date_paiement"));
        return p;
    }
}
