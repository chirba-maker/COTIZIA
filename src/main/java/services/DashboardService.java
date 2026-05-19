package services;

import utils.DBConnection;
import models.Utilisateur;
import models.Collecteur;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.*;

/**
 * Service for Dashboard Statistics — Real-Time SQL Queries.
 * All KPIs and chart data are computed directly in the database
 * for maximum performance and accuracy.
 * 
 * @author Major117
 */
public class DashboardService {

        public Map<String, Object> getStats(Utilisateur user) {
                Map<String, Object> stats = new HashMap<String, Object>();

                int idCollecteur = -1;
                if (user != null && user.getRole() == Utilisateur.Role.COLLECTEUR) {
                        Collecteur c = new dao.CollecteurDaoImplement().findByUserId(user.getIdUtilisateur());
                        if (c != null) {
                                idCollecteur = c.getIdCollecteur();
                        }
                }

                // --- KPI 1: Total Adhérents (actifs) ---
                int totalAdherents = 0;
                if (idCollecteur != -1) {
                        String sqlAdherent = "SELECT COUNT(DISTINCT a.id_adherent) FROM adherent a WHERE (a.id_utilisateur = ? OR a.id_adherent IN (SELECT p.id_adherent FROM participant p JOIN cycle cy ON p.id_cycle = cy.id_cycle WHERE cy.id_collecteur = ?)) AND a.statut = 'actif'";
                        try (Connection conn = DBConnection.getConnection();
                             PreparedStatement stmt = conn.prepareStatement(sqlAdherent)) {
                                stmt.setInt(1, user.getIdUtilisateur());
                                stmt.setInt(2, idCollecteur);
                                try (ResultSet rs = stmt.executeQuery()) {
                                        if (rs.next()) {
                                                totalAdherents = rs.getInt(1);
                                        }
                                }
                        } catch (SQLException e) {
                                e.printStackTrace();
                        }
                } else {
                        String sqlAdherent = "SELECT COUNT(*) FROM adherent WHERE statut = 'actif'";
                        totalAdherents = countQuery(sqlAdherent, -1);
                }
                stats.put("totalAdherents", totalAdherents);

                // --- KPI 2: Total Collecte (somme des paiements) ---
                String sqlCollecte = "SELECT COALESCE(SUM(p.montant), 0) FROM paiement p ";
                if (idCollecteur != -1) {
                        sqlCollecte += "JOIN echeance e ON p.id_echeance = e.id_echeance JOIN participant part ON e.id_participant = part.id_participant JOIN cycle c ON part.id_cycle = c.id_cycle WHERE c.id_collecteur = ?";
                }
                BigDecimal totalCollecte = sumQuery(sqlCollecte, idCollecteur);
                stats.put("totalCollecte", totalCollecte);

                // --- KPI 3: Taux de Recouvrement ---
                String sqlTotalDu = "SELECT COALESCE(SUM(montant_du), 0) FROM echeance ";
                if (idCollecteur != -1) {
                        sqlTotalDu += "e JOIN cycle c ON e.id_cycle = c.id_cycle WHERE c.id_collecteur = ?";
                }
                BigDecimal totalDu = sumQuery(sqlTotalDu, idCollecteur);
                BigDecimal tauxRecouvrement = BigDecimal.ZERO;
                if (totalDu.compareTo(BigDecimal.ZERO) > 0) {
                        tauxRecouvrement = totalCollecte
                                        .multiply(new BigDecimal(100))
                                        .divide(totalDu, 1, RoundingMode.HALF_UP);
                }
                stats.put("tauxRecouvrement", tauxRecouvrement);

                // --- KPI 4: Total Impayés ---
                String sqlImpayes = "SELECT COUNT(*) FROM echeance ";
                if (idCollecteur != -1) {
                        sqlImpayes += "e JOIN cycle c ON e.id_cycle = c.id_cycle WHERE e.statut = 'impaye' AND c.id_collecteur = ?";
                } else {
                        sqlImpayes += "WHERE statut = 'impaye'";
                }
                stats.put("totalImpayes", countQuery(sqlImpayes, idCollecteur));

                // --- Graph: Monthly Trend (last 6 months) ---
                List<String> historyLabels = new ArrayList<String>();
                List<BigDecimal> historyValues = new ArrayList<BigDecimal>();
                loadMonthlyTrend(historyLabels, historyValues, idCollecteur);
                stats.put("historyLabels", historyLabels);
                stats.put("historyValues", historyValues);

                // --- Graph: Status Distribution (Pie Chart) ---
                List<String> statusLabels = new ArrayList<String>();
                List<Long> statusValues = new ArrayList<Long>();
                loadStatusDistribution(statusLabels, statusValues, idCollecteur);
                stats.put("statusDistLabels", statusLabels);
                stats.put("statusDistValues", statusValues);

                // --- Recent Activities (last 5 logs) ---
                List<Map<String, Object>> recentActivities = new ArrayList<>();
                loadRecentActivities(recentActivities, user);
                stats.put("recentActivities", recentActivities);

                // --- Latest Payments (last 5 payments) ---
                List<Map<String, Object>> latestPayments = new ArrayList<>();
                loadLatestPayments(latestPayments, idCollecteur);
                stats.put("latestPayments", latestPayments);

                return stats;
        }

