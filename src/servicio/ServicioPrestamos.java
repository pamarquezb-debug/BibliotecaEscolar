package servicio;

import dao.PrestamoDAO;
import modelo.Prestamo;

import javax.swing.SwingUtilities;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Servicio encargado de ejecutar las operaciones
 * de préstamos en segundo plano.
 *
 * Utiliza ExecutorService para evitar bloquear
 * el hilo gráfico de Swing.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class ServicioPrestamos {

    private final PrestamoDAO prestamoDAO;

    private final ExecutorService executor;

    public ServicioPrestamos() {

        prestamoDAO = new PrestamoDAO();

        executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Registra un préstamo en segundo plano.
     *
     * @param prestamo préstamo a registrar
     * @param callback función que recibe el resultado
     */
    public void registrarPrestamoAsync(
            Prestamo prestamo,
            Consumer<Boolean> callback) {

        executor.submit(() -> {

            boolean resultado =
                    prestamoDAO.create(prestamo);

            /*
             * La respuesta vuelve al Event Dispatch Thread
             * de Swing para actualizar la interfaz.
             */
            SwingUtilities.invokeLater(() ->
                    callback.accept(resultado)
            );
        });
    }

    /**
     * Registra una devolución en segundo plano.
     *
     * @param idPrestamo ID del préstamo
     * @param callback función que recibe el resultado
     */
    public void devolverAsync(
            int idPrestamo,
            Consumer<Boolean> callback) {

        executor.submit(() -> {

            boolean resultado =
                    prestamoDAO.devolver(idPrestamo);

            SwingUtilities.invokeLater(() ->
                    callback.accept(resultado)
            );
        });
    }

    /**
     * Finaliza ordenadamente el ExecutorService.
     */
    public void cerrar() {

        executor.shutdown();
    }
}