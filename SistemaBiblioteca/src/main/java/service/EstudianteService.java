package service;

import dao.EstudianteDAO;
import model.Estudiante;

import java.sql.SQLException;
import java.util.List;

public class EstudianteService {
    private final EstudianteDAO dao = new EstudianteDAO();

    public List<Estudiante> listar() throws SQLException { return dao.listar(); }
    public void crear(Estudiante e) throws SQLException { validar(e); dao.insertar(e); }
    public void actualizar(Estudiante e) throws SQLException { validar(e); dao.actualizar(e); }
    public void eliminar(int id) throws SQLException { dao.eliminar(id); }

    private void validar(Estudiante e) {
        if (e.getNombre().isBlank() || e.getRut().isBlank() || e.getCurso().isBlank()) {
            throw new IllegalArgumentException("Nombre, RUT y curso son obligatorios.");
        }
    }
}