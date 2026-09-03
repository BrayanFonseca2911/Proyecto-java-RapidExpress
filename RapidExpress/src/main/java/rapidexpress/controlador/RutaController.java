package rapidexpress.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import rapidexpress.dominio.Ruta;
import rapidexpress.enums.EstadoPaquete;
import rapidexpress.servicio.RutaService;
import rapidexpress.vista.RutaView;

/**
 * Controlador para la planificación y seguimiento de rutas desde la consola.
 *
 * @author User
 */
public class RutaController {

    private RutaView vista = new RutaView();
    private RutaService servicio = new RutaService();
    private final Scanner scanner;

    public RutaController(Scanner scanner) {
        this.scanner = scanner;
    }

    public void gestionar() {
        boolean continuar = true;

        while (continuar) {
            vista.mostrarMenuRutas();
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    crearRuta();
                    break;
                case "2":
                    iniciarRuta();
                    break;
                case "3":
                    monitorearRutas();
                    break;
                case "4":
                    actualizarPaqueteEnRuta();
                    break;
                case "5":
                    completarRuta();
                    break;
                case "6":
                    continuar = false;
                    System.out.println("Volviendo al Menu Principal...");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        }
    }

    private void crearRuta() {
        System.out.println("\n ====== CREANDO UNA NUEVA RUTA ======");
        try {
            System.out.println("ID del vehiculo: ");
            int vehiculoId = Integer.parseInt(scanner.nextLine());
            System.out.println("ID del conductor: ");
            int conductorId = Integer.parseInt(scanner.nextLine());
            System.out.println("Cantidad de paquetes: ");
            int cantidad = Integer.parseInt(scanner.nextLine());

            List<Integer> paqueteIds = new ArrayList<>();
            for (int i = 0; i < cantidad; i++) {
                System.out.println("Id del paquete " + (i + 1) + ": ");
                int id = Integer.parseInt(scanner.nextLine());
                paqueteIds.add(id);
            }

            Integer rutaId = servicio.crearRuta(vehiculoId, conductorId, paqueteIds);
            System.out.println("Ruta creada exitosamente");
            System.out.println("ID de ruta: " + rutaId);
        } catch (NumberFormatException e) {
            System.out.println("Error: Datos numericos invalidados. ");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void iniciarRuta() {
        System.out.println("\n ====== INICIAR RUTA ======");
        System.out.println("\n Ingrese el ID de la ruta a iniciar: ");
        int rutaId = Integer.parseInt(scanner.nextLine());
        try {
            servicio.iniciarRuta(rutaId);
            System.out.println("Ruta " + rutaId + " Iniciada exitosamente");
            System.out.println("El Vehiculo y el conductor ahora estan EN RUTA");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private void monitorearRutas() {
        System.out.println("\n ====== MONITOREAR RUTAS ======");
        try {
            List<Ruta> rutas = servicio.monitorearRutasActivas();
            vista.mostrarRutasActivas(rutas);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void actualizarPaqueteEnRuta() {
        System.out.println("\n ====== ACTUALIZAR PAQUETE EN RUTA ======");
        System.out.println("\n ID de la ruta: ");
        int rutaId = Integer.parseInt(scanner.nextLine());
        System.out.println("Tracking ID  del paquete: ");
        String trackingId = scanner.nextLine();

        System.out.println("\nNuevo estado del paquete:");
        System.out.println("1. En Transito");
        System.out.println("2. Entregado");
        System.out.print("Opcion: ");
        int opcion = Integer.parseInt(scanner.nextLine());

        try {
            if (opcion == 1) {
                servicio.actualizarPaqueteEnRuta(rutaId, trackingId, EstadoPaquete.EN_TRANSITO);
            } else if (opcion == 2) {
                servicio.actualizarPaqueteEnRuta(rutaId, trackingId, EstadoPaquete.ENTREGADO);
            }
            System.out.println("Paquete Actualizado en Ruta");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void completarRuta() {
        System.out.println("\n ====== COMPLETAR RUTA ======");
        System.out.println("\nIngrese el ID de la ruta a completar: ");
        int rutaId = Integer.parseInt(scanner.nextLine());
        try {
            servicio.completarRuta(rutaId);
            System.out.println("Ruta " + rutaId + "completada exitosamente");
            System.out.println("Vehiculo y conductor liberados");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
