package controlador;

import dao.LibroDAO;
import modelo.Libro;

import java.util.List;

/**
 * Controlador para la gestión de libros.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class LibroController {

    private final LibroDAO libroDAO;

    public LibroController() {
        libroDAO = new LibroDAO();
    }

    public List<Libro> listar() {
        return libroDAO.readAll();
    }

    public List<Libro> listarDisponibles() {
        return libroDAO.readDisponibles();
    }

    public Libro buscar(int id) {
        return libroDAO.readById(id);
    }

    public boolean guardar(Libro libro) {
        return libroDAO.create(libro);
    }

    public boolean actualizar(Libro libro) {
        return libroDAO.update(libro);
    }

    public boolean eliminar(int id) {
        return libroDAO.delete(id);
    }
}