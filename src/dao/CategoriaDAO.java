package dao;

import modelo.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar categorías.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class CategoriaDAO {

    public boolean create(Categoria categoria) {

        String sql =
                "INSERT INTO categorias (nombre) VALUES (?)";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Error al crear categoría: " +
                            e.getMessage()
            );
            return false;
        }
    }

    public List<Categoria> readAll() {

        List<Categoria> categorias =
                new ArrayList<>();

        String sql =
                "SELECT id, nombre " +
                        "FROM categorias ORDER BY nombre";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                categorias.add(
                        new Categoria(
                                rs.getInt("id"),
                                rs.getString("nombre")
                        )
                );
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al consultar categorías: " +
                            e.getMessage()
            );
        }

        return categorias;
    }

    public Categoria readById(int id) {

        String sql =
                "SELECT id, nombre " +
                        "FROM categorias WHERE id = ?";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new Categoria(
                            rs.getInt("id"),
                            rs.getString("nombre")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al buscar categoría: " +
                            e.getMessage()
            );
        }

        return null;
    }

    public boolean update(Categoria categoria) {

        String sql =
                "UPDATE categorias SET nombre = ? " +
                        "WHERE id = ?";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Error al actualizar categoría: " +
                            e.getMessage()
            );
            return false;
        }
    }

    public boolean delete(int id) {

        String sql =
                "DELETE FROM categorias WHERE id = ?";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Error al eliminar categoría: " +
                            e.getMessage()
            );
            return false;
        }
    }
}