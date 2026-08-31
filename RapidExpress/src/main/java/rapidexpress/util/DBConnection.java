package rapidexpress.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión a la base de datos MySQL (Patrón Singleton)
 * 
 * @author User
 */
public class DBConnection {
    
    private static DBConnection instance;
    private Connection connection;
    
    // Configuración de la base de datos
    private final String URL = "jdbc:mysql://localhost:3306/rapidexpress";
    private final String USER = "root";
    private final String PASSWORD = "tu_contraseña";
    private final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    /**
     * Constructor privado (Singleton)
     */
    private DBConnection() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión establecida correctamente");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene la instancia única de la conexión
     * @return Instancia de DBConnection
     */
    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
    
    /**
     * Obtiene la conexión activa
     * @return Objeto Connection
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Cierra la conexión
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión cerrada");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}