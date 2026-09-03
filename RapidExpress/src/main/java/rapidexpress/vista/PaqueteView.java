package rapidexpress.vista;

import rapidexpress.dominio.Paquete;
import rapidexpress.util.ConsoleUtil;

import java.util.List;

/**
 * Vista para la gestión de paquetes.
 * 
 * @author User
 */
public class PaqueteView {
    
    /**
     * Muestra el menú de gestión de paquetes
     */
    public void mostrarMenuPaquetes() {
        ConsoleUtil.limpiarPantalla();
        ConsoleUtil.imprimirTitulo("GESTIÓN DE PAQUETES");
        
        System.out.println("\n   1. Registrar nuevo paquete");
        System.out.println("   2. Consultar paquete por tracking");
        System.out.println("   3. Listar paquetes en bodega");
        System.out.println("   4. Actualizar estado de paquete");
        System.out.println("   5. Volver al menú principal");
        ConsoleUtil.imprimirSeparador();
        System.out.print("\n   Seleccione una opción: ");
    }
    
    /**
     * Muestra un paquete con todos sus detalles
     * @param paquete Paquete a mostrar
     */
    public void mostrarPaquete(Paquete paquete) {
        if (paquete == null) {
            ConsoleUtil.imprimirError("Paquete no encontrado");
            return;
        }
        
        ConsoleUtil.imprimirTitulo("Detalles del Paquete");
        System.out.println("   Tracking ID:   " + paquete.getTrackingId());
        System.out.println("   Descripción:   " + paquete.getDescripcion());
        System.out.println("   Peso:          " + paquete.getPeso() + " kg");
        System.out.println("   Dimensiones:   " + paquete.getDimensiones());
        System.out.println("   Origen:        " + paquete.getOrigen());
        System.out.println("   Destino:       " + paquete.getDestino());
        System.out.println("   Estado:        " + paquete.getEstado().getDescripcion());
        
        if (paquete.getRemitente() != null) {
            System.out.println("\n   --- Remitente ---");
            System.out.println("   Nombre:        " + paquete.getRemitente().getNombre());
            System.out.println("   Dirección:     " + paquete.getRemitente().getDireccion());
            System.out.println("   Teléfono:      " + paquete.getRemitente().getTelefono());
        }
        
        if (paquete.getDestinatario() != null) {
            System.out.println("\n   --- Destinatario ---");
            System.out.println("   Nombre:        " + paquete.getDestinatario().getNombre());
            System.out.println("   Dirección:     " + paquete.getDestinatario().getDireccion());
            System.out.println("   Teléfono:      " + paquete.getDestinatario().getTelefono());
        }
        
        ConsoleUtil.imprimirSeparador();
    }
    
    /**
     * Muestra una lista de paquetes en formato tabla
     * @param paquetes Lista de paquetes a mostrar
     */
    public void mostrarListaPaquetes(List<Paquete> paquetes) {
        if (paquetes == null || paquetes.isEmpty()) {
            ConsoleUtil.imprimirAdvertencia("No hay paquetes registrados");
            return;
        }
        
        ConsoleUtil.imprimirTitulo("Lista de Paquetes");
        System.out.printf("   %-15s | %-25s | %-8s | %-20s | %-15s%n",
            "Tracking", "Descripción", "Peso", "Destino", "Estado");
        ConsoleUtil.imprimirSeparador();
        
        for (Paquete p : paquetes) {
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
        }
        
        System.out.println("\n   Total: " + paquetes.size() + " paquete(s)");
        ConsoleUtil.pausar();
    }
}