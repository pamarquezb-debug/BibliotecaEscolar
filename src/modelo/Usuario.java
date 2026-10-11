package modelo;

/**
 * Clase abstracta que representa un usuario del sistema.
 *
 * Permite aplicar herencia y polimorfismo según
 * el rol almacenado en la base de datos.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public abstract class Usuario {

    private int id;
    private String nombre;
    private String rut;
    private String correo;
    private String contrasena;

    public Usuario() {
    }

    public Usuario(int id,
                   String nombre,
                   String rut,
                   String correo,
                   String contrasena) {

        this.id = id;
        this.nombre = nombre;
        this.rut = rut;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public abstract String getRol();

    public abstract String getDescripcionRol();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        return nombre + " (" + getRol() + ")";
    }
}