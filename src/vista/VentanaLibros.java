package vista;

import controlador.CategoriaController;
import controlador.LibroController;
import modelo.Categoria;
import modelo.Libro;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ventana para la gestión y consulta de libros
 * del sistema Biblioteca Escolar.
 *
 * Los bibliotecarios pueden realizar operaciones CRUD.
 * Los estudiantes solamente pueden consultar.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class VentanaLibros extends JFrame {

    private final Usuario usuario;

    private final LibroController libroController;
    private final CategoriaController categoriaController;

    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtIsbn;
    private JTextField txtEditorial;
    private JSpinner spStock;

    private JComboBox<Categoria> cboCategoria;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnCerrar;

    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;

    /*
     * Permite obtener rápidamente el nombre
     * de una categoría mediante su ID.
     */
    private final Map<Integer, String> categoriasPorId;

    /*
     * ID del libro actualmente seleccionado.
     * 0 significa que no hay selección.
     */
    private int idSeleccionado = 0;

    public VentanaLibros(Usuario usuario) {

        this.usuario = usuario;

        libroController = new LibroController();
        categoriaController = new CategoriaController();

        categoriasPorId = new HashMap<>();

        configurarVentana();
        crearComponentes();

        cargarCategorias();
        cargarLibros();

        configurarPermisos();
    }

    private void configurarVentana() {

        setTitle("Biblioteca Escolar - Gestión de Libros");

        setSize(1000, 650);

        /*
         * Al cerrar esta ventana no queremos cerrar
         * toda la aplicación.
         */
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);
    }

    private void crearComponentes() {

        JPanel panelPrincipal =
                new JPanel(new BorderLayout(10, 10));

        panelPrincipal.setBorder(
                new EmptyBorder(15, 15, 15, 15)
        );

        /*
         * TÍTULO
         */
        JLabel lblTitulo =
                new JLabel(
                        "Gestión de Libros",
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
                new JPanel(new BorderLayout(10, 10));

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
         * PARTE INFERIOR
         */
        JPanel panelInferior =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        btnCerrar =
                new JButton("Cerrar");

        panelInferior.add(btnCerrar);

        panelPrincipal.add(
                panelInferior,
                BorderLayout.SOUTH
        );

        add(panelPrincipal);

        registrarEventos();
    }

    private JPanel crearPanelFormulario() {

        JPanel contenedor =
                new JPanel(new BorderLayout());

        contenedor.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos del libro"
                )
        );

        JPanel formulario =
                new JPanel(
                        new GridLayout(
                                3,
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

        txtTitulo =
                new JTextField();

        txtAutor =
                new JTextField();

        txtIsbn =
                new JTextField();

        txtEditorial =
                new JTextField();

        spStock =
                new JSpinner(
                        new SpinnerNumberModel(
                                0,
                                0,
                                100000,
                                1
                        )
                );

        cboCategoria =
                new JComboBox<>();

        formulario.add(
                new JLabel("Título:")
        );

        formulario.add(txtTitulo);

        formulario.add(
                new JLabel("Autor:")
        );

        formulario.add(txtAutor);

        formulario.add(
                new JLabel("ISBN:")
        );

        formulario.add(txtIsbn);

        formulario.add(
                new JLabel("Editorial:")
        );

        formulario.add(txtEditorial);

        formulario.add(
                new JLabel("Stock:")
        );

        formulario.add(spStock);

        formulario.add(
                new JLabel("Categoría:")
        );

        formulario.add(cboCategoria);

        /*
         * Botones CRUD
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

        panelBotones.add(btnNuevo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

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
                                "Título",
                                "Autor",
                                "ISBN",
                                "Editorial",
                                "Stock",
                                "Categoría"
                        },
                        0
                ) {

                    /*
                     * Impide editar directamente
                     * las celdas de la JTable.
                     */
                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        tablaLibros =
                new JTable(modeloTabla);

        tablaLibros.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaLibros.setRowHeight(25);

        /*
         * El ID no necesita demasiado espacio.
         */
        tablaLibros
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(40);

        return new JScrollPane(tablaLibros);
    }

    private void registrarEventos() {

        btnNuevo.addActionListener(
                e -> limpiarFormulario()
        );

        btnGuardar.addActionListener(
                e -> guardarLibro()
        );

        btnActualizar.addActionListener(
                e -> actualizarLibro()
        );

        btnEliminar.addActionListener(
                e -> eliminarLibro()
        );

        btnCerrar.addActionListener(
                e -> dispose()
        );

        /*
         * Cuando seleccionamos una fila,
         * cargamos sus datos en el formulario.
         */
        tablaLibros
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        cargarLibroSeleccionado();
                    }
                });
    }

    /**
     * Carga las categorías almacenadas
     * en la base de datos.
     */
    private void cargarCategorias() {

        cboCategoria.removeAllItems();

        categoriasPorId.clear();

        List<Categoria> categorias =
                categoriaController.listar();

        for (Categoria categoria : categorias) {

            cboCategoria.addItem(categoria);

            categoriasPorId.put(
                    categoria.getId(),
                    categoria.getNombre()
            );
        }
    }

    /**
     * Carga los libros de MySQL en la JTable.
     */
    private void cargarLibros() {

        modeloTabla.setRowCount(0);

        List<Libro> libros =
                libroController.listar();

        for (Libro libro : libros) {

            String nombreCategoria =
                    categoriasPorId.getOrDefault(
                            libro.getIdCategoria(),
                            "Sin categoría"
                    );

            modeloTabla.addRow(
                    new Object[]{
                            libro.getId(),
                            libro.getTitulo(),
                            libro.getAutor(),
                            libro.getIsbn(),
                            libro.getEditorial(),
                            libro.getStock(),
                            nombreCategoria
                    }
            );
        }
    }

    /**
     * Carga en el formulario el libro
     * seleccionado en la tabla.
     */
    private void cargarLibroSeleccionado() {

        int fila =
                tablaLibros.getSelectedRow();

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

        Libro libro =
                libroController.buscar(
                        idSeleccionado
                );

        if (libro == null) {
            return;
        }

        txtTitulo.setText(
                libro.getTitulo()
        );

        txtAutor.setText(
                libro.getAutor()
        );

        txtIsbn.setText(
                libro.getIsbn()
        );

        txtEditorial.setText(
                libro.getEditorial()
        );

        spStock.setValue(
                libro.getStock()
        );

        seleccionarCategoria(
                libro.getIdCategoria()
        );
    }

    private void seleccionarCategoria(
            int idCategoria) {

        for (int i = 0;
             i < cboCategoria.getItemCount();
             i++) {

            Categoria categoria =
                    cboCategoria.getItemAt(i);

            if (categoria.getId()
                    == idCategoria) {

                cboCategoria.setSelectedIndex(i);

                return;
            }
        }
    }

    private void guardarLibro() {

        if (!validarFormulario()) {
            return;
        }

        Categoria categoria =
                (Categoria)
                        cboCategoria.getSelectedItem();

        Libro libro =
                new Libro(
                        txtTitulo.getText().trim(),
                        txtAutor.getText().trim(),
                        txtIsbn.getText().trim(),
                        txtEditorial.getText().trim(),
                        (Integer) spStock.getValue(),
                        categoria.getId()
                );

        boolean resultado =
                libroController.guardar(libro);

        if (resultado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Libro registrado correctamente.",
                    "Biblioteca Escolar",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarLibros();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible registrar el libro.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void actualizarLibro() {

        if (idSeleccionado == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un libro de la tabla.",
                    "Actualizar",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!validarFormulario()) {
            return;
        }

        Categoria categoria =
                (Categoria)
                        cboCategoria.getSelectedItem();

        Libro libro =
                new Libro(
                        idSeleccionado,
                        txtTitulo.getText().trim(),
                        txtAutor.getText().trim(),
                        txtIsbn.getText().trim(),
                        txtEditorial.getText().trim(),
                        (Integer) spStock.getValue(),
                        categoria.getId()
                );

        boolean resultado =
                libroController.actualizar(libro);

        if (resultado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Libro actualizado correctamente.",
                    "Biblioteca Escolar",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarLibros();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible actualizar el libro.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarLibro() {

        if (idSeleccionado == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un libro de la tabla.",
                    "Eliminar",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar el libro seleccionado?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (respuesta !=
                JOptionPane.YES_OPTION) {

            return;
        }

        boolean resultado =
                libroController.eliminar(
                        idSeleccionado
                );

        if (resultado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Libro eliminado correctamente.",
                    "Biblioteca Escolar",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarLibros();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible eliminar el libro.\n"
                            + "Puede estar asociado a un préstamo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private boolean validarFormulario() {

        if (txtTitulo
                .getText()
                .trim()
                .isEmpty()) {

            mostrarValidacion(
                    "Debe ingresar el título.",
                    txtTitulo
            );

            return false;
        }

        if (txtAutor
                .getText()
                .trim()
                .isEmpty()) {

            mostrarValidacion(
                    "Debe ingresar el autor.",
                    txtAutor
            );

            return false;
        }

        if (txtIsbn
                .getText()
                .trim()
                .isEmpty()) {

            mostrarValidacion(
                    "Debe ingresar el ISBN.",
                    txtIsbn
            );

            return false;
        }

        if (cboCategoria.getSelectedItem()
                == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar una categoría.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        return true;
    }

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

    private void limpiarFormulario() {

        idSeleccionado = 0;

        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtEditorial.setText("");

        spStock.setValue(0);

        if (cboCategoria.getItemCount() > 0) {
            cboCategoria.setSelectedIndex(0);
        }

        tablaLibros.clearSelection();

        txtTitulo.requestFocusInWindow();
    }

    /**
     * Configura los permisos según el rol.
     *
     * Bibliotecario: CRUD completo.
     * Estudiante: solamente consulta.
     */
    private void configurarPermisos() {

        boolean esBibliotecario =
                usuario
                        .getRol()
                        .equalsIgnoreCase(
                                "bibliotecario"
                        );

        btnNuevo.setVisible(
                esBibliotecario
        );

        btnGuardar.setVisible(
                esBibliotecario
        );

        btnActualizar.setVisible(
                esBibliotecario
        );

        btnEliminar.setVisible(
                esBibliotecario
        );

        /*
         * En modo estudiante dejamos los campos
         * visibles, pero no editables.
         */
        txtTitulo.setEditable(
                esBibliotecario
        );

        txtAutor.setEditable(
                esBibliotecario
        );

        txtIsbn.setEditable(
                esBibliotecario
        );

        txtEditorial.setEditable(
                esBibliotecario
        );

        spStock.setEnabled(
                esBibliotecario
        );

        cboCategoria.setEnabled(
                esBibliotecario
        );

        if (!esBibliotecario) {

            setTitle(
                    "Biblioteca Escolar - Consulta de Libros"
            );
        }
    }
}