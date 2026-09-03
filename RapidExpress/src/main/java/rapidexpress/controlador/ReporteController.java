package rapidexpress.controlador;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
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

    public void gestionar() {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            vista.mostrarMenuReportes();
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    reporteEntregasPorConducir(scanner);
                    break;
                case "2":
                    reporteHistorialVehiculo(scanner);
                    break;
                case "3":
                    reporteResumenPaquetes();
                    break;
                case "4":
                    continuar = false;
                    System.out.println("Volviendo al Menú Principal...");
                    break;
                default:
                    System.out.println(" Opcion Invalida");
            }
        }
    }

    private void reporteEntregasPorConducir(Scanner scanner) {
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

    private void reporteHistorialVehiculo(Scanner scanner) {
        System.out.println("\n ====== REPORTE HISTORIAL DEL VEHICULO ======");
        System.out.println("\nPlaca del vehículo: ");
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
}
