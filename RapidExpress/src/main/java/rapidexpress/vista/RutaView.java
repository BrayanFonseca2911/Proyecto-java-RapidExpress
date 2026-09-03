package rapidexpress.vista;

import rapidexpress.dominio.Ruta;
import rapidexpress.dominio.Vehiculo;
import rapidexpress.dominio.Conductor;
import rapidexpress.dominio.Paquete;
import rapidexpress.util.ConsoleUtil;

import java.util.List;

/**
 * Vista para la planificación y seguimiento de rutas.
 * 
 * @author User
 */
public class RutaView {
    
    /**
     * Muestra el menú de gestión de rutas
     */
    public void mostrarMenuRutas() {
        ConsoleUtil.limpiarPantalla();
        ConsoleUtil.imprimirTitulo("PLANIFICACIÓN DE RUTAS");
        
        System.out.println("\n   1. Crear nueva ruta");
        System.out.println("   2. Iniciar ruta");
        System.out.println("   3. Monitorear rutas activas");
        System.out.println("   4. Actualizar estado de paquete en ruta");
        System.out.println("   5. Completar ruta");
        System.out.println("   6. Volver al menú principal");
        ConsoleUtil.imprimirSeparador();
        System.out.print("\n   Seleccione una opción: ");
    }
    
    /**
     * Muestra una lista de vehículos disponibles para selección
     * @param vehiculos Lista de vehículos disponibles
     */
    public void mostrarVehiculosDisponibles(List<Vehiculo> vehiculos) {
        ConsoleUtil.imprimirTitulo("Vehículos Disponibles");
        
        if (vehiculos == null || vehiculos.isEmpty()) {
            ConsoleUtil.imprimirAdvertencia("No hay vehículos disponibles");
            return;
        }
        
        for (int i = 0; i < vehiculos.size(); i++) {
            Vehiculo v = vehiculos.get(i);
            System.out.printf("   %d. %s - %s %s (%.2f kg)%n",
                i + 1, v.getPlaca(), v.getMarca(), v.getModelo(), v.getCapacidadCarga());
        }
    }
    
    /**
     * Muestra una lista de conductores disponibles para selección
     * @param conductores Lista de conductores disponibles
     */
    public void mostrarConductoresDisponibles(List<Conductor> conductores) {
        ConsoleUtil.imprimirTitulo("Conductores Disponibles");
        
        if (conductores == null || conductores.isEmpty()) {
            ConsoleUtil.imprimirAdvertencia("No hay conductores disponibles");
            return;
        }
        
        for (int i = 0; i < conductores.size(); i++) {
            Conductor c = conductores.get(i);
            System.out.printf("   %d. %s - Licencia: %s%n",
                i + 1, c.getNombre(), c.getTipoLicencia());
        }
    }
    
    /**
     * Muestra una lista de paquetes en bodega para selección
     * @param paquetes Lista de paquetes en bodega
     */
    public void mostrarPaquetesEnBodega(List<Paquete> paquetes) {
        ConsoleUtil.imprimirTitulo("Paquetes en Bodega");
        
        if (paquetes == null || paquetes.isEmpty()) {
            ConsoleUtil.imprimirAdvertencia("No hay paquetes en bodega");
            return;
        }
        
        for (int i = 0; i < paquetes.size(); i++) {
            Paquete p = paquetes.get(i);
            System.out.printf("   %d. %s - %.2f kg - %s%n",
                i + 1, p.getTrackingId(), p.getPeso(), p.getDestino());
        }
    }
    
    /**
     * Muestra una ruta con todos sus detalles
     * @param ruta Ruta a mostrar
     */
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
            System.out.println("   Vehículo:      " + ruta.getVehiculo().getPlaca());
        }
        if (ruta.getConductor() != null) {
            System.out.println("   Conductor:     " + ruta.getConductor().getNombre());
        }
        
        if (ruta.getPaquetes() != null && !ruta.getPaquetes().isEmpty()) {
            System.out.println("\n   --- Paquetes Asignados ---");
            for (Paquete p : ruta.getPaquetes()) {
                System.out.printf("      • %s (%.2f kg) - %s%n",
                    p.getTrackingId(), p.getPeso(), p.getDestino());
            }
        }
        
        ConsoleUtil.imprimirSeparador();
    }
    
    /**
     * Muestra una lista de rutas activas
     * @param rutas Lista de rutas activas
     */
    public void mostrarRutasActivas(List<Ruta> rutas) {
        if (rutas == null || rutas.isEmpty()) {
            ConsoleUtil.imprimirAdvertencia("No hay rutas activas en este momento");
            ConsoleUtil.pausar();
            return;
        }
        
        ConsoleUtil.imprimirTitulo("Rutas Activas");
        System.out.printf("   %-5s | %-10s | %-25s | %-10s | %-15s%n",
            "ID", "Vehículo", "Conductor", "Paquetes", "Peso Total");
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