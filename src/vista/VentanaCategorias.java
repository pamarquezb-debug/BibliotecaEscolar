package vista;

import controlador.CategoriaController;
import modelo.Categoria;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Ventana para la gestión CRUD de categorías
 * de la Biblioteca Escolar.
 *
 * Permite crear, consultar, actualizar y eliminar
 * categorías almacenadas en la base de datos MySQL.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class VentanaCategorias extends JFrame {

    private final CategoriaController categoriaController;

    private JTextField txtNombre;

    private JTable tablaCategorias;
    private DefaultTableModel modeloTabla;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnCerrar;

    private int idCategoriaSeleccionada = 0;

    public VentanaCategorias() {

        categoriaController =
                new CategoriaController();

        configurarVentana();
        crearComponentes();
        cargarCategorias();
    }

    private void configurarVentana() {

        setTitle(
                "Biblioteca Escolar - Gestión de Categorías"
        );

        setSize(650, 500);

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
                        "Gestión de Categorías",
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

        JPanel panelCentro =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        panelCentro.add(
                crearFormulario(),
                BorderLayout.NORTH
        );

        panelCentro.add(
                crearTabla(),
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                panelCentro,
                BorderLayout.CENTER
        );

        panelPrincipal.add(
                crearPanelBotones(),
                BorderLayout.SOUTH
        );

        add(panelPrincipal);

        registrarEventos();
    }

    private JPanel crearFormulario() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                10,
                                10
                        )
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "Datos de la categoría"
                )
        );

        txtNombre =
                new JTextField();

        panel.add(
                new JLabel(
                        "Nombre:"
                )
        );

        panel.add(
                txtNombre
        );

        return panel;
    }

    private JScrollPane crearTabla() {

        modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Categoría"
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

        tablaCategorias =
                new JTable(modeloTabla);

        tablaCategorias.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaCategorias.setRowHeight(25);

        return new JScrollPane(
                tablaCategorias
        );
    }

    private JPanel crearPanelBotones() {

        JPanel panel =
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

        btnCerrar =
                new JButton("Cerrar");

        panel.add(btnNuevo);
        panel.add(btnGuardar);
        panel.add(btnActualizar);
        panel.add(btnEliminar);
        panel.add(btnCerrar);

        return panel;
    }

    private void registrarEventos() {

        btnNuevo.addActionListener(
                e -> limpiarFormulario()
        );

        btnGuardar.addActionListener(
                e -> guardarCategoria()
        );

        btnActualizar.addActionListener(
                e -> actualizarCategoria()
        );

        btnEliminar.addActionListener(
                e -> eliminarCategoria()
        );

        btnCerrar.addActionListener(
                e -> dispose()
        );

        tablaCategorias
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        seleccionarCategoria();
                    }
                });
    }

    /**
     * Obtiene todas las categorías desde MySQL
     * y actualiza la JTable.
     */
    private void cargarCategorias() {

        modeloTabla.setRowCount(0);

        List<Categoria> lista =
                categoriaController.listar();

        for (Categoria categoria : lista) {

            modeloTabla.addRow(
                    new Object[]{
                            categoria.getId(),
                            categoria.getNombre()
                    }
            );
        }
    }

    /**
     * Carga en el formulario la categoría
     * seleccionada en la tabla.
     */
    private void seleccionarCategoria() {

        int fila =
                tablaCategorias.getSelectedRow();

        if (fila < 0) {

            idCategoriaSeleccionada = 0;
            return;
        }

        idCategoriaSeleccionada =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(
                                        fila,
                                        0
                                )
                                .toString()
                );

        txtNombre.setText(
                modeloTabla
                        .getValueAt(
                                fila,
                                1
                        )
                        .toString()
        );
    }

    /**
     * Crea una nueva categoría.
     */
    private void guardarCategoria() {

        String nombre =
                txtNombre
                        .getText()
                        .trim();

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar el nombre de la categoría.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            txtNombre.requestFocus();

            return;
        }

        Categoria categoria =
                new Categoria(
                        0,
                        nombre
                );

        boolean resultado =
                categoriaController.guardar(
                        categoria
                );

        if (resultado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Categoría registrada correctamente.",
                    "Biblioteca Escolar",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarCategorias();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible registrar la categoría.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Actualiza la categoría seleccionada.
     */
    private void actualizarCategoria() {

        if (idCategoriaSeleccionada == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una categoría de la tabla.",
                    "Actualizar",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String nombre =
                txtNombre
                        .getText()
                        .trim();

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar el nombre de la categoría.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Categoria categoria =
                new Categoria(
                        idCategoriaSeleccionada,
                        nombre
                );

        boolean resultado =
                categoriaController.actualizar(
                        categoria
                );

        if (resultado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Categoría actualizada correctamente.",
                    "Biblioteca Escolar",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarCategorias();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible actualizar la categoría.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Elimina la categoría seleccionada.
     */
    private void eliminarCategoria() {

        if (idCategoriaSeleccionada == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una categoría de la tabla.",
                    "Eliminar",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar la categoría seleccionada?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (respuesta !=
                JOptionPane.YES_OPTION) {

            return;
        }

        boolean resultado =
                categoriaController.eliminar(
                        idCategoriaSeleccionada
                );

        if (resultado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Categoría eliminada correctamente.",
                    "Biblioteca Escolar",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cargarCategorias();
            limpiarFormulario();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible eliminar la categoría.\n\n"
                            + "Compruebe que no existan libros "
                            + "asociados a ella.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Limpia el formulario y la selección.
     */
    private void limpiarFormulario() {

        idCategoriaSeleccionada = 0;

        txtNombre.setText("");

        tablaCategorias.clearSelection();

        txtNombre.requestFocus();
    }
}