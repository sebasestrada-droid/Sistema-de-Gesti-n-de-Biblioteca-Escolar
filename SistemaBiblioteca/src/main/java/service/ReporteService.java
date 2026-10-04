package service;

import config.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReporteService {
    public List<String[]> librosMasPrestados() throws SQLException {
        List<String[]> rows = new ArrayList<>();
        String sql = """
                SELECT l.titulo, COUNT(p.id) cantidad
                FROM prestamos p JOIN libros l ON l.id=p.id_libro
                GROUP BY l.id, l.titulo
                ORDER BY cantidad DESC, l.titulo
                """;
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) rows.add(new String[]{rs.getString(1), String.valueOf(rs.getInt(2))});
        }
        return rows;
    }

    public List<String[]> prestamosActivos() throws SQLException {
        List<String[]> rows = new ArrayList<>();
        String sql = """
                SELECT e.nombre, l.titulo, p.fecha_prestamo, p.fecha_devolucion
                FROM prestamos p
                JOIN estudiantes e ON e.id=p.id_estudiante
                JOIN libros l ON l.id=p.id_libro
                WHERE p.devuelto=FALSE
                ORDER BY p.fecha_devolucion
                """;
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) rows.add(new String[]{
                    rs.getString(1), rs.getString(2),
                    rs.getDate(3).toString(), rs.getDate(4).toString()
            });
        }
        return rows;
    }

    public List<String[]> historialEstudiante(int idEstudiante) throws SQLException {
        List<String[]> rows = new ArrayList<>();
        String sql = """
                SELECT l.titulo, p.fecha_prestamo, p.fecha_devolucion, p.devuelto
                FROM prestamos p JOIN libros l ON l.id=p.id_libro
                WHERE p.id_estudiante=?
                ORDER BY p.fecha_prestamo DESC
                """;
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) rows.add(new String[]{
                        rs.getString(1), rs.getDate(2).toString(),
                        rs.getDate(3).toString(), rs.getBoolean(4) ? "Sí" : "No"
                });
            }
        }
        return rows;
    }
}