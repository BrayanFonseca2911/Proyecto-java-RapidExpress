package rapidexpress.controlador;

import java.util.List;
import java.util.Scanner;
import rapidexpress.dominio.Destinatario;
import rapidexpress.dominio.Paquete;
import rapidexpress.dominio.Remitente;
import rapidexpress.enums.EstadoPaquete;
import rapidexpress.servicio.PaqueteService;
import rapidexpress.vista.PaqueteView;

/**
 * Controlador para la gestión de paquetes desde la consola.
 *
 * @author User
 */
public class PaqueteController {

    private PaqueteView vista = new PaqueteView();
    private PaqueteService servicio = new PaqueteService();
    private final Scanner scanner;

    public PaqueteController(Scanner scanner) {
        this.scanner = scanner;
    }

    public void gestionar() {
        boolean continuar = true;

        while (continuar) {
            vista.mostrarMenuPaquetes();
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    registrarPaquete();
                    break;
                case "2":
                    consultarPaquete();
                    break;
                case "3":
                    listarEnBodega();
                    break;
                case "4":
                    actualizarEstado();
                    break;
                case "5":
                    continuar = false;
                    System.out.println("Volviendo al Menu Principal...");
                    break;
                default:
                    System.out.println("Opcion Inválida");
            }
        }
    }

    private void registrarPaquete() {
        System.out.println("\n ====== REGISTRAR PAQUETE ======");
        try {
            System.out.print("Descripcion del contenido: ");
            String descripcion = scanner.nextLine();
            System.out.print("Peso(kg): ");
            double peso = Double.parseDouble(scanner.nextLine());
            System.out.print("Dimensiones (ej: 30x20x15):  ");
            String dimensiones = scanner.nextLine();
            System.out.print("Direccion de origen: ");
            String origen = scanner.nextLine();
            System.out.print("Direccion de destino: ");
            String destino = scanner.nextLine();

            System.out.println("\n ------ Datos del Reminente ------");
            System.out.print("Nombre: ");
            String nombreRemitente = scanner.nextLine();
            System.out.print("Telefono: ");
            String telRemitente = scanner.nextLine();
            Remitente remitente = new Remitente(nombreRemitente, origen, telRemitente);

            System.out.println("\n ------ Datos del Destinatario ------");
            System.out.print("Nombre: ");
            String nombreDestinatario = scanner.nextLine();
            System.out.println("Telefono: ");
            String telDestinatario = scanner.nextLine();
            Destinatario destinatario = new Destinatario(nombreDestinatario, destino, telDestinatario);

            Paquete paquete = new Paquete(descripcion, peso, dimensiones, origen, destino, remitente, destinatario);
            servicio.registrarPaquete(paquete);

            System.out.println("\n Paquete registrado exitosamente");
            System.out.println(" Tracking ID: " + paquete.getTrackingId());
        } catch (NumberFormatException e) {
            System.out.println("Error: Datos numericos invalidos. ");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void consultarPaquete() {
        System.out.println("\n ====== CONSULTAR PAQUETES ======");
        System.out.println("\n Ingrese el tracking ID: ");
        String trackingId = scanner.nextLine();
        try {
            Paquete paquete = servicio.buscarPorTracking(trackingId);
            if (paquete != null) {
                vista.mostrarPaquete(paquete);
            } else {
                System.out.println("Paquete no encontrado");
            }
        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    private void listarEnBodega() {
        System.out.println("\n ====== PAQUETES EN BODEGA ======");
        try {
            List<Paquete> paquetes = servicio.listarEnBodega();
            vista.mostrarListaPaquetes(paquetes);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void actualizarEstado() {
        System.out.println("\n ====== ACTUALIZAR ESTADO ======");
        System.out.println("\n Ingrese el tracking ID: ");
        String trackingId = scanner.nextLine();

        System.out.println("\nSeleccione el nuevo estado:");
        System.out.println("1. En Bodega");
        System.out.println("2. Asignado a Ruta");
        System.out.println("3. En Tránsito");
        System.out.println("4. Entregado");
        System.out.println("5. Devuelto");
        System.out.print("Opción: ");
        int opcion = Integer.parseInt(scanner.nextLine());

        EstadoPaquete estado;
        switch (opcion) {
            case 1:
                estado = EstadoPaquete.EN_BODEGA;
                break;
            case 2:
                estado = EstadoPaquete.ASIGNADO_A_RUTA;
                break;
            case 3:
                estado = EstadoPaquete.EN_TRANSITO;
                break;
            case 4:
                estado = EstadoPaquete.ENTREGADO;
                break;
            case 5:
                estado = EstadoPaquete.DEVUELTO;
                break;
            default:
                System.out.println("Opción inválida");
                return;
        }

        try {
            servicio.actualizarEstado(trackingId, estado);
            System.out.println("Estado del paquete actualiazado a: " + estado);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
