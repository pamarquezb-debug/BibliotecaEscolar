package vista;

import controlador.ReporteController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Ventana de reportes del sistema Biblioteca Escolar.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class VentanaReportes extends JFrame {

    private final ReporteController reporteController;

    private JTable tablaLibros;
    private JTable tablaHistorial;
    private JTable tablaActivos;

    public VentanaReportes() {

        reporteController =
                new ReporteController();

        configurarVentana();
        crearComponentes();
        cargarReportes();
    }

    private void configurarVentana() {

        setTitle(
                "Biblioteca Escolar - Reportes"
        );

        setSize(1050, 650);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);
    }

    private void crearComponentes() {

        JPanel panelPrincipal =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        panelPrincipal.setBorder(
                new EmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        JLabel titulo =
                new JLabel(
                        "Reportes",
                        SwingConstants.CENTER
                );

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        panelPrincipal.add(
                titulo,
                BorderLayout.NORTH
        );

        JTabbedPane pestañas =
                new JTabbedPane();

        tablaLibros = new JTable();
        tablaHistorial = new JTable();
        tablaActivos = new JTable();

        pestañas.addTab(
                "Libros más prestados",
                new JScrollPane(tablaLibros)
        );

        pestañas.addTab(
                "Historial",
                new JScrollPane(tablaHistorial)
        );

        pestañas.addTab(
                "Préstamos activos",
                new JScrollPane(tablaActivos)
        );

        panelPrincipal.add(
                pestañas,
                BorderLayout.CENTER
        );

        JButton btnActualizar =
                new JButton(
                        "Actualizar reportes"
                );

        JButton btnCerrar =
                new JButton("Cerrar");

        JPanel inferior =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        inferior.add(btnActualizar);
        inferior.add(btnCerrar);

        panelPrincipal.add(
                inferior,
                BorderLayout.SOUTH
        );

        btnActualizar.addActionListener(
                e -> cargarReportes()
        );

        btnCerrar.addActionListener(
                e -> dispose()
        );

        add(panelPrincipal);
    }

    private void cargarReportes() {

        tablaLibros.setModel(
                reporteController
                        .librosMasPrestados()
        );

        tablaHistorial.setModel(
                reporteController
                        .historialPrestamos()
        );

        tablaActivos.setModel(
                reporteController
                        .prestamosActivos()
        );

        tablaLibros.setRowHeight(25);
        tablaHistorial.setRowHeight(25);
        tablaActivos.setRowHeight(25);
    }
}