# HealthTrack Pro - Healthcare Management System

A modern Java Swing application for managing patient records and medical procedures with PostgreSQL database integration.

## Features

- **Patient Management**: Add, view, and manage patient records with validation
- **Medical Records**: Track medical procedures and consultations
- **Modern UI**: Clean, professional interface with sidebar navigation
- **Database Integration**: PostgreSQL backend with sample data
- **Input Validation**: Comprehensive data validation and error handling

## Screenshots

### Dashboard
Modern dashboard with feature cards and gradient design

### Patient Management
Comprehensive patient record management with NCIN validation

### Medical Records
Track consultations, hospitalizations, and medical procedures

## Prerequisites

- Java 8 or higher
- PostgreSQL 12 or higher
- PostgreSQL JDBC Driver (included)

## Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/healthtrack-pro.git
   cd healthtrack-pro
   ```

2. **Set up PostgreSQL Database**
   - Create database: `gestionhospitalisation`
   - Run the SQL script: `database_setup.sql`
   ```sql
   psql -U postgres -d gestionhospitalisation -f database_setup.sql
   ```

3. **Configure Database Connection**
   - Update `DatabaseConnection.java` with your credentials
   - Or use system properties:
   ```bash
   java -Ddb.user=your_user -Ddb.password=your_password MainWindow
   ```

## Running the Application

### Windows
```bash
# Compile
javac -cp ".;postgresql-42.7.1.jar" *.java

# Run
java -cp ".;postgresql-42.7.1.jar" MainWindow
```

### Linux/Mac
```bash
# Compile
javac -cp ".:postgresql-42.7.1.jar" *.java

# Run
java -cp ".:postgresql-42.7.1.jar" MainWindow
```

## Database Schema

### Patient Table
- `id_patient` - Primary key (auto-increment)
- `ncin` - National ID (unique)
- `nom_prenom_patient` - Full name
- `adresse_patient` - Address
- `tel_patient` - Phone number
- `mutuelle_patient` - Insurance status
- `type_mutuelle_patient` - Insurance type

### Medical Records Table
- `id_acte_medical` - Primary key
- `id_dossier_medical` - Medical file ID
- `id_patient` - Foreign key to patient
- `type_acte_medical` - Procedure type
- `date_acte_medical` - Date
- `medecin_acte_medical` - Doctor name

## Project Structure

```
src/
├── MainWindow.java          # Main application window
├── AppConfig.java           # Configuration constants
├── DatabaseConnection.java  # Database connectivity
├── Patient.java            # Patient model with validation
├── ActeMedical.java        # Medical record model
├── PatientPanel.java       # Patient management UI
├── ActeMedicalPanel.java   # Medical records UI
├── PatientService.java     # Patient data operations
├── ActeMedicalService.java # Medical record operations
├── UIUtils.java            # UI utility components
├── HealthTrackException.java # Custom exceptions
├── ButtonEditor.java       # Table button editor
└── ButtonRenderer.java     # Table button renderer
```

## Configuration

### Database Settings
```java
// Default configuration in DatabaseConnection.java
URL: jdbc:postgresql://localhost:5432/gestionhospitalisation
USER: postgres
PASSWORD: (configure as needed)
```

### UI Customization
Modify `AppConfig.java` for:
- Color scheme
- Fonts
- Window dimensions
- Application metadata

## Sample Data

The application includes sample data:
- 5 sample patients with Moroccan names and addresses
- 8 sample medical records
- Various insurance types (CNOPS, CNSS, RAMED)

## Features in Detail

### Patient Management
- Add new patients with comprehensive validation
- NCIN format validation (Moroccan National ID)
- Phone number format validation
- Insurance information tracking
- Patient search and filtering

### Medical Records
- Track different procedure types:
  - Consultation
  - Complete hospitalization
  - Day hospitalization
  - Other procedures
- Date tracking
- Doctor assignment
- Patient linkage

### Modern UI
- Professional sidebar navigation
- Dashboard with feature cards
- Real-time status updates
- Hover effects and animations
- Consistent styling throughout

## Error Handling

- Custom exception classes for different error types
- Database connection error handling
- Input validation with user-friendly messages
- Graceful degradation when database unavailable

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Authors

- **Youssef Louzi** - Lead Developer
- **Mehdi Aboulouafa** - Developer
- **Abdellah Ezzari** - Developer

## Acknowledgments

- PostgreSQL team for the excellent database system
- Java Swing community for UI components
- Contributors and testers

## Support

For support, please open an issue on GitHub or contact the development team.

---

**HealthTrack Pro v2.0** - Modern Healthcare Management System