/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.vista;

// Importaciones de colecciones de Java y herramientas de lectura.
import java.util.List;
import java.util.Scanner;

// Importaciones del modelo de dominio, enumeradores, excepciones y servicios.
import rapidexpress.dominio.Vehiculo;
import rapidexpress.enums.EstadoVehiculo;
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.servicio.MantenimientoService;
import rapidexpress.servicio.VehiculoService;

/**
 * Propósito: Interfaz de usuario por consola para la gestión interactiva de vehículos 
 * en el sistema RapidExpress (registro, búsquedas, actualización, cambios de estado e historial).
 * 
 * @author User
 * @version 1.0
 */
public class VehiculoView {

    // Atributos de clase para lectura de datos y servicios asociados.
    private final Scanner scanner;
    private final VehiculoService vehiculoService;
    private final MantenimientoService mantenimientoService;

    /**
     * Constructor predeterminado que inicializa el escáner y los servicios requeridos.
     */
    public VehiculoView() {
        // Inicializa la lectura por teclado.
        this.scanner = new Scanner(System.in);
        // Inicializa el servicio principal de vehículos.
        this.vehiculoService = new VehiculoService();
        // Inicializa el servicio de mantenimientos para la consulta del historial.
        this.mantenimientoService = new MantenimientoService();
    }

    /**
     * Muestra el menú de opciones para vehículos y procesa la navegación del usuario.
     */
    public void mostrarMenu() {
        boolean continuar = true;

        while (continuar) {
            // Imprime las opciones del módulo de vehículos.
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║          GESTIÓN DE VEHÍCULOS          ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. Registrar nuevo vehículo");
            System.out.println("2. Buscar vehículo por placa");
            System.out.println("3. Listar todos los vehículos");
            System.out.println("4. Ver historial de mantenimiento de un vehículo");
            System.out.println("5. Cambiar estado de un vehículo");
            System.out.println("6. Volver al Menú Principal");
            System.out.print("\nSeleccione una opción: ");

            // Lee la opción igresada.
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    // Procesa el registro de un vehículo.
                    registrarVehiculo();
                    break;
                case "2":
                    // Consulta un vehículo por su placa.
                    buscarVehiculo();
                    break;
                case "3":
                    // Muestra el inventario total de vehículos.
                    listarVehiculos();
                    break;
                case "4":
                    // Consulta los mantenimientos previos del vehículo.
                    consultarHistorialMantenimiento();
                    break;
                case "5":
                    // Cambia el estado operativo del vehículo.
                    cambiarEstadoVehiculo();
                    break;
                case "6":
                    // Sale del menú y retorna a la vista principal.
                    continuar = false;
                    break;
                default:
                    mostrarMensajeError("Opción no válida. Intente nuevamente.");
            }
        }
    }

    /**
     * Captura los atributos del vehículo desde la entrada estándar.
     * 
     * @return Instancia del objeto Vehiculo con la información diligenciada.
     */
    public Vehiculo solicitarDatosVehiculo() {
        // Crea una instancia del dominio Vehiculo.
        Vehiculo vehiculo = new Vehiculo();

        // Pide y asigna la placa en mayúsculas.
        vehiculo.setPlaca(solicitarPlaca());

        // Captura la marca.
        System.out.print("Ingrese la marca del vehículo: ");
        vehiculo.setMarca(scanner.nextLine().trim());

        // Captura el modelo o línea.
        System.out.print("Ingrese el modelo / línea: ");
        vehiculo.setModelo(scanner.nextLine().trim());

        // Captura el año de fabricación con validación numérica simple.
        int anio = 2024;
        try {
            System.out.print("Ingrese el año de fabricación (ej. 2022): ");
            anio = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Formato de año no válido. Se asignará por defecto 2024.");
        }
        vehiculo.setAnio(anio);

        // Captura la capacidad de carga en kilogramos.
        double capacidad = 1000.0;
        try {
            System.out.print("Ingrese capacidad de carga en kg (ej. 3500.0): ");
            capacidad = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Formato de capacidad no válido. Se asignará por defecto 1000.0 kg.");
        }
        vehiculo.setCapacidadCarga(capacidad);

        // Define el estado inicial del vehículo como DISPONIBLE por defecto.
        vehiculo.setEstado(EstadoVehiculo.DISPONIBLE);

        return vehiculo;
    }

    /**
     * Solicita al usuario el número de placa e ingresa la respuesta en formato mayúscula.
     * 
     * @return Cadena formateada de la placa.
     */
    public String solicitarPlaca() {
        // Imprime la etiqueta y lee la placa asegurando mayúsculas sostenidas.
        System.out.print("Placa: ");
        return scanner.nextLine().trim().toUpperCase();
    }

    /**
     * Presenta en pantalla los datos pormenorizados de un vehículo específico.
     * 
     * @param v Objeto Vehiculo a presentar.
     */
    public void mostrarVehiculo(Vehiculo v) {
        // Maneja la ausencia del registro.
        if (v == null) {
            mostrarMensajeError("No se encontró información del vehículo.");
            return;
        }

        // Imprime los datos formateados.
        System.out.println("\n────────────────────────────────────────");
        System.out.println(" Placa           : " + v.getPlaca());
        System.out.println(" Marca           : " + v.getMarca());
        System.out.println(" Modelo          : " + v.getModelo());
        System.out.println(" Año             : " + v.getAnio());
        System.out.println(" Capacidad Carga : " + v.getCapacidadCarga() + " kg");
        System.out.println(" Estado          : " + (v.getEstado() != null ? v.getEstado() : "N/A"));
        System.out.println("────────────────────────────────────────");
    }

    /**
     * Muestra una tabla estructurada con alineación de columnas para el listado de vehículos.
     * 
     * @param vehiculos Lista de vehículos a renderizar en la tabla.
     */
    public void mostrarListaVehiculos(List<Vehiculo> vehiculos) {
        // Valida si la lista viene vacía o nula.
        if (vehiculos == null || vehiculos.isEmpty()) {
            mostrarMensajeError("No hay vehículos registrados en la flota.");
            return;
        }

        // Imprime la cabecera formateada con alineación de espacios.
        System.out.println("\n══════════════════════════════════════════════════════════════");
        System.out.printf("%-12s %-15s %-15s %-15s\n", "PLACA", "MARCA", "MODELO", "ESTADO");
        System.out.println("══════════════════════════════════════════════════════════════");

        // Utiliza System.out.printf para alinear horizontalmente los registros.
        for (Vehiculo v : vehiculos) {
            System.out.printf("%-12s %-15s %-15s %-15s\n",
                    v.getPlaca(),
                    v.getMarca() != null ? v.getMarca() : "N/A",
                    v.getModelo() != null ? v.getModelo() : "N/A",
                    v.getEstado() != null ? v.getEstado().name() : "N/A");
        }
        System.out.println("══════════════════════════════════════════════════════════════");
    }

    /**
     * Imprime un mensaje de operación exitosa y realiza una pausa táctica.
     * 
     * @param msg Mensaje de confirmación a desplegar.
     */
    public void mostrarMensajeExito(String msg) {
        System.out.println("\n✅ " + msg);
        pausarPantalla();
    }

    /**
     * Imprime un mensaje de fallo o anomalía y realiza una pausa táctica.
     * 
     * @param msg Detalle del error ocurrido.
     */
    public void mostrarMensajeError(String msg) {
        System.out.println("\n❌ " + msg);
        pausarPantalla();
    }

    /**
     * Despliega la lista de registros de mantenimiento asociados al vehículo.
     * 
     * @param mantenimientos Lista de cadenas con las descripciones del historial.
     */
    public void mostrarHistorialMantenimiento(List<String> mantenimientos) {
        // Verifica si hay o no entradas en la lista.
        if (mantenimientos == null || mantenimientos.isEmpty()) {
            System.out.println("\n⚠️ No se registran eventos de mantenimiento para este vehículo.");
            return;
        }

        // Recorre la lista imprimiendo cada entrada.
        System.out.println("\n══════════════════════════════════════════════════════════════");
        System.out.println("             HISTORIAL DE MANTENIMIENTO DEL VEHÍCULO           ");
        System.out.println("══════════════════════════════════════════════════════════════");
        for (String m : mantenimientos) {
            System.out.println(" 🛠️  " + m);
        }
        System.out.println("══════════════════════════════════════════════════════════════");
    }

    // ── MÉTODOS PRIVADOS AUXILIARES DE FLUJO Y NAVEGACIÓN ──

    /**
     * Procesa la creación e inserción de un nuevo vehículo en la base de datos.
     */
    private void registrarVehiculo() {
        System.out.println("\n--- REGISTRO DE NUEVO VEHÍCULO ---");
        // Solicita el objeto datos del vehículo.
        Vehiculo nuevo = solicitarDatosVehiculo();
        try {
            // Guarda mediante el servicio.
            vehiculoService.registrarVehiculo(nuevo);
            mostrarMensajeExito("Vehículo con placa " + nuevo.getPlaca() + " registrado con éxito.");
        } catch (DataBaseException | IllegalArgumentException e) {
            mostrarMensajeError("Error al registrar vehículo: " + e.getMessage());
        }
    }

    /**
     * Solicita la placa e invoca la búsqueda puntual en el servicio.
     */
    private void buscarVehiculo() {
        System.out.println("\n--- BÚSQUEDA DE VEHÍCULO POR PLACA ---");
        // Captura la placa.
        String placa = solicitarPlaca();
        try {
            // Consulta en base de datos.
            Vehiculo v = vehiculoService.buscarPorPlaca(placa);
            // Presenta el vehículo.
            mostrarVehiculo(v);
        } catch (DataBaseException | IllegalArgumentException e) {
            mostrarMensajeError("Error al buscar el vehículo: " + e.getMessage());
        }
    }

    /**
     * Recupera y despliega en tabla la flota completa de vehículos.
     */
    private void listarVehiculos() {
        System.out.println("\n--- LISTADO COMPLETO DE VEHÍCULOS ---");
        try {
            // Lista los vehículos.
            List<Vehiculo> lista = vehiculoService.listarTodos();
            // Despliega la tabla.
            mostrarListaVehiculos(lista);
        } catch (DataBaseException e) {
            mostrarMensajeError("Error al obtener la lista de vehículos: " + e.getMessage());
        }
    }

    /**
     * Solicita la placa de un vehículo para buscar sus mantenimientos asociados.
     */
    private void consultarHistorialMantenimiento() {
        System.out.println("\n--- HISTORIAL DE MANTENIMIENTO ---");
        // Captura la placa del vehículo objetivo.
        String placa = solicitarPlaca();
        try {
            // Llama al servicio de mantenimiento para traer la lista.
            List<String> historial = mantenimientoService.obtenerHistorialPorPlaca(placa);
            // Imprime el reporte.
            mostrarHistorialMantenimiento(historial);
        } catch (DataBaseException | IllegalArgumentException e) {
            mostrarMensajeError("Error al consultar el historial de mantenimiento: " + e.getMessage());
        }
    }

    /**
     * Actualiza el estado operativo de un vehículo en la flota.
     */
    private void cambiarEstadoVehiculo() {
        System.out.println("\n--- CAMBIAR ESTADO DE VEHÍCULO ---");
        // Captura la placa.
        String placa = solicitarPlaca();
        try {
            // Valida que el vehículo exista.
            Vehiculo v = vehiculoService.buscarPorPlaca(placa);
            if (v != null) {
                System.out.println("Estado actual: " + v.getEstado());
                System.out.println("1. DISPONIBLE");
                System.out.println("2. EN_RUTA");
                System.out.println("3. EN_MANTENIMIENTO");
                System.out.println("4. INACTIVO");
                System.out.print("Seleccione el nuevo estado: ");

                String op = scanner.nextLine().trim();
                EstadoVehiculo nuevoEstado;

                switch (op) {
                    case "1":
                        nuevoEstado = EstadoVehiculo.DISPONIBLE;
                        break;
                    case "2":
                        nuevoEstado = EstadoVehiculo.EN_RUTA;
                        break;
                    case "3":
                        nuevoEstado = EstadoVehiculo.EN_MANTENIMIENTO;
                        break;
                    case "4":
                        nuevoEstado = EstadoVehiculo.INACTIVO;
                        break;
                    default:
                        mostrarMensajeError("Selección de estado inválida.");
                        return;
                }

                // Aplica la actualización mediante la capa de servicio.
                vehiculoService.cambiarEstado(placa, nuevoEstado);
                mostrarMensajeExito("Estado del vehículo " + placa + " actualizado a: " + nuevoEstado);
            }
        } catch (DataBaseException | IllegalArgumentException e) {
            mostrarMensajeError("Error al cambiar el estado del vehículo: " + e.getMessage());
        }
    }

    /**
     * Utilidad para pausar la consola y permitir la lectura antes de continuar.
     */
    private void pausarPantalla() {
        System.out.print("\nPresione ENTER para continuar...");
        scanner.nextLine();
    }
}