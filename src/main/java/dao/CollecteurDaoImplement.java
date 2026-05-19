package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Collecteur;
import utils.DBConnection;

/**
 * Implementation of Collecteur DAO.
 * 
 * @author Major117
 */
public class CollecteurDaoImplement implements CollecteurInterface {

    @Override
    public void save(Collecteur c) {
        String sql = "INSERT INTO collecteur (id_utilisateur, nom, prenom, telephone, email, zone_collecte, statut) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, c.getIdUtilisateur());
            stmt.setString(2, c.getNom());
            stmt.setString(3, c.getPrenom());
            stmt.setString(4, c.getTelephone());
            stmt.setString(5, c.getEmail());
            stmt.setString(6, c.getZoneCollecte());
            stmt.setString(7, c.getStatut().name().toLowerCase());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setIdCollecteur(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Collecteur c) {
        String sql = "UPDATE collecteur SET id_utilisateur = ?, nom = ?, prenom = ?, telephone = ?, email = ?, zone_collecte = ?, statut = ? WHERE id_collecteur = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, c.getIdUtilisateur());
            stmt.setString(2, c.getNom());
            stmt.setString(3, c.getPrenom());
            stmt.setString(4, c.getTelephone());
            stmt.setString(5, c.getEmail());
            stmt.setString(6, c.getZoneCollecte());
            stmt.setString(7, c.getStatut().name().toLowerCase());
            stmt.setInt(8, c.getIdCollecteur());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Collecteur> getAll() {
        List<Collecteur> collectors = new ArrayList<>();
        String sql = "SELECT * FROM collecteur ORDER BY date_enregistrement DESC";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                collectors.add(mapResultSetToCollecteur(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return collectors;
    }

    @Override
    public void delete(Collecteur c) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Delete paiements via echeances of cycles of this collecteur
            String sqlPaiements = "DELETE pa FROM paiement pa "
                    + "JOIN echeance e ON pa.id_echeance = e.id_echeance "
                    + "JOIN participant p ON e.id_participant = p.id_participant "
                    + "JOIN cycle cy ON p.id_cycle = cy.id_cycle "
                    + "WHERE cy.id_collecteur = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlPaiements)) {
                stmt.setInt(1, c.getIdCollecteur());
                stmt.executeUpdate();
            }

            // 2. Delete echeances of cycles of this collecteur
            String sqlEcheances = "DELETE e FROM echeance e "
                    + "JOIN cycle cy ON e.id_cycle = cy.id_cycle "
                    + "WHERE cy.id_collecteur = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlEcheances)) {
                stmt.setInt(1, c.getIdCollecteur());
                stmt.executeUpdate();
            }

            // 3. Delete participants of cycles of this collecteur
            String sqlParticipants = "DELETE p FROM participant p "
                    + "JOIN cycle cy ON p.id_cycle = cy.id_cycle "
                    + "WHERE cy.id_collecteur = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlParticipants)) {
                stmt.setInt(1, c.getIdCollecteur());
                stmt.executeUpdate();
            }

            // 4. Delete cycles of this collecteur
            String sqlCycles = "DELETE FROM cycle WHERE id_collecteur = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlCycles)) {
                stmt.setInt(1, c.getIdCollecteur());
                stmt.executeUpdate();
            }

            // 5. Delete the collecteur
            String sqlCollecteur = "DELETE FROM collecteur WHERE id_collecteur = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlCollecteur)) {
                stmt.setInt(1, c.getIdCollecteur());
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
            throw new RuntimeException("Impossible de supprimer le collecteur : " + e.getMessage(), e);
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
    public Collecteur findById(int id) {
        String sql = "SELECT * FROM collecteur WHERE id_collecteur = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCollecteur(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Collecteur findByUserId(int userId) {
        String sql = "SELECT * FROM collecteur WHERE id_utilisateur = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCollecteur(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Collecteur mapResultSetToCollecteur(ResultSet rs) throws SQLException {
        Collecteur c = new Collecteur();
        c.setIdCollecteur(rs.getInt("id_collecteur"));
        c.setIdUtilisateur(rs.getInt("id_utilisateur"));
        c.setNom(rs.getString("nom"));
        c.setPrenom(rs.getString("prenom"));
        c.setTelephone(rs.getString("telephone"));
        c.setEmail(rs.getString("email"));
        c.setZoneCollecte(rs.getString("zone_collecte"));
        String statutStr = rs.getString("statut");
        if (statutStr != null) {
            c.setStatut(Collecteur.Statut.valueOf(statutStr.toUpperCase()));
        }
        c.setDateEnregistrement(rs.getTimestamp("date_enregistrement"));
        c.setDateModification(rs.getTimestamp("date_modification"));
        return c;
    }
}
