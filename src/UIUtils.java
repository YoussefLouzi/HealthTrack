import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class UIUtils {
    
    public static JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(15);
        field.setFont(AppConfig.REGULAR_FONT);
        field.setForeground(AppConfig.TEXT_COLOR);
        field.setBorder(createFieldBorder());
        
        // Add placeholder functionality
        if (placeholder != null && !placeholder.isEmpty()) {
            addPlaceholder(field, placeholder);
        }
        
        return field;
    }
    
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(AppConfig.REGULAR_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(AppConfig.ACCENT_COLOR);
        button.setBorder(new EmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(AppConfig.ACCENT_COLOR.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(AppConfig.ACCENT_COLOR);
            }
        });
        
        return button;
    }
    
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(AppConfig.REGULAR_FONT);
        button.setForeground(AppConfig.TEXT_COLOR);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(AppConfig.PRIMARY_COLOR, 1),
            new EmptyBorder(12, 24, 12, 24)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    public static Border createFieldBorder() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(10, 15, 10, 15)
        );
    }
    
    public static void addPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(AppConfig.LIGHT_TEXT);
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(AppConfig.TEXT_COLOR);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(AppConfig.LIGHT_TEXT);
                }
            }
        });
    }
    
    public static void showSuccessMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void showErrorMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static boolean showConfirmDialog(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "Confirm", 
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}