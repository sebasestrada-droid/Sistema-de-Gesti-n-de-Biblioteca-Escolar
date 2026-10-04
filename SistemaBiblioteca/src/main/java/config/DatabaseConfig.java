package config;

public final class DatabaseConfig {
    public static final String DB_URL =
            "jdbc:mysql://localhost:3306/biblioteca?useSSL=false&serverTimezone=America/Santiago&allowPublicKeyRetrieval=true";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "Atreus_1214";

    private DatabaseConfig() {
    }
}