package rapidexpress.vista;

import java.util.List;
import rapidexpress.dominio.Paquete;
import rapidexpress.util.ConsoleUtil;

/**
 * Vista para la gestión de paquetes en la consola.
 *
 * @author User
 */
public class PaqueteView {

    public void mostrarMenuPaquetes() {
        ConsoleUtil.limpiarPantalla();
        ConsoleUtil.imprimirTitulo("GESTION DE PAQUETES");
        System.out.println("\n   1. Registrar nuevo paquete");
        System.out.println("   2. Consultar paquete por tracking");
        System.out.println("   3. Listar paquetes en bodega");
        System.out.println("   4. Actualizar estado de paquete");
        System.out.println("   5. Volver al menu principal");
        ConsoleUtil.imprimirSeparador();
        System.out.print("\n   Seleccione una opcion: ");
    }

    public void mostrarPaquete(Paquete paquete) {
        if (paquete == null) {
            ConsoleUtil.imprimirError("Paquete no encontrado");
            return;
        }
        ConsoleUtil.imprimirTitulo("Detalles del Paquete");
        System.out.println("   Tracking ID:   " + paquete.getTrackingId());
        System.out.println("   Descripcion:   " + paquete.getDescripcion());
        System.out.println("   Peso:          " + paquete.getPeso() + " kg");
        System.out.println("   Dimensiones:   " + paquete.getDimensiones());
        System.out.println("   Origen:        " + paquete.getOrigen());
        System.out.println("   Destino:       " + paquete.getDestino());
        System.out.println("   Estado:        " + paquete.getEstado().getDescripcion());

        if (paquete.getRemitente() != null) {
            System.out.println("\n   --- Remitente ---");
            System.out.println("   Nombre:        " + paquete.getRemitente().getNombre());
            System.out.println("   Direccion:     " + paquete.getRemitente().getDireccion());
            System.out.println("   Telefono:      " + paquete.getRemitente().getTelefono());
        }

        if (paquete.getDestinatario() != null) {
            System.out.println("\n   --- Destinatario ---");
            System.out.println("   Nombre:        " + paquete.getDestinatario().getNombre());
            System.out.println("   Direccion:     " + paquete.getDestinatario().getDireccion());
            System.out.println("   Telefono:      " + paquete.getDestinatario().getTelefono());
        }

        ConsoleUtil.imprimirSeparador();
    }

    /**
     * Muestra la lista de paquetes en formato tabular
     * @param paquetes Lista de paquetes a mostrar
     */
    public void mostrarListaPaquetes(List<Paquete> paquetes) {
        if (paquetes == null || paquetes.isEmpty()) {
            ConsoleUtil.imprimirAdvertencia("No hay paquetes registrados");
            return;
        }

        ConsoleUtil.imprimirTitulo("Lista de Paquetes");
        System.out.printf("   %-15s | %-25s | %-8s | %-20s | %-15s%n",
            "Tracking", "Descripcion", "Peso", "Destino", "Estado");
        ConsoleUtil.imprimirSeparador();

        paquetes.forEach(p -> {
            String descripcion = p.getDescripcion();
            if (descripcion.length() > 25) {
                descripcion = descripcion.substring(0, 22) + "...";
            }

            System.out.printf("   %-15s | %-25s | %-8.2f | %-20s | %-15s%n",
                p.getTrackingId(),
                descripcion,
                p.getPeso(),
                p.getDestino(),
                p.getEstado().getDescripcion());
        });

        System.out.println("\n   Total: " + paquetes.size() + " paquete(s)");
        ConsoleUtil.pausar();
    }
}
