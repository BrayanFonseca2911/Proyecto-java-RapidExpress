package com.mycompany.rapidexpress;

import rapidexpress.controlador.MenuPrincipalController;
import rapidexpress.util.DBConnection;
import rapidexpress.excepciones.DataBaseException;

/**
 * Clase principal de la aplicación RapidExpress.
 * Sistema de Gestión de Flotas y Rutas.
 *
 * Esta clase es el punto de entrada de la aplicación.
 * Inicializa la conexión a la base de datos y lanza el menú principal.
 *
 * @author User
 */
public class RapidExpress {

    /**
     * Método principal de la aplicación
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {

        
        // Mostrar mensaje de bienvenida
        System.out.println("+==================================================+");
        System.out.println("|                                        |");
        System.out.println("|       RAPIDEXPRESS - Sistema de Gestion|");
        System.out.println("|       de Flotas y Rutas                |");
        System.out.println("|                                        |");
        System.out.println("|       Version 1.0                      |");
        System.out.println("|                                        |");
        System.out.println("+==================================================+");
        System.out.println();

        try {
            // Paso 1: Establecer conexión a la base de datos
            System.out.println(" Conectando a la base de datos...");
            DBConnection dbConnection = DBConnection.getInstance();

            // Verificar que la conexión esté activa
            if (dbConnection.isConnected()) {
                System.out.println(" Conexion establecida correctamente");
                System.out.println();
            } else {
                System.out.println(" No se pudo establecer la conexion");
                System.out.println("Verifica la configuracion en database.properties");
                System.exit(1);
            }

            // Paso 2: Crear el controlador del menú principal
            MenuPrincipalController controller = new MenuPrincipalController();

            // Paso 3: Iniciar el menú principal
            System.out.println("Iniciando sistema...");
            System.out.println();
            controller.iniciar();

        } catch (DataBaseException e) {
            // Error de conexión a la base de datos
            System.err.println();
            System.err.println("+==================================================+");
            System.err.println("|   ERROR DE CONEXION A LA BASE DE DATOS         |");
            System.err.println("+==================================================+");
            System.err.println();
            System.err.println("Mensaje: " + e.getMessage());
            System.err.println();
            System.err.println("Posibles causas:");
            System.err.println("  1. MySQL no esta ejecutandose");
            System.err.println("  2. Credenciales incorrectas en database.properties");
            System.err.println("  3. La base de datos 'rapidexpress' no existe");
            System.err.println("  4. El driver MySQL no esta en el classpath");
            System.err.println();
            System.err.println("Solucion:");
            System.err.println("  - Verifica que MySQL este corriendo");
            System.err.println("  - Revisa el archivo database.properties");
            System.err.println("  - Ejecuta los scripts SQL para crear la BD");
            System.err.println();

        } catch (Exception e) {
            // Error genérico
            System.err.println();
            System.err.println("+==================================================+");
            System.err.println("|   ERROR CRITICO EN LA APLICACION               |");
            System.err.println("+==================================================+");
            System.err.println();
            System.err.println("Mensaje: " + e.getMessage());
            System.err.println();
            e.printStackTrace();

        } finally {
            // Asegurar que la conexión se cierre al salir
            System.out.println();
            System.out.println(" Cerrando conexion a la base de datos...");
            try {
                DBConnection.getInstance().closeConnection();
                System.out.println(" Conexion cerrada correctamente");
            } catch (Exception e) {
                System.err.println(" Error al cerrar la conexion: " + e.getMessage());
            }

            System.out.println();
            System.out.println(" !Gracias por usar RapidExpress!");
            System.out.println();
        }
    }
}
