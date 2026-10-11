package controlador;

import dao.CategoriaDAO;
import modelo.Categoria;

import java.util.List;

/**
 * Controlador para la gestión de categorías.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class CategoriaController {

    private final CategoriaDAO categoriaDAO;

    public CategoriaController() {
        categoriaDAO = new CategoriaDAO();
    }

    public List<Categoria> listar() {
        return categoriaDAO.readAll();
    }

    public Categoria buscar(int id) {
        return categoriaDAO.readById(id);
    }

    public boolean guardar(Categoria categoria) {
        return categoriaDAO.create(categoria);
    }

    public boolean actualizar(Categoria categoria) {
        return categoriaDAO.update(categoria);
    }

    public boolean eliminar(int id) {
        return categoriaDAO.delete(id);
    }
}