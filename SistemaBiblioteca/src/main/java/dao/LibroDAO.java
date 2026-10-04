package dao;

import config.DatabaseConnection;
import model.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {
    public List<Libro> listar() throws SQLException {
        List<Libro> lista = new ArrayList<>();
        String sql = """
                SELECT l.*, c.nombre AS categoria_nombre
                FROM libros l JOIN categorias c ON c.id=l.id_categoria
                ORDER BY l.id
                """;
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    public Libro buscarPorId(int id) throws SQLException {
        String sql = "SELECT l.*, c.nombre AS categoria_nombre FROM libros l JOIN categorias c ON c.id=l.id_categoria WHERE l.id=?";
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public void insertar(Libro l) throws SQLException {
        String sql = "INSERT INTO libros(titulo,autor,isbn,editorial,stock,id_categoria) VALUES(?,?,?,?,?,?)";
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getAutor());
            ps.setString(3, l.getIsbn());
            ps.setString(4, l.getEditorial());
            ps.setInt(5, l.getStock());
            ps.setInt(6, l.getIdCategoria());
            ps.executeUpdate();
        }
    }

    public void actualizar(Libro l) throws SQLException {
        String sql = "UPDATE libros SET titulo=?,autor=?,isbn=?,editorial=?,stock=?,id_categoria=? WHERE id=?";
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getAutor());
            ps.setString(3, l.getIsbn());
            ps.setString(4, l.getEditorial());
            ps.setInt(5, l.getStock());
            ps.setInt(6, l.getIdCategoria());
            ps.setInt(7, l.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM libros WHERE id=?";
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Libro map(ResultSet rs) throws SQLException {
        Libro l = new Libro(
                rs.getInt("id"), rs.getString("titulo"), rs.getString("autor"),
                rs.getString("isbn"), rs.getString("editorial"),
                rs.getInt("stock"), rs.getInt("id_categoria")
        );
        l.setCategoriaNombre(rs.getString("categoria_nombre"));
        return l;
    }
}