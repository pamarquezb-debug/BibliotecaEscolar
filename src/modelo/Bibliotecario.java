package modelo;

/**
 * Usuario con rol de bibliotecario.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class Bibliotecario extends Usuario {

    public Bibliotecario() {
        super();
    }

    public Bibliotecario(int id,
                         String nombre,
                         String rut,
                         String correo,
                         String contrasena) {

        super(id, nombre, rut, correo, contrasena);
    }

    @Override
    public String getRol() {
        return "BIBLIOTECARIO";
    }

    @Override
    public String getDescripcionRol() {
        return "Gestión completa de biblioteca";
    }
}