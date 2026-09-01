/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.util;

// Se agrega la importación de FileInputStream que faltaba
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Propósito: Cargar y proporcionar el acceso a los parámetros de conexión
 * de la base de datos a partir del archivo de propiedades del proyecto.
 * 
 * @author User
 */
public class Config {

    // Atributo estático que almacena las claves y valores del archivo de configuración.
    private static Properties properties;

    // Bloque de inicialización estático: se ejecuta una sola vez al cargar la clase en memoria.
    static {
        // Inicializa el objeto Properties.
        properties = new Properties();
        
        // Se corrige el nombre a "DataBase.properties" respetando las mayúsculas del proyecto
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("DataBase.properties")) {
            
            // Verifica si el recurso fue encontrado dentro del classpath.
            if (input != null) {
                // Carga los pares clave-valor dentro de la instancia de Properties.
                properties.load(input);
            } else {
                // Intenta la lectura directa si no se encuentra en el classpath.
                try (FileInputStream fileInput = new FileInputStream("DataBase.properties")) {
                    properties.load(fileInput);
                } catch (IOException e) {
                    System.err.println("Error: No se encontró el archivo 'DataBase.properties'.");
                }
            }
            
        } catch (IOException ex) {
            // Imprime la traza de la excepción en caso de falla de lectura de I/O.
            ex.printStackTrace();
        }
    }

    // Constructor privado para impedir que se creen instancias con 'new Config()'.
    private Config() {}

    // Retorna la URL de conexión a la base de datos (clave 'db.url').
    public static String getDbUrl() {
        return properties.getProperty("db.url");
    }

    // Retorna el usuario autenticado de la base de datos (clave 'db.user').
    public static String getDbUser() {
        return properties.getProperty("db.user");
    }

    // Retorna la contraseña del usuario de la base de datos (clave 'db.password').
    public static String getDbPassword() {
        return properties.getProperty("db.password");
    }

    // Retorna el nombre completo de la clase del Driver JDBC (clave 'db.driver').
    public static String getDbDriver() {
        return properties.getProperty("db.driver");
    }
}