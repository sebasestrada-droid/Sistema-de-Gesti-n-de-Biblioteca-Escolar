package view;

import controller.LoginController;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField correo = UI.field(20);
    private final JPasswordField clave = new JPasswordField(20);
    private final JButton ingresar = UI.button("Ingresar");
    private final LoginController controller = new LoginController();

    public LoginFrame() {
        setTitle("Biblioteca Escolar - Inicio de sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(430, 260);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(7, 7, 7, 7);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Sistema de Gestión de Biblioteca", SwingConstants.CENTER);
        title.setFont(UI.TITLE);

        c.gridx=0; c.gridy=0; c.gridwidth=2;
        root.add(title, c);
        c.gridwidth=1;
        c.gridy++;
        root.add(new JLabel("Correo:"), c);
        c.gridx=1;
        root.add(correo, c);
        c.gridx=0; c.gridy++;
        root.add(new JLabel("Contraseña:"), c);
        c.gridx=1;
        root.add(clave, c);
        c.gridx=0; c.gridy++; c.gridwidth=2;
        root.add(ingresar, c);

        ingresar.addActionListener(e -> login());
        clave.addActionListener(e -> login());
        add(root);
    }

    private void login() {
        try {
            Usuario usuario = controller.autenticar(correo.getText(), new String(clave.getPassword()));
            if (usuario == null) {
                UI.error(this, "Correo o contraseña incorrectos.");
                return;
            }
            dispose();
            MainFrame main = new MainFrame(usuario);
            main.setVisible(true);
        } catch (Exception ex) {
            UI.error(this, "No fue posible iniciar sesión:\n" + ex.getMessage());
        }
    }
}