package rapidexpress.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Lee la configuración de la base de datos desde un archivo properties.
 * Permite cambiar credenciales sin modificar el código fuente.
 * 
 * @author User
 */
public class Config {
    
    /** Objeto Properties para almacenar la configuración */
    private static Properties properties;
    
    /** Nombre del archivo de configuración */
    private static final String CONFIG_FILE = "database.properties";
    
    // Valores por defecto (se usan si no existe el archivo)
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/rapidexpress?useSSL=false&serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";
    private static final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    /**
     * Bloque estático que carga la configuración al iniciar la clase
     */
    static {
        properties = new Properties();
        try {
            // Intentar cargar desde el archivo de propiedades
            FileInputStream fis = new FileInputStream(CONFIG_FILE);
            properties.load(fis);
            fis.close();
            System.out.println("✅ Configuración cargada desde " + CONFIG_FILE);
        } catch (IOException e) {
            System.out.println("⚠️ No se encontró " + CONFIG_FILE + ", usando valores por defecto");
            // Establecer valores por defecto
            properties.setProperty("db.url", DEFAULT_URL);
            properties.setProperty("db.user", DEFAULT_USER);
            properties.setProperty("db.password", DEFAULT_PASSWORD);
            properties.setProperty("db.driver", DEFAULT_DRIVER);
        }
    }
    
    /**
     * Obtiene la URL de conexión a la base de datos
     * @return URL de conexión
     */
    public static String getDbUrl() {
        return properties.getProperty("db.url", DEFAULT_URL);
    }
    
    /**
     * Obtiene el usuario de la base de datos
     * @return Nombre de usuario
     */
    public static String getDbUser() {
        return properties.getProperty("db.user", DEFAULT_USER);
    }
    
    /**
     * Obtiene la contraseña de la base de datos
     * @return Contraseña
     */
    public static String getDbPassword() {
        return properties.getProperty("db.password", DEFAULT_PASSWORD);
    }
    
    /**
     * Obtiene el driver de la base de datos
     * @return Nombre del driver
     */
    public static String getDbDriver() {
        return properties.getProperty("db.driver", DEFAULT_DRIVER);
    }
    
    /**
     * Obtiene cualquier propiedad de configuración
     * @param key Nombre de la propiedad
     * @return Valor de la propiedad
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Obtiene cualquier propiedad con valor por defecto
     * @param key Nombre de la propiedad
     * @param defaultValue Valor por defecto si no existe
     * @return Valor de la propiedad
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Muestra la configuración actual (útil para debugging)
     */
    public static void mostrarConfiguracion() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║      CONFIGURACIÓN DE BASE DE DATOS    ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("URL: " + getDbUrl());
        System.out.println("Usuario: " + getDbUser());
        System.out.println("Password: " + (getDbPassword().isEmpty() ? "(vacío)" : "****"));
        System.out.println("Driver: " + getDbDriver());
    }
}