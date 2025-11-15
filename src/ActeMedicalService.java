import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ActeMedicalService {
    private static final Logger logger = Logger.getLogger(ActeMedicalService.class.getName());

    private static final String INSERT_SQL = "INSERT INTO ActeMedical (idDossierMedical, idActeMedical, idPatient, typeActeMedical, dateActeMedical, medecinActeMedical) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM ActeMedical";
    private static final String UPDATE_SQL = "UPDATE ActeMedical SET typeActeMedical = ?, dateActeMedical = ?, medecinActeMedical = ? WHERE idActeMedical = ?";
    private static final String DELETE_SQL = "DELETE FROM ActeMedical WHERE idActeMedical = ?";

    public void addActeMedical(ActeMedical acte) throws SQLException {
        validateActeMedical(acte);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            stmt.setInt(1, acte.getIdDossierMedical());
            stmt.setInt(2, acte.getIdActeMedical());
            stmt.setInt(3, acte.getIdPatient());
            stmt.setString(4, acte.getTypeActeMedical());
            stmt.setDate(5, new java.sql.Date(acte.getDateActeMedical().getTime()));
            stmt.setString(6, acte.getMedecinActeMedical());
            stmt.executeUpdate();
        }
    }

    public List<ActeMedical> getAllActeMedicals() throws SQLException {
        List<ActeMedical> acteMedicals = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                acteMedicals.add(createActeMedicalFromResultSet(rs));
            }
        }
        return acteMedicals;
    }

    private ActeMedical createActeMedicalFromResultSet(ResultSet rs) throws SQLException {
        return new ActeMedical(
                rs.getInt("idDossierMedical"),
                rs.getInt("idActeMedical"),
                rs.getInt("idPatient"),
                rs.getString("typeActeMedical"),
                rs.getDate("dateActeMedical"),
                rs.getString("medecinActeMedical")
        );
    }

    public void updateActeMedical(ActeMedical acte) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, acte.getTypeActeMedical());
            stmt.setDate(2, new java.sql.Date(acte.getDateActeMedical().getTime()));
            stmt.setString(3, acte.getMedecinActeMedical());
            stmt.setInt(4, acte.getIdActeMedical());
            stmt.executeUpdate();
        }
    }

    public void deleteActeMedical(int idActeMedical) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            stmt.setInt(1, idActeMedical);
            stmt.executeUpdate();
        }
    }

    private void validateActeMedical(ActeMedical acte) throws SQLException {
        if (acte == null) {
            throw new SQLException("ActeMedical object cannot be null");
        }
        if (acte.getTypeActeMedical() == null || acte.getTypeActeMedical().trim().isEmpty()) {
            throw new SQLException("Type of medical act cannot be null or empty");
        }
        if (acte.getDateActeMedical() == null) {
            throw new SQLException("Date cannot be null");
        }
        if (acte.getMedecinActeMedical() == null || acte.getMedecinActeMedical().trim().isEmpty()) {
            throw new SQLException("Doctor name cannot be null or empty");
        }
    }
}