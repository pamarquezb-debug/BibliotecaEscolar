package vista;

import controlador.EstudianteController;
import controlador.LibroController;
import controlador.PrestamoController;
import modelo.Estudiante;
import modelo.Libro;
import modelo.Prestamo;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ventana para registrar préstamos y devoluciones
 * del sistema Biblioteca Escolar.
 *
 * Las operaciones que modifican el stock se ejecutan
 * en segundo plano mediante ServicioPrestamos.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class VentanaPrestamos extends JFrame {

    private final Usuario usuario;

    private final PrestamoController prestamoController;
    private final EstudianteController estudianteController;
    private final LibroController libroController;

    private JComboBox<Estudiante> cboEstudiante;
    private JComboBox<Libro> cboLibro;

    private JButton btnPrestar;
    private JButton btnDevolver;
    private JButton btnActualizar;
    private JButton btnCerrar;

    private JTable tablaPrestamos;
    private DefaultTableModel modeloTabla;

    private final Map<Integer, Estudiante> estudiantes;
    private final Map<Integer, Libro> libros;

    private int idPrestamoSeleccionado = 0;

    public VentanaPrestamos(Usuario usuario) {

        this.usuario = usuario;

        prestamoController =
                new PrestamoController();

        estudianteController =
                new EstudianteController();

        libroController =
                new LibroController();

        estudiantes = new HashMap<>();
        libros = new HashMap<>();

        configurarVentana();
        crearComponentes();

        cargarEstudiantes();
        cargarLibros();
        cargarPrestamos();

        configurarPermisos();
    }

    private void configurarVentana() {

        setTitle(
                "Biblioteca Escolar - Préstamos y Devoluciones"
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

        JLabel lblTitulo =
                new JLabel(
                        "Préstamos y Devoluciones",
                        SwingConstants.CENTER
                );

        lblTitulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        panelPrincipal.add(
                lblTitulo,
                BorderLayout.NORTH
        );

        JPanel panelCentro =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        panelCentro.add(
                crearPanelPrestamo(),
                BorderLayout.NORTH
        );

        panelCentro.add(
                crearPanelTabla(),
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                panelCentro,
                BorderLayout.CENTER
        );

        JPanel panelInferior =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        btnActualizar =
                new JButton("Actualizar lista");

        btnCerrar =
                new JButton("Cerrar");

        panelInferior.add(
                btnActualizar
        );

        panelInferior.add(
                btnCerrar
        );

        panelPrincipal.add(
                panelInferior,
                BorderLayout.SOUTH
        );

        add(panelPrincipal);

        registrarEventos();
    }

    private JPanel crearPanelPrestamo() {

        JPanel contenedor =
                new JPanel(
                        new BorderLayout()
                );

        contenedor.setBorder(
                BorderFactory.createTitledBorder(
                        "Registrar préstamo"
                )
        );

        JPanel formulario =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                10,
                                10
                        )
                );

        formulario.setBorder(
                new EmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        cboEstudiante =
                new JComboBox<>();

        cboLibro =
                new JComboBox<>();

        formulario.add(
                new JLabel("Estudiante:")
        );

        formulario.add(
                cboEstudiante
        );

        formulario.add(
                new JLabel("Libro disponible:")
        );

        formulario.add(
                cboLibro
        );

        JPanel panelBotones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER
                        )
                );

        btnPrestar =
                new JButton(
                        "Registrar préstamo"
                );

        btnDevolver =
                new JButton(
                        "Registrar devolución"
                );

        panelBotones.add(
                btnPrestar
        );

        panelBotones.add(
                btnDevolver
        );

        contenedor.add(
                formulario,
                BorderLayout.CENTER
        );

        contenedor.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        return contenedor;
    }

    private JScrollPane crearPanelTabla() {

        modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Estudiante",
                                "Libro",
                                "Fecha préstamo",
                                "Fecha devolución",
                                "Estado"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        tablaPrestamos =
                new JTable(modeloTabla);

        tablaPrestamos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaPrestamos.setRowHeight(25);

        return new JScrollPane(
                tablaPrestamos
        );
    }

    private void registrarEventos() {

        btnPrestar.addActionListener(
                e -> registrarPrestamo()
        );

        btnDevolver.addActionListener(
                e -> devolverPrestamo()
        );

        btnActualizar.addActionListener(
                e -> actualizarDatos()
        );

        btnCerrar.addActionListener(
                e -> cerrarVentana()
        );

        tablaPrestamos
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        seleccionarPrestamo();
                    }
                });
    }

    /**
     * Carga estudiantes para el JComboBox
     * y para resolver sus nombres en la tabla.
     */
    private void cargarEstudiantes() {

        cboEstudiante.removeAllItems();
        estudiantes.clear();

        boolean esBibliotecario =
                usuario.getRol()
                        .equalsIgnoreCase("bibliotecario");

        if (esBibliotecario) {

            List<Estudiante> lista =
                    estudianteController.listar();

            for (Estudiante estudiante : lista) {

                estudiantes.put(
                        estudiante.getId(),
                        estudiante
                );

                cboEstudiante.addItem(
                        estudiante
                );
            }

        } else {

            Estudiante estudiante =
                    estudianteController.buscarPorRut(
                            usuario.getRut()
                    );

            if (estudiante != null) {

                estudiantes.put(
                        estudiante.getId(),
                        estudiante
                );

                cboEstudiante.addItem(
                        estudiante
                );
            }
        }
    }

    /**
     * Carga todos los libros para resolver nombres,
     * pero muestra en el ComboBox solamente los
     * que actualmente tienen stock.
     */
    private void cargarLibros() {

        cboLibro.removeAllItems();
        libros.clear();

        List<Libro> todos =
                libroController.listar();

        for (Libro libro : todos) {

            libros.put(
                    libro.getId(),
                    libro
            );
        }

        List<Libro> disponibles =
                libroController.listarDisponibles();

        for (Libro libro : disponibles) {

            cboLibro.addItem(
                    libro
            );
        }
    }

    /**
     * Carga los préstamos desde MySQL.
     */
    private void cargarPrestamos() {

        modeloTabla.setRowCount(0);

        List<Prestamo> lista =
                prestamoController.listar();

        for (Prestamo prestamo : lista) {

            Estudiante estudiante =
                    estudiantes.get(
                            prestamo.getIdEstudiante()
                    );

            Libro libro =
                    libros.get(
                            prestamo.getIdLibro()
                    );

            String nombreEstudiante =
                    estudiante != null
                            ? estudiante.getNombre()
                            : "ID " + prestamo.getIdEstudiante();

            String nombreLibro =
                    libro != null
                            ? libro.getTitulo()
                            : "ID " + prestamo.getIdLibro();

            String estado;

            if (prestamo.isDevuelto()) {

                estado = "DEVUELTO";

            } else if (prestamo.estaAtrasado()) {

                estado = "ATRASADO";

            } else {

                estado = "ACTIVO";
            }

            modeloTabla.addRow(
                    new Object[]{
                            prestamo.getId(),
                            nombreEstudiante,
                            nombreLibro,
                            prestamo.getFechaPrestamo(),
                            prestamo.getFechaDevolucion(),
                            estado
                    }
            );
        }
    }

    private void seleccionarPrestamo() {

        int fila =
                tablaPrestamos.getSelectedRow();

        if (fila < 0) {

            idPrestamoSeleccionado = 0;
            return;
        }

        idPrestamoSeleccionado =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(
                                        fila,
                                        0
                                )
                                .toString()
                );
    }

    /**
     * Registra el préstamo de manera asíncrona.
     */
    private void registrarPrestamo() {

        Estudiante estudiante =
                (Estudiante)
                        cboEstudiante.getSelectedItem();

        Libro libro =
                (Libro)
                        cboLibro.getSelectedItem();

        if (estudiante == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un estudiante.",
                    "Préstamo",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (libro == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No existen libros con stock disponible.",
                    "Préstamo",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Registrar el préstamo?\n\n"
                                + "Estudiante: "
                                + estudiante.getNombre()
                                + "\nLibro: "
                                + libro.getTitulo()
                                + "\nFecha: "
                                + LocalDate.now()
                                + "\nDevolución: "
                                + LocalDate.now().plusDays(7),
                        "Confirmar préstamo",
                        JOptionPane.YES_NO_OPTION
                );

        if (respuesta !=
                JOptionPane.YES_OPTION) {

            return;
        }

        Prestamo prestamo =
                new Prestamo(
                        estudiante.getId(),
                        libro.getId()
                );

        /*
         * Mientras trabaja el hilo secundario,
         * evitamos un doble clic accidental.
         */
        btnPrestar.setEnabled(false);

        prestamoController.registrar(
                prestamo,
                resultado -> {

                    btnPrestar.setEnabled(true);

                    if (resultado) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Préstamo registrado correctamente.",
                                "Biblioteca Escolar",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        actualizarDatos();

                    } else {

                        JOptionPane.showMessageDialog(
                                this,
                                "No fue posible registrar el préstamo.\n"
                                        + "Verifique el stock disponible.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
        );
    }

    /**
     * Registra la devolución del préstamo
     * seleccionado.
     */
    private void devolverPrestamo() {

        if (idPrestamoSeleccionado == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un préstamo de la tabla.",
                    "Devolución",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Prestamo prestamo =
                prestamoController.buscar(
                        idPrestamoSeleccionado
                );

        if (prestamo == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró el préstamo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (prestamo.isDevuelto()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Este préstamo ya se encuentra devuelto.",
                    "Devolución",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Registrar la devolución del préstamo #"
                                + idPrestamoSeleccionado
                                + "?",
                        "Confirmar devolución",
                        JOptionPane.YES_NO_OPTION
                );

        if (respuesta !=
                JOptionPane.YES_OPTION) {

            return;
        }

        btnDevolver.setEnabled(false);

        prestamoController.devolver(
                idPrestamoSeleccionado,
                resultado -> {

                    btnDevolver.setEnabled(true);

                    if (resultado) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Devolución registrada correctamente.",
                                "Biblioteca Escolar",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        idPrestamoSeleccionado = 0;

                        actualizarDatos();

                    } else {

                        JOptionPane.showMessageDialog(
                                this,
                                "No fue posible registrar la devolución.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
        );
    }

    /**
     * Recarga la información después de una operación.
     */
    private void actualizarDatos() {

        cargarEstudiantes();
        cargarLibros();
        cargarPrestamos();

        tablaPrestamos.clearSelection();

        idPrestamoSeleccionado = 0;
    }

    /**
     * Configura las opciones de acuerdo con
     * el rol autenticado.
     */
    private void configurarPermisos() {

        boolean esBibliotecario =
                usuario
                        .getRol()
                        .equalsIgnoreCase(
                                "bibliotecario"
                        );

        /*
         * Por el momento ambos roles pueden acceder
         * al módulo de préstamos y devoluciones,
         * según los requisitos de la actividad.
         *
         * La administración general permanece
         * restringida al bibliotecario.
         */
        btnPrestar.setEnabled(true);
        btnDevolver.setEnabled(true);

        if (!esBibliotecario) {

            setTitle(
                    "Biblioteca Escolar - Mis Préstamos"
            );
        }
        cboEstudiante.setEnabled(
                esBibliotecario
        );
    }

    /**
     * Cierra correctamente el ExecutorService
     * utilizado por el controlador.
     */
    private void cerrarVentana() {

        prestamoController.cerrar();

        dispose();
    }
}