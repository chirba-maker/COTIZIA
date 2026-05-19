package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Cycle;
import utils.DBConnection;

/**
 * Implementation of Cycle DAO.
 * 
 * @author Major117
 */
public class CycleDaoImplement implements CycleInterface {

    @Override
    public void save(Cycle c) {
        String sql = "INSERT INTO cycle (id_collecteur, libelle, description, montant_cotisation, frequence, date_debut, nombre_tours, statut, support_demande) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, c.getIdCollecteur());
            stmt.setString(2, c.getLibelle());
            stmt.setString(3, c.getDescription());
            stmt.setBigDecimal(4, c.getMontantCotisation());
            stmt.setString(5, c.getFrequence().name().toLowerCase());
            stmt.setDate(6, c.getDateDebut());
            stmt.setInt(7, c.getNombreTours());
            stmt.setString(8, c.getStatut().name().toLowerCase());
            stmt.setBoolean(9, c.isSupportDemande());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setIdCycle(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Cycle c) {
        String sql = "UPDATE cycle SET id_collecteur = ?, libelle = ?, description = ?, montant_cotisation = ?, frequence = ?, date_debut = ?, nombre_tours = ?, statut = ?, support_demande = ? WHERE id_cycle = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, c.getIdCollecteur());
            stmt.setString(2, c.getLibelle());
            stmt.setString(3, c.getDescription());
            stmt.setBigDecimal(4, c.getMontantCotisation());
            stmt.setString(5, c.getFrequence().name().toLowerCase());
            stmt.setDate(6, c.getDateDebut());
            stmt.setInt(7, c.getNombreTours());
            stmt.setString(8, c.getStatut().name().toLowerCase());
            stmt.setBoolean(9, c.isSupportDemande());
            stmt.setInt(10, c.getIdCycle());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Cycle> getAll() {
        List<Cycle> cycles = new ArrayList<>();
        String sql = "SELECT * FROM cycle ORDER BY date_creation DESC";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cycles.add(mapResultSetToCycle(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cycles;
    }

    @Override
    public void delete(Cycle c) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Delete paiements via echeances of this cycle
            String sqlPaiements = "DELETE pa FROM paiement pa "
                    + "JOIN echeance e ON pa.id_echeance = e.id_echeance "
                    + "WHERE e.id_cycle = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlPaiements)) {
                stmt.setInt(1, c.getIdCycle());
                stmt.executeUpdate();
            }

            // 2. Delete echeances of this cycle
            String sqlEcheances = "DELETE FROM echeance WHERE id_cycle = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlEcheances)) {
                stmt.setInt(1, c.getIdCycle());
                stmt.executeUpdate();
            }

            // 3. Delete participants of this cycle
            String sqlParticipants = "DELETE FROM participant WHERE id_cycle = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlParticipants)) {
                stmt.setInt(1, c.getIdCycle());
                stmt.executeUpdate();
            }

            // 4. Delete the cycle
            String sqlCycle = "DELETE FROM cycle WHERE id_cycle = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlCycle)) {
                stmt.setInt(1, c.getIdCycle());
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
            throw new RuntimeException("Impossible de supprimer le cycle : " + e.getMessage(), e);
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
    public Cycle findById(int id) {
        String sql = "SELECT * FROM cycle WHERE id_cycle = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCycle(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Cycle mapResultSetToCycle(ResultSet rs) throws SQLException {
        Cycle c = new Cycle();
        c.setIdCycle(rs.getInt("id_cycle"));
        c.setIdCollecteur(rs.getInt("id_collecteur"));
        c.setLibelle(rs.getString("libelle"));
        c.setDescription(rs.getString("description"));
        c.setMontantCotisation(rs.getBigDecimal("montant_cotisation"));
        String freqStr = rs.getString("frequence");
        if (freqStr != null) {
            c.setFrequence(Cycle.Frequence.valueOf(freqStr.toUpperCase()));
        }
        c.setDateDebut(rs.getDate("date_debut"));
        c.setNombreTours(rs.getInt("nombre_tours"));
        String statutStr = rs.getString("statut");
        if (statutStr != null) {
            c.setStatut(Cycle.Statut.valueOf(statutStr.toUpperCase()));
        }
        c.setDateCreation(rs.getTimestamp("date_creation"));
        c.setDateModification(rs.getTimestamp("date_modification"));
        c.setSupportDemande(rs.getBoolean("support_demande"));
        return c;
    }
}
