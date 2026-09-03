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
        ConsoleUtil.imprimirTitulo("GESTION DE VEHICULOS");
        System.out.println("\n   1. Registrar nuevo vehiculo");
        System.out.println("   2. Listar todos los vehiculos");
        System.out.println("   3. Buscar vehiculo por placa");
        System.out.println("   4. Actualizar vehiculo");
        System.out.println("   5. Programar mantenimiento");
        System.out.println("   6. Volver al menu principal");
        ConsoleUtil.imprimirSeparador();
        System.out.print("\n   Seleccione una opcion: ");
    }

    public void mostrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            ConsoleUtil.imprimirError("Vehiculo no encontrado");
            return;
        }
        ConsoleUtil.imprimirTitulo("Detalles del Vehiculo");
        System.out.println("   Placa:       " + vehiculo.getPlaca());
        System.out.println("   Marca:       " + vehiculo.getMarca());
        System.out.println("   Modelo:      " + vehiculo.getModelo());
        System.out.println("   Ano:         " + vehiculo.getYear());
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
            ConsoleUtil.imprimirAdvertencia("No hay vehiculos registrados");
            return;
        }

        ConsoleUtil.imprimirTitulo("Lista de Vehiculos");
        System.out.printf("   %-10s | %-15s | %-15s | %-6s | %-10s | %-15s%n",
            "Placa", "Marca", "Modelo", "Ano", "Capacidad", "Estado");
        ConsoleUtil.imprimirSeparador();

        vehiculos.forEach(v -> System.out.printf("   %-10s | %-15s | %-15s | %-6d | %-10.2f | %-15s%n",
                v.getPlaca(),
                v.getMarca(),
                v.getModelo(),
                v.getYear(),
                v.getCapacidadCarga(),
                v.getEstado().getDescripcion()));

        System.out.println("\n   Total: " + vehiculos.size() + " vehiculo(s)");
        ConsoleUtil.pausar();
    }
}