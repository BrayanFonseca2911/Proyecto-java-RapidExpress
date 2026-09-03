package rapidexpress.vista;

import java.util.List;
import rapidexpress.dominio.Vehiculo;
import rapidexpress.util.ConsoleUtil;

/**
 * Vista para la gestión de vehículos en la consola.
 *
 * @author User
 */
public class VehiculoView {

    public void mostrarMenuVehiculos() {
        ConsoleUtil.limpiarPantalla();
        ConsoleUtil.imprimirTitulo("GESTIÓN DE VEHÍCULOS");
        System.out.println("\n   1. Registrar nuevo vehículo");
        System.out.println("   2. Listar todos los vehículos");
        System.out.println("   3. Buscar vehículo por placa");
        System.out.println("   4. Actualizar vehículo");
        System.out.println("   5. Programar mantenimiento");
        System.out.println("   6. Volver al menú principal");
        ConsoleUtil.imprimirSeparador();
        System.out.print("\n   Seleccione una opción: ");
    }

    public void mostrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            ConsoleUtil.imprimirError("Vehículo no encontrado");
            return;
        }
        ConsoleUtil.imprimirTitulo("Detalles del Vehículo");
        System.out.println("   Placa:       " + vehiculo.getPlaca());
        System.out.println("   Marca:       " + vehiculo.getMarca());
        System.out.println("   Modelo:      " + vehiculo.getModelo());
        System.out.println("   Año:         " + vehiculo.getYear());
        System.out.println("   Capacidad:   " + vehiculo.getCapacidadCarga() + " kg");
        System.out.println("   Estado:      " + vehiculo.getEstado().getDescripcion());
        ConsoleUtil.imprimirSeparador();
    }

    /**
     * Muestra la lista de vehículos en formato tabular
     * @param vehiculos Lista de vehículos a mostrar
     */
    public void mostrarListaVehiculos(List<Vehiculo> vehiculos) {
        if (vehiculos == null || vehiculos.isEmpty()) {
            ConsoleUtil.imprimirAdvertencia("No hay vehículos registrados");
            return;
        }

        ConsoleUtil.imprimirTitulo("Lista de Vehículos");
        System.out.printf("   %-10s | %-15s | %-15s | %-6s | %-10s | %-15s%n",
            "Placa", "Marca", "Modelo", "Año", "Capacidad", "Estado");
        ConsoleUtil.imprimirSeparador();

        vehiculos.forEach(v -> System.out.printf("   %-10s | %-15s | %-15s | %-6d | %-10.2f | %-15s%n",
                v.getPlaca(),
                v.getMarca(),
                v.getModelo(),
                v.getYear(),
                v.getCapacidadCarga(),
                v.getEstado().getDescripcion()));

        System.out.println("\n   Total: " + vehiculos.size() + " vehículo(s)");
        ConsoleUtil.pausar();
    }
}
