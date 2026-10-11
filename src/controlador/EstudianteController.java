package controlador;

import dao.EstudianteDAO;
import modelo.Estudiante;

import java.util.List;

/**
 * Controlador para la gestión de estudiantes.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class EstudianteController {

    private final EstudianteDAO estudianteDAO;

    public EstudianteController() {
        estudianteDAO = new EstudianteDAO();
    }

    public List<Estudiante> listar() {
        return estudianteDAO.readAll();
    }

    public Estudiante buscar(int id) {
        return estudianteDAO.readById(id);
    }

    public boolean guardar(Estudiante estudiante) {
        return estudianteDAO.create(estudiante);
    }

    public boolean actualizar(Estudiante estudiante) {
        return estudianteDAO.update(estudiante);
    }

    public boolean eliminar(int id) {
        return estudianteDAO.delete(id);
    }
    public Estudiante buscarPorRut(String rut) {
        return estudianteDAO.readByRut(rut);
    }
}