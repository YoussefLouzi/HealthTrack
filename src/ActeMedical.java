import java.util.Date;

public class ActeMedical {
    private int idDossierMedical;
    private int idActeMedical;
    private int idPatient;  // Foreign key linking to Patient
    private String typeActeMedical;
    private Date dateActeMedical;
    private String medecinActeMedical;

    // Constructor
    public ActeMedical(int idDossierMedical, int idActeMedical, int idPatient,
                       String typeActeMedical, Date dateActeMedical, String medecinActeMedical) {
        this.idDossierMedical = idDossierMedical;
        this.idActeMedical = idActeMedical;
        this.idPatient = idPatient;
        this.typeActeMedical = typeActeMedical;
        this.dateActeMedical = dateActeMedical;
        this.medecinActeMedical = medecinActeMedical;
    }

    // Getters and Setters
    public int getIdDossierMedical() { return idDossierMedical; }
    public void setIdDossierMedical(int idDossierMedical) { this.idDossierMedical = idDossierMedical; }

    public int getIdActeMedical() { return idActeMedical; }
    public void setIdActeMedical(int idActeMedical) { this.idActeMedical = idActeMedical; }

    public int getIdPatient() { return idPatient; }
    public void setIdPatient(int idPatient) { this.idPatient = idPatient; }

    public String getTypeActeMedical() { return typeActeMedical; }
    public void setTypeActeMedical(String typeActeMedical) { this.typeActeMedical = typeActeMedical; }

    public Date getDateActeMedical() { return dateActeMedical; }
    public void setDateActeMedical(Date dateActeMedical) { this.dateActeMedical = dateActeMedical; }

    public String getMedecinActeMedical() { return medecinActeMedical; }
    public void setMedecinActeMedical(String medecinActeMedical) { this.medecinActeMedical = medecinActeMedical; }
}
