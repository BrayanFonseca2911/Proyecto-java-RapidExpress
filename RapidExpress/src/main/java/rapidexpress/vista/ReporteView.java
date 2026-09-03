package rapidexpress.vista;

import rapidexpress.dominio.Vehiculo;
import rapidexpress.util.ConsoleUtil;

import java.util.List;
import java.util.Map;

/**
 * Vista para la visualización de reportes.
 *
 * @author User
 */
public class ReporteView {

    /**
     * Muestra el menú de reportes
     */
    public void mostrarMenuReportes() {
        ConsoleUtil.limpiarPantalla();
        ConsoleUtil.imprimirTitulo("REPORTES");

        System.out.println("\n   1. Entregas por conductor en rango de fechas");
        System.out.println("   2. Historial de rutas de un vehiculo");
        System.out.println("   3. Resumen de paquetes por estado");
        System.out.println("   4. Vehiculos en mantenimiento");
        System.out.println("   5. Volver al menu principal");
        ConsoleUtil.imprimirSeparador();
        System.out.print("\n   Seleccione una opcion: ");
    }

    /**
     * Muestra un resumen de paquetes por estado
     * @param resumen Mapa con estado y cantidad
     */
    public void mostrarResumenPaquetes(Map<String, Integer> resumen) {
        ConsoleUtil.imprimirTitulo("Resumen de Paquetes por Estado");

        if (resumen == null || resumen.isEmpty()) {
            ConsoleUtil.imprimirAdvertencia("No hay datos disponibles");
            ConsoleUtil.pausar();
            return;
        }

        System.out.printf("   %-25s | %-10s%n", "Estado", "Cantidad");
        ConsoleUtil.imprimirSeparador();

        resumen.forEach((estado, cantidad) ->
                System.out.printf("   %-25s | %-10d%n", estado, cantidad));

        int total = resumen.values().stream().mapToInt(Integer::intValue).sum();

        ConsoleUtil.imprimirSeparador();
        System.out.printf("   %-25s | %-10d%n", "TOTAL", total);
        ConsoleUtil.pausar();
    }

    /**
     * Muestra un reporte de entregas por conductor
     * @param entregas Mapa con conductor y lista de entregas
     */
    public void mostrarReporteEntregas(Map<String, List<String>> entregas) {
        ConsoleUtil.imprimirTitulo("Entregas por Conductor");

        if (entregas == null || entregas.isEmpty()) {
            ConsoleUtil.imprimirAdvertencia("No hay entregas en el rango de fechas");
            ConsoleUtil.pausar();
            return;
        }

        entregas.forEach((conductor, listaEntregas) -> {
            System.out.println("\n   Conductor: " + conductor);
            System.out.println("   Total de entregas: " + listaEntregas.size());
            ConsoleUtil.imprimirSeparador();

            listaEntregas.forEach(entrega -> System.out.println("      • " + entrega));
        });

        ConsoleUtil.pausar();
    }

    /**
     * Muestra el historial de rutas de un vehículo
     * @param placa Placa del vehículo
     * @param historial Lista de descripciones de rutas
     */
    public void mostrarHistorialVehiculo(String placa, List<String> historial) {
        ConsoleUtil.imprimirTitulo("Historial de Rutas - Vehiculo " + placa);

        if (historial == null || historial.isEmpty()) {
            ConsoleUtil.imprimirAdvertencia("No hay rutas registradas para este vehiculo");
            ConsoleUtil.pausar();
            return;
        }

        for (int i = 0; i < historial.size(); i++) {
            System.out.printf("   %d. %s%n", i + 1, historial.get(i));
        }

        ConsoleUtil.pausar();
    }

    /**
     * Muestra los vehículos que están actualmente en mantenimiento
     * @param vehiculos Lista de vehículos en mantenimiento
     */
    public void mostrarVehiculosEnMantenimiento(List<Vehiculo> vehiculos) {
        ConsoleUtil.imprimirTitulo("Vehiculos en Mantenimiento");

        if (vehiculos == null || vehiculos.isEmpty()) {
            ConsoleUtil.imprimirAdvertencia("No hay vehiculos en mantenimiento");
            ConsoleUtil.pausar();
            return;
        }

        System.out.printf("   %-10s | %-15s | %-15s | %-10s%n", "Placa", "Marca", "Modelo", "Capacidad");
        ConsoleUtil.imprimirSeparador();

        vehiculos.forEach(v -> System.out.printf("   %-10s | %-15s | %-15s | %-10.2f%n",
                v.getPlaca(), v.getMarca(), v.getModelo(), v.getCapacidadCarga()));

        System.out.println("\n   Total: " + vehiculos.size() + " vehiculo(s) en mantenimiento");
        ConsoleUtil.pausar();
    }

    /**
     * Muestra un mensaje informativo
     * @param mensaje Mensaje a mostrar
     */
    public void mostrarMensaje(String mensaje) {
        ConsoleUtil.imprimirInfo(mensaje);
        ConsoleUtil.pausar();
    }
}