        private void loadRecentActivities(List<Map<String, Object>> activities, Utilisateur user) {
                String sql = "SELECT m.action, m.entite, m.date_action, u.nom, u.prenom " +
                                "FROM mouchard m " +
                                "LEFT JOIN utilisateur u ON m.id_utilisateur = u.id_utilisateur ";

                if (user.getRole() == Utilisateur.Role.COLLECTEUR) {
                        sql += "WHERE m.id_utilisateur = ? ";
                }

                sql += "ORDER BY m.date_action DESC LIMIT 5";

                Connection conn = null;
                PreparedStatement stmt = null;
                ResultSet rs = null;
                try {
                        conn = DBConnection.getConnection();
                        stmt = conn.prepareStatement(sql);
                        if (user.getRole() == Utilisateur.Role.COLLECTEUR) {
                                stmt.setInt(1, user.getIdUtilisateur());
                        }
                        rs = stmt.executeQuery();
                        while (rs.next()) {
                                Map<String, Object> log = new HashMap<>();
                                log.put("action", rs.getString("action"));
                                log.put("entite", rs.getString("entite"));
                                log.put("date", rs.getTimestamp("date_action"));
                                log.put("user", rs.getString("prenom") + " " + rs.getString("nom"));
                                activities.add(log);
                        }
                } catch (SQLException e) {
                        System.err.println("[DashboardService] loadRecentActivities error: " + e.getMessage());
                } finally {
                        closeQuietly(rs, stmt, conn);
                }
        }

        private void loadLatestPayments(List<Map<String, Object>> payments, int idCollecteur) {
                String sql = "SELECT p.montant, p.date_paiement, a.nom, a.prenom " +
                                "FROM paiement p " +
                                "JOIN echeance e ON p.id_echeance = e.id_echeance " +
                                "JOIN participant part ON e.id_participant = part.id_participant " +
                                "JOIN adherent a ON part.id_adherent = a.id_adherent ";

                if (idCollecteur != -1) {
                        sql += "JOIN cycle c ON part.id_cycle = c.id_cycle WHERE c.id_collecteur = ? ";
                }

                sql += "ORDER BY p.date_paiement DESC LIMIT 5";

                Connection conn = null;
                PreparedStatement stmt = null;
                ResultSet rs = null;
                try {
                        conn = DBConnection.getConnection();
                        stmt = conn.prepareStatement(sql);
                        if (idCollecteur != -1) {
                                stmt.setInt(1, idCollecteur);
                        }
                        rs = stmt.executeQuery();
                        while (rs.next()) {
                                Map<String, Object> pay = new HashMap<>();
                                pay.put("montant", rs.getBigDecimal("montant"));
                                pay.put("date", rs.getTimestamp("date_paiement"));
                                pay.put("adherent", rs.getString("prenom") + " " + rs.getString("nom"));
                                payments.add(pay);
                        }
                } catch (SQLException e) {
                        System.err.println("[DashboardService] loadLatestPayments error: " + e.getMessage());
                } finally {
                        closeQuietly(rs, stmt, conn);
                }
        }

        private int countQuery(String sql, int idCollecteur) {
                Connection conn = null;
                PreparedStatement stmt = null;
                ResultSet rs = null;
                try {
                        conn = DBConnection.getConnection();
                        stmt = conn.prepareStatement(sql);
                        if (idCollecteur != -1) {
                                stmt.setInt(1, idCollecteur);
                        }
                        rs = stmt.executeQuery();
                        if (rs.next()) {
                                return rs.getInt(1);
                        }
                } catch (SQLException e) {
                        System.err.println("[DashboardService] countQuery error: " + e.getMessage());
                        e.printStackTrace();
                } finally {
                        closeQuietly(rs, stmt, conn);
                }
                return 0;
        }

        private BigDecimal sumQuery(String sql, int idCollecteur) {
                Connection conn = null;
                PreparedStatement stmt = null;
                ResultSet rs = null;
                try {
                        conn = DBConnection.getConnection();
                        stmt = conn.prepareStatement(sql);
                        if (idCollecteur != -1) {
                                stmt.setInt(1, idCollecteur);
                        }
                        rs = stmt.executeQuery();
                        if (rs.next()) {
                                BigDecimal val = rs.getBigDecimal(1);
                                return val != null ? val : BigDecimal.ZERO;
                        }
                } catch (SQLException e) {
                        System.err.println("[DashboardService] sumQuery error: " + e.getMessage());
                        e.printStackTrace();
                } finally {
                        closeQuietly(rs, stmt, conn);
                }
                return BigDecimal.ZERO;
        }

        private void loadMonthlyTrend(List<String> labels, List<BigDecimal> values, int idCollecteur) {
                String sql = "SELECT " +
                                "  DATE_FORMAT(m.month_date, '%b %Y') AS label, " +
                                "  COALESCE(SUM(p.montant), 0) AS total " +
                                "FROM ( " +
                                "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL n MONTH), '%Y-%m-01') AS month_date "
                                +
                                "  FROM (SELECT 0 AS n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) months "
                                +
                                ") m " +
                                "LEFT JOIN ( " +
                                "  SELECT p2.montant, p2.date_paiement FROM paiement p2 ";

                if (idCollecteur != -1) {
                        sql += "  JOIN echeance e2 ON p2.id_echeance = e2.id_echeance " +
                                        "  JOIN cycle c2 ON e2.id_cycle = c2.id_cycle " +
                                        "  WHERE c2.id_collecteur = ? ";
                }

                sql += ") p ON DATE_FORMAT(p.date_paiement, '%Y-%m') = DATE_FORMAT(m.month_date, '%Y-%m') " +
                                "GROUP BY m.month_date " +
                                "ORDER BY m.month_date ASC";

                Connection conn = null;
                PreparedStatement stmt = null;
                ResultSet rs = null;
                try {
                        conn = DBConnection.getConnection();
                        stmt = conn.prepareStatement(sql);
                        if (idCollecteur != -1) {
                                stmt.setInt(1, idCollecteur);
                        }
                        rs = stmt.executeQuery();
                        while (rs.next()) {
                                labels.add(rs.getString("label"));
                                BigDecimal val = rs.getBigDecimal("total");
                                values.add(val != null ? val : BigDecimal.ZERO);
                        }
                } catch (SQLException e) {
                        System.err.println("[DashboardService] loadMonthlyTrend error: " + e.getMessage());
                        e.printStackTrace();
                        if (labels.isEmpty()) {
                                for (int i = 0; i < 6; i++) {
                                        labels.add("Mois " + (i + 1));
                                        values.add(BigDecimal.ZERO);
                                }
                        }
                } finally {
                        closeQuietly(rs, stmt, conn);
                }
        }

        private void loadStatusDistribution(List<String> labels, List<Long> values, int idCollecteur) {
                String sql = "SELECT e.statut, COUNT(*) AS cnt FROM echeance e ";
                if (idCollecteur != -1) {
                        sql += "JOIN cycle c ON e.id_cycle = c.id_cycle WHERE c.id_collecteur = ? ";
                }
                sql += "GROUP BY e.statut ORDER BY e.statut";

                Connection conn = null;
                PreparedStatement stmt = null;
                ResultSet rs = null;
                try {
                        conn = DBConnection.getConnection();
                        stmt = conn.prepareStatement(sql);
                        if (idCollecteur != -1) {
                                stmt.setInt(1, idCollecteur);
                        }
                        rs = stmt.executeQuery();
                        while (rs.next()) {
                                String statut = rs.getString("statut");
                                if (statut != null) {
                                        labels.add(statut.toUpperCase());
                                }
                                values.add(rs.getLong("cnt"));
                        }
                } catch (SQLException e) {
                        System.err.println("[DashboardService] loadStatusDistribution error: " + e.getMessage());
                        e.printStackTrace();
                } finally {
                        closeQuietly(rs, stmt, conn);
                }

                if (labels.isEmpty()) {
                        labels.add("AUCUNE");
                        values.add(0L);
                }
        }

        private void closeQuietly(ResultSet rs, Statement stmt, Connection conn) {
                try {
                        if (rs != null)
                                rs.close();
                } catch (SQLException ignored) {
                }
                try {
                        if (stmt != null)
                                stmt.close();
                } catch (SQLException ignored) {
                }
                try {
                        if (conn != null)
                                conn.close();
                } catch (SQLException ignored) {
                }
        }
}
