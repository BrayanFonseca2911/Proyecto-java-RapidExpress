package rapidexpress.util;

/**
 * Gestiona la conexión a la base de datos MySQL (Patrón Singleton)
 * 
 * @author User
 */

// Importación de la interfaz Connection para manejar la sesión activa con la base de datos MySQL.
import java.sql.Connection;
// Importación del administrador de controladores JDBC que establece la conexión con la base de datos.
import java.sql.DriverManager;
// Importación de la excepción de SQL para capturar fallos de autenticación, puerto o red.
import java.sql.SQLException;

// Clase de utilidad encubierta que provee conexiones estáticas a la base de datos del sistema.
public class DBConnection {

    // Cadena de conexión JDBC que especifica el host (localhost), puerto (3307), nombre de la BD (rapidexpress_db) y parámetros de seguridad/zona horaria.
    private static final String URL = "jdbc:mysql://localhost:3307/appdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    
    // Usuario administrador por defecto para acceder a la base de datos MySQL.
    private static final String USER = "root";
    
    // Contraseña asignada para la autenticación del usuario root en la instancia local.
    private static final String PASSWORD = "industryPIGS123";

    // Constructor privado para evitar que la clase de utilidad sea instanciada directamente con 'new DBConnection()'.
    private DBConnection() {}

    // Método estático público que crea y retorna una conexión activa a MySQL.
    public static Connection getConnection() throws SQLException {
        try {
            // Carga dinámicamente en memoria la clase del controlador (Driver) de MySQL Connector/J.
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Solicita al DriverManager una conexión utilizando las credenciales y la URL configuradas.
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            // En caso de no encontrar la librería JAR del conector de MySQL, relanza el error encapsulado en una SQLException.
            throw new SQLException("No se encontró el driver JDBC de MySQL en el classpath: " + e.getMessage(), e);
        }
    }
}