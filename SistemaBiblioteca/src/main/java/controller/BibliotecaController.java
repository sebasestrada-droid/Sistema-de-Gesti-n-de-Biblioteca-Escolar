package controller;

import model.*;
import service.*;

import java.sql.SQLException;
import java.util.List;

public class BibliotecaController {
    private final LibroService libroService = new LibroService();
    private final EstudianteService estudianteService = new EstudianteService();
    private final PrestamoService prestamoService = new PrestamoService();
    private final ReporteService reporteService = new ReporteService();

    public List<Libro> libros() throws SQLException { return libroService.listar(); }
    public void crearLibro(Libro l) throws SQLException { libroService.crear(l); }
    public void editarLibro(Libro l) throws SQLException { libroService.actualizar(l); }
    public void borrarLibro(int id) throws SQLException { libroService.eliminar(id); }

    public List<Estudiante> estudiantes() throws SQLException { return estudianteService.listar(); }
    public void crearEstudiante(Estudiante e) throws SQLException { estudianteService.crear(e); }
    public void editarEstudiante(Estudiante e) throws SQLException { estudianteService.actualizar(e); }
    public void borrarEstudiante(int id) throws SQLException { estudianteService.eliminar(id); }

    public List<Prestamo> prestamos() throws SQLException { return prestamoService.listar(); }
    public List<Prestamo> historial(int idEstudiante) throws SQLException { return prestamoService.listarPorEstudiante(idEstudiante); }

    public void prestar(int idEstudiante, int idLibro, PrestamoService.OperationCallback cb) {
        prestamoService.prestarAsync(idEstudiante, idLibro, cb);
    }

    public void devolver(int idPrestamo, PrestamoService.OperationCallback cb) {
        prestamoService.devolverAsync(idPrestamo, cb);
    }

    public List<String[]> librosMasPrestados() throws SQLException { return reporteService.librosMasPrestados(); }
    public List<String[]> prestamosActivos() throws SQLException { return reporteService.prestamosActivos(); }
    public List<String[]> historialReporte(int idEstudiante) throws SQLException { return reporteService.historialEstudiante(idEstudiante); }

    public void shutdown() { prestamoService.shutdown(); }
}