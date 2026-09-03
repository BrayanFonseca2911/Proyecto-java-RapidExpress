package rapidexpress.controlador;

import java.util.List;
import java.util.Scanner;
import rapidexpress.dominio.Conductor;
import rapidexpress.enums.EstadoConductor;
import rapidexpress.servicio.ConductorService;
import rapidexpress.vista.ConductorView;

/**
 * Controlador para la gestión de conductores desde la consola.
 *
 * @author User
 */
public class ConductorController {

    private ConductorView vista = new ConductorView();
    private ConductorService servicio = new ConductorService();

    public void gestionar() {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            vista.mostrarMenuConductores();
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    registrarConductor(scanner);
                    break;
                case "2":
                    listarConductores();
                    break;
                case "3":
                    buscarConductor(scanner);
                    break;
                case "4":
                    asignarVehiculo(scanner);
                    break;
                case "5":
                    liberarVehiculo(scanner);
                    break;
                case "6":
                    continuar = false;
                    System.out.println("\n Volviendo al Menú Principal...");
                    break;
                default:
                    System.out.println("Opcion Incorrecta");
            }
        }
    }

    private void registrarConductor(Scanner scanner) {
        System.out.println("\n ====== REGISTRAR CONDUCTOR ======");
        try {
            System.out.print("Numero de indentificación: ");
            String numeroId = scanner.nextLine();
            System.out.print("Nombre Completo: ");
            String nombre = scanner.nextLine();
            System.out.print("Tipo de licencia: ");
            String tipoLicencia = scanner.nextLine();
            System.out.print("Telefono: ");
            String contacto = scanner.nextLine();

            Conductor conductor = new Conductor(numeroId, nombre, tipoLicencia, contacto);
            conductor.setEstado(EstadoConductor.ACTIVO);
            servicio.registrarConductor(conductor);

            System.out.println("Conductor registrado exitosamente");
            System.out.println("Numero ID: " + numeroId + "-" + nombre);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listarConductores() {
        System.out.println("\n ====== LISTAR CONDUCTORES ======");
        try {
            List<Conductor> conductores = servicio.listarTodos();
            vista.mostrarListaConductores(conductores);
        } catch (Exception e) {
            System.out.println("Error al listar conductores: " + e.getMessage());
        }
    }

    private void buscarConductor(Scanner scanner) {
        System.out.println("\n ====== BUSCAR CONDUCTOR ======");
        System.out.println("\n Numero de identificacion: ");
        String numeroId = scanner.nextLine();
        try {
            Conductor conductor = servicio.buscarPorNumeroIdentificacion(numeroId);
            if (conductor != null) {
                vista.mostrarConductor(conductor);
            } else {
                System.out.println("Conductor no encontrado");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void asignarVehiculo(Scanner scanner) {
        System.out.println("\n ====== ASIGNAR VEHICULO ======");
        System.out.println("Numero de Identificacion: ");
        String numeroId = scanner.nextLine();
        System.out.println("Placa del Vehiculo: ");
        String placa = scanner.nextLine();
        try {
            servicio.asignarVehiculo(numeroId, placa);
            System.out.println("El vehico con la placa " + placa + " es asignado al conductor" + numeroId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void liberarVehiculo(Scanner scanner) {
        System.out.println("\n Numero de identificacion del conductor: ");
        String numeroId = scanner.nextLine();
        try {
            servicio.liberarVehiculo(numeroId);
            System.out.println("Vehiculo liberado al conductor " + numeroId);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
