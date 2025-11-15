import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class ActeMedicalPanel extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(236, 240, 241);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color HOVER_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color ERROR_COLOR = new Color(231, 76, 60);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private final JTextField patientIdField, doctorField, dossierIdField;
    private final JComboBox<String> typeComboBox;
    private final JSpinner dateSpinner;
    private final JTable acteMedicalTable;
    private final DefaultTableModel tableModel;
    private final ActeMedicalService acteMedicalService;
    private final SimpleDateFormat dateFormat;

    public ActeMedicalPanel() {
        acteMedicalService = new ActeMedicalService();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        setLayout(new BorderLayout(10, 0));
        setBackground(SECONDARY_COLOR);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Initialize form components
        patientIdField = createStyledTextField();
        doctorField = createStyledTextField();
        dossierIdField = createStyledTextField();

        // Initialize date spinner
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);

        // Initialize type combo box
        String[] types = {"hospitalisation complète", "hospitalisation journalière", "consultation", "autre"};
        typeComboBox = new JComboBox<>(types);
        styleComboBox();

        // Initialize table
        tableModel = createTableModel();
        acteMedicalTable = new JTable(tableModel);

        initializeUI();
        refreshTableData();
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(15);
        field.setFont(REGULAR_FONT);
        field.setForeground(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    private void styleComboBox() {
        typeComboBox.setFont(REGULAR_FONT);
        typeComboBox.setForeground(TEXT_COLOR);
        typeComboBox.setBackground(Color.WHITE);
        typeComboBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
    }

    private DefaultTableModel createTableModel() {
        return new DefaultTableModel(
                new Object[]{"Dossier ID", "Patient ID", "Type", "Date", "Doctor", "Actions"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
    }

    private void initializeUI() {
        add(createSideBar(), BorderLayout.WEST);
        contentPanel.add(createAddPanel(), "Add");
        contentPanel.add(createManagePanel(), "Manage");
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createSideBar() {
        JPanel sideBar = new JPanel();
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
        sideBar.setBackground(PRIMARY_COLOR);
        sideBar.setBorder(new EmptyBorder(25, 20, 25, 20));
        sideBar.setPreferredSize(new Dimension(250, 0));

        JLabel titleLabel = new JLabel("Medical Records");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        sideBar.add(titleLabel);
        sideBar.add(Box.createRigidArea(new Dimension(0, 30)));
        sideBar.add(createNavButton("Add New Record", "Add"));
        sideBar.add(Box.createRigidArea(new Dimension(0, 15)));
        sideBar.add(createNavButton("Manage Records", "Manage"));

        return sideBar;
    }

    private JButton createNavButton(String text, String command) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(12, 20, 12, 20),
                new LineBorder(Color.WHITE, 1, true)
        ));
        button.setMaximumSize(new Dimension(220, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        button.addActionListener(e -> {
            cardLayout.show(contentPanel, command);
            refreshTableData();
        });

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(HOVER_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    private JPanel createAddPanel() {
        JPanel addPanel = new JPanel(new BorderLayout(20, 20));
        addPanel.setBackground(Color.WHITE);
        addPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 8, 12, 8);

        addFormField(formPanel, "Dossier ID:", dossierIdField, gbc, 0);
        addFormField(formPanel, "Patient ID:", patientIdField, gbc, 1);
        addFormField(formPanel, "Type:", typeComboBox, gbc, 2);
        addFormField(formPanel, "Date:", dateSpinner, gbc, 3);
        addFormField(formPanel, "Doctor:", doctorField, gbc, 4);

        JButton submitButton = createStyledButton("Save Record", SUCCESS_COLOR);
        submitButton.addActionListener(e -> handleAdd());

        addPanel.add(createHeaderLabel("Add New Medical Record"), BorderLayout.NORTH);
        addPanel.add(formPanel, BorderLayout.CENTER);
        addPanel.add(createButtonPanel(submitButton), BorderLayout.SOUTH);

        return addPanel;
    }

    private JPanel createManagePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        configureTable();
        JScrollPane scrollPane = new JScrollPane(acteMedicalTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(createHeaderLabel("Manage Medical Records"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void configureTable() {
        acteMedicalTable.setRowHeight(40);
        acteMedicalTable.setShowGrid(false);
        acteMedicalTable.setIntercellSpacing(new Dimension(0, 0));
        acteMedicalTable.setFont(REGULAR_FONT);

        JTableHeader header = acteMedicalTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        acteMedicalTable.setSelectionBackground(new Color(224, 236, 250));
        acteMedicalTable.setSelectionForeground(TEXT_COLOR);

        // Set up action column
        TableColumn actionColumn = acteMedicalTable.getColumnModel().getColumn(5);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor());
    }

    private void refreshTableData() {
        try {
            tableModel.setRowCount(0);
            List<ActeMedical> records = acteMedicalService.getAllActeMedicals();
            for (ActeMedical record : records) {
                tableModel.addRow(new Object[]{
                        record.getIdDossierMedical(),
                        record.getIdPatient(),
                        record.getTypeActeMedical(),
                        dateFormat.format(record.getDateActeMedical()),
                        record.getMedecinActeMedical(),
                        "Actions"
                });
            }
        } catch (SQLException ex) {
            showError("Error loading records: " + ex.getMessage());
        }
    }

    private void handleAdd() {
        try {
            validateFields();
            ActeMedical newActe = new ActeMedical(
                    Integer.parseInt(dossierIdField.getText()),
                    0, // ID will be generated
                    Integer.parseInt(patientIdField.getText()),
                    typeComboBox.getSelectedItem().toString(),
                    (Date) dateSpinner.getValue(),
                    doctorField.getText()
            );

            acteMedicalService.addActeMedical(newActe);
            refreshTableData();
            clearFields();
            showSuccess("Record added successfully!");
        } catch (Exception ex) {
            showError("Error adding record: " + ex.getMessage());
        }
    }

    private void handleEdit(int row) {
        try {
            String type = typeComboBox.getSelectedItem().toString();
            String doctor = JOptionPane.showInputDialog(this, "Enter new doctor name:",
                    tableModel.getValueAt(row, 4));

            if (doctor != null && !doctor.trim().isEmpty()) {
                ActeMedical acte = new ActeMedical(
                        (Integer) tableModel.getValueAt(row, 0),
                        0, // ID will be updated
                        (Integer) tableModel.getValueAt(row, 1),
                        type,
                        dateFormat.parse((String) tableModel.getValueAt(row, 3)),
                        doctor
                );

                acteMedicalService.updateActeMedical(acte);
                refreshTableData();
                showSuccess("Record updated successfully!");
            }
        } catch (Exception ex) {
            showError("Error updating record: " + ex.getMessage());
        }
    }

    private void handleDelete(int row) {
        try {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this record?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                int id = (Integer) tableModel.getValueAt(row, 0);
                acteMedicalService.deleteActeMedical(id);
                refreshTableData();
                showSuccess("Record deleted successfully!");
            }
        } catch (Exception ex) {
            showError("Error deleting record: " + ex.getMessage());
        }
    }

    private void validateFields() throws Exception {
        if (dossierIdField.getText().trim().isEmpty() ||
                patientIdField.getText().trim().isEmpty() ||
                doctorField.getText().trim().isEmpty()) {
            throw new Exception("All fields are required");
        }
        try {
            Integer.parseInt(dossierIdField.getText());
            Integer.parseInt(patientIdField.getText());
        } catch (NumberFormatException e) {
            throw new Exception("Dossier ID and Patient ID must be numbers");
        }
    }

    private void addFormField(JPanel panel, String label, JComponent field,
                              GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(HEADER_FONT);
        label.setForeground(PRIMARY_COLOR);
        return label;
    }

    private JPanel createButtonPanel(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        for (JButton button : buttons) {
            panel.add(button);
        }
        return panel;
    }

    private void clearFields() {
        dossierIdField.setText("");
        patientIdField.setText("");
        typeComboBox.setSelectedIndex(0);
        dateSpinner.setValue(new Date());
        doctorField.setText("");
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }


    // Custom renderer for action buttons
    private class ButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton editButton, deleteButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            editButton = createStyledButton("Edit", HOVER_COLOR);
            deleteButton = createStyledButton("Delete", ERROR_COLOR);
            add(editButton);
            add(deleteButton);
            setBackground(Color.WHITE);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }

    // Custom editor for action buttons
    private class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel;
        private final JButton editButton;
        private final JButton deleteButton;

        public ButtonEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            editButton = createStyledButton("Edit", HOVER_COLOR);
            deleteButton = createStyledButton("Delete", ERROR_COLOR);

            editButton.addActionListener(e -> {
                int row = acteMedicalTable.getSelectedRow();
                handleEdit(row);
                fireEditingStopped();
            });

            deleteButton.addActionListener(e -> {
                int row = acteMedicalTable.getSelectedRow();
                handleDelete(row);
                fireEditingStopped();
            });

            panel.add(editButton);
            panel.add(deleteButton);
            panel.setBackground(Color.WHITE);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Actions";
        }
    }

    // Main method to test the panel
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("Medical Records Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);

            // Add the ActeMedicalPanel to the frame
            frame.add(new ActeMedicalPanel());
            frame.setVisible(true);
        });
    }
}