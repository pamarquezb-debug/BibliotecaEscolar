package vista;

import modelo.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Ventana principal del sistema Biblioteca Escolar.
 *
 * Las opciones disponibles se configuran según
 * el rol del usuario autenticado.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class VentanaPrincipal extends JFrame {

    private final Usuario usuario;

    private JButton btnLibros;
    private JButton btnEstudiantes;
    private JButton btnCategorias;
    private JButton btnPrestamos;
    private JButton btnReportes;
    private JButton btnCerrarSesion;

    /**
     * Constructor de la ventana principal.
     *
     * @param usuario usuario autenticado en el sistema
     */
    public VentanaPrincipal(Usuario usuario) {

        this.usuario = usuario;

        configurarVentana();
        crearComponentes();
        configurarPermisos();
    }

    /**
     * Configura las propiedades generales
     * de la ventana.
     */
    private void configurarVentana() {

        setTitle(
                "Biblioteca Escolar - "
                        + usuario.getNombre()
        );

        setSize(650, 500);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setResizable(false);
    }

    /**
     * Crea los componentes visuales
     * de la ventana principal.
     */
    private void crearComponentes() {

        JPanel panelPrincipal =
                new JPanel(
                        new BorderLayout()
                );

        panelPrincipal.setBorder(
                new EmptyBorder(
                        20,
                        30,
                        20,
                        30
                )
        );

        /*
         * Encabezado
         */
        JPanel panelEncabezado =
                new JPanel(
                        new GridLayout(
                                3,
                                1
                        )
                );

        JLabel lblTitulo =
                new JLabel(
                        "Biblioteca Escolar",
                        SwingConstants.CENTER
                );

        lblTitulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        JLabel lblUsuario =
                new JLabel(
                        "Usuario: "
                                + usuario.getNombre(),
                        SwingConstants.CENTER
                );

        JLabel lblRol =
                new JLabel(
                        "Rol: "
                                + usuario.getRol(),
                        SwingConstants.CENTER
                );

        panelEncabezado.add(lblTitulo);
        panelEncabezado.add(lblUsuario);
        panelEncabezado.add(lblRol);

        panelPrincipal.add(
                panelEncabezado,
                BorderLayout.NORTH
        );

        /*
         * Menú principal
         *
         * Se utilizan tres filas y dos columnas
         * para permitir cinco módulos principales.
         */
        JPanel panelMenu =
                new JPanel(
                        new GridLayout(
                                3,
                                2,
                                15,
                                15
                        )
                );

        panelMenu.setBorder(
                new EmptyBorder(
                        35,
                        30,
                        35,
                        30
                )
        );

        /*
         * Botón gestión de libros
         */
        btnLibros =
                new JButton(
                        "Gestión de Libros"
                );

        /*
         * Botón gestión de estudiantes
         */
        btnEstudiantes =
                new JButton(
                        "Gestión de Estudiantes"
                );

        /*
         * Botón gestión de categorías
         */
        btnCategorias =
                new JButton(
                        "Gestión de Categorías"
                );

        /*
         * Botón préstamos y devoluciones
         */
        btnPrestamos =
                new JButton(
                        "Préstamos / Devoluciones"
                );

        /*
         * Botón reportes
         */
        btnReportes =
                new JButton(
                        "Reportes"
                );

        /*
         * Agregamos los botones al menú.
         */
        panelMenu.add(btnLibros);
        panelMenu.add(btnEstudiantes);

        panelMenu.add(btnCategorias);
        panelMenu.add(btnPrestamos);

        panelMenu.add(btnReportes);

        panelPrincipal.add(
                panelMenu,
                BorderLayout.CENTER
        );

        /*
         * Panel inferior
         */
        btnCerrarSesion =
                new JButton(
                        "Cerrar sesión"
                );

        JPanel panelInferior =
                new JPanel();

        panelInferior.add(
                btnCerrarSesion
        );

        panelPrincipal.add(
                panelInferior,
                BorderLayout.SOUTH
        );

        add(panelPrincipal);

        /*
         * Eventos
         */
        btnLibros.addActionListener(
                e -> abrirLibros()
        );

        btnEstudiantes.addActionListener(
                e -> abrirEstudiantes()
        );

        btnCategorias.addActionListener(
                e -> abrirCategorias()
        );

        btnPrestamos.addActionListener(
                e -> abrirPrestamos()
        );

        btnReportes.addActionListener(
                e -> abrirReportes()
        );

        btnCerrarSesion.addActionListener(
                e -> cerrarSesion()
        );
    }

    /**
     * Configura las opciones disponibles
     * según el rol del usuario.
     */
    private void configurarPermisos() {

        boolean esBibliotecario =
                usuario
                        .getRol()
                        .equalsIgnoreCase(
                                "BIBLIOTECARIO"
                        );

        /*
         * Gestión de estudiantes:
         * solamente bibliotecario.
         */
        btnEstudiantes.setEnabled(
                esBibliotecario
        );

        /*
         * Gestión de categorías:
         * solamente bibliotecario.
         */
        btnCategorias.setEnabled(
                esBibliotecario
        );

        /*
         * Libros queda habilitado para ambos roles.
         *
         * La propia VentanaLibros determina si
         * permite solamente consulta o CRUD.
         */
        btnLibros.setEnabled(true);

        /*
         * Los estudiantes también pueden utilizar
         * préstamos y devoluciones.
         */
        btnPrestamos.setEnabled(true);

        /*
         * Reportes administrativos:
         * solamente bibliotecario.
         */
        btnReportes.setEnabled(
                esBibliotecario
        );
    }

    /**
     * Abre la ventana de gestión o consulta
     * de libros según el usuario autenticado.
     */
    private void abrirLibros() {

        VentanaLibros ventana =
                new VentanaLibros(
                        usuario
                );

        ventana.setVisible(true);
    }

    /**
     * Abre la gestión de estudiantes.
     * Esta opción está disponible solamente
     * para bibliotecarios.
     */
    private void abrirEstudiantes() {

        if (!usuario
                .getRol()
                .equalsIgnoreCase(
                        "bibliotecario"
                )) {

            JOptionPane.showMessageDialog(
                    this,
                    "No tiene permisos para gestionar estudiantes.",
                    "Acceso restringido",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        VentanaEstudiantes ventana =
                new VentanaEstudiantes();

        ventana.setVisible(true);
    }

    /**
     * Abre la gestión de categorías.
     * Esta opción está disponible solamente
     * para bibliotecarios.
     */
    private void abrirCategorias() {

        if (!usuario
                .getRol()
                .equalsIgnoreCase(
                        "bibliotecario"
                )) {

            JOptionPane.showMessageDialog(
                    this,
                    "No tiene permisos para gestionar categorías.",
                    "Acceso restringido",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        VentanaCategorias ventana =
                new VentanaCategorias();

        ventana.setVisible(true);
    }

    /**
     * Abre el módulo de préstamos
     * y devoluciones.
     */
    private void abrirPrestamos() {

        VentanaPrestamos ventana =
                new VentanaPrestamos(
                        usuario
                );

        ventana.setVisible(true);
    }

    /**
     * Abre el módulo de reportes.
     * Disponible solamente para bibliotecarios.
     */
    private void abrirReportes() {

        if (!usuario
                .getRol()
                .equalsIgnoreCase(
                        "bibliotecario"
                )) {

            JOptionPane.showMessageDialog(
                    this,
                    "No tiene permisos para acceder a los reportes.",
                    "Acceso restringido",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        VentanaReportes ventana =
                new VentanaReportes();

        ventana.setVisible(true);
    }

    /**
     * Cierra la sesión actual y vuelve
     * a mostrar la ventana de autenticación.
     */
    private void cerrarSesion() {

        int opcion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea cerrar la sesión?",
                        "Cerrar sesión",
                        JOptionPane.YES_NO_OPTION
                );

        if (opcion ==
                JOptionPane.YES_OPTION) {

            dispose();

            new VentanaLogin()
                    .setVisible(true);
        }
    }
}