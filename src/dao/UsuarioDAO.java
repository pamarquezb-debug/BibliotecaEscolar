package dao;

import modelo.Bibliotecario;
import modelo.EstudianteUsuario;
import modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO encargado de la persistencia de usuarios.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class UsuarioDAO {

    public boolean create(Usuario usuario) {

        String sql =
                "INSERT INTO usuarios " +
                        "(nombre, rut, correo, contraseña, rol) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getRut());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getContrasena());
            ps.setString(5, usuario.getRol());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Error al crear usuario: " +
                            e.getMessage()
            );
            return false;
        }
    }

    public List<Usuario> readAll() {

        List<Usuario> usuarios = new ArrayList<>();

        String sql =
                "SELECT id, nombre, rut, correo, " +
                        "contraseña, rol " +
                        "FROM usuarios ORDER BY id";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Usuario usuario = crearUsuario(rs);

                if (usuario != null) {
                    usuarios.add(usuario);
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al consultar usuarios: " +
                            e.getMessage()
            );
        }

        return usuarios;
    }

    public Usuario readById(int id) {

        String sql =
                "SELECT id, nombre, rut, correo, " +
                        "contraseña, rol " +
                        "FROM usuarios WHERE id = ?";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return crearUsuario(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al buscar usuario: " +
                            e.getMessage()
            );
        }

        return null;
    }

    /**
     * Autentica mediante correo y contraseña.
     */
    public Usuario autenticar(
            String correo,
            String contrasena) {

        String sql =
                "SELECT id, nombre, rut, correo, " +
                        "contraseña, rol " +
                        "FROM usuarios " +
                        "WHERE correo = ? AND contraseña = ?";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return crearUsuario(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error al autenticar usuario: " +
                            e.getMessage()
            );
        }

        return null;
    }

    public boolean update(Usuario usuario) {

        String sql =
                "UPDATE usuarios SET " +
                        "nombre = ?, rut = ?, correo = ?, " +
                        "contraseña = ?, rol = ? " +
                        "WHERE id = ?";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getRut());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getContrasena());
            ps.setString(5, usuario.getRol());
            ps.setInt(6, usuario.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Error al actualizar usuario: " +
                            e.getMessage()
            );
            return false;
        }
    }

    public boolean delete(int id) {

        String sql =
                "DELETE FROM usuarios WHERE id = ?";

        try (Connection conexion =
                     DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(
                    "Error al eliminar usuario: " +
                            e.getMessage()
            );
            return false;
        }
    }

    /**
     * Crea la subclase correspondiente según el rol.
     */
    private Usuario crearUsuario(ResultSet rs)
            throws SQLException {

        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        String rut = rs.getString("rut");
        String correo = rs.getString("correo");
        String contrasena =
                rs.getString("contraseña");
        String rol = rs.getString("rol");

        if (rol == null) {
            return null;
        }

        if (rol.equalsIgnoreCase("BIBLIOTECARIO")) {

            return new Bibliotecario(
                    id,
                    nombre,
                    rut,
                    correo,
                    contrasena
            );
        }

        if (rol.equalsIgnoreCase("ESTUDIANTE")) {

            return new EstudianteUsuario(
                    id,
                    nombre,
                    rut,
                    correo,
                    contrasena
            );
        }

        System.err.println(
                "Rol desconocido: " + rol
        );

        return null;
    }
}