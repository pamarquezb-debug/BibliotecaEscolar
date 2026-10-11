package dao;

import modelo.Prestamo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO encargado de administrar los préstamos y devoluciones.
 *
 * Las operaciones que modifican el stock se realizan
 * mediante transacciones JDBC y sincronización para
 * mantener la consistencia de los datos.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class PrestamoDAO {

    /**
     * Registra un préstamo y descuenta una unidad del stock.
     *
     * La operación es synchronized para evitar que dos hilos
     * de esta aplicación modifiquen simultáneamente el stock.
     *
     * Además, SELECT ... FOR UPDATE bloquea el registro del
     * libro durante la transacción.
     *
     * @param prestamo préstamo a registrar
     * @return true si se registró correctamente
     */
    public synchronized boolean create(Prestamo prestamo) {

        String sqlStock =
                "SELECT stock FROM libros " +
                        "WHERE id = ? FOR UPDATE";

        String sqlPrestamo =
                "INSERT INTO prestamos " +
                        "(id_estudiante, id_libro, fecha_prestamo, " +
                        "fecha_devolucion, devuelto) " +
                        "VALUES (?, ?, ?, ?, ?)";

        String sqlActualizarStock =
                "UPDATE libros " +
                        "SET stock = stock - 1 " +
                        "WHERE id = ?";

        Connection conexion = null;

        try {

            conexion = DatabaseConnection
                    .getInstance()
                    .getConnection();

            conexion.setAutoCommit(false);

            // 1. Bloquear libro y verificar stock.
            try (PreparedStatement ps =
                         conexion.prepareStatement(sqlStock)) {

                ps.setInt(1, prestamo.getIdLibro());

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        conexion.rollback();
                        System.err.println(
                                "El libro no existe."
                        );
                        return false;
                    }

                    int stock = rs.getInt("stock");

                    if (stock <= 0) {
                        conexion.rollback();
                        System.err.println(
                                "El libro no tiene stock disponible."
                        );
                        return false;
                    }
                }
            }

            // 2. Registrar préstamo.
            try (PreparedStatement ps =
                         conexion.prepareStatement(sqlPrestamo)) {

                ps.setInt(
                        1,
                        prestamo.getIdEstudiante()
                );

                ps.setInt(
                        2,
                        prestamo.getIdLibro()
                );

                ps.setDate(
                        3,
                        Date.valueOf(
                                prestamo.getFechaPrestamo()
                        )
                );

                if (prestamo.getFechaDevolucion() == null) {

                    ps.setNull(
                            4,
                            Types.DATE
                    );

                } else {

                    ps.setDate(
                            4,
                            Date.valueOf(
                                    prestamo.getFechaDevolucion()
                            )
                    );
                }

                ps.setBoolean(
                        5,
                        prestamo.isDevuelto()
                );

                ps.executeUpdate();
            }

            // 3. Descontar stock.
            try (PreparedStatement ps =
                         conexion.prepareStatement(
                                 sqlActualizarStock
                         )) {

                ps.setInt(
                        1,
                        prestamo.getIdLibro()
                );

                ps.executeUpdate();
            }

            conexion.commit();

            return true;

        } catch (SQLException e) {

            rollback(conexion);

            System.err.println(
                    "Error al registrar préstamo: "
                            + e.getMessage()
            );

            return false;

        } finally {

            cerrarConexion(conexion);
        }
    }

    /**
     * Consulta todos los préstamos.
     */
    public List<Prestamo> readAll() {

        List<Prestamo> prestamos =
                new ArrayList<>();

        String sql =
                "SELECT id, id_estudiante, id_libro, " +
                        "fecha_prestamo, fecha_devolucion, devuelto " +
                        "FROM prestamos ORDER BY id";

        try (Connection conexion =
                     DatabaseConnection
                             .getInstance()
                             .getConnection();

             PreparedStatement ps =
                     conexion.prepareStatement(sql);

             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                prestamos.add(
                        crearPrestamo(rs)
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al consultar préstamos: "
                            + e.getMessage()
            );
        }

        return prestamos;
    }

    /**
     * Busca un préstamo por ID.
     */
    public Prestamo readById(int id) {

        String sql =
                "SELECT id, id_estudiante, id_libro, " +
                        "fecha_prestamo, fecha_devolucion, devuelto " +
                        "FROM prestamos WHERE id = ?";

        try (Connection conexion =
                     DatabaseConnection
                             .getInstance()
                             .getConnection();

             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return crearPrestamo(rs);
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar préstamo: "
                            + e.getMessage()
            );
        }

        return null;
    }

    /**
     * Modifica los datos generales de un préstamo.
     *
     * No modifica directamente el stock. Los cambios
     * de devolución deben realizarse mediante devolver().
     */
    public boolean update(Prestamo prestamo) {

        String sql =
                "UPDATE prestamos SET " +
                        "id_estudiante = ?, " +
                        "id_libro = ?, " +
                        "fecha_prestamo = ? " +
                        "WHERE id = ?";

        try (Connection conexion =
                     DatabaseConnection
                             .getInstance()
                             .getConnection();

             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    prestamo.getIdEstudiante()
            );

            ps.setInt(
                    2,
                    prestamo.getIdLibro()
            );

            ps.setDate(
                    3,
                    Date.valueOf(
                            prestamo.getFechaPrestamo()
                    )
            );

            ps.setInt(
                    4,
                    prestamo.getId()
            );

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al actualizar préstamo: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Registra la devolución de un libro y aumenta su stock.
     *
     * @param idPrestamo ID del préstamo
     * @return true si la devolución fue realizada
     */
    public synchronized boolean devolver(
            int idPrestamo) {

        String sqlPrestamo =
                "SELECT id_libro, devuelto " +
                        "FROM prestamos " +
                        "WHERE id = ? FOR UPDATE";

        String sqlDevolver =
                "UPDATE prestamos " +
                        "SET devuelto = true " +
                        "WHERE id = ?";

        String sqlStock =
                "UPDATE libros " +
                        "SET stock = stock + 1 " +
                        "WHERE id = ?";

        Connection conexion = null;

        try {

            conexion = DatabaseConnection
                    .getInstance()
                    .getConnection();

            conexion.setAutoCommit(false);

            int idLibro;
            boolean devuelto;

            // 1. Obtener y bloquear préstamo.
            try (PreparedStatement ps =
                         conexion.prepareStatement(
                                 sqlPrestamo
                         )) {

                ps.setInt(
                        1,
                        idPrestamo
                );

                try (ResultSet rs =
                             ps.executeQuery()) {

                    if (!rs.next()) {

                        conexion.rollback();

                        System.err.println(
                                "El préstamo no existe."
                        );

                        return false;
                    }

                    idLibro =
                            rs.getInt("id_libro");

                    devuelto =
                            rs.getBoolean("devuelto");
                }
            }

            if (devuelto) {

                conexion.rollback();

                System.err.println(
                        "El préstamo ya fue devuelto."
                );

                return false;
            }

            // 2. Marcar préstamo como devuelto.
            try (PreparedStatement ps =
                         conexion.prepareStatement(
                                 sqlDevolver
                         )) {

                ps.setInt(
                        1,
                        idPrestamo
                );

                ps.executeUpdate();
            }

            // 3. Devolver unidad al stock.
            try (PreparedStatement ps =
                         conexion.prepareStatement(
                                 sqlStock
                         )) {

                ps.setInt(
                        1,
                        idLibro
                );

                ps.executeUpdate();
            }

            conexion.commit();

            return true;

        } catch (SQLException e) {

            rollback(conexion);

            System.err.println(
                    "Error al devolver libro: "
                            + e.getMessage()
            );

            return false;

        } finally {

            cerrarConexion(conexion);
        }
    }

    /**
     * Elimina un préstamo.
     *
     * Si el préstamo todavía estaba activo, devuelve
     * previamente una unidad al stock.
     */
    public synchronized boolean delete(
            int idPrestamo) {

        String sqlConsultar =
                "SELECT id_libro, devuelto " +
                        "FROM prestamos " +
                        "WHERE id = ? FOR UPDATE";

        String sqlEliminar =
                "DELETE FROM prestamos " +
                        "WHERE id = ?";

        String sqlStock =
                "UPDATE libros " +
                        "SET stock = stock + 1 " +
                        "WHERE id = ?";

        Connection conexion = null;

        try {

            conexion = DatabaseConnection
                    .getInstance()
                    .getConnection();

            conexion.setAutoCommit(false);

            int idLibro;
            boolean devuelto;

            // Consultar préstamo.
            try (PreparedStatement ps =
                         conexion.prepareStatement(
                                 sqlConsultar
                         )) {

                ps.setInt(
                        1,
                        idPrestamo
                );

                try (ResultSet rs =
                             ps.executeQuery()) {

                    if (!rs.next()) {

                        conexion.rollback();

                        return false;
                    }

                    idLibro =
                            rs.getInt("id_libro");

                    devuelto =
                            rs.getBoolean("devuelto");
                }
            }

            /*
             * Si estaba activo, devolvemos la unidad
             * al stock antes de eliminarlo.
             */
            if (!devuelto) {

                try (PreparedStatement ps =
                             conexion.prepareStatement(
                                     sqlStock
                             )) {

                    ps.setInt(
                            1,
                            idLibro
                    );

                    ps.executeUpdate();
                }
            }

            try (PreparedStatement ps =
                         conexion.prepareStatement(
                                 sqlEliminar
                         )) {

                ps.setInt(
                        1,
                        idPrestamo
                );

                if (ps.executeUpdate() == 0) {

                    conexion.rollback();

                    return false;
                }
            }

            conexion.commit();

            return true;

        } catch (SQLException e) {

            rollback(conexion);

            System.err.println(
                    "Error al eliminar préstamo: "
                            + e.getMessage()
            );

            return false;

        } finally {

            cerrarConexion(conexion);
        }
    }

    /**
     * Convierte un ResultSet en un objeto Prestamo.
     */
    private Prestamo crearPrestamo(
            ResultSet rs)
            throws SQLException {

        Date fechaPrestamoSQL =
                rs.getDate("fecha_prestamo");

        Date fechaDevolucionSQL =
                rs.getDate("fecha_devolucion");

        LocalDate fechaPrestamo =
                fechaPrestamoSQL != null
                        ? fechaPrestamoSQL.toLocalDate()
                        : null;

        LocalDate fechaDevolucion =
                fechaDevolucionSQL != null
                        ? fechaDevolucionSQL.toLocalDate()
                        : null;

        return new Prestamo(
                rs.getInt("id"),
                rs.getInt("id_estudiante"),
                rs.getInt("id_libro"),
                fechaPrestamo,
                fechaDevolucion,
                rs.getBoolean("devuelto")
        );
    }

    /**
     * Ejecuta rollback de manera segura.
     */
    private void rollback(Connection conexion) {

        if (conexion != null) {

            try {
                conexion.rollback();
            } catch (SQLException e) {
                System.err.println(
                        "Error en rollback: "
                                + e.getMessage()
                );
            }
        }
    }

    /**
     * Restaura AutoCommit y cierra la conexión.
     */
    private void cerrarConexion(
            Connection conexion) {

        if (conexion != null) {

            try {

                conexion.setAutoCommit(true);
                conexion.close();

            } catch (SQLException e) {

                System.err.println(
                        "Error al cerrar conexión: "
                                + e.getMessage()
                );
            }
        }
    }
}