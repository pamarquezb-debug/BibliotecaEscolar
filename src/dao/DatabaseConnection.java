package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de administrar la conexión
 * con la base de datos MySQL de Biblioteca Escolar.
 *
 * Implementa el patrón de diseño Singleton para
 * garantizar una única instancia de configuración
 * de conexión dentro de la aplicación.
 *
 * @author Pablo Marquez
 * @version 1.0
 */
public class DatabaseConnection {

    private static DatabaseConnection instancia;

    private static final String URL =
            "jdbc:mysql://localhost:3306/biblioteca"
                    + "?useSSL=false"
                    + "&serverTimezone=America/Santiago";

    private static final String USER = "root";

    /*
     * Modificar si MySQL utiliza contraseña.
     */
    private static final String PASSWORD = "";

    /**
     * Constructor privado.
     *
     * Impide crear objetos DatabaseConnection
     * directamente desde otras clases.
     */
    private DatabaseConnection() {
    }

    /**
     * Retorna la única instancia de DatabaseConnection.
     *
     * @return instancia única de DatabaseConnection
     */
    public static synchronized DatabaseConnection getInstance() {

        if (instancia == null) {
            instancia = new DatabaseConnection();
        }

        return instancia;
    }

    /**
     * Obtiene una nueva conexión con la base de datos.
     *
     * @return conexión JDBC
     * @throws SQLException si ocurre un error de conexión
     */
    public Connection getConnection() throws SQLException {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}