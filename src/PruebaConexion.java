import dao.DatabaseConnection;

import java.sql.Connection;

/**
 * Prueba de conexión JDBC con la base de datos biblioteca.
 */
public class PruebaConexion {

    public static void main(String[] args) {

        try (Connection conexion =
                     DatabaseConnection
                             .getInstance()
                             .getConnection()) {

            if (conexion != null
                    && !conexion.isClosed()) {

                System.out.println(
                        "Conexión exitosa a la base de datos biblioteca."
                );
            }

        } catch (Exception e) {

            System.err.println(
                    "Error de conexión: "
                            + e.getMessage()
            );
        }
    }
}