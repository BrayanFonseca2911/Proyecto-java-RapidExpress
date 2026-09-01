package rapidexpress.vista;

// Importación para la lectura de entradas del usuario por consola.
import java.util.Scanner;

/**
 * Propósito: Actuar como el punto de entrada de la interfaz de consola del sistema RapidExpress.
 * Administra el bucle principal de navegación, despliega el menú general e invoca los
 * módulos de gestión correspondientes (Vehículos, Conductores, Paquetes, Rutas y Reportes).
 * 
 * @author User
 * @version 1.0
 */
public class MenuPrincipal {
    
    // Objeto Scanner compartido para la captura de opciones en el menú principal.
    private final Scanner scanner;
    
    // Declaración de las subvistas asociadas a cada módulo del sistema.
    private final VehiculoView vehiculoView;
    private final ConductorView conductorView;
    private final PaqueteView paqueteView;
    private final RutaView rutaView;
    private final ReporteView reporteView;
    
    /**
     * Constructor predeterminado. Inicializa el lector de consola y
     * crea las instancias de las subvistas que conforman el sistema.
     */
    public MenuPrincipal() {
        this.scanner = new Scanner(System.in);
        // Instanciación de los componentes de la capa de vista.
        this.vehiculoView = new VehiculoView();
        this.conductorView = new ConductorView();
        this.paqueteView = new PaqueteView();
        this.rutaView = new RutaView();
        this.reporteView = new ReporteView();
    }
    
    /**
     * Despliega el menú principal en consola y mantiene el ciclo de interacción
     * hasta que el usuario elija finalizar la ejecución del programa.
     */
    public void mostrarMenu() {
        boolean continuar = true;
        
        while (continuar) {
            // Imprime la cabecera visual del menú principal.
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║         SISTEMA RAPIDEXPRESS           ║");
            System.out.println("║             MENÚ PRINCIPAL             ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. Gestión de Vehículos");
            System.out.println("2. Gestión de Conductores");
            System.out.println("3. Gestión de Paquetes");
            System.out.println("4. Planificación de Rutas");
            System.out.println("5. Reportes y Estadísticas");
            System.out.println("6. Salir del Sistema");
            System.out.print("\nSeleccione una opción: ");
            
            // Lectura de la opción seleccionada limpiando espacios en blanco.
            String opcion = scanner.nextLine().trim();
            
            // Evaluador de opciones para delegar el control a la subvista adecuada.
            switch (opcion) {
                case "1":
                    // Invoca el submódulo para administración de vehículos.
                    vehiculoView.mostrarMenu();
                    break;
                case "2":
                    // Invoca el submódulo para administración de conductores.
                    conductorView.mostrarMenu();
                    break;
                case "3":
                    // Invoca el submódulo para administración de paquetes.
                    paqueteView.mostrarMenu();
                    break;
                case "4":
                    // Invoca el submódulo para planificación y gestión de rutas.
                    rutaView.mostrarMenu();
                    break;
                case "5":
                    // Invoca el submódulo para generación de reportes y estadísticas.
                    reporteView.mostrarMenu();
                    break;
                case "6":
                    // Finaliza el bucle de ejecución y despide al usuario.
                    System.out.println("\n¡Hasta luego! Gracias por usar RapidExpress.");
                    continuar = false;
                    break;
                default:
                    // Manejo de opción no válida.
                    System.out.println("\n❌ Opción inválida. Intente nuevamente con un número del 1 al 6.");
            }
        }
    }
}