package dao;

import modelo.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO encargado del CRUD de libros.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class LibroDAO {

    public boolean create(Libro libro) {

        String sql =
                "INSERT INTO libros " +
                        "(titulo, autor, isbn, editorial, stock, id_categoria) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setString(4, libro.getEditorial());
            ps.setInt(5, libro.getStock());
            ps.setInt(6, libro.getIdCategoria());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Error al crear libro: " +
                            e.getMessage()
            );
            return false;
        }
    }

    public List<Libro> readAll() {

        List<Libro> libros = new ArrayList<>();

        String sql =
                "SELECT id, titulo, autor, isbn, " +
                        "editorial, stock, id_categoria " +
                        "FROM libros ORDER BY titulo";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                libros.add(crearLibro(rs));
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al consultar libros: " +
                            e.getMessage()
            );
        }

        return libros;
    }

    public Libro readById(int id) {

        String sql =
                "SELECT id, titulo, autor, isbn, " +
                        "editorial, stock, id_categoria " +
                        "FROM libros WHERE id = ?";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return crearLibro(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al buscar libro: " +
                            e.getMessage()
            );
        }

        return null;
    }

    /**
     * Retorna solamente libros con stock disponible.
     */
    public List<Libro> readDisponibles() {

        List<Libro> libros = new ArrayList<>();

        String sql =
                "SELECT id, titulo, autor, isbn, " +
                        "editorial, stock, id_categoria " +
                        "FROM libros WHERE stock > 0 " +
                        "ORDER BY titulo";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                libros.add(crearLibro(rs));
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al consultar libros disponibles: " +
                            e.getMessage()
            );
        }

        return libros;
    }

    public boolean update(Libro libro) {

        String sql =
                "UPDATE libros SET " +
                        "titulo = ?, autor = ?, isbn = ?, " +
                        "editorial = ?, stock = ?, id_categoria = ? " +
                        "WHERE id = ?";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setString(4, libro.getEditorial());
            ps.setInt(5, libro.getStock());
            ps.setInt(6, libro.getIdCategoria());
            ps.setInt(7, libro.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Error al actualizar libro: " +
                            e.getMessage()
            );
            return false;
        }
    }

    public boolean delete(int id) {

        String sql =
                "DELETE FROM libros WHERE id = ?";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Error al eliminar libro: " +
                            e.getMessage()
            );
            return false;
        }
    }

    private Libro crearLibro(ResultSet rs)
            throws SQLException {

        return new Libro(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("isbn"),
                rs.getString("editorial"),
                rs.getInt("stock"),
                rs.getInt("id_categoria")
        );
    }
}