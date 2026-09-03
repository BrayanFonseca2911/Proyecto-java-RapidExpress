package rapidexpress.vista;

import rapidexpress.util.ConsoleUtil;

/**
 * Vista del menú principal del sistema RapidExpress.
 * Muestra las opciones principales al usuario.
 *
 * @author User
 */
public class MenuPrincipal {

    /**
     * Muestra el menú principal con las opciones disponibles
     */
    public void mostrarMenu() {
        ConsoleUtil.limpiarPantalla();

        ConsoleUtil.imprimirTitulo("RAPIDEXPRESS - Sistema de Gestion");
        System.out.println("\n   Bienvenido al sistema de gestion de flotas y rutas\n");
        ConsoleUtil.imprimirSeparador();

        System.out.println("   1. Gestion de Vehiculos");
        System.out.println("   2. Gestion de Conductores");
        System.out.println("   3. Gestion de Paquetes");
        System.out.println("   4. Planificacion de Rutas");
        System.out.println("   5. Reportes");
        System.out.println("   6. Salir");
        ConsoleUtil.imprimirSeparador();
    }
}