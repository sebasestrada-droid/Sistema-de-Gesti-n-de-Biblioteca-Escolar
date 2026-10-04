package app;

import view.LoginFrame;
import view.UI;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        UI.configure();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}