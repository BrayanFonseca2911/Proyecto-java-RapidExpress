/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.vista;

// Importaciones de colecciones de Java y lectura de consola.
import java.util.List;
import java.util.Scanner;

// Importaciones del modelo de dominio, enumeradores, excepciones y servicios.
import rapidexpress.dominio.Paquete;
import rapidexpress.enums.EstadoPaquete;
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.servicio.PaqueteService;

/**
 * Propósito: Proveer la interfaz gráfica por consola para la gestión interactiva 
 * de paquetes dentro del sistema RapidExpress (admisión, rastreo, listados y actualización de estado).
 * 
 * @author User
 * @version 1.0
 */
public class PaqueteView {

    // Servicio que ejecuta la lógica de negocio y persistencia de paquetes.
    private final PaqueteService paqueteService;
    // Captura de entradas desde la consola de comandos.
    private final Scanner scanner;

    /**
     * Constructor predeterminado que inicializa el lector de consola y la capa de servicio.
     */
    public PaqueteView() {
        // Inicializa el lector de scanner.
        this.scanner = new Scanner(System.in);
        // Inicializa el servicio de gestión de paquetes.
        this.paqueteService = new PaqueteService();
    }

    /**
     * Despliega el menú de gestión de paquetes y controla la navegación del usuario.
     */
    public void mostrarMenu() {
        boolean continuar = true;

        while (continuar) {
            // Imprime la cabecera visual del módulo de paquetes.
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║          GESTIÓN DE PAQUETES           ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. Registrar y admitir nuevo paquete");
            System.out.println("2. Rastrear paquete por Código de Seguimiento (Tracking ID)");
            System.out.println("3. Listar todos los paquetes registrados");
            System.out.println("4. Cambiar estado de un paquete");
            System.out.println("5. Volver al Menú Principal");
            System.out.print("\nSeleccione una opción: ");

            // Lee la opción seleccionada eliminando espacios iniciales o finales.
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    // Inicia el proceso de registro de paquete.
                    registrarPaquete();
                    break;
                case "2":
                    // Inicia la búsqueda por código de seguimiento.
                    rastrearPaquete();
                    break;
                case "3":
                    // Despliega la lista general de paquetes.
                    listarPaquetes();
                    break;
                case "4":
                    // Permite actualizar el estado de envío de un paquete.
                    actualizarEstadoPaquete();
                    break;
                case "5":
                    // Finaliza el menú y regresa al menú principal.
                    continuar = false;
                    break;
                default:
                    // Manejo de opciones fuera de rango.
                    System.out.println("\n❌ Opción no válida. Intente nuevamente.");
            }
        }
    }

    /**
     * Captura los datos generales del envío, así como la información de remitente y destinatario.
     * 
     * @return Objeto Paquete completo instanciado con los valores capturados.
     */
    public Paquete solicitarDatosPaquete() {
        // Crea una nueva instancia del dominio Paquete.
        Paquete paquete = new Paquete();

        System.out.println("\n--- INFORMACIÓN GENERAL DEL PAQUETE ---");
        // Solicita la descripción del contenido.
        System.out.print("Ingrese descripción del contenido: ");
        paquete.setDescripcion(scanner.nextLine().trim());

        // Captura el peso validando entrada numérica.
        double peso = 0.0;
        try {
            System.out.print("Ingrese peso en kilogramos (ej. 2.5): ");
            peso = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Formato de peso inválido. Se asignará 1.0 kg por defecto.");
            peso = 1.0;
        }
        paquete.setPeso(peso);

        // Captura las dimensiones físicas.
        System.out.print("Ingrese dimensiones (ej. 30x20x10 cm): ");
        paquete.setDimensiones(scanner.nextLine().trim());

        // Captura la ciudad o dirección de origen.
        System.out.print("Ingrese ciudad / dirección de origen: ");
        paquete.setOrigen(scanner.nextLine().trim());

        // Captura la ciudad o dirección de destino.
        System.out.print("Ingrese ciudad / dirección de destino: ");
        paquete.setDestino(scanner.nextLine().trim());

        System.out.println("\n--- DATOS DEL REMITENTE ---");
        // Captura el nombre de quien envía.
        System.out.print("Nombre del remitente: ");
        paquete.setRemitenteNombre(scanner.nextLine().trim());

        // Captura el teléfono del remitente.
        System.out.print("Teléfono del remitente: ");
        paquete.setRemitenteTelefono(scanner.nextLine().trim());

        // Captura la dirección física del remitente.
        System.out.print("Dirección del remitente: ");
        paquete.setRemitenteDireccion(scanner.nextLine().trim());

        System.out.println("\n--- DATOS DEL DESTINATARIO ---");
        // Captura el nombre de quien recibe.
        System.out.print("Nombre del destinatario: ");
        paquete.setDestinatarioNombre(scanner.nextLine().trim());

        // Captura el teléfono del destinatario.
        System.out.print("Teléfono del destinatario: ");
        paquete.setDestinatarioTelefono(scanner.nextLine().trim());

        // Captura la dirección física de entrega del destinatario.
        System.out.print("Dirección de entrega del destinatario: ");
        paquete.setDestinatarioDireccion(scanner.nextLine().trim());

        // Asigna el estado inicial por defecto como REGISTRADO.
        paquete.setEstado(EstadoPaquete.REGISTRADO);

        return paquete;
    }

    /**
     * Solicita al usuario la clave de rastreo (Tracking ID) única de un paquete.
     * 
     * @return Cadena con el identificador de seguimiento.
     */
    public String solicitarTrackingId() {
        // Pide el código de rastreo por consola.
        System.out.print("Ingrese el código de seguimiento (Tracking ID): ");
        return scanner.nextLine().trim();
    }

    /**
     * Solicita al usuario seleccionar un nuevo estado para el paquete desde un menú de opciones.
     * 
     * @return Objeto EstadoPaquete correspondiente a la elección.
     */
    public EstadoPaquete solicitarEstadoPaquete() {
        System.out.println("\n--- SELECCIÓN DE ESTADO DE PAQUETE ---");
        System.out.println("1. REGISTRADO");
        System.out.println("2. EN_TRANSITO");
        System.out.println("3. ENTREGADO");
        System.out.println("4. CANCELADO");
        System.out.print("Seleccione el nuevo estado (1-4): ");

        String opcion = scanner.nextLine().trim();

        // Mapea la opción seleccionada al enumerador correspondiente.
        switch (opcion) {
            case "1":
                return EstadoPaquete.REGISTRADO;
            case "2":
                return EstadoPaquete.EN_TRANSITO;
            case "3":
                return EstadoPaquete.ENTREGADO;
            case "4":
                return EstadoPaquete.CANCELADO;
            default:
                System.out.println("⚠️ Opción no reconocida. Se conservará como REGISTRADO.");
                return EstadoPaquete.REGISTRADO;
        }
    }

    /**
     * Muestra en consola la totalidad de la información almacenada de un paquete.
     * 
     * @param p Instancia de la entidad Paquete a desplegar.
     */
    public void mostrarPaquete(Paquete p) {
        // Valida si el paquete es nulo antes de imprimir.
        if (p == null) {
            System.out.println("\n⚠️ No se encontró ningún paquete con la información proporcionada.");
            return;
        }

        // Formatea y despliega los detalles completos del envío.
        System.out.println("\n──────────────────────────────────────────────────");
        System.out.println(" Tracking ID   : " + p.getTrackingId());
        System.out.println(" Estado        : " + (p.getEstado() != null ? p.getEstado() : "N/A"));
        System.out.println(" Descripción   : " + p.getDescripcion());
        System.out.println(" Peso          : " + p.getPeso() + " kg");
        System.out.println(" Dimensiones   : " + p.getDimensiones());
        System.out.println(" Ruta          : " + p.getOrigen() + " → " + p.getDestino());
        System.out.println("──────────────────────────────────────────────────");
        System.out.println(" Remitente     : " + p.getRemitenteNombre());
        System.out.println(" Tel. Remitente: " + p.getRemitenteTelefono());
        System.out.println(" Dir. Remitente: " + p.getRemitenteDireccion());
        System.out.println("──────────────────────────────────────────────────");
        System.out.println(" Destinatario  : " + p.getDestinatarioNombre());
        System.out.println(" Tel. Destin.  : " + p.getDestinatarioTelefono());
        System.out.println(" Dir. Destino  : " + p.getDestinatarioDireccion());
        System.out.println("──────────────────────────────────────────────────");
    }

    /**
     * Muestra una tabla resumen con el listado completo de paquetes recibidos.
     * 
     * @param lista Colección de paquetes a imprimir.
     */
    public void mostrarListaPaquetes(List<Paquete> lista) {
        // Maneja colecciones nulas o vacías.
        if (lista == null || lista.isEmpty()) {
            System.out.println("\n⚠️ No existen paquetes registrados en el sistema.");
            return;
        }

        // Imprime la cabecera formateada de la tabla.
        System.out.println("\n═════════════════════════════════════════════════════════════════════════════");
        System.out.printf("%-18s %-25s %-10s %-15s\n", "TRACKING ID", "DESCRIPCIÓN", "PESO (KG)", "ESTADO");
        System.out.println("═════════════════════════════════════════════════════════════════════════════");

        // Recorre la lista imprimiendo cada paquete en una fila alineada.
        for (Paquete p : lista) {
            System.out.printf("%-18s %-25s %-10.2f %-15s\n",
                    p.getTrackingId(),
                    p.getDescripcion() != null ? p.getDescripcion() : "Sin descripción",
                    p.getPeso(),
                    p.getEstado() != null ? p.getEstado().name() : "N/A");
        }
        System.out.println("═════════════════════════════════════════════════════════════════════════════");
    }

    // ── MÉTODOS PRIVADOS AUXILIARES DE NAVEGACIÓN Y LOGICA INTERACTIVA ──

    /**
     * Ejecuta la secuencia para solicitar datos y registrar un paquete en el sistema.
     */
    private void registrarPaquete() {
        System.out.println("\n--- REGISTRO DE NUEVO PAQUETE ---");
        // Solicita toda la información del paquete.
        Paquete nuevo = solicitarDatosPaquete();
        try {
            // Guarda el paquete mediante el servicio.
            paqueteService.registrarPaquete(nuevo);
            System.out.println("\n✅ Paquete admitido exitosamente.");
            System.out.println("📌 Tracking ID generado: " + nuevo.getTrackingId());
        } catch (DataBaseException | IllegalArgumentException e) {
            System.out.println("\n❌ Error al registrar el paquete: " + e.getMessage());
        }
    }

    /**
     * Solicita el código de rastreo e imprime el resumen detallado del paquete encontrado.
     */
    private void rastrearPaquete() {
        System.out.println("\n--- RASTREO Y CONSULTA DE PAQUETE ---");
        // Pide el código de tracking.
        String trackingId = solicitarTrackingId();
        try {
            // Busca el registro en la base de datos a través de la capa de servicio.
            Paquete p = paqueteService.buscarPorTrackingId(trackingId);
            // Muestra el detalle.
            mostrarPaquete(p);
        } catch (DataBaseException | IllegalArgumentException e) {
            System.out.println("\n❌ Error al buscar paquete: " + e.getMessage());
        }
    }

    /**
     * Consulta y despliega el listado completo de todos los paquetes.
     */
    private void listarPaquetes() {
        System.out.println("\n--- LISTADO GENERAL DE PAQUETES ---");
        try {
            // Recupera la lista general de paquetes.
            List<Paquete> lista = paqueteService.listarTodos();
            // Imprime la tabla resumen.
            mostrarListaPaquetes(lista);
        } catch (DataBaseException e) {
            System.out.println("\n❌ Error al obtener el listado de paquetes: " + e.getMessage());
        }
    }

    /**
     * Procesa la actualización del estado logístico de un paquete.
     */
    private void actualizarEstadoPaquete() {
        System.out.println("\n--- CAMBIO DE ESTADO DE PAQUETE ---");
        // Solicita el código de seguimiento.
        String trackingId = solicitarTrackingId();
        try {
            // Valida la existencia del paquete antes de pedir el nuevo estado.
            Paquete p = paqueteService.buscarPorTrackingId(trackingId);
            if (p != null) {
                System.out.println("Estado actual del paquete: " + p.getEstado());
                // Solicita la elección del nuevo estado.
                EstadoPaquete nuevoEstado = solicitarEstadoPaquete();
                // Aplica el cambio en el servicio.
                paqueteService.actualizarEstado(trackingId, nuevoEstado);
                System.out.println("\n✅ Estado del paquete actualizado a: " + nuevoEstado);
            } else {
                System.out.println("\n⚠️ El paquete con Tracking ID " + trackingId + " no existe.");
            }
        } catch (DataBaseException | IllegalArgumentException e) {
            System.out.println("\n❌ Error al cambiar estado del paquete: " + e.getMessage());
        }
    }
}