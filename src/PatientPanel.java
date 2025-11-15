import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.border.*;
import java.awt.geom.RoundRectangle2D;

public class PatientPanel extends JPanel {
    // Composants du formulaire
    private JTextField ncinField, nameField, addressField, phoneField;
    private JCheckBox mutuelleCheckbox;
    private JComboBox<String> mutuelleTypeBox;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JPanel formPanel;
    private RoundedButton submitButton, clearButton;
    private boolean isUpdateMode = false;
    private int currentPatientId = -1;

    // Couleurs personnalisées pour un design moderne
    private static final Color PRIMARY_COLOR = new Color(49, 130, 206);    // Bleu professionnel
    private static final Color SECONDARY_COLOR = new Color(247, 250, 252); // Fond clair
    private static final Color SUCCESS_COLOR = new Color(72, 187, 120);    // Vert
    private static final Color DELETE_COLOR = new Color(245, 101, 101);    // Rouge
    private static final Color TEXT_COLOR = new Color(45, 55, 72);         // Texte foncé
    private static final Color BORDER_COLOR = new Color(226, 232, 240);    // Bordure claire

    // Polices personnalisées
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public PatientPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(SECONDARY_COLOR);
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // En-tête avec titre et bouton d'ajout
        createHeader();

        // Panel principal avec grille
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(SECONDARY_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();

        // Création et ajout du formulaire
        createFormPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 15);
        mainPanel.add(formPanel, gbc);

        // Création et ajout du tableau
        createTable();
        JScrollPane tableScrollPane = new JScrollPane(patientTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Panel contenant le tableau avec titre
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(createRoundedBorder(10));

        JLabel tableTitle = new JLabel("Liste des Patients");
        tableTitle.setFont(HEADER_FONT);
        tableTitle.setForeground(TEXT_COLOR);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        mainPanel.add(tablePanel, gbc);

        add(mainPanel, BorderLayout.CENTER);
        refreshTable();
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBackground(SECONDARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Gestion des Patients");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
    }

    private void createFormPanel() {
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(createRoundedBorder(10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // Titre du formulaire
        JLabel formTitle = new JLabel("Informations du Patient");
        formTitle.setFont(HEADER_FONT);
        formTitle.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 15, 20, 15);
        formPanel.add(formTitle, gbc);

        // Réinitialisation des insets pour les champs
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.gridwidth = 1;

        // Champs du formulaire
        addFormField(formPanel, gbc, 1, "NCIN:", ncinField = createStyledTextField());
        addFormField(formPanel, gbc, 2, "Nom et Prénom:", nameField = createStyledTextField());
        addFormField(formPanel, gbc, 3, "Adresse:", addressField = createStyledTextField());
        addFormField(formPanel, gbc, 4, "Téléphone:", phoneField = createStyledTextField());

        // Section Mutuelle
        JPanel mutuellePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        mutuellePanel.setBackground(Color.WHITE);
        mutuelleCheckbox = new JCheckBox("Mutuelle");
        mutuelleCheckbox.setFont(LABEL_FONT);
        mutuelleCheckbox.setBackground(Color.WHITE);

        mutuelleTypeBox = new JComboBox<>(new String[]{"Cnops", "Cnss", "assurance", "autre type"});
        mutuelleTypeBox.setFont(LABEL_FONT);
        styleComboBox(mutuelleTypeBox);
        mutuelleTypeBox.setEnabled(false);

        mutuellePanel.add(mutuelleCheckbox);
        mutuellePanel.add(mutuelleTypeBox);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(mutuellePanel, gbc);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        submitButton = new RoundedButton("Ajouter Patient", SUCCESS_COLOR);
        clearButton = new RoundedButton("Retour", new Color(100, 116, 139));

        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 15, 15, 15);
        formPanel.add(buttonPanel, gbc);

        // Événements
        mutuelleCheckbox.addActionListener(e -> mutuelleTypeBox.setEnabled(mutuelleCheckbox.isSelected()));
        submitButton.addActionListener(e -> handleSubmit());
        clearButton.addActionListener(e -> clearForm());
    }

    private void createTable() {
        tableModel = new DefaultTableModel(
                new String[]{"ID", "NCIN", "Nom et Prénom", "Adresse", "Téléphone", "Mutuelle", "Type", "Actions"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };

        patientTable = new JTable(tableModel);
        patientTable.setRowHeight(40);
        patientTable.setFont(LABEL_FONT);
        patientTable.setGridColor(BORDER_COLOR);
        patientTable.setShowVerticalLines(false);
        patientTable.setSelectionBackground(new Color(238, 242, 255));

        // Style de l'en-tête
        JTableHeader header = patientTable.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setBackground(SECONDARY_COLOR);
        header.setForeground(TEXT_COLOR);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        // Configuration des colonnes
        TableColumnModel columnModel = patientTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);    // ID
        columnModel.getColumn(1).setPreferredWidth(100);   // NCIN
        columnModel.getColumn(2).setPreferredWidth(200);   // Nom
        columnModel.getColumn(3).setPreferredWidth(200);   // Adresse
        columnModel.getColumn(4).setPreferredWidth(120);   // Téléphone
        columnModel.getColumn(5).setPreferredWidth(80);    // Mutuelle
        columnModel.getColumn(6).setPreferredWidth(100);   // Type
        columnModel.getColumn(7).setPreferredWidth(200);   // Actions

        // Rendu personnalisé pour la colonne Actions
        TableColumn actionColumn = columnModel.getColumn(7);
        actionColumn.setCellRenderer(new ActionButtonRenderer());
        actionColumn.setCellEditor(new ActionButtonEditor());
    }

    // Classes internes pour les boutons d'action
    class ActionButtonRenderer extends JPanel implements TableCellRenderer {
        private RoundedButton editButton, deleteButton;

        public ActionButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setBackground(Color.WHITE);

            editButton = new RoundedButton("Modifier", PRIMARY_COLOR);
            deleteButton = new RoundedButton("Supprimer", DELETE_COLOR);

            editButton.setPreferredSize(new Dimension(60, 30));
            deleteButton.setPreferredSize(new Dimension(60, 30));

            add(editButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }

    class ActionButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private RoundedButton editButton, deleteButton;

        public ActionButtonEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setBackground(Color.WHITE);

            editButton = new RoundedButton("✎", PRIMARY_COLOR);
            deleteButton = new RoundedButton("×", DELETE_COLOR);

            editButton.setPreferredSize(new Dimension(30, 30));
            deleteButton.setPreferredSize(new Dimension(30, 30));

            editButton.addActionListener(e -> handleEdit());
            deleteButton.addActionListener(e -> handleDelete());

            panel.add(editButton);
            panel.add(deleteButton);
        }

        private void handleEdit() {
            int row = patientTable.getSelectedRow();
            if (row != -1) {
                currentPatientId = (int) patientTable.getValueAt(row, 0);
                ncinField.setText((String) patientTable.getValueAt(row, 1));
                nameField.setText((String) patientTable.getValueAt(row, 2));
                addressField.setText((String) patientTable.getValueAt(row, 3));
                phoneField.setText((String) patientTable.getValueAt(row, 4));
                boolean hasMutuelle = patientTable.getValueAt(row, 5).equals("Oui");
                mutuelleCheckbox.setSelected(hasMutuelle);
                mutuelleTypeBox.setEnabled(hasMutuelle);
                mutuelleTypeBox.setSelectedItem(patientTable.getValueAt(row, 6));

                isUpdateMode = true;
                submitButton.setText("Mettre à jour");
                submitButton.setBackground(PRIMARY_COLOR);
            }
            fireEditingStopped();
        }

        private void handleDelete() {
            int row = patientTable.getSelectedRow();
            if (row != -1) {
                int id = (int) patientTable.getValueAt(row, 0);
                int response = JOptionPane.showConfirmDialog(
                        panel,
                        "Êtes-vous sûr de vouloir supprimer ce patient ?",
                        "Confirmation de suppression",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (response == JOptionPane.YES_OPTION) {
                    try {
                        PatientService service = new PatientService();
                        service.deletePatient(id);
                        refreshTable();
                        showMessage("Patient supprimé avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException ex) {
                        showMessage("Erreur lors de la suppression: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            fireEditingStopped();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Actions";
        }
    }

    // Classe pour les boutons arrondis
    class RoundedButton extends JButton {
        public RoundedButton(String text, Color backgroundColor) {
            super(text);
            setFont(LABEL_FONT);
            setForeground(Color.WHITE);
            setBackground(backgroundColor);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Dimensions minimales
            setPreferredSize(new Dimension(120, 35));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Dessiner le fond arrondi
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, width, height, 10, 10));

            // Effet de brillance
            if (getModel().isPressed()) {
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fill(new RoundRectangle2D.Float(0, 0, width, height, 10, 10));
            }

            // Dessiner le texte
            FontMetrics fm = g2.getFontMetrics();
            Rectangle2D r = fm.getStringBounds(getText(), g2);
            int x = (width - (int) r.getWidth()) / 2;
            int y = (height - (int) r.getHeight()) / 2 + fm.getAscent();

            g2.setColor(getForeground());
            g2.drawString(getText(), x, y);
            g2.dispose();
        }
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(LABEL_FONT);
        field.setForeground(TEXT_COLOR);
        field.setPreferredSize(new Dimension(200, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
                createRoundedBorder(5),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setPreferredSize(new Dimension(150, 35));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(createRoundedBorder(5));
        ((JComponent) comboBox.getRenderer()).setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private Border createRoundedBorder(int radius) {
        return new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(x, y, width - 1, height - 1, radius * 2, radius * 2);
                g2.dispose();
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(radius, radius, radius, radius);
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        };
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_COLOR);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    private void handleSubmit() {
        try {
            if (!validateForm()) {
                return;
            }

            PatientService service = new PatientService();
            Patient patient = getPatientFromForm();

            if (isUpdateMode) {
                service.updatePatient(patient);
                showMessage("Patient mis à jour avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                service.addPatient(patient);
                showMessage("Patient ajouté avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }

            refreshTable();
            clearForm();
        } catch (SQLException ex) {
            showMessage("Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateForm() {
        if (ncinField.getText().trim().isEmpty()) {
            showMessage("Le NCIN est obligatoire!", "Erreur de validation", JOptionPane.ERROR_MESSAGE);
            ncinField.requestFocus();
            return false;
        }
        if (nameField.getText().trim().isEmpty()) {
            showMessage("Le nom et prénom sont obligatoires!", "Erreur de validation", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return false;
        }
        return true;
    }

    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                messageType
        );
    }

    private void clearForm() {
        ncinField.setText("");
        nameField.setText("");
        addressField.setText("");
        phoneField.setText("");
        mutuelleCheckbox.setSelected(false);
        mutuelleTypeBox.setSelectedIndex(0);
        mutuelleTypeBox.setEnabled(false);
        isUpdateMode = false;
        currentPatientId = -1;
        submitButton.setText("Ajouter Patient");
        submitButton.setBackground(SUCCESS_COLOR);
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            PatientService service = new PatientService();
            List<Patient> patients = service.getAllPatients();

            for (Patient patient : patients) {
                tableModel.addRow(new Object[]{
                        patient.getIdPatient(),
                        patient.getNcin(),
                        patient.getNomPrenomPatient(),
                        patient.getAdressePatient(),
                        patient.getTelPatient(),
                        patient.isMutuellePatient() ? "Oui" : "Non",
                        patient.getTypeMutuellePatient(),
                        "Actions"
                });
            }
        } catch (SQLException ex) {
            showMessage("Erreur lors du chargement des patients: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private Patient getPatientFromForm() {
        return new Patient(
                currentPatientId,
                ncinField.getText().trim(),
                nameField.getText().trim(),
                addressField.getText().trim(),
                phoneField.getText().trim(),
                mutuelleCheckbox.isSelected(),
                mutuelleCheckbox.isSelected() ? (String) mutuelleTypeBox.getSelectedItem() : null
        );
    }
}