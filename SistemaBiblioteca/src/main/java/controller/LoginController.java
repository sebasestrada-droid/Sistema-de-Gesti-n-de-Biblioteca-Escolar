package controller;

import model.Usuario;
import service.AutenticacionService;

public class LoginController {
    private final AutenticacionService service = new AutenticacionService();

    public Usuario autenticar(String correo, String clave) throws Exception {
        return service.login(correo, clave);
    }
}