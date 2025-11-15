import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private JLabel statusLabel;
    private JButton currentActiveButton;

    public MainWindow() {
        initializeFrame();
        createComponents();
        setupLayout();
        startStatusUpdater();
    }
    
    private void initializeFrame() {
        setTitle(AppConfig.APP_NAME + " v" + AppConfig.VERSION);
        setSize(AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Set application icon
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage("R.png"));
        } catch (Exception e) {
            System.out.println("Icon not found, using default");
        }
    }
    
    private void createComponents() {
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        
        // Create panels
        JPanel homePanel = createEnhancedHomePanel();
        PatientPanel patientPanel = new PatientPanel();
        ActeMedicalPanel acteMedicalPanel = new ActeMedicalPanel();
        
        // Add panels to card layout
        mainContentPanel.add(homePanel, "Home");
        mainContentPanel.add(patientPanel, "Patients");
        mainContentPanel.add(acteMedicalPanel, "ActeMedical");
    }
    
    private void setupLayout() {
        add(createModernSidebar(), BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel createModernSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(AppConfig.PRIMARY_COLOR);
        sidebar.setPreferredSize(new Dimension(AppConfig.SIDEBAR_WIDTH, 0));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Logo/Title section
        JPanel logoPanel = createLogoPanel();
        sidebar.add(logoPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Navigation buttons
        JButton homeBtn = createSidebarButton("Dashboard", "Home");
        JButton patientsBtn = createSidebarButton("Patients", "Patients");
        JButton medicalBtn = createSidebarButton("Medical Records", "ActeMedical");
        
        sidebar.add(homeBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(patientsBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(medicalBtn);
        
        sidebar.add(Box.createVerticalGlue());
        
        // Settings and about section
        JButton settingsBtn = createSidebarButton("Settings", null);
        JButton aboutBtn = createSidebarButton("About", null);
        
        settingsBtn.addActionListener(e -> showSettingsDialog());
        aboutBtn.addActionListener(e -> showAboutDialog());
        
        sidebar.add(settingsBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(aboutBtn);
        
        // Set first button as active
        currentActiveButton = homeBtn;
        setActiveButton(homeBtn);
        
        return sidebar;
    }
    
    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setOpaque(false);
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("HealthTrack");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Pro v" + AppConfig.VERSION);
        subtitleLabel.setFont(AppConfig.SMALL_FONT);
        subtitleLabel.setForeground(AppConfig.LIGHT_TEXT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        logoPanel.add(titleLabel);
        logoPanel.add(subtitleLabel);
        
        return logoPanel;
    }
    
    private JButton createSidebarButton(String text, String cardName) {
        JButton button = new JButton(text);
        button.setFont(AppConfig.REGULAR_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(AppConfig.PRIMARY_COLOR);
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setMaximumSize(new Dimension(AppConfig.SIDEBAR_WIDTH - 20, AppConfig.BUTTON_HEIGHT));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (cardName != null) {
            button.addActionListener(e -> {
                cardLayout.show(mainContentPanel, cardName);
                setActiveButton(button);
            });
        }
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (button != currentActiveButton) {
                    button.setBackground(AppConfig.HOVER_COLOR);
                }
            }
            public void mouseExited(MouseEvent e) {
                if (button != currentActiveButton) {
                    button.setBackground(AppConfig.PRIMARY_COLOR);
                }
            }
        });
        
        return button;
    }
    
    private void setActiveButton(JButton button) {
        if (currentActiveButton != null) {
            currentActiveButton.setBackground(AppConfig.PRIMARY_COLOR);
        }
        currentActiveButton = button;
        button.setBackground(AppConfig.ACCENT_COLOR);
    }

    private JPanel createEnhancedHomePanel() {
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(AppConfig.SECONDARY_COLOR);
        
        // Header with gradient background
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, AppConfig.PRIMARY_COLOR, 0, getHeight(), AppConfig.ACCENT_COLOR);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 200));
        
        JLabel welcomeLabel = new JLabel("Welcome to " + AppConfig.APP_NAME, JLabel.CENTER);
        welcomeLabel.setFont(AppConfig.TITLE_FONT);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(new EmptyBorder(50, 0, 20, 0));
        
        JLabel subtitleLabel = new JLabel("Advanced Healthcare Management System", JLabel.CENTER);
        subtitleLabel.setFont(AppConfig.REGULAR_FONT);
        subtitleLabel.setForeground(Color.WHITE);
        
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Dashboard cards
        JPanel dashboardPanel = createDashboardCards();
        
        homePanel.add(headerPanel, BorderLayout.NORTH);
        homePanel.add(dashboardPanel, BorderLayout.CENTER);
        
        return homePanel;
    }
    
    private JPanel createDashboardCards() {
        JPanel dashboard = new JPanel(new GridLayout(2, 2, 20, 20));
        dashboard.setBackground(AppConfig.SECONDARY_COLOR);
        dashboard.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        dashboard.add(createDashboardCard("P", "Patients", "Manage patient records", AppConfig.ACCENT_COLOR));
        dashboard.add(createDashboardCard("M", "Medical Records", "Track medical procedures", AppConfig.WARNING_COLOR));
        dashboard.add(createDashboardCard("A", "Analytics", "View reports and statistics", AppConfig.PRIMARY_COLOR));
        dashboard.add(createDashboardCard("S", "Settings", "Configure application", AppConfig.DANGER_COLOR));
        
        return dashboard;
    }
    
    private JPanel createDashboardCard(String icon, String title, String description, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color, 2, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel iconLabel = new JLabel(icon, JLabel.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(AppConfig.HEADER_FONT);
        titleLabel.setForeground(AppConfig.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel descLabel = new JLabel(description, JLabel.CENTER);
        descLabel.setFont(AppConfig.SMALL_FONT);
        descLabel.setForeground(AppConfig.LIGHT_TEXT);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(descLabel);
        
        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(248, 249, 250));
            }
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });
        
        return card;
    }
    
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(AppConfig.PRIMARY_COLOR);
        statusBar.setBorder(new EmptyBorder(8, 15, 8, 15));
        
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(AppConfig.SMALL_FONT);
        statusLabel.setForeground(Color.WHITE);
        
        JLabel timeLabel = new JLabel();
        timeLabel.setFont(AppConfig.SMALL_FONT);
        timeLabel.setForeground(Color.WHITE);
        
        statusBar.add(statusLabel, BorderLayout.WEST);
        statusBar.add(timeLabel, BorderLayout.EAST);
        
        // Update time every second
        Timer timer = new Timer(1000, e -> {
            timeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        });
        timer.start();
        
        return statusBar;
    }
    
    private void startStatusUpdater() {
        Timer statusTimer = new Timer(3000, e -> {
            if (statusLabel.getText().equals("Ready")) {
                statusLabel.setText("System running smoothly");
            } else {
                statusLabel.setText("Ready");
            }
        });
        statusTimer.start();
    }
    
    private void showSettingsDialog() {
        JOptionPane.showMessageDialog(this, "Settings panel coming soon!", "Settings", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showAboutDialog() {
        String aboutText = String.format(
            "%s v%s\n\nDeveloped by:\n%s\n\nA modern healthcare management system.",
            AppConfig.APP_NAME, AppConfig.VERSION, String.join("\n", AppConfig.DEVELOPERS)
        );
        JOptionPane.showMessageDialog(this, aboutText, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }
}
