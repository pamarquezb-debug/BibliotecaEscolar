package vista;

import controlador.AuthController;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Ventana de autenticación del sistema Biblioteca Escolar.
 *
 * Permite el ingreso de usuarios registrados en la
 * base de datos y deriva a la ventana principal según
 * el rol obtenido.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class VentanaLogin extends JFrame {

    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;

    private final AuthController authController;

    public VentanaLogin() {

        authController = new AuthController();

        configurarVentana();
        crearComponentes();
    }

    private void configurarVentana() {

        setTitle("Biblioteca Escolar - Inicio de sesión");

        setSize(450, 330);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setResizable(false);
    }

    private void crearComponentes() {

        JPanel panelPrincipal =
                new JPanel(new BorderLayout());

        panelPrincipal.setBorder(
                new EmptyBorder(
                        25,
                        40,
                        25,
                        40
                )
        );

        /*
         * Título
         */
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

        JLabel lblSubtitulo =
                new JLabel(
                        "Sistema de Gestión Bibliotecaria",
                        SwingConstants.CENTER
                );

        JPanel panelTitulo =
                new JPanel(
                        new GridLayout(
                                2,
                                1,
                                5,
                                5
                        )
                );

        panelTitulo.add(lblTitulo);
        panelTitulo.add(lblSubtitulo);

        panelPrincipal.add(
                panelTitulo,
                BorderLayout.NORTH
        );

        /*
         * Formulario
         */
        JPanel panelFormulario =
                new JPanel(
                        new GridLayout(
                                4,
                                1,
                                5,
                                5
                        )
                );

        panelFormulario.setBorder(
                new EmptyBorder(
                        25,
                        0,
                        20,
                        0
                )
        );

        JLabel lblCorreo =
                new JLabel("Correo:");

        txtCorreo =
                new JTextField();

        JLabel lblContrasena =
                new JLabel("Contraseña:");

        txtContrasena =
                new JPasswordField();

        panelFormulario.add(lblCorreo);
        panelFormulario.add(txtCorreo);

        panelFormulario.add(lblContrasena);
        panelFormulario.add(txtContrasena);

        panelPrincipal.add(
                panelFormulario,
                BorderLayout.CENTER
        );

        /*
         * Botón
         */
        btnIngresar =
                new JButton("Ingresar");

        btnIngresar.setPreferredSize(
                new Dimension(
                        120,
                        35
                )
        );

        JPanel panelBoton =
                new JPanel();

        panelBoton.add(btnIngresar);

        panelPrincipal.add(
                panelBoton,
                BorderLayout.SOUTH
        );

        add(panelPrincipal);

        /*
         * Eventos
         */
        btnIngresar.addActionListener(
                e -> iniciarSesion()
        );

        txtContrasena.addActionListener(
                e -> iniciarSesion()
        );

        /*
         * Al abrir la ventana,
         * el cursor queda en correo.
         */
        SwingUtilities.invokeLater(
                () -> txtCorreo.requestFocusInWindow()
        );
    }

    /**
     * Intenta autenticar al usuario mediante
     * AuthController.
     */
    private void iniciarSesion() {

        String correo =
                txtCorreo
                        .getText()
                        .trim();

        String contrasena =
                new String(
                        txtContrasena.getPassword()
                );

        if (correo.isEmpty() ||
                contrasena.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar correo y contraseña.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        btnIngresar.setEnabled(false);

        Usuario usuario =
                authController.autenticar(
                        correo,
                        contrasena
                );

        btnIngresar.setEnabled(true);

        if (usuario == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Correo o contraseña incorrectos.",
                    "Acceso denegado",
                    JOptionPane.ERROR_MESSAGE
            );

            txtContrasena.setText("");

            txtContrasena.requestFocus();

            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Bienvenido "
                        + usuario.getNombre()
                        + "\nRol: "
                        + usuario.getRol(),
                "Acceso correcto",
                JOptionPane.INFORMATION_MESSAGE
        );

        /*
         * Abrir ventana principal.
         */
        VentanaPrincipal ventana =
                new VentanaPrincipal(usuario);

        ventana.setVisible(true);

        dispose();
    }
}