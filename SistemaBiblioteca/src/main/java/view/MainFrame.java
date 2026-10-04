package view;

import controller.BibliotecaController;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final Usuario usuario;
    private final BibliotecaController controller = new BibliotecaController();
    private final JPanel content = new JPanel(new CardLayout());

    public MainFrame(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Biblioteca Escolar - " + usuario.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        add(crearMenu(), BorderLayout.WEST);
        add(content, BorderLayout.CENTER);

        content.add(new InicioPanel(usuario), "inicio");
        content.add(new LibrosPanel(controller, usuario), "libros");
        content.add(new EstudiantesPanel(controller), "estudiantes");
        content.add(new PrestamosPanel(controller, usuario), "prestamos");
        content.add(new ReportesPanel(controller), "reportes");

        mostrar("inicio");
    }

    private JPanel crearMenu() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        menu.setPreferredSize(new Dimension(190, 0));

        JLabel titulo = new JLabel("BIBLIOTECA");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        menu.add(titulo);
        menu.add(Box.createVerticalStrut(20));

        addMenu(menu, "Inicio", "inicio");
        addMenu(menu, "Libros", "libros");
        if ("bibliotecario".equalsIgnoreCase(usuario.getRol())) {
            addMenu(menu, "Estudiantes", "estudiantes");
        }
        addMenu(menu, "Préstamos", "prestamos");
        addMenu(menu, "Reportes", "reportes");

        menu.add(Box.createVerticalGlue());
        JButton salir = UI.button("Cerrar sesión");
        salir.setAlignmentX(Component.CENTER_ALIGNMENT);
        salir.addActionListener(e -> {
            controller.shutdown();
            dispose();
            new LoginFrame().setVisible(true);
        });
        menu.add(salir);
        return menu;
    }

    private void addMenu(JPanel menu, String text, String card) {
        JButton b = UI.button(text);
        b.setMaximumSize(new Dimension(170, 36));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.addActionListener(e -> mostrar(card));
        menu.add(b);
        menu.add(Box.createVerticalStrut(7));
    }

    private void mostrar(String card) {
        ((CardLayout) content.getLayout()).show(content, card);
    }

    private static class InicioPanel extends JPanel {
        InicioPanel(Usuario u) {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
            JLabel t = new JLabel("<html><h1>Bienvenido/a</h1><p>" +
                    u.getNombre() + "</p><p>Rol: " + u.getRol() +
                    "</p><p>Seleccione una opción del menú.</p></html>");
            add(t, BorderLayout.NORTH);
        }
    }
}