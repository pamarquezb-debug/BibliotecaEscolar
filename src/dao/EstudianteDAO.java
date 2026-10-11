package dao;

import modelo.Estudiante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO encargado del CRUD de estudiantes.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class EstudianteDAO {

    public boolean create(Estudiante estudiante) {

        String sql =
                "INSERT INTO estudiantes " +
                        "(nombre, rut, curso, correo) " +
                        "VALUES (?, ?, ?, ?)";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getRut());
            ps.setString(3, estudiante.getCurso());
            ps.setString(4, estudiante.getCorreo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Error al crear estudiante: " +
                            e.getMessage()
            );
            return false;
        }
    }

    public List<Estudiante> readAll() {

        List<Estudiante> estudiantes =
                new ArrayList<>();

        String sql =
                "SELECT id, nombre, rut, curso, correo " +
                        "FROM estudiantes ORDER BY nombre";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                estudiantes.add(
                        new Estudiante(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("rut"),
                                rs.getString("curso"),
                                rs.getString("correo")
                        )
                );
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al consultar estudiantes: " +
                            e.getMessage()
            );
        }

        return estudiantes;
    }

    public Estudiante readById(int id) {

        String sql =
                "SELECT id, nombre, rut, curso, correo " +
                        "FROM estudiantes WHERE id = ?";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Estudiante(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("rut"),
                            rs.getString("curso"),
                            rs.getString("correo")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al buscar estudiante: " +
                            e.getMessage()
            );
        }

        return null;
    }

    public boolean update(Estudiante estudiante) {

        String sql =
                "UPDATE estudiantes SET " +
                        "nombre = ?, rut = ?, curso = ?, correo = ? " +
                        "WHERE id = ?";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getRut());
            ps.setString(3, estudiante.getCurso());
            ps.setString(4, estudiante.getCorreo());
            ps.setInt(5, estudiante.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Error al actualizar estudiante: " +
                            e.getMessage()
            );
            return false;
        }
    }

    public boolean delete(int id) {

        String sql =
                "DELETE FROM estudiantes WHERE id = ?";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Error al eliminar estudiante: " +
                            e.getMessage()
            );
            return false;
        }
    }
    public Estudiante readByRut(String rut) {

        String sql =
                "SELECT id, nombre, rut, curso, correo " +
                        "FROM estudiantes WHERE rut = ?";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setString(1, rut);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Estudiante(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("rut"),
                            rs.getString("curso"),
                            rs.getString("correo")
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar estudiante por RUT: "
                            + e.getMessage()
            );
        }

        return null;
    }
}