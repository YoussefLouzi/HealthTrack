public class HealthTrackException extends Exception {
    private final String errorCode;
    
    public HealthTrackException(String message) {
        super(message);
        this.errorCode = "GENERAL_ERROR";
    }
    
    public HealthTrackException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public HealthTrackException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "GENERAL_ERROR";
    }
    
    public HealthTrackException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public static class DatabaseException extends HealthTrackException {
        public DatabaseException(String message) {
            super(message, "DB_ERROR");
        }
        
        public DatabaseException(String message, Throwable cause) {
            super(message, "DB_ERROR", cause);
        }
    }
    
    public static class ValidationException extends HealthTrackException {
        public ValidationException(String message) {
            super(message, "VALIDATION_ERROR");
        }
    }
    
    public static class PatientNotFoundException extends HealthTrackException {
        public PatientNotFoundException(int patientId) {
            super("Patient with ID " + patientId + " not found", "PATIENT_NOT_FOUND");
        }
    }
}