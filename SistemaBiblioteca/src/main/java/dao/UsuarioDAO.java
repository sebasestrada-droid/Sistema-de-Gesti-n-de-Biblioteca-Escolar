package dao;

import config.DatabaseConnection;
import model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    public Usuario autenticar(String correo, String contrasena) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND contraseña = ?";
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, contrasena);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public List<Usuario> listar() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id";
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    public void insertar(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios(nombre,rut,correo,contraseña,rol) VALUES(?,?,?,?,?)";
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getRut());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getContrasena());
            ps.setString(5, u.getRol());
            ps.executeUpdate();
        }
    }

    public void actualizar(Usuario u) throws SQLException {
        String sql = "UPDATE usuarios SET nombre=?,rut=?,correo=?,contraseña=?,rol=? WHERE id=?";
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getRut());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getContrasena());
            ps.setString(5, u.getRol());
            ps.setInt(6, u.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id=?";
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Usuario map(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id"), rs.getString("nombre"), rs.getString("rut"),
                rs.getString("correo"), rs.getString("contraseña"), rs.getString("rol")
        );
    }
}