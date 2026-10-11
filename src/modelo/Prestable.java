package modelo;

/**
 * Define el comportamiento de un recurso
 * que puede ser prestado.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public interface Prestable {

    boolean estaDisponible();

    boolean prestar();

    void devolver();
}