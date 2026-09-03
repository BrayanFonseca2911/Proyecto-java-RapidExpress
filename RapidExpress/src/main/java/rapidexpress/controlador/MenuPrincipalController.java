package rapidexpress.controlador;

import java.util.Scanner;
import rapidexpress.servicio.ConductorService;
import rapidexpress.servicio.PaqueteService;
import rapidexpress.servicio.ReporteService;
import rapidexpress.servicio.RutaService;
import rapidexpress.servicio.VehiculoService;
import rapidexpress.vista.MenuPrincipal;
/**
 *
 * @author User
 */
public class MenuPrincipalController {
    private MenuPrincipal vista;
    private VehiculoService vehiculoservice;
    private ConductorService conductorservice;
    private PaqueteService paqueteservice;
    private RutaService rutaservice;
    private ReporteService reporteservice;


    public MenuPrincipalController() {

        this.vista = new MenuPrincipal();

        this.vehiculoservice =  new VehiculoService();
        this.conductorservice = new ConductorService();
        this.paqueteservice  = new PaqueteService();
        this.rutaservice = new RutaService();
        this.reporteservice = new ReporteService();
    }

    //Inicia el menú principal y gestiona la navegación
    public void iniciar(){
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            vista.mostrarMenu();
            System.out.println("\nSeleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    gestionarVehiculos();
                    break;
                case "2":
                    gestionarConductores();
                    break;
                case "3":
                    gestionarPaquetes();
                    break;
                case "4":
                    gestionarRutas();
                    break;
                case "5":
                    gestionarReportes();
                    break;
                case "6":
                    salir();
                    continuar = false;
                    break;
                default:
                    System.out.println("Opcion no encontrada. Intenta otra vez");
            }
        }
        scanner.close();
    }

    public void gestionarVehiculos() {
        System.out.println("\n ==== MODULO DE VEHICULOS ==== ");
        VehiculoController controller = new VehiculoController();
        controller.gestionar();
    }

    public void gestionarConductores() {
        System.out.println("\n ====== MODULO DE CONDUCTORES ====== ");
        ConductorController controller = new ConductorController();
        controller.gestionar();
    }

    public void gestionarPaquetes() {
        System.out.println("\n ====== MODULO DE PAQUETES ====== ");
        PaqueteController controller = new PaqueteController();
        controller.gestionar();
    }

    public void gestionarRutas() {
        System.out.println("\n ====== MODULO DE RUTAS ====== ");
        RutaController controller = new RutaController();
        controller.gestionar();
    }

    public void gestionarReportes() {
        System.out.println("\n ====== MODULO DE REPORTES ======");
        ReporteController controller = new ReporteController();
        controller.gestionar();
    }

    public void salir() {
        System.out.println("\n¡Hasta la proxima! Gracias por preferir RapidExpress. ");
    }
}
