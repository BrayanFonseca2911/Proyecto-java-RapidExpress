/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.vista;

// Importaciones de colecciones de Java y herramientas de lectura de consola.
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Importaciones de las entidades del dominio, excepciones y servicio de rutas.
import rapidexpress.dominio.Conductor;
import rapidexpress.dominio.Paquete;
import rapidexpress.dominio.Ruta;
import rapidexpress.dominio.Vehiculo;
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.servicio.ConductorService;
import rapidexpress.servicio.PaqueteService;
import rapidexpress.servicio.RutaService;
import rapidexpress.servicio.VehiculoService;

/**
 * Propósito: Proveer la interfaz gráfica por consola para la gestión interactiva
 * de rutas en el sistema RapidExpress (planificación, asignación de vehículos,
 * conductores y paquetes, y consulta de rutas activas).
 * 
 * @author User
 * @version 1.0
 */
public class RutaView {

    // Servidores requeridos para recuperar componentes y procesar la planificación.
    private final RutaService rutaService;
    private final VehiculoService vehiculoService;
    private final ConductorService conductorService;
    private final PaqueteService paqueteService;

    // Lector de entrada estándar.
    private final Scanner scanner;

    /**
     * Constructor predeterminado que inicializa el escáner y las dependencias de servicio.
     */
    public RutaView() {
        // Inicializa la lectura por consola.
        this.scanner = new Scanner(System.in);
        // Inicializa el servicio de rutas.
        this.rutaService = new RutaService();
        // Inicializa los servicios auxiliares para selección de componentes de la ruta.
        this.vehiculoService = new VehiculoService();
        this.conductorService = new ConductorService();
        this.paqueteService = new PaqueteService();
    }

    /**
     * Despliega el menú de gestión de rutas y controla la navegación dentro del submódulo.
     */
    public void mostrarMenu() {
        boolean continuar = true;

        while (continuar) {
            // Imprime el encabezado del módulo de rutas.
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║         PLANIFICACIÓN DE RUTAS         ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. Planificar y despachar nueva ruta");
            System.out.println("2. Consultar rutas activas");
            System.out.println("3. Finalizar una ruta en curso");
            System.out.println("4. Volver al Menú Principal");
            System.out.print("\nSeleccione una opción: ");

            // Lee la opción ingresada eliminando espacios redundantes.
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    // Asigna y crea una nueva ruta de distribución.
                    planificarNuevaRuta();
                    break;
                case "2":
                    // Consulta el listado de rutas en estado activo/en curso.
                    consultarRutasActivas();
                    break;
                case "3":
                    // Marca como completada una ruta activa.
                    finalizarRuta();
                    break;
                case "4":
                    // Finaliza la ejecución del ciclo y retorna al menú general.
                    continuar = false;
                    break;
                default:
                    // Manejo de opción fuera de rango.
                    System.out.println("\n❌ Opción no válida. Intente nuevamente.");
            }
        }
    }

    /**
     * Muestra la lista de vehículos disponibles y retorna la entidad seleccionada por índice.
     * 
     * @param vehiculos Lista de vehículos disponibles.
     * @return El objeto Vehiculo seleccionado o null si la opción es inválida o la lista está vacía.
     */
    public Vehiculo seleccionarVehiculo(List<Vehiculo> vehiculos) {
        // Valida que existan vehículos.
        if (vehiculos == null || vehiculos.isEmpty()) {
            System.out.println("\n⚠️ No hay vehículos disponibles para asignar a la ruta.");
            return null;
        }

        // Muestra el listado numerado.
        System.out.println("\n--- SELECCIONE UN VEHÍCULO ---");
        for (int i = 0; i < vehiculos.size(); i++) {
            Vehiculo v = vehiculos.get(i);
            System.out.printf("%d. Placa: %s | Modelo: %s | Capacidad: %.2f kg\n",
                    (i + 1), v.getPlaca(), v.getModelo(), v.getCapacidadCarga());
        }

        System.out.print("Ingrese el número correspondiente al vehículo: ");
        try {
            int opcion = Integer.parseInt(scanner.nextLine().trim());
            // Verifica que el índice esté dentro del rango del arreglo.
            if (opcion >= 1 && opcion <= vehiculos.size()) {
                return vehiculos.get(opcion - 1);
            }
        } catch (NumberFormatException e) {
            // Manejo silencioso de error de conversión de texto.
        }

        System.out.println("❌ Selección de vehículo inválida.");
        return null;
    }

    /**
     * Muestra la lista de conductores disponibles y retorna el elegido por el usuario.
     * 
     * @param conductores Lista de conductores aptos para asignar.
     * @return Objeto Conductor seleccionado o null en caso de error.
     */
    public Conductor seleccionarConductor(List<Conductor> conductores) {
        // Valida la colección de conductores.
        if (conductores == null || conductores.isEmpty()) {
            System.out.println("\n⚠️ No hay conductores disponibles para asignar a la ruta.");
            return null;
        }

        // Muestra la lista numerada.
        System.out.println("\n--- SELECCIONE UN CONDUCTOR ---");
        for (int i = 0; i < conductores.size(); i++) {
            Conductor c = conductores.get(i);
            System.out.printf("%d. ID: %s | Nombre: %s | Licencia: %s\n",
                    (i + 1), c.getNumeroIdentificacion(), c.getNombre(), c.getTipoLicencia());
        }

        System.out.print("Ingrese el número correspondiente al conductor: ");
        try {
            int opcion = Integer.parseInt(scanner.nextLine().trim());
            // Valida el rango ingresado.
            if (opcion >= 1 && opcion <= conductores.size()) {
                return conductores.get(opcion - 1);
            }
        } catch (NumberFormatException e) {
            // Error de formato capturado.
        }

        System.out.println("❌ Selección de conductor inválida.");
        return null;
    }

    /**
     * Despliega la lista de paquetes elegibles y permite seleccionar múltiples elementos
     * mediante una cadena de índices separados por comas.
     * 
     * @param paquetes Lista de paquetes pendientes por envío.
     * @return Colección de objetos Paquete seleccionados por el usuario.
     */
    public List<Paquete> seleccionarPaquetes(List<Paquete> paquetes) {
        List<Paquete> seleccionados = new ArrayList<>();

        // Valida la disponibilidad de paquetes.
        if (paquetes == null || paquetes.isEmpty()) {
            System.out.println("\n⚠️ No hay paquetes pendientes para asignar a una ruta.");
            return seleccionados;
        }

        // Imprime el listado ordenado y numerado de paquetes.
        System.out.println("\n--- SELECCIÓN MÚLTIPLE DE PAQUETES ---");
        for (int i = 0; i < paquetes.size(); i++) {
            Paquete p = paquetes.get(i);
            System.out.printf("%d. Tracking: %s | Desc: %s | Peso: %.2f kg | Destino: %s\n",
                    (i + 1), p.getTrackingId(), p.getDescripcion(), p.getPeso(), p.getDestino());
        }

        // Solicita el patrón de selección múltiple (ej. 1,3,5).
        System.out.print("\nSeleccione los paquetes separados por coma (ej: 1,3,5): ");
        String input = scanner.nextLine().trim();

        // Procesa la cadena ingresada.
        if (!input.isEmpty()) {
            String[] indices = input.split(",");
            for (String idxStr : indices) {
                try {
                    int idx = Integer.parseInt(idxStr.trim());
                    // Asigna a la lista si el número ingresado corresponde a una posición real.
                    if (idx >= 1 && idx <= paquetes.size()) {
                        Paquete seleccionado = paquetes.get(idx - 1);
                        // Evita agregar duplicados a la misma selección.
                        if (!seleccionados.contains(seleccionado)) {
                            seleccionados.add(seleccionado);
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Índice ignorado por formato inválido: " + idxStr);
                }
            }
        }

        return seleccionados;
    }

    /**
     * Muestra el resumen pormenorizado de una ruta individual activa.
     * 
     * @param r Objeto Ruta a presentar.
     */
    public void mostrarRutaActiva(Ruta r) {
        // Control de valor nulo.
        if (r == null) {
            System.out.println("\n⚠️ No se encontró la información de la ruta solicitada.");
            return;
        }

        // Formatea el detalle de la ruta.
        System.out.println("\n──────────────────────────────────────────────────");
        System.out.println(" ID Ruta           : " + r.getIdRuta());
        System.out.println(" Origen → Destino  : " + r.getOrigen() + " → " + r.getDestino());
        System.out.println(" Fecha Salida      : " + r.getFechaSalida());
        System.out.println(" Vehículo Asignado : " + (r.getVehiculo() != null ? r.getVehiculo().getPlaca() : "N/A"));
        System.out.println(" Conductor Asignado: " + (r.getConductor() != null ? r.getConductor().getNombre() : "N/A"));
        System.out.println(" Estado de la Ruta : " + r.getEstado());
        System.out.println(" Total Paquetes    : " + (r.getPaquetes() != null ? r.getPaquetes().size() : 0));
        System.out.println("──────────────────────────────────────────────────");
    }

    /**
     * Presenta en forma de tabla el conjunto de rutas actualmente en curso.
     * 
     * @param rutas Colección de rutas activas a listar.
     */
    public void mostrarRutasActivas(List<Ruta> rutas) {
        // Control de listas vacías.
        if (rutas == null || rutas.isEmpty()) {
            System.out.println("\n⚠️ No hay rutas activas registradas en este momento.");
            return;
        }

        // Encabezado de la tabla general de rutas.
        System.out.println("\n═════════════════════════════════════════════════════════════════════════════");
        System.out.printf("%-8s %-15s %-15s %-12s %-20s\n", "ID RUTA", "ORIGEN", "DESTINO", "VEHÍCULO", "CONDUCTOR");
        System.out.println("═════════════════════════════════════════════════════════════════════════════");

        // Recorre e imprime cada fila.
        for (Ruta r : rutas) {
            String placa = (r.getVehiculo() != null) ? r.getVehiculo().getPlaca() : "Sin Vehículo";
            String conductor = (r.getConductor() != null) ? r.getConductor().getNombre() : "Sin Conductor";

            System.out.printf("%-8d %-15s %-15s %-12s %-20s\n",
                    r.getIdRuta(),
                    r.getOrigen(),
                    r.getDestino(),
                    placa,
                    conductor);
        }
        System.out.println("═════════════════════════════════════════════════════════════════════════════");
    }

    // ── MÉTODOS PRIVADOS AUXILIARES DE LÓGICA INTERACTIVA ──

    /**
     * Coordina el flujo paso a paso para ensamblar y despachar una nueva ruta de envío.
     */
    private void planificarNuevaRuta() {
        System.out.println("\n--- PLANIFICACIÓN DE NUEVA RUTA ---");
        try {
            // 1. Carga los elementos disponibles usando los servicios respectivos.
            List<Vehiculo> vehiculosDisponibles = vehiculoService.listarTodos();
            List<Conductor> conductoresDisponibles = conductorService.listarTodos();
            List<Paquete> paquetesPendientes = paqueteService.listarTodos();

            // 2. Ejecuta la selección de los 3 pilares de la ruta.
            Vehiculo vehiculoElegido = seleccionarVehiculo(vehiculosDisponibles);
            if (vehiculoElegido == null) return;

            Conductor conductorElegido = seleccionarConductor(conductoresDisponibles);
            if (conductorElegido == null) return;

            List<Paquete> paquetesElegidos = seleccionarPaquetes(paquetesPendientes);
            if (paquetesElegidos.isEmpty()) {
                System.out.println("❌ Debe seleccionar al menos un paquete para crear la ruta.");
                return;
            }

            // 3. Solicita los puntos de partida y llegada generales.
            System.out.print("Ingrese ciudad / punto de origen general: ");
            String origen = scanner.nextLine().trim();

            System.out.print("Ingrese ciudad / punto de destino final: ");
            String destino = scanner.nextLine().trim();

            // 4. Instancia y registra la nueva ruta mediante el servicio.
            Ruta nuevaRuta = new Ruta();
            nuevaRuta.setOrigen(origen);
            nuevaRuta.setDestino(destino);
            nuevaRuta.setVehiculo(vehiculoElegido);
            nuevaRuta.setConductor(conductorElegido);
            nuevaRuta.setPaquetes(paquetesElegidos);

            rutaService.crearRuta(nuevaRuta);
            System.out.println("\n✅ Ruta planificada y despachada con éxito.");

        } catch (DataBaseException | IllegalArgumentException e) {
            System.out.println("\n❌ Error al crear la ruta: " + e.getMessage());
        }
    }

    /**
     * Recupera y despliega las rutas activas de la base de datos.
     */
    private void consultarRutasActivas() {
        System.out.println("\n--- CONSULTA DE RUTAS ACTIVAS ---");
        try {
            List<Ruta> activas = rutaService.listarRutasActivas();
            mostrarRutasActivas(activas);
        } catch (DataBaseException e) {
            System.out.println("\n❌ Error al consultar rutas activas: " + e.getMessage());
        }
    }

    /**
     * Permite seleccionar el ID de una ruta en curso para marcarla como finalizada.
     */
    private void finalizarRuta() {
        System.out.println("\n--- FINALIZAR RUTA EN CURSO ---");
        System.out.print("Ingrese el ID de la ruta a finalizar: ");
        try {
            int idRuta = Integer.parseInt(scanner.nextLine().trim());
            rutaService.finalizarRuta(idRuta);
            System.out.println("\n✅ La ruta ID " + idRuta + " ha sido finalizada correctamente.");
        } catch (NumberFormatException e) {
            System.out.println("\n❌ El ID ingresado no es un número válido.");
        } catch (DataBaseException | IllegalArgumentException e) {
            System.out.println("\n❌ Error al finalizar la ruta: " + e.getMessage());
        }
    }
}