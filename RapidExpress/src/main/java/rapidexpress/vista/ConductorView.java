package rapidexpress.vista;

import rapidexpress.dominio.Conductor;
import rapidexpress.util.ConsoleUtil;

import java.util.List;

/**
 * Vista para la gestión de conductores.
 * 
 * @author User
 */
public class ConductorView {
    
    /**
     * Muestra el menú de gestión de conductores
     */
    public void mostrarMenuConductores() {
        ConsoleUtil.limpiarPantalla();
        ConsoleUtil.imprimirTitulo("GESTIÓN DE CONDUCTORES");
        
        System.out.println("\n   1. Registrar nuevo conductor");
        System.out.println("   2. Listar todos los conductores");
        System.out.println("   3. Buscar conductor por identificación");
        System.out.println("   4. Asignar vehículo");
        System.out.println("   5. Liberar vehículo");
        System.out.println("   6. Volver al menú principal");
        ConsoleUtil.imprimirSeparador();
        System.out.print("\n   Seleccione una opción: ");
    }
    
    /**
     * Muestra un conductor con todos sus detalles
     * @param conductor Conductor a mostrar
     */
    public void mostrarConductor(Conductor conductor) {
        if (conductor == null) {
            ConsoleUtil.imprimirError("Conductor no encontrado");
            return;
        }
        
        ConsoleUtil.imprimirTitulo("Detalles del Conductor");
        System.out.println("   ID:              " + conductor.getId());
        System.out.println("   Identificación:  " + conductor.getNumeroIdentificacion());
        System.out.println("   Nombre:          " + conductor.getNombre());
        System.out.println("   Licencia:        " + conductor.getTipoLicencia());
        System.out.println("   Contacto:        " + conductor.getContacto());
        System.out.println("   Estado:          " + conductor.getEstado().getDescripcion());
        
        if (conductor.getVehiculoAsignado() != null) {
            System.out.println("   Vehículo:        " + conductor.getVehiculoAsignado().getPlaca());
        } else {
            System.out.println("   Vehículo:        Sin asignar");
        }
        ConsoleUtil.imprimirSeparador();
    }
    
    /**
     * Muestra una lista de conductores en formato tabla
     * @param conductores Lista de conductores a mostrar
     */
    public void mostrarListaConductores(List<Conductor> conductores) {
        if (conductores == null || conductores.isEmpty()) {
            ConsoleUtil.imprimirAdvertencia("No hay conductores registrados");
            return;
        }
        
        ConsoleUtil.imprimirTitulo("Lista de Conductores");
        System.out.printf("   %-5s | %-15s | %-25s | %-10s | %-15s%n",
            "ID", "Identificación", "Nombre", "Licencia", "Estado");
        ConsoleUtil.imprimirSeparador();
        
        for (Conductor c : conductores) {
            System.out.printf("   %-5d | %-15s | %-25s | %-10s | %-15s%n",
                c.getId(),
                c.getNumeroIdentificacion(),
                c.getNombre(),
                c.getTipoLicencia(),
                c.getEstado().getDescripcion());
        }
        
        System.out.println("\n   Total: " + conductores.size() + " conductor(es)");
        ConsoleUtil.pausar();
    }
}