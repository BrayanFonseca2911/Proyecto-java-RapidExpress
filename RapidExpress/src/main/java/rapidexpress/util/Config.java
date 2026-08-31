/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 *
 * @author User
 */

/*Propósito: Lee configuración de base de datos.
Atributos:
private static Properties properties
Métodos:
static { properties = new Properties(); try { properties.load(new FileInputStream("database.properties")) } catch ... }
public static String getDbUrl(): Retorna properties.getProperty("db.url")
public static String getDbUser(): Retorna properties.getProperty("db.user")
public static String getDbPassword(): Retorna properties.getProperty("db.password")
public static String getDbDriver(): Retorna properties.getProperty("db.driver")*/
public class Config {
    private Properties properties;
    
    public Config() {
        properties = new Properties();
        cargarConfiguracion();
    }
    
    private void cargarConfiguracion() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("database.properties")) {
            
            if (input == null) {
                System.out.println("Lo siento, no se pudo encontrar el archivo database.properties");
                return;
            }
            
            properties.load(input);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public String getDbUrl() {
        return properties.getProperty("db.url");
    }
    
    public String getDbUser() {
        return properties.getProperty("db.user");
    }
    
    public String getDbPassword() {
        return properties.getProperty("db.password");
    }
    
    public String getDbDriver() {
        return properties.getProperty("db.driver");
    }
}
