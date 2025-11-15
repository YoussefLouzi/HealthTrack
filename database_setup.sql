-- HealthTrack Database Setup Script
-- PostgreSQL Database: gestionhospitalisation

-- Create database (run this first as superuser)
-- CREATE DATABASE gestionhospitalisation;

-- Connect to the database and run the following:

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS acte_medical CASCADE;
DROP TABLE IF EXISTS patient CASCADE;

-- Create Patient table
CREATE TABLE patient (
    id_patient SERIAL PRIMARY KEY,
    ncin VARCHAR(10) NOT NULL UNIQUE,
    nom_prenom_patient VARCHAR(100) NOT NULL,
    adresse_patient TEXT NOT NULL,
    tel_patient VARCHAR(20),
    mutuelle_patient BOOLEAN DEFAULT FALSE,
    type_mutuelle_patient VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create ActeMedical table
CREATE TABLE acte_medical (
    id_acte_medical SERIAL PRIMARY KEY,
    id_dossier_medical INTEGER NOT NULL,
    id_patient INTEGER NOT NULL REFERENCES patient(id_patient) ON DELETE CASCADE,
    type_acte_medical VARCHAR(50) NOT NULL,
    date_acte_medical DATE NOT NULL DEFAULT CURRENT_DATE,
    medecin_acte_medical VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_patient_ncin ON patient(ncin);
CREATE INDEX idx_acte_medical_patient ON acte_medical(id_patient);
CREATE INDEX idx_acte_medical_date ON acte_medical(date_acte_medical);
CREATE INDEX idx_acte_medical_type ON acte_medical(type_acte_medical);

-- Insert sample data
INSERT INTO patient (ncin, nom_prenom_patient, adresse_patient, tel_patient, mutuelle_patient, type_mutuelle_patient) VALUES
('AB123456', 'Ahmed Ben Ali', '123 Rue Mohammed V, Casablanca', '0612345678', TRUE, 'CNOPS'),
('CD789012', 'Fatima El Mansouri', '456 Avenue Hassan II, Rabat', '0687654321', TRUE, 'CNSS'),
('EF345678', 'Omar Benali', '789 Boulevard Zerktouni, Marrakech', '0698765432', FALSE, NULL),
('GH901234', 'Aicha Alami', '321 Rue Ibn Battuta, Fes', '0623456789', TRUE, 'RAMED'),
('IJ567890', 'Youssef Tazi', '654 Avenue Mohammed VI, Agadir', '0634567890', FALSE, NULL);

INSERT INTO acte_medical (id_dossier_medical, id_patient, type_acte_medical, date_acte_medical, medecin_acte_medical) VALUES
(1001, 1, 'consultation', '2024-01-15', 'Dr. Hassan Benali'),
(1002, 2, 'hospitalisation complète', '2024-01-20', 'Dr. Amina Tazi'),
(1003, 3, 'hospitalisation journalière', '2024-01-25', 'Dr. Mohammed Alami'),
(1004, 1, 'autre', '2024-02-01', 'Dr. Fatima Benali'),
(1005, 4, 'consultation', '2024-02-05', 'Dr. Omar El Mansouri'),
(1006, 5, 'hospitalisation complète', '2024-02-10', 'Dr. Aicha Tazi'),
(1007, 2, 'consultation', '2024-02-15', 'Dr. Hassan Benali'),
(1008, 3, 'autre', '2024-02-20', 'Dr. Youssef Alami');

-- Create a view for patient summary
CREATE VIEW patient_summary AS
SELECT 
    p.id_patient,
    p.ncin,
    p.nom_prenom_patient,
    p.tel_patient,
    p.mutuelle_patient,
    p.type_mutuelle_patient,
    COUNT(am.id_acte_medical) as total_actes,
    MAX(am.date_acte_medical) as derniere_visite
FROM patient p
LEFT JOIN acte_medical am ON p.id_patient = am.id_patient
GROUP BY p.id_patient, p.ncin, p.nom_prenom_patient, p.tel_patient, p.mutuelle_patient, p.type_mutuelle_patient;

-- Grant permissions (adjust username as needed)
-- GRANT ALL PRIVILEGES ON DATABASE gestionhospitalisation TO postgres;
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO postgres;

-- Display table information
SELECT 'Database setup completed successfully!' as status;
SELECT 'Patient table created with ' || COUNT(*) || ' records' as patient_info FROM patient;
SELECT 'ActeMedical table created with ' || COUNT(*) || ' records' as acte_info FROM acte_medical;