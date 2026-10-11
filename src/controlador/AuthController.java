package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;

/**
 * Controlador encargado de la autenticación
 * de usuarios del sistema Biblioteca Escolar.
 *
 * Forma parte del patrón MVC y actúa como
 * intermediario entre la vista y el DAO.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class AuthController {

    private final UsuarioDAO usuarioDAO;

    public AuthController() {
        usuarioDAO = new UsuarioDAO();
    }

    /**
     * Autentica un usuario mediante correo y contraseña.
     *
     * @param correo correo del usuario
     * @param contrasena contraseña del usuario
     * @return Usuario autenticado o null si las
     *         credenciales son incorrectas
     */
    public Usuario autenticar(
            String correo,
            String contrasena) {

        if (correo == null ||
                correo.trim().isEmpty()) {
            return null;
        }

        if (contrasena == null ||
                contrasena.trim().isEmpty()) {
            return null;
        }

        return usuarioDAO.autenticar(
                correo.trim(),
                contrasena
        );
    }
}