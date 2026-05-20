package utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class for managing database connections using HikariCP.
 * Configuration is externalized to /db.properties and can be overridden using
 * environment variables DB_URL, DB_USER, DB_PASSWORD.
 *
 * @author Major117
 */
public class DBConnection {

    private static final String DB_PROPERTIES_FILE = "/db.properties";
    private static final Properties DB_PROPERTIES = loadDbProperties();

    private static final HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName(getProperty("db.driverClassName", "com.mysql.cj.jdbc.Driver"));
            config.setJdbcUrl(getProperty("db.url", "jdbc:mysql://localhost:3306/cotizia_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true"));
            config.setUsername(getProperty("db.user", "root"));
            config.setPassword(getProperty("db.password", ""));

            config.addDataSourceProperty("cachePrepStmts", getProperty("db.pool.cachePrepStmts", "true"));
            config.addDataSourceProperty("prepStmtCacheSize", getProperty("db.pool.prepStmtCacheSize", "250"));
            config.addDataSourceProperty("prepStmtCacheSqlLimit", getProperty("db.pool.prepStmtCacheSqlLimit", "2048"));

            config.setMaximumPoolSize(getIntProperty("db.pool.maxPoolSize", 10));
            config.setMinimumIdle(getIntProperty("db.pool.minimumIdle", 5));
            config.setIdleTimeout(getLongProperty("db.pool.idleTimeout", 300000));
            config.setConnectionTimeout(getLongProperty("db.pool.connectionTimeout", 20000));

            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            System.err.println("CRITICAL: Failed to initialize database connection. Check configuration and database availability.");
            e.printStackTrace();
            throw new RuntimeException("Error initializing HikariCP DataSource: " + e.getMessage(), e);
        }
    }

    static Properties loadDbProperties() {
        Properties properties = new Properties();
        try (InputStream input = DBConnection.class.getResourceAsStream(DB_PROPERTIES_FILE)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("WARNING: Could not load " + DB_PROPERTIES_FILE + ". Falling back to environment variables and defaults.");
        }
        return properties;
    }

    private static String getProperty(String key, String defaultValue) {
        String envValue = System.getenv(keyToEnvName(key));
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        return DB_PROPERTIES.getProperty(key, defaultValue);
    }

    private static int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static long getLongProperty(String key, long defaultValue) {
        try {
            return Long.parseLong(getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static String keyToEnvName(String key) {
        return key.toUpperCase().replace('.', '_');
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    private DBConnection() {
        // Prevent instantiation
    }
}
