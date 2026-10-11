package dao;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

/**
 * DAO encargado de generar consultas para
 * los reportes de Biblioteca Escolar.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class ReporteDAO {

    /**
     * Obtiene los libros ordenados por cantidad
     * de préstamos realizados.
     */
    public DefaultTableModel librosMasPrestados() {

        DefaultTableModel modelo =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Libro",
                                "Autor",
                                "Total préstamos"
                        },
                        0
                ) {
                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {
                        return false;
                    }
                };

        String sql =
                "SELECT l.id, l.titulo, l.autor, " +
                        "COUNT(p.id) AS total " +
                        "FROM libros l " +
                        "LEFT JOIN prestamos p " +
                        "ON p.id_libro = l.id " +
                        "GROUP BY l.id, l.titulo, l.autor " +
                        "ORDER BY total DESC, l.titulo";

        try (Connection conexion =
                     DatabaseConnection
                             .getInstance()
                             .getConnection();

             PreparedStatement ps =
                     conexion.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {

                modelo.addRow(
                        new Object[]{
                                rs.getInt("id"),
                                rs.getString("titulo"),
                                rs.getString("autor"),
                                rs.getInt("total")
                        }
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error en reporte de libros: "
                            + e.getMessage()
            );
        }

        return modelo;
    }

    /**
     * Obtiene el historial completo de préstamos.
     */
    public DefaultTableModel historialPrestamos() {

        DefaultTableModel modelo =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Estudiante",
                                "Curso",
                                "Libro",
                                "Préstamo",
                                "Devolución",
                                "Estado"
                        },
                        0
                ) {
                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {
                        return false;
                    }
                };

        String sql =
                "SELECT p.id, " +
                        "e.nombre AS estudiante, " +
                        "e.curso, " +
                        "l.titulo AS libro, " +
                        "p.fecha_prestamo, " +
                        "p.fecha_devolucion, " +
                        "p.devuelto " +
                        "FROM prestamos p " +
                        "INNER JOIN estudiantes e " +
                        "ON e.id = p.id_estudiante " +
                        "INNER JOIN libros l " +
                        "ON l.id = p.id_libro " +
                        "ORDER BY e.nombre, p.fecha_prestamo DESC";

        try (Connection conexion =
                     DatabaseConnection
                             .getInstance()
                             .getConnection();

             PreparedStatement ps =
                     conexion.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {

                String estado =
                        rs.getBoolean("devuelto")
                                ? "DEVUELTO"
                                : "PENDIENTE";

                modelo.addRow(
                        new Object[]{
                                rs.getInt("id"),
                                rs.getString("estudiante"),
                                rs.getString("curso"),
                                rs.getString("libro"),
                                rs.getDate("fecha_prestamo"),
                                rs.getDate("fecha_devolucion"),
                                estado
                        }
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error en historial: "
                            + e.getMessage()
            );
        }

        return modelo;
    }

    /**
     * Obtiene los libros actualmente prestados.
     */
    public DefaultTableModel prestamosActivos() {

        DefaultTableModel modelo =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Estudiante",
                                "Libro",
                                "Fecha préstamo",
                                "Fecha devolución",
                                "Estado"
                        },
                        0
                ) {
                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {
                        return false;
                    }
                };

        String sql =
                "SELECT p.id, " +
                        "e.nombre AS estudiante, " +
                        "l.titulo AS libro, " +
                        "p.fecha_prestamo, " +
                        "p.fecha_devolucion, " +
                        "CASE " +
                        "WHEN p.fecha_devolucion < CURDATE() " +
                        "THEN 'ATRASADO' " +
                        "ELSE 'ACTIVO' END AS estado " +
                        "FROM prestamos p " +
                        "INNER JOIN estudiantes e " +
                        "ON e.id = p.id_estudiante " +
                        "INNER JOIN libros l " +
                        "ON l.id = p.id_libro " +
                        "WHERE p.devuelto = 0 " +
                        "ORDER BY p.fecha_devolucion";

        try (Connection conexion =
                     DatabaseConnection
                             .getInstance()
                             .getConnection();

             PreparedStatement ps =
                     conexion.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {

                modelo.addRow(
                        new Object[]{
                                rs.getInt("id"),
                                rs.getString("estudiante"),
                                rs.getString("libro"),
                                rs.getDate("fecha_prestamo"),
                                rs.getDate("fecha_devolucion"),
                                rs.getString("estado")
                        }
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error en préstamos activos: "
                            + e.getMessage()
            );
        }

        return modelo;
    }
}