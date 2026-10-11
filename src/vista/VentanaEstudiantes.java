package vista;

import controlador.EstudianteController;
import modelo.Estudiante;
import util.RutUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Ventana para la gestión de estudiantes
 * del sistema Biblioteca Escolar.
 *
 * Permite realizar operaciones CRUD sobre
 * los estudiantes registrados en la base de datos.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class VentanaEstudiantes extends JFrame {

    private final EstudianteController estudianteController;

    private JTextField txtNombre;
    private JTextField txtRut;
    private JTextField txtCurso;
    private JTextField txtCorreo;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnCerrar;

    private JTable tablaEstudiantes;
    private DefaultTableModel modeloTabla;

    /*
     * ID del estudiante seleccionado.
     * 0 significa que no existe selección.
     */
    private int idSeleccionado = 0;

    public VentanaEstudiantes() {

        estudianteController =
                new EstudianteController();

        configurarVentana();
        crearComponentes();
        cargarEstudiantes();
    }

    /**
     * Configuración general de la ventana.
     */
    private void configurarVentana() {

        setTitle(
                "Biblioteca Escolar - Gestión de Estudiantes"
        );

        setSize(900, 600);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);
    }

    /**
     * Construye los componentes gráficos.
     */
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

        /*
         * TÍTULO
         */
        JLabel lblTitulo =
                new JLabel(
                        "Gestión de Estudiantes",
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

        /*
         * CENTRO
         */
        JPanel panelCentro =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        panelCentro.add(
                crearPanelFormulario(),
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

        /*
         * BOTÓN CERRAR
         */
        JPanel panelInferior =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        btnCerrar =
                new JButton("Cerrar");

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

    /**
     * Crea el formulario de datos.
     */
    private JPanel crearPanelFormulario() {

        JPanel contenedor =
                new JPanel(
                        new BorderLayout()
                );

        contenedor.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos del estudiante"
                )
        );

        JPanel formulario =
                new JPanel(
                        new GridLayout(
                                2,
                                4,
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

        txtNombre =
                new JTextField();

        txtRut =
                new JTextField();

        txtCurso =
                new JTextField();

        txtCorreo =
                new JTextField();

        formulario.add(
                new JLabel("Nombre:")
        );

        formulario.add(
                txtNombre
        );

        formulario.add(
                new JLabel("RUT:")
        );

        formulario.add(
                txtRut
        );

        formulario.add(
                new JLabel("Curso:")
        );

        formulario.add(
                txtCurso
        );

        formulario.add(
                new JLabel("Correo:")
        );

        formulario.add(
                txtCorreo
        );

        /*
         * BOTONES CRUD
         */
        JPanel panelBotones =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER
                        )
                );

        btnNuevo =
                new JButton("Nuevo");

        btnGuardar =
                new JButton("Guardar");

        btnActualizar =
                new JButton("Actualizar");

        btnEliminar =
                new JButton("Eliminar");

        panelBotones.add(
                btnNuevo
        );

        panelBotones.add(
                btnGuardar
        );

        panelBotones.add(
                btnActualizar
        );

        panelBotones.add(
                btnEliminar
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

    /**
     * Crea la tabla de estudiantes.
     */
    private JScrollPane crearPanelTabla() {

        modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Nombre",
                                "RUT",
                                "Curso",
                                "Correo"
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

        tablaEstudiantes =
                new JTable(modeloTabla);

        tablaEstudiantes.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaEstudiantes.setRowHeight(25);

        tablaEstudiantes
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(40);

        return new JScrollPane(
                tablaEstudiantes
        );
    }

    /**
     * Registra los eventos de la ventana.
     */
    private void registrarEventos() {

        btnNuevo.addActionListener(
                e -> limpiarFormulario()
        );

        btnGuardar.addActionListener(
                e -> guardarEstudiante()
        );

        btnActualizar.addActionListener(
                e -> actualizarEstudiante()
        );

        btnEliminar.addActionListener(
                e -> eliminarEstudiante()
        );

        btnCerrar.addActionListener(
                e -> dispose()
        );

        tablaEstudiantes
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        cargarEstudianteSeleccionado();
                    }
                });
    }

    /**
     * Obtiene todos los estudiantes desde MySQL
     * y los muestra en la tabla.
     */
    private void cargarEstudiantes() {

        modeloTabla.setRowCount(0);

        List<Estudiante> estudiantes =
                estudianteController.listar();

        for (Estudiante estudiante : estudiantes) {

            modeloTabla.addRow(
                    new Object[]{
                            estudiante.getId(),
                            estudiante.getNombre(),
                            estudiante.getRut(),
                            estudiante.getCurso(),
                            estudiante.getCorreo()
                    }
            );
        }
    }

    /**
     * Carga en el formulario el estudiante
     * seleccionado en la tabla.
     */
    private void cargarEstudianteSeleccionado() {

        int fila =
                tablaEstudiantes.getSelectedRow();

        if (fila < 0) {
            return;
        }

        idSeleccionado =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(
                                        fila,
                                        0
                                )
                                .toString()
                );

        Estudiante estudiante =
                estudianteController.buscar(
                        idSeleccionado
                );

        if (estudiante == null) {
            return;
        }

        txtNombre.setText(
                estudiante.getNombre()
        );

        txtRut.setText(
                estudiante.getRut()
        );

        txtCurso.setText(
                estudiante.getCurso()
        );

        txtCorreo.setText(
                estudiante.getCorreo()
        );
    }

    /**
     * Registra un estudiante.
     */
    private void guardarEstudiante() {

        if (!validarFormulario()) {
            return;
        }

        Estudiante estudiante =
                new Estudiante(
                        txtNombre
                                .getText()
                                .trim(),

                        txtRut
                                .getText()
                                .trim(),

                        txtCurso
                                .getText()
                                .trim(),

                        txtCorreo
                                .getText()
                                .trim()
                );

        boolean resultado =
                estudianteController.guardar(
                        estudiante
                );

        if (resultado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Estudiante registrado correctamente.",
                    "Biblioteca Escolar",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarEstudiantes();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible registrar el estudiante.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Actualiza el estudiante seleccionado.
     */
    private void actualizarEstudiante() {

        if (idSeleccionado == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un estudiante de la tabla.",
                    "Actualizar",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!validarFormulario()) {
            return;
        }

        Estudiante estudiante =
                new Estudiante(
                        idSeleccionado,

                        txtNombre
                                .getText()
                                .trim(),

                        txtRut
                                .getText()
                                .trim(),

                        txtCurso
                                .getText()
                                .trim(),

                        txtCorreo
                                .getText()
                                .trim()
                );

        boolean resultado =
                estudianteController.actualizar(
                        estudiante
                );

        if (resultado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Estudiante actualizado correctamente.",
                    "Biblioteca Escolar",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarEstudiantes();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible actualizar el estudiante.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Elimina el estudiante seleccionado.
     */
    private void eliminarEstudiante() {

        if (idSeleccionado == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un estudiante de la tabla.",
                    "Eliminar",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar el estudiante seleccionado?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (respuesta !=
                JOptionPane.YES_OPTION) {

            return;
        }

        boolean resultado =
                estudianteController.eliminar(
                        idSeleccionado
                );

        if (resultado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Estudiante eliminado correctamente.",
                    "Biblioteca Escolar",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarEstudiantes();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible eliminar el estudiante.\n"
                            + "Puede tener préstamos asociados.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Valida los datos antes de guardar
     * o actualizar.
     */
    private boolean validarFormulario() {

        if (txtNombre
                .getText()
                .trim()
                .isEmpty()) {

            mostrarValidacion(
                    "Debe ingresar el nombre.",
                    txtNombre
            );

            return false;
        }

        if (txtRut
                .getText()
                .trim()
                .isEmpty()) {

            mostrarValidacion(
                    "Debe ingresar el RUT.",
                    txtRut
            );

            return false;
        }
        String rut = txtRut.getText().trim();

        if (!RutUtil.validar(rut)) {

            JOptionPane.showMessageDialog(
                    this,
                    "El RUT ingresado no es válido.\n\n"
                            + "Ejemplo: 12345678-5",
                    "RUT inválido",
                    JOptionPane.WARNING_MESSAGE
            );

            txtRut.requestFocus();

            return false;
        }

        rut = RutUtil.formatear(rut);

        if (txtCurso
                .getText()
                .trim()
                .isEmpty()) {

            mostrarValidacion(
                    "Debe ingresar el curso.",
                    txtCurso
            );

            return false;
        }

        if (txtCorreo
                .getText()
                .trim()
                .isEmpty()) {

            mostrarValidacion(
                    "Debe ingresar el correo.",
                    txtCorreo
            );

            return false;
        }

        /*
         * Validación básica de correo.
         */
        String correo =
                txtCorreo
                        .getText()
                        .trim();

        if (!correo.contains("@")) {

            mostrarValidacion(
                    "Ingrese un correo válido.",
                    txtCorreo
            );

            return false;
        }

        return true;
    }

    /**
     * Muestra mensaje de validación y posiciona
     * el cursor en el componente correspondiente.
     */
    private void mostrarValidacion(
            String mensaje,
            JComponent componente) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        componente.requestFocusInWindow();
    }

    /**
     * Limpia el formulario.
     */
    private void limpiarFormulario() {

        idSeleccionado = 0;

        txtNombre.setText("");
        txtRut.setText("");
        txtCurso.setText("");
        txtCorreo.setText("");

        tablaEstudiantes.clearSelection();

        txtNombre.requestFocusInWindow();
    }
}