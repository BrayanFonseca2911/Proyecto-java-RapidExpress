package rapidexpress.vista;

import java.util.Scanner;

/**
 * Menú principal del sistema RapidExpress
 * 
 * @author User
 */
public class MenuPrincipal {
    
    private Scanner scanner;
    
    /**
     * Constructor del menú principal
     */
    public MenuPrincipal() {
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Muestra el menú principal y gestiona la navegación
     */
    public void mostrarMenu() {
        boolean continuar = true;
        
        while (continuar) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║        MENÚ PRINCIPAL                  ║");
            System.out.println("════════════════════════════════════════╝");
            System.out.println("1. Gestión de Vehículos");
            System.out.println("2. Gestión de Conductores");
            System.out.println("3. Gestión de Paquetes");
            System.out.println("4. Planificación de Rutas");
            System.out.println("5. Reportes");
            System.out.println("6. Salir");
            System.out.print("\nSeleccione una opción: ");
            
            String opcion = scanner.nextLine();
            
            switch (opcion) {
                case "1":
                    System.out.println("\n→ Módulo de Vehículos (En desarrollo)");
                    break;
                case "2":
                    System.out.println("\n→ Módulo de Conductores (En desarrollo)");
                    break;
                case "3":
                    System.out.println("\n→ Módulo de Paquetes (En desarrollo)");
                    break;
                case "4":
                    System.out.println("\n→ Módulo de Rutas (En desarrollo)");
                    break;
                case "5":
                    System.out.println("\n→ Módulo de Reportes (En desarrollo)");
                    break;
                case "6":
                    System.out.println("\n¡Hasta luego! Gracias por usar RapidExpress.");
                    continuar = false;
                    break;
                default:
                    System.out.println("\n❌ Opción inválida. Intente nuevamente.");
            }
        }
        
        scanner.close();
    }
}