package controlador;

import dao.PrestamoDAO;
import modelo.Prestamo;
import servicio.ServicioPrestamos;

import java.util.List;
import java.util.function.Consumer;

/**
 * Controlador encargado de gestionar
 * préstamos y devoluciones.
 *
 * Utiliza ServicioPrestamos para ejecutar
 * las operaciones que modifican stock
 * en segundo plano.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class PrestamoController {

    private final PrestamoDAO prestamoDAO;
    private final ServicioPrestamos servicioPrestamos;

    public PrestamoController() {

        prestamoDAO =
                new PrestamoDAO();

        servicioPrestamos =
                new ServicioPrestamos();
    }

    public List<Prestamo> listar() {

        return prestamoDAO.readAll();
    }

    public Prestamo buscar(int id) {

        return prestamoDAO.readById(id);
    }

    /**
     * Registra préstamo en segundo plano.
     */
    public void registrar(
            Prestamo prestamo,
            Consumer<Boolean> callback) {

        servicioPrestamos.registrarPrestamoAsync(
                prestamo,
                callback
        );
    }

    /**
     * Registra devolución en segundo plano.
     */
    public void devolver(
            int idPrestamo,
            Consumer<Boolean> callback) {

        servicioPrestamos.devolverAsync(
                idPrestamo,
                callback
        );
    }

    public boolean actualizar(
            Prestamo prestamo) {

        return prestamoDAO.update(prestamo);
    }

    public boolean eliminar(
            int idPrestamo) {

        return prestamoDAO.delete(idPrestamo);
    }

    public void cerrar() {

        servicioPrestamos.cerrar();
    }
}