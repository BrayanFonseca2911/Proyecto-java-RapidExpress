package rapidexpress.util;

import rapidexpress.excepciones.DataBaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión a la base de datos MySQL.
 * Implementa el patrón Singleton para garantizar una única conexión en toda la aplicación.
 * 
 * @author User
 */
public class DBConnection {
    
    /** Instancia única de la conexión (Singleton) */
    private static DBConnection instance;
    
    /** Objeto Connection de JDBC */
    private Connection connection;
    
    /** URL de conexión a la base de datos */
    private final String url;
    
    /** Usuario de la base de datos */
    private final String user;
    
    /** Contraseña de la base de datos */
    private final String password;
    
    /** Driver de MySQL */
    private final String driver = "com.mysql.cj.jdbc.Driver";
    
    /**
     * Constructor privado (Patrón Singleton)
     * Lee la configuración desde Config.java y establece la conexión
     * 
     * @throws DataBaseException Si ocurre un error al conectar
     */
    private DBConnection() throws DataBaseException {
        this.url = Config.getDbUrl();
        this.user = Config.getDbUser();
        this.password = Config.getDbPassword();
        
        try {
            // Cargar el driver de MySQL
            Class.forName(driver);
            
            // Establecer la conexión
            this.connection = DriverManager.getConnection(url, user, password);
            
            System.out.println(" Conexion a base de datos establecida correctamente");
            System.out.println(" URL: " + url);
            
        } catch (ClassNotFoundException e) {
            throw new DataBaseException("Driver MySQL no encontrado: " + e.getMessage(), e);
        } catch (SQLException e) {
            throw new DataBaseException("Error al conectar a la base de datos: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene la instancia única de DBConnection.
     * Si no existe, la crea. Si existe, la retorna.
     * 
     * @return Instancia única de DBConnection
     * @throws DataBaseException Si ocurre un error al crear la conexión
     */
    public static DBConnection getInstance() throws DataBaseException {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }
    
    /**
     * Obtiene la conexión activa a la base de datos.
     * Si la conexión está cerrada, intenta reconectar.
     * 
     * @return Objeto Connection activo
     * @throws DataBaseException Si no se puede obtener la conexión
     */
    public Connection getConnection() throws DataBaseException {
        try {
            // Verificar si la conexión está cerrada o nula
            if (connection == null || connection.isClosed()) {
                System.out.println("Conexión cerrada, reconectando...");
                connection = DriverManager.getConnection(url, user, password);
            }
            return connection;
        } catch (SQLException e) {
            throw new DataBaseException("Error al obtener la conexion: " + e.getMessage(), e);
        }
    }
    
    /**
     * Cierra la conexión a la base de datos.
     * Solo debe llamarse al finalizar la aplicación.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión a base de datos cerrada");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
    
    /**
     * Verifica si la conexión está activa
     * 
     * @return true si la conexión está activa, false en caso contrario
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Reinicia la conexión (cierra y crea una nueva)
     * Útil en caso de errores de conexión
     * 
     * @throws DataBaseException Si ocurre un error al reconectar
     */
    public void resetConnection() throws DataBaseException {
        closeConnection();
        instance = null;
        // La próxima llamada a getInstance() creará una nueva conexión
    }
}