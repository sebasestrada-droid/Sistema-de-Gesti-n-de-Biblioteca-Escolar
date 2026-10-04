package view;

import controller.BibliotecaController;
import model.Categoria;
import model.Libro;
import dao.CategoriaDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LibrosPanel extends JPanel {
    private final BibliotecaController controller;
    private final boolean admin;
    private final JTable table = new JTable();
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID","Título","Autor","ISBN","Editorial","Stock","Categoría"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };

    public LibrosPanel(BibliotecaController controller, model.Usuario usuario) {
        this.controller = controller;
        this.admin = "bibliotecario".equalsIgnoreCase(usuario.getRol());
        setLayout(new BorderLayout(8,8));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel title = new JLabel("Gestión de libros");
        title.setFont(UI.TITLE);
        add(title, BorderLayout.NORTH);

        table.setModel(model);
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton actualizar = UI.button("Actualizar");
        buttons.add(actualizar);
        if (admin) {
            JButton nuevo = UI.button("Nuevo");
            JButton editar = UI.button("Editar");
            JButton eliminar = UI.button("Eliminar");
            buttons.add(nuevo); buttons.add(editar); buttons.add(eliminar);
            nuevo.addActionListener(e -> formulario(null));
            editar.addActionListener(e -> editarSeleccionado());
            eliminar.addActionListener(e -> eliminarSeleccionado());
        }
        actualizar.addActionListener(e -> cargar());
        add(buttons, BorderLayout.SOUTH);
        cargar();
    }

    private void cargar() {
        try {
            model.setRowCount(0);
            for (Libro l : controller.libros()) {
                model.addRow(new Object[]{l.getId(),l.getTitulo(),l.getAutor(),l.getIsbn(),
                        l.getEditorial(),l.getStock(),l.getCategoriaNombre()});
            }
        } catch (Exception e) { UI.error(this, e.getMessage()); }
    }

    private Libro seleccionado() throws Exception {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        int id = Integer.parseInt(table.getValueAt(table.convertRowIndexToModel(row),0).toString());
        for (Libro l : controller.libros()) if (l.getId()==id) return l;
        return null;
    }

    private void formulario(Libro existente) {
        JTextField titulo = UI.field(18), autor = UI.field(18), isbn = UI.field(15), editorial = UI.field(15), stock = UI.field(5);
        JComboBox<Categoria> categoria = new JComboBox<>();
        try {
            for (Categoria c : new CategoriaDAO().listar()) categoria.addItem(c);
        } catch (Exception e) { UI.error(this, e.getMessage()); return; }

        if (existente != null) {
            titulo.setText(existente.getTitulo()); autor.setText(existente.getAutor());
            isbn.setText(existente.getIsbn()); editorial.setText(existente.getEditorial());
            stock.setText(String.valueOf(existente.getStock()));
            for (int i=0;i<categoria.getItemCount();i++)
                if (categoria.getItemAt(i).getId()==existente.getIdCategoria()) categoria.setSelectedIndex(i);
        }

        JPanel p = new JPanel(new GridLayout(0,2,6,6));
        p.add(new JLabel("Título:")); p.add(titulo);
        p.add(new JLabel("Autor:")); p.add(autor);
        p.add(new JLabel("ISBN:")); p.add(isbn);
        p.add(new JLabel("Editorial:")); p.add(editorial);
        p.add(new JLabel("Stock:")); p.add(stock);
        p.add(new JLabel("Categoría:")); p.add(categoria);

        int result = JOptionPane.showConfirmDialog(this,p,
                existente==null?"Nuevo libro":"Editar libro",JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        try {
            int stockValue = Integer.parseInt(stock.getText().trim());
            Categoria c = (Categoria) categoria.getSelectedItem();
            Libro l = existente == null ? new Libro() : existente;
            l.setTitulo(titulo.getText().trim()); l.setAutor(autor.getText().trim());
            l.setIsbn(isbn.getText().trim()); l.setEditorial(editorial.getText().trim());
            l.setStock(stockValue); l.setIdCategoria(c.getId());
            if (existente == null) controller.crearLibro(l); else controller.editarLibro(l);
            cargar();
        } catch (Exception e) { UI.error(this, e.getMessage()); }
    }

    private void editarSeleccionado() {
        try {
            Libro l = seleccionado();
            if (l == null) { UI.info(this,"Seleccione un libro."); return; }
            formulario(l);
        } catch (Exception e) { UI.error(this,e.getMessage()); }
    }

    private void eliminarSeleccionado() {
        try {
            Libro l = seleccionado();
            if (l == null) { UI.info(this,"Seleccione un libro."); return; }
            int ok = JOptionPane.showConfirmDialog(this,"¿Eliminar \""+l.getTitulo()+"\"?",
                    "Confirmar",JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                controller.borrarLibro(l.getId());
                cargar();
            }
        } catch (Exception e) { UI.error(this,e.getMessage()); }
    }
}