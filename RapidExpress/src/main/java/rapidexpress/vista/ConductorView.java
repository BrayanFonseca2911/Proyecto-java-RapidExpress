package rapidexpress.vista;

import java.util.List;
import rapidexpress.dominio.Conductor;
import rapidexpress.util.ConsoleUtil;

/**
 * Vista para la gestión de conductores en la consola.
 *
 * @author User
 */
public class ConductorView {

    public void mostrarMenuConductores() {
        ConsoleUtil.limpiarPantalla();
        ConsoleUtil.imprimirTitulo("GESTION DE CONDUCTORES");
        System.out.println("\n   1. Registrar nuevo conductor");
        System.out.println("   2. Listar todos los conductores");
        System.out.println("   3. Buscar conductor por identificacion");
        System.out.println("   4. Asignar vehiculo");
        System.out.println("   5. Liberar vehiculo");
        System.out.println("   6. Volver al menu principal");
        ConsoleUtil.imprimirSeparador();
        System.out.print("\n   Seleccione una opcion: ");
    }

    public void mostrarConductor(Conductor conductor) {
        if (conductor == null) {
            ConsoleUtil.imprimirError("Conductor no encontrado");
            return;
        }
        ConsoleUtil.imprimirTitulo("Detalles del Conductor");
        System.out.println("   ID:              " + conductor.getId());
        System.out.println("   Identificacion:  " + conductor.getNumeroIdentificacion());
        System.out.println("   Nombre:          " + conductor.getNombre());
        System.out.println("   Licencia:        " + conductor.getTipoLicencia());
        System.out.println("   Contacto:        " + conductor.getContacto());
        System.out.println("   Estado:          " + conductor.getEstado().getDescripcion());
        if (conductor.getVehiculoAsignado() != null) {
            System.out.println("   Vehiculo:        " + conductor.getVehiculoAsignado().getPlaca());
        } else {
            System.out.println("   Vehiculo:        Sin asignar");
        }
        ConsoleUtil.imprimirSeparador();
    }

    /**
     * Muestra la lista de conductores en formato tabular
     * @param conductores Lista de conductores a mostrar
     */
    public void mostrarListaConductores(List<Conductor> conductores) {
        if (conductores == null || conductores.isEmpty()) {
            ConsoleUtil.imprimirAdvertencia("No hay conductores registrados");
            return;
        }

        ConsoleUtil.imprimirTitulo("Lista de Conductores");
        System.out.printf("   %-5s | %-15s | %-25s | %-10s | %-15s%n",
            "ID", "Identificacion", "Nombre", "Licencia", "Estado");
        ConsoleUtil.imprimirSeparador();

        conductores.forEach(c -> System.out.printf("   %-5d | %-15s | %-25s | %-10s | %-15s%n",
                c.getId(),
                c.getNumeroIdentificacion(),
                c.getNombre(),
                c.getTipoLicencia(),
                c.getEstado().getDescripcion()));

        System.out.println("\n   Total: " + conductores.size() + " conductor(es)");
        ConsoleUtil.pausar();
    }
}
