/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.vista;

// Importaciones de colecciones de Java y lectura de entradas de consola.
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// Importaciones de la capa de servicio y excepciones de la base de datos.
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.servicio.ReporteService;

/**
 * Propósito: Proveer la interfaz gráfica por consola para la generación y despliegue
 * interactivo de reportes, estadísticas de entregas por conductor y resúmenes de paquetes.
 * 
 * @author User
 * @version 1.0
 */
public class ReporteView {

    // Servicio encargado de la extracción y procesamiento de reportes.
    private final ReporteService reporteService;
    // Lector de datos desde la consola de comandos.
    private final Scanner scanner;

    /**
     * Constructor predeterminado que inicializa el escáner y la capa de servicio.
     */
    public ReporteView() {
        // Inicializa la lectura por consola.
        this.scanner = new Scanner(System.in);
        // Inicializa el servicio de reportes del sistema.
        this.reporteService = new ReporteService();
    }

    /**
     * Muestra el menú de opciones para la generación de reportes y gestiona la navegación.
     */
    public void mostrarMenu() {
        boolean continuar = true;

        while (continuar) {
            // Imprime la cabecera visual del módulo de reportes.
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║         MÓDULO DE REPORTES             ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. Reporte de entregas por conductor (por fechas)");
            System.out.println("2. Resumen de paquetes por estado");
            System.out.println("3. Volver al Menú Principal");
            System.out.print("\nSeleccione una opción: ");

            // Lee la opción igresada limpiando espacios sobrantes.
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    // Genera el reporte filtrado de entregas por conductor.
                    generarReporteEntregas();
                    break;
                case "2":
                    // Genera el reporte cuantitativo del estado de paquetes.
                    generarResumenPaquetes();
                    break;
                case "3":
                    // Retorna al menú principal finalizando el ciclo.
                    continuar = false;
                    break;
                default:
                    // Notifica opción no válida.
                    System.out.println("\n❌ Opción no válida. Intente nuevamente.");
            }
        }
    }

    /**
     * Solicita al usuario ingresar un rango de fechas en formato ISO (YYYY-MM-DD).
     * 
     * @return Arreglo de cadenas donde el índice 0 es la fecha de inicio y el índice 1 es la fecha de fin.
     */
    public String[] solicitarRangoFechas() {
        System.out.println("\n--- FILTRO POR RANGO DE FECHAS ---");

        // Captura la fecha de inicio.
        System.out.print("Fecha inicio (YYYY-MM-DD): ");
        String inicio = scanner.nextLine().trim();

        // Captura la fecha final.
        System.out.print("Fecha fin (YYYY-MM-DD): ");
        String fin = scanner.nextLine().trim();

        // Retorna las fechas en un arreglo de Strings.
        return new String[]{inicio, fin};
    }

    /**
     * Muestra la relación detallada de conductores y los códigos de paquetes entregados.
     * 
     * @param entregas Mapa donde la clave es el identificador del conductor y el valor es la lista de entregas.
     */
    public void mostrarReporteEntregas(Map<String, List<String>> entregas) {
        // Valida si el reporte no contiene información.
        if (entregas == null || entregas.isEmpty()) {
            System.out.println("\n⚠️ No se encontraron entregas registradas en el rango de fechas especificado.");
            return;
        }

        // Imprime la cabecera del reporte de entregas.
        System.out.println("\n═════════════════════════════════════════════════════════════════════");
        System.out.println("            REPORTE DE ENTREGAS REALIZADAS POR CONDUCTOR            ");
        System.out.println("═════════════════════════════════════════════════════════════════════");

        // Recorre cada entrada del mapa (Conductor -> Lista de entregas/paquetes).
        for (Map.Entry<String, List<String>> entry : entregas.entrySet()) {
            String conductor = entry.getKey();
            List<String> listaPaquetes = entry.getValue();

            System.out.println("\n👤 Conductor / Asignación: " + conductor);
            System.out.println("─────────────────────────────────────────────────────────────────────");

            // Verifica si el conductor posee entregas registradas.
            if (listaPaquetes == null || listaPaquetes.isEmpty()) {
                System.out.println("   (Sin entregas registradas en este período)");
            } else {
                // Imprime los códigos o descripciones de paquetes completados.
                for (String paquete : listaPaquetes) {
                    System.out.println("   📦 Paquete / Tracking ID: " + paquete);
                }
                System.out.println("   -> Total entregas realizadas: " + listaPaquetes.size());
            }
        }
        System.out.println("═════════════════════════════════════════════════════════════════════");
    }

    /**
     * Muestra una tabla con el conteo acumulado de paquetes agrupados por su estado operativo.
     * 
     * @param resumen Mapa cuya clave es el nombre del estado y el valor es la cantidad de paquetes.
     */
    public void mostrarResumenPaquetes(Map<String, Integer> resumen) {
        // Controla reportes vacíos.
        if (resumen == null || resumen.isEmpty()) {
            System.out.println("\n⚠️ No hay datos disponibles para generar el resumen de paquetes.");
            return;
        }

        // Imprime la cabecera formateada de la tabla cuantitativa.
        System.out.println("\n════════════════════════════════════════════════════════");
        System.out.printf("%-30s %-15s\n", "ESTADO DEL PAQUETE", "CANTIDAD");
        System.out.println("════════════════════════════════════════════════════════");

        // Variable para acumular el total general de paquetes.
        int totalGeneral = 0;

        // Recorre las entradas del resumen e imprime cada fila.
        for (Map.Entry<String, Integer> entry : resumen.entrySet()) {
            String estado = entry.getKey();
            Integer cantidad = entry.getValue() != null ? entry.getValue() : 0;

            System.out.printf("%-30s %-15d\n", estado, cantidad);
            // Acumula el total.
            totalGeneral += cantidad;
        }

        // Muestra el total consolidado al final de la tabla.
        System.out.println("────────────────────────────────────────────────────────");
        System.out.printf("%-30s %-15d\n", "TOTAL GENERAL DE PAQUETES", totalGeneral);
        System.out.println("════════════════════════════════════════════════════════");
    }

    // ── MÉTODOS PRIVADOS AUXILIARES DE LÓGICA E INTERACCIÓN CON EL SERVICIO ──

    /**
     * Captura el rango de fechas e invoca el servicio para desplegar las entregas por conductor.
     */
    private void generarReporteEntregas() {
        // Solicita las fechas filtro.
        String[] fechas = solicitarRangoFechas();
        String inicio = fechas[0];
        String fin = fechas[1];

        try {
            // Llama al servicio para obtener el reporte mapeado.
            Map<String, List<String>> entregas = reporteService.generarReporteEntregasPorConductor(inicio, fin);
            // Despliega el informe en pantalla.
            mostrarReporteEntregas(entregas);
        } catch (DataBaseException | IllegalArgumentException e) {
            System.out.println("\n❌ Error al generar el reporte de entregas: " + e.getMessage());
        }
    }

    /**
     * Invoca la capa de servicio para consolidar e imprimir el estado actual de los paquetes.
     */
    private void generarResumenPaquetes() {
        System.out.println("\n--- RESUMEN DE PAQUETES POR ESTADO ---");
        try {
            // Solicita al servicio el mapa de totales por estado.
            Map<String, Integer> resumen = reporteService.obtenerResumenPaquetesPorEstado();
            // Despliega la tabla del resumen.
            mostrarResumenPaquetes(resumen);
        } catch (DataBaseException e) {
            System.out.println("\n❌ Error al consultar el resumen de paquetes: " + e.getMessage());
        }
    }
}