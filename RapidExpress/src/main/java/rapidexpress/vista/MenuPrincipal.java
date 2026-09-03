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

        ConsoleUtil.imprimirTitulo("RAPIDEXPRESS - Sistema de Gestión");
        System.out.println("\n   Bienvenido al sistema de gestión de flotas y rutas\n");
        ConsoleUtil.imprimirSeparador();

        System.out.println("   1. Gestión de Vehículos");
        System.out.println("   2. Gestión de Conductores");
        System.out.println("   3. Gestión de Paquetes");
        System.out.println("   4. Planificación de Rutas");
        System.out.println("   5. Reportes");
        System.out.println("   6. Salir");
        ConsoleUtil.imprimirSeparador();
    }
}
