import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientService {
    // Add a new Patient
    public void addPatient(Patient patient) throws SQLException {
        String sql = "INSERT INTO Patient (ncin, nomPrenomPatient, adressePatient, telPatient, mutuellePatient, typeMutuellePatient) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patient.getNcin());
            stmt.setString(2, patient.getNomPrenomPatient());
            stmt.setString(3, patient.getAdressePatient());
            stmt.setString(4, patient.getTelPatient());
            stmt.setBoolean(5, patient.isMutuellePatient());
            stmt.setString(6, patient.getTypeMutuellePatient());
            stmt.executeUpdate();
        }
    }

    // Retrieve all Patients
    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patient";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Patient patient = new Patient(
                        rs.getInt("idPatient"),
                        rs.getString("ncin"),
                        rs.getString("nomPrenomPatient"),
                        rs.getString("adressePatient"),
                        rs.getString("telPatient"),
                        rs.getBoolean("mutuellePatient"),
                        rs.getString("typeMutuellePatient")
                );
                patients.add(patient);
            }
        }
        return patients;
    }

    // Update a Patient
    public void updatePatient(Patient patient) throws SQLException {
        String sql = "UPDATE Patient SET ncin = ?, nomPrenomPatient = ?, adressePatient = ?, telPatient = ?, mutuellePatient = ?, typeMutuellePatient = ? WHERE idPatient = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patient.getNcin());
            stmt.setString(2, patient.getNomPrenomPatient());
            stmt.setString(3, patient.getAdressePatient());
            stmt.setString(4, patient.getTelPatient());
            stmt.setBoolean(5, patient.isMutuellePatient());
            stmt.setString(6, patient.getTypeMutuellePatient());
            stmt.setInt(7, patient.getIdPatient());
            stmt.executeUpdate();
        }
    }

    // Delete a Patient
    public void deletePatient(int idPatient) throws SQLException {
        String sql = "DELETE FROM Patient WHERE idPatient = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPatient);
            stmt.executeUpdate();
        }
    }
}
