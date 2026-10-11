package controlador;

import dao.ReporteDAO;

import javax.swing.table.DefaultTableModel;

/**
 * Controlador encargado de los reportes.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class ReporteController {

    private final ReporteDAO reporteDAO;

    public ReporteController() {
        reporteDAO = new ReporteDAO();
    }

    public DefaultTableModel librosMasPrestados() {
        return reporteDAO.librosMasPrestados();
    }

    public DefaultTableModel historialPrestamos() {
        return reporteDAO.historialPrestamos();
    }

    public DefaultTableModel prestamosActivos() {
        return reporteDAO.prestamosActivos();
    }
}