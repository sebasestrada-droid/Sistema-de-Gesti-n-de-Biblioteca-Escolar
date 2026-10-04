package service;

import dao.UsuarioDAO;
import model.Usuario;

import java.sql.SQLException;

public class AutenticacionService {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario login(String correo, String clave) throws SQLException {
        if (correo == null || correo.isBlank() || clave == null || clave.isBlank()) {
            return null;
        }
        return usuarioDAO.autenticar(correo.trim(), clave);
    }
}