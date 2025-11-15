import java.util.regex.Pattern;

public class Patient {
    private int idPatient;
    private String ncin;
    private String nomPrenomPatient;
    private String adressePatient;
    private String telPatient;
    private boolean mutuellePatient;
    private String typeMutuellePatient;
    
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[0-9]{10,15}$");
    private static final Pattern NCIN_PATTERN = Pattern.compile("^[A-Z]{1,2}[0-9]{6}$");

    // Constructor
    public Patient(int idPatient, String ncin, String nomPrenomPatient, String adressePatient,
                   String telPatient, boolean mutuellePatient, String typeMutuellePatient) {
        setIdPatient(idPatient);
        setNcin(ncin);
        setNomPrenomPatient(nomPrenomPatient);
        setAdressePatient(adressePatient);
        setTelPatient(telPatient);
        setMutuellePatient(mutuellePatient);
        setTypeMutuellePatient(typeMutuellePatient);
    }

    // Getters and Setters with validation
    public int getIdPatient() { return idPatient; }
    public void setIdPatient(int idPatient) { 
        if (idPatient <= 0) throw new IllegalArgumentException("Patient ID must be positive");
        this.idPatient = idPatient; 
    }

    public String getNcin() { return ncin; }
    public void setNcin(String ncin) { 
        if (ncin == null || ncin.trim().isEmpty()) {
            throw new IllegalArgumentException("NCIN cannot be empty");
        }
        if (!NCIN_PATTERN.matcher(ncin.trim().toUpperCase()).matches()) {
            throw new IllegalArgumentException("Invalid NCIN format");
        }
        this.ncin = ncin.trim().toUpperCase(); 
    }

    public String getNomPrenomPatient() { return nomPrenomPatient; }
    public void setNomPrenomPatient(String nomPrenomPatient) { 
        if (nomPrenomPatient == null || nomPrenomPatient.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.nomPrenomPatient = nomPrenomPatient.trim(); 
    }

    public String getAdressePatient() { return adressePatient; }
    public void setAdressePatient(String adressePatient) { 
        if (adressePatient == null || adressePatient.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }
        this.adressePatient = adressePatient.trim(); 
    }

    public String getTelPatient() { return telPatient; }
    public void setTelPatient(String telPatient) { 
        if (telPatient != null && !telPatient.trim().isEmpty() && 
            !PHONE_PATTERN.matcher(telPatient.trim()).matches()) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        this.telPatient = telPatient != null ? telPatient.trim() : null; 
    }

    public boolean isMutuellePatient() { return mutuellePatient; }
    public void setMutuellePatient(boolean mutuellePatient) { this.mutuellePatient = mutuellePatient; }

    public String getTypeMutuellePatient() { return typeMutuellePatient; }
    public void setTypeMutuellePatient(String typeMutuellePatient) { 
        this.typeMutuellePatient = typeMutuellePatient != null ? typeMutuellePatient.trim() : null; 
    }
    
    @Override
    public String toString() {
        return String.format("Patient{id=%d, ncin='%s', name='%s'}", 
            idPatient, ncin, nomPrenomPatient);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Patient patient = (Patient) obj;
        return idPatient == patient.idPatient;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(idPatient);
    }
}
