package rapidexpress.controlador;

import java.util.List;
import java.util.Scanner;
import rapidexpress.dominio.Mantenimiento;
import rapidexpress.dominio.Vehiculo;
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.servicio.VehiculoService;
import rapidexpress.vista.VehiculoView;

/**
 * Controlador para la gestión de vehículos desde la consola.
 *
 * @author User
 */
public class VehiculoController {

    private VehiculoView vista = new VehiculoView();
    private VehiculoService servicio = new VehiculoService();
    private final Scanner scanner;

    public VehiculoController(Scanner scanner) {
        this.scanner = scanner;
    }

    public void gestionar() {
        boolean continuar = true;

        while (continuar) {
            vista.mostrarMenuVehiculos();
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    registrarVehiculo();
                    break;
                case "2":
                    listarVehiculos();
                    break;
                case "3":
                    buscarVehiculo();
                    break;
                case "4":
                    actualizarVehiculo();
                    break;
                case "5":
                    programarMantenimiento();
                    break;
                case "6":
                    continuar = false;
                    System.out.println(" Volviendo al Menú Principal...");
                    break;
                default:
                    System.out.println("Opcion Invalida. Intente otra vez. ");
            }
        }
    }

    private void registrarVehiculo() {
        System.out.println("\n ====== REGISTRAR VEHICULO ======");
        try {
            System.out.print("Placa: ");
            String placa = scanner.nextLine();
            System.out.print("Marca: ");
            String marca = scanner.nextLine();
            System.out.print("Modelo: ");
            String modelo = scanner.nextLine();
            System.out.print("Año: ");
            int year = Integer.parseInt(scanner.nextLine());
            System.out.println("Capacidad de Carga(kg): ");
            double capacidad = Double.parseDouble(scanner.nextLine());

            Vehiculo vehiculo = new Vehiculo(placa, marca, modelo, year, capacidad);
            servicio.registrarVehiculo(vehiculo);
            System.out.println("El Vehiculo con placa " + placa + "ha sido registrado ");
        } catch (NumberFormatException e) {
            System.out.println("Error: Datos numericos invalidados");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listarVehiculos() {
        System.out.println("\n ====== LISTA DE VEHICULOS ======");
        try {
            List<Vehiculo> vehiculos = servicio.listarTodos();
            vista.mostrarListaVehiculos(vehiculos);
        } catch (DataBaseException e) {
            System.out.println("Error al listar vehículos: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    private void buscarVehiculo() {
        System.out.println("\n ====== BUSCAR VEHICULO ======");
        System.out.println("\n Ingrese la placa de su Vehiculo: ");
        String placa = scanner.nextLine();
        try {
            Vehiculo vehiculo = servicio.buscarPorPlaca(placa);
            if (vehiculo != null) {
                vista.mostrarVehiculo(vehiculo);
            } else {
                System.out.println("Vehículo no encontrado");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void actualizarVehiculo() {
        System.out.println("\n ====== ACTUALIZAR VEHICULO ======");
        System.out.println("\n Ingrese la placa del vehiculo a actualizar: ");
        String placa = scanner.nextLine();
        try {
            Vehiculo vehiculo = servicio.buscarPorPlaca(placa);
            if (vehiculo == null) {
                System.out.println("Vehículo no encontrado");
                return;
            }

            vista.mostrarVehiculo(vehiculo);
            System.out.println("\n--- Nuevos datos (deje vacío para mantener los datos actuales) ---");

            System.out.print("Marca [" + vehiculo.getMarca() + "]: ");
            String marca = scanner.nextLine();
            if (!marca.isEmpty()) {
                vehiculo.setMarca(marca);
            }

            System.out.print("Modelo [" + vehiculo.getModelo() + "]: ");
            String modelo = scanner.nextLine();
            if (!modelo.isEmpty()) {
                vehiculo.setModelo(modelo);
            }

            System.out.print("Año [" + vehiculo.getYear() + "]: ");
            String anioStr = scanner.nextLine();
            if (!anioStr.isEmpty()) {
                vehiculo.setYear(Integer.parseInt(anioStr));
            }

            System.out.print("Capacidad [" + vehiculo.getCapacidadCarga() + "]: ");
            String capStr = scanner.nextLine();
            if (!capStr.isEmpty()) {
                vehiculo.setCapacidadCarga(Double.parseDouble(capStr));
            }

            servicio.actualizarVehiculo(vehiculo);
            System.out.println("Vehículo actualizado exitosamente");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void programarMantenimiento() {
        System.out.println("\n ====== PROGRAMAR MANTENIMIENTO ======");
        System.out.print("\n Ingrese la placa del vehiculo: ");
        String placa = scanner.nextLine();
        try {
            Vehiculo vehiculo = servicio.buscarPorPlaca(placa);
            if (vehiculo == null) {
                System.out.println("Vehículo no encontrado");
                return;
            }

            System.out.print("Descripcion del mantenimiento: ");
            String descripcion = scanner.nextLine();
            System.out.print("Costo: ");
            double costo = Double.parseDouble(scanner.nextLine());

            Mantenimiento mantenimiento = new Mantenimiento();
            mantenimiento.setVehiculo(vehiculo);
            mantenimiento.setDescripcion(descripcion);
            mantenimiento.setCosto(costo);

            servicio.programarMantenimiento(placa, mantenimiento);
            System.out.println("Mantenimiento programado exitosamente");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
