import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:" + System.getProperty("db.port", "5432") + "/gestionhospitalisation";
    private static final String USER = System.getProperty("db.user", "postgres");
    private static final String PASSWORD = System.getProperty("db.password", "");
    
    private static Connection connection;
    private static boolean driverLoaded = false;
    
    static {
        try {
            Class.forName("org.postgresql.Driver");
            driverLoaded = true;
            System.out.println("PostgreSQL driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL driver not found. Please download postgresql-42.7.1.jar");
            System.err.println("From: https://jdbc.postgresql.org/download/");
        }
    }
    
    public static Connection getConnection() throws SQLException {
        if (!driverLoaded) {
            throw new SQLException("PostgreSQL driver not available. Please add postgresql-42.7.1.jar to classpath");
        }
        
        if (connection == null || connection.isClosed()) {
            try {
                Properties props = new Properties();
                props.setProperty("user", USER);
                if (!PASSWORD.isEmpty()) {
                    props.setProperty("password", PASSWORD);
                }
                props.setProperty("ssl", "false");
                connection = DriverManager.getConnection(URL, props);
                System.out.println("Database connected successfully");
            } catch (SQLException e) {
                System.err.println("Database connection failed: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }
    
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
