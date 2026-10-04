package service;

import config.DatabaseConnection;
import dao.PrestamoDAO;
import model.Prestamo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PrestamoService {
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    public List<Prestamo> listar() throws SQLException {
        return prestamoDAO.listar();
    }

    public List<Prestamo> listarPorEstudiante(int idEstudiante) throws SQLException {
        return prestamoDAO.listarPorEstudiante(idEstudiante);
    }

    public void prestarAsync(int idEstudiante, int idLibro, OperationCallback callback) {
        executor.submit(() -> {
            try {
                registrarPrestamoSincronizado(idEstudiante, idLibro);
                callback.onSuccess("Préstamo registrado correctamente.");
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }

    public void devolverAsync(int idPrestamo, OperationCallback callback) {
        executor.submit(() -> {
            try {
                devolverSincronizado(idPrestamo);
                callback.onSuccess("Devolución registrada correctamente.");
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }


    private synchronized void registrarPrestamoSincronizado(int idEstudiante, int idLibro) throws SQLException {
        try (Connection cn = DatabaseConnection.getInstance().getConnection()) {
            cn.setAutoCommit(false);
            try {
                int stock;
                String lockSql = "SELECT stock FROM libros WHERE id=? FOR UPDATE";
                try (PreparedStatement ps = cn.prepareStatement(lockSql)) {
                    ps.setInt(1, idLibro);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new SQLException("El libro no existe.");
                        stock = rs.getInt("stock");
                    }
                }

                if (stock <= 0) throw new SQLException("No hay stock disponible para este libro.");

                LocalDate hoy = LocalDate.now();
                Prestamo p = new Prestamo(0, idEstudiante, idLibro, hoy, hoy.plusDays(7), false);
                prestamoDAO.insertar(p, cn);

                try (PreparedStatement ps = cn.prepareStatement(
                        "UPDATE libros SET stock=stock-1 WHERE id=?")) {
                    ps.setInt(1, idLibro);
                    ps.executeUpdate();
                }

                cn.commit();
            } catch (SQLException e) {
                cn.rollback();
                throw e;
            } finally {
                cn.setAutoCommit(true);
            }
        }
    }

    private synchronized void devolverSincronizado(int idPrestamo) throws SQLException {
        try (Connection cn = DatabaseConnection.getInstance().getConnection()) {
            cn.setAutoCommit(false);
            try {
                Prestamo p = prestamoDAO.buscarPorId(idPrestamo, cn);
                if (p == null) throw new SQLException("El préstamo no existe.");
                if (p.isDevuelto()) throw new SQLException("El préstamo ya está devuelto.");

                prestamoDAO.marcarDevuelto(idPrestamo, cn);

                try (PreparedStatement ps = cn.prepareStatement(
                        "UPDATE libros SET stock=stock+1 WHERE id=?")) {
                    ps.setInt(1, p.getIdLibro());
                    ps.executeUpdate();
                }

                cn.commit();
            } catch (SQLException e) {
                cn.rollback();
                throw e;
            } finally {
                cn.setAutoCommit(true);
            }
        }
    }

    public void shutdown() {
        executor.shutdown();
    }

    public interface OperationCallback {
        void onSuccess(String message);
        void onError(String message);
    }
}