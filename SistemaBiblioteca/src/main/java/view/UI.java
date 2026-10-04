package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public final class UI {
    private UI() {}

    public static final Font NORMAL = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font BOLD = new Font("SansSerif", Font.BOLD, 13);
    public static final Font TITLE = new Font("SansSerif", Font.BOLD, 20);

    public static void configure() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
    }

    public static JButton button(String text) {
        JButton b = new JButton(text);
        b.setFont(NORMAL);
        b.setMargin(new Insets(5, 10, 5, 10));
        return b;
    }

    public static JTextField field(int columns) {
        JTextField f = new JTextField(columns);
        f.setFont(NORMAL);
        return f;
    }

    public static JPanel titled(String title) {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel l = new JLabel(title);
        l.setFont(TITLE);
        p.add(l, BorderLayout.NORTH);
        return p;
    }

    public static void error(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void info(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Biblioteca", JOptionPane.INFORMATION_MESSAGE);
    }
}