package rapidexpress.controlador;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import rapidexpress.dominio.Vehiculo;
import rapidexpress.servicio.ReporteService;
import rapidexpress.vista.ReporteView;

/**
 * Controlador para la generación de reportes desde la consola.
 *
 * @author User
 */
public class ReporteController {

    private ReporteView vista = new ReporteView();
    private ReporteService servicio = new ReporteService();
    private final Scanner scanner;

    public ReporteController(Scanner scanner) {
        this.scanner = scanner;
    }

    public void gestionar() {
        boolean continuar = true;

        while (continuar) {
            vista.mostrarMenuReportes();
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    reporteEntregasPorConducir();
                    break;
                case "2":
                    reporteHistorialVehiculo();
                    break;
                case "3":
                    reporteResumenPaquetes();
                    break;
                case "4":
                    reporteVehiculosEnMantenimiento();
                    break;
                case "5":
                    continuar = false;
                    System.out.println("Volviendo al Menu Principal...");
                    break;
                default:
                    System.out.println(" Opcion Invalida");
            }
        }
    }

    private void reporteEntregasPorConducir() {
        System.out.println("\n ====== ENTREGAS POR CONDUCTOR ======");
        System.out.println("Fecha de inicio (dd/mm/yyyy): ");
        String inicio = scanner.nextLine();
        System.out.println("Fecha fin (dd/mm/yyyy): ");
        String fin = scanner.nextLine();
        try {
            Map<String, List<String>> entregas = servicio.getEntregasPorConductor(inicio, fin);
            vista.mostrarReporteEntregas(entregas);
        } catch (Exception e) {
            System.out.println("Error al generar el reporte: " + e.getMessage());
        }
    }

    private void reporteHistorialVehiculo() {
        System.out.println("\n ====== REPORTE HISTORIAL DEL VEHICULO ======");
        System.out.println("\nPlaca del vehiculo: ");
        String placa = scanner.nextLine();
        try {
            List<String> historial = servicio.getHistorialRutasVehiculo(placa);
            vista.mostrarHistorialVehiculo(placa, historial);
        } catch (Exception e) {
            System.out.println("Error al generar el reporte: " + e.getMessage());
        }
    }

    private void reporteResumenPaquetes() {
        System.out.println("\n ====== RESUMEN DE PAQUETES POR ESTADO ======");
        try {
            Map<String, Integer> resumen = servicio.getResumenPaquetesPorEstado();
            vista.mostrarResumenPaquetes(resumen);
        } catch (Exception e) {
            System.out.println("Error al generar el reporte: " + e.getMessage());
        }
    }

    private void reporteVehiculosEnMantenimiento() {
        System.out.println("\n ====== VEHICULOS EN MANTENIMIENTO ======");
        try {
            List<Vehiculo> vehiculos = servicio.getVehiculosEnMantenimiento();
            vista.mostrarVehiculosEnMantenimiento(vehiculos);
        } catch (Exception e) {
            System.out.println("Error al generar el reporte: " + e.getMessage());
        }
    }
}
