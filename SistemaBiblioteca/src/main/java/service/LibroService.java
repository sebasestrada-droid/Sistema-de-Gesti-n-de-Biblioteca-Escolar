package service;

import dao.LibroDAO;
import model.Libro;

import java.sql.SQLException;
import java.util.List;

public class LibroService {
    private final LibroDAO dao = new LibroDAO();

    public List<Libro> listar() throws SQLException { return dao.listar(); }
    public void crear(Libro l) throws SQLException { validar(l); dao.insertar(l); }
    public void actualizar(Libro l) throws SQLException { validar(l); dao.actualizar(l); }
    public void eliminar(int id) throws SQLException { dao.eliminar(id); }

    private void validar(Libro l) {
        if (l.getTitulo().isBlank() || l.getAutor().isBlank() || l.getIsbn().isBlank()) {
            throw new IllegalArgumentException("Título, autor e ISBN son obligatorios.");
        }
        if (l.getStock() < 0) throw new IllegalArgumentException("El stock no puede ser negativo.");
    }
}