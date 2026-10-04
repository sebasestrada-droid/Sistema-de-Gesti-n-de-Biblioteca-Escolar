package dao;

import config.DatabaseConnection;
import model.Prestamo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {
    public List<Prestamo> listar() throws SQLException {
        List<Prestamo> lista = new ArrayList<>();
        String sql = """
                SELECT p.*, e.nombre AS estudiante_nombre, l.titulo AS libro_titulo
                FROM prestamos p
                JOIN estudiantes e ON e.id=p.id_estudiante
                JOIN libros l ON l.id=p.id_libro
                ORDER BY p.id DESC
                """;
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Prestamo p = map(rs);
                p.setEstudianteNombre(rs.getString("estudiante_nombre"));
                p.setLibroTitulo(rs.getString("libro_titulo"));
                lista.add(p);
            }
        }
        return lista;
    }

    public List<Prestamo> listarPorEstudiante(int idEstudiante) throws SQLException {
        List<Prestamo> lista = new ArrayList<>();
        String sql = """
                SELECT p.*, e.nombre AS estudiante_nombre, l.titulo AS libro_titulo
                FROM prestamos p
                JOIN estudiantes e ON e.id=p.id_estudiante
                JOIN libros l ON l.id=p.id_libro
                WHERE p.id_estudiante=?
                ORDER BY p.id DESC
                """;
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prestamo p = map(rs);
                    p.setEstudianteNombre(rs.getString("estudiante_nombre"));
                    p.setLibroTitulo(rs.getString("libro_titulo"));
                    lista.add(p);
                }
            }
        }
        return lista;
    }

    public void insertar(Prestamo p, Connection cn) throws SQLException {
        String sql = "INSERT INTO prestamos(id_estudiante,id_libro,fecha_prestamo,fecha_devolucion,devuelto) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, p.getIdEstudiante());
            ps.setInt(2, p.getIdLibro());
            ps.setDate(3, Date.valueOf(p.getFechaPrestamo()));
            ps.setDate(4, Date.valueOf(p.getFechaDevolucion()));
            ps.setBoolean(5, p.isDevuelto());
            ps.executeUpdate();
        }
    }

    public void marcarDevuelto(int id, Connection cn) throws SQLException {
        String sql = "UPDATE prestamos SET devuelto=TRUE WHERE id=? AND devuelto=FALSE";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new SQLException("El préstamo ya fue devuelto o no existe.");
            }
        }
    }

    public Prestamo buscarPorId(int id, Connection cn) throws SQLException {
        String sql = "SELECT * FROM prestamos WHERE id=?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    private Prestamo map(ResultSet rs) throws SQLException {
        Date f1 = rs.getDate("fecha_prestamo");
        Date f2 = rs.getDate("fecha_devolucion");
        return new Prestamo(
                rs.getInt("id"), rs.getInt("id_estudiante"), rs.getInt("id_libro"),
                f1.toLocalDate(), f2.toLocalDate(), rs.getBoolean("devuelto")
        );
    }
}