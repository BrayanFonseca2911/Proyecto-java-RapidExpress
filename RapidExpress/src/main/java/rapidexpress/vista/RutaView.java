package rapidexpress.vista;

import java.util.List;
import rapidexpress.dominio.Conductor;
import rapidexpress.dominio.Paquete;
import rapidexpress.dominio.Ruta;
import rapidexpress.dominio.Vehiculo;
import rapidexpress.util.ConsoleUtil;

/**
 * Vista para la planificación y seguimiento de rutas.
 *
 * @author User
 */
public class RutaView {

    public void mostrarMenuRutas() {
        ConsoleUtil.limpiarPantalla();
        ConsoleUtil.imprimirTitulo("PLANIFICACION DE RUTAS");
        System.out.println("\n   1. Crear nueva ruta");
        System.out.println("   2. Iniciar ruta");
        System.out.println("   3. Monitorear rutas activas");
        System.out.println("   4. Actualizar estado de paquete en ruta");
        System.out.println("   5. Completar ruta");
        System.out.println("   6. Volver al menu principal");
        ConsoleUtil.imprimirSeparador();
        System.out.print("\n   Seleccione una opcion: ");
    }

    public void mostrarVehiculosDisponibles(List<Vehiculo> vehiculos) {
        ConsoleUtil.imprimirTitulo("Vehiculos Disponibles");
        if (vehiculos == null || vehiculos.isEmpty()) {
            ConsoleUtil.imprimirAdvertencia("No hay vehiculos disponibles");
            return;
        }
        for (int i = 0; i < vehiculos.size(); i++) {
            Vehiculo v = vehiculos.get(i);
            System.out.printf("   %d. %s - %s %s (%.2f kg)%n",
                    i + 1, v.getPlaca(), v.getMarca(), v.getModelo(), v.getCapacidadCarga());
        }
    }

    public void mostrarConductoresDisponibles(List<Conductor> conductores) {
        ConsoleUtil.imprimirTitulo("Conductores Disponibles");
        if (conductores == null || conductores.isEmpty()) {
            ConsoleUtil.imprimirAdvertencia("No hay conductores disponibles");
            return;
        }
        for (int i = 0; i < conductores.size(); i++) {
            Conductor c = conductores.get(i);
            System.out.printf("   %d. %s - Licencia: %s%n", i + 1, c.getNombre(), c.getTipoLicencia());
        }
    }

    public void mostrarPaquetesEnBodega(List<Paquete> paquetes) {
        ConsoleUtil.imprimirTitulo("Paquetes en Bodega");
        if (paquetes == null || paquetes.isEmpty()) {
            ConsoleUtil.imprimirAdvertencia("No hay paquetes en bodega");
            return;
        }
        for (int i = 0; i < paquetes.size(); i++) {
            Paquete p = paquetes.get(i);
            System.out.printf("   %d. %s - %.2f kg - %s%n", i + 1, p.getTrackingId(), p.getPeso(), p.getDestino());
        }
    }

    public void mostrarRuta(Ruta ruta) {
        if (ruta == null) {
            ConsoleUtil.imprimirError("Ruta no encontrada");
            return;
        }
        ConsoleUtil.imprimirTitulo("Detalles de la Ruta");
        System.out.println("   ID Ruta:       " + ruta.getId());
        System.out.println("   Fecha:         " + ruta.getFecha());
        System.out.println("   Estado:        " + ruta.getEstado().getDescripcion());
        System.out.println("   Peso Total:    " + ruta.getPesoTotal() + " kg");

        if (ruta.getVehiculo() != null) {
            System.out.println("   Vehiculo:      " + ruta.getVehiculo().getPlaca());
        }
        if (ruta.getConductor() != null) {
            System.out.println("   Conductor:     " + ruta.getConductor().getNombre());
        }

        if (ruta.getPaquetes() != null && !ruta.getPaquetes().isEmpty()) {
            System.out.println("\n   --- Paquetes Asignados ---");
            for (Paquete p : ruta.getPaquetes()) {
                System.out.printf("      • %s (%.2f kg) - %s%n", p.getTrackingId(), p.getPeso(), p.getDestino());
            }
        }
        ConsoleUtil.imprimirSeparador();
    }

    public void mostrarRutasActivas(List<Ruta> rutas) {
        if (rutas == null || rutas.isEmpty()) {
            ConsoleUtil.imprimirAdvertencia("No hay rutas activas en este momento");
            ConsoleUtil.pausar();
            return;
        }

        ConsoleUtil.imprimirTitulo("Rutas Activas");
        System.out.printf("   %-5s | %-10s | %-25s | %-10s | %-15s%n",
                "ID", "Vehiculo", "Conductor", "Paquetes", "Peso Total");
        ConsoleUtil.imprimirSeparador();

        for (Ruta r : rutas) {
            System.out.printf("   %-5d | %-10s | %-25s | %-10d | %-15.2f%n",
                    r.getId(),
                    r.getVehiculo() != null ? r.getVehiculo().getPlaca() : "N/A",
                    r.getConductor() != null ? r.getConductor().getNombre() : "N/A",
                    r.getPaquetes() != null ? r.getPaquetes().size() : 0,
                    r.getPesoTotal());
        }
        ConsoleUtil.pausar();
    }
}
