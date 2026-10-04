package dao;

import config.DatabaseConnection;
import model.Estudiante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {
    public List<Estudiante> listar() throws SQLException {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT * FROM estudiantes ORDER BY id";
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    public Estudiante buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM estudiantes WHERE id=?";
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public void insertar(Estudiante e) throws SQLException {
        String sql = "INSERT INTO estudiantes(nombre,rut,curso,correo) VALUES(?,?,?,?)";
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getRut());
            ps.setString(3, e.getCurso());
            ps.setString(4, e.getCorreo());
            ps.executeUpdate();
        }
    }

    public void actualizar(Estudiante e) throws SQLException {
        String sql = "UPDATE estudiantes SET nombre=?,rut=?,curso=?,correo=? WHERE id=?";
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getRut());
            ps.setString(3, e.getCurso());
            ps.setString(4, e.getCorreo());
            ps.setInt(5, e.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM estudiantes WHERE id=?";
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Estudiante map(ResultSet rs) throws SQLException {
        return new Estudiante(
                rs.getInt("id"), rs.getString("nombre"), rs.getString("rut"),
                rs.getString("curso"), rs.getString("correo")
        );
    }
}