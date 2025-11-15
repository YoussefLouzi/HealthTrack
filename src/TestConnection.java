import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/gestionhospitalisation";
        String user = "postgres";
        String password = "mehdimehdi";
        
        System.out.println("Testing database connection...");
        System.out.println("URL: " + url);
        System.out.println("User: " + user);
        
        try {
            // Load PostgreSQL driver
            Class.forName("org.postgresql.Driver");
            System.out.println("PostgreSQL driver loaded successfully");
            
            // Test connection
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✓ Database connection successful!");
            
            // Test query
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM patient");
            if (rs.next()) {
                System.out.println("✓ Found " + rs.getInt(1) + " patients in database");
            }
            
            conn.close();
            System.out.println("✓ Connection closed successfully");
            
        } catch (ClassNotFoundException e) {
            System.err.println("✗ PostgreSQL driver not found!");
            System.err.println("Download postgresql-42.7.1.jar from https://jdbc.postgresql.org/download/");
        } catch (SQLException e) {
            System.err.println("✗ Database connection failed:");
            System.err.println("Error: " + e.getMessage());
            System.err.println("\nCheck:");
            System.err.println("1. PostgreSQL is running");
            System.err.println("2. Database 'gestionhospitalisation' exists");
            System.err.println("3. Username/password is correct");
            System.err.println("4. Port 5432 is correct");
        }
    }
}