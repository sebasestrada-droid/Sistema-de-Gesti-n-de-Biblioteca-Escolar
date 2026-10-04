package dao;

import config.DatabaseConnection;
import model.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    public List<Categoria> listar() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias ORDER BY nombre";
        try (Connection cn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Categoria(rs.getInt("id"), rs.getString("nombre")));
        }
        return lista;
    }
}