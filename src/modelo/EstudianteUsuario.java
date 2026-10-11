package modelo;

/**
 * Usuario autenticado con rol estudiante.
 *
 * Tiene acceso limitado a consulta, préstamo
 * y devolución.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class EstudianteUsuario extends Usuario {

    public EstudianteUsuario() {
        super();
    }

    public EstudianteUsuario(int id,
                             String nombre,
                             String rut,
                             String correo,
                             String contrasena) {

        super(id, nombre, rut, correo, contrasena);
    }

    @Override
    public String getRol() {
        return "ESTUDIANTE";
    }

    @Override
    public String getDescripcionRol() {
        return "Consulta, préstamo y devolución";
    }
}