package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public final class DatabaseConnection {
    private static DatabaseConnection instance;

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No se encontró el driver de MySQL.", e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DatabaseConfig.DB_URL,
                DatabaseConfig.DB_USER,
                DatabaseConfig.DB_PASSWORD
        );
    }
}