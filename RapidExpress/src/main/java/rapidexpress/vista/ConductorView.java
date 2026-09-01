/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.vista;

// Importaciones de colecciones y componentes de entrada.
import java.util.List;
import java.util.Scanner;

// Importaciones del modelo de dominio, servicios y excepciones.
import rapidexpress.dominio.Conductor;
import rapidexpress.enums.EstadoConductor;
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.servicio.ConductorService;

/**
 * Propósito: Proporcionar la interfaz gráfica por consola para la gestión interactiva
 * de conductores en el sistema RapidExpress (registro, consultas, actualización y bajas).
 * 
 * @author User
 * @version 1.0
 */
public class ConductorView {

    // Instancia del servicio para la lógica de negocio de conductores.
    private final ConductorService conductorService;
    // Captura de datos ingresados por la consola.
    private final Scanner scanner;

    /**
     * Constructor que inicializa las dependencias de lectura y la capa de servicios.
     */
    public ConductorView() {
        // Inicializa el lector de entradas de consola.
        this.scanner = new Scanner(System.in);
        // Inicializa el servicio de gestión de conductores.
        this.conductorService = new ConductorService();
    }

    /**
     * Muestra el menú de gestión de conductores y procesa la navegación del usuario.
     */
    public void mostrarMenu() {
        boolean continuar = true;

        while (continuar) {
            // Imprime la cabecera del submódulo.
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║         GESTIÓN DE CONDUCTORES         ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. Registrar nuevo conductor");
            System.out.println("2. Buscar conductor por identificación");
            System.out.println("3. Listar todos los conductores");
            System.out.println("4. Actualizar información de conductor");
            System.out.println("5. Inactivar / Dar de baja conductor");
            System.out.println("6. Volver al Menú Principal");
            System.out.print("\nSeleccione una opción: ");

            // Lectura limpia de la opción ingresada.
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    // Ejecuta el flujo de registro de un conductor.
                    registrarConductor();
                    break;
                case "2":
                    // Ejecuta la búsqueda individual por ID.
                    buscarConductor();
                    break;
                case "3":
                    // Despliega el listado completo de conductores.
                    listarConductores();
                    break;
                case "4":
                    // Modifica los datos de un conductor existente.
                    actualizarConductor();
                    break;
                case "5":
                    // Cambia el estado del conductor a inactivo.
                    inactivarConductor();
                    break;
                case "6":
                    // Cancela el bucle y retorna al menú general.
                    continuar = false;
                    break;
                default:
                    // Mensaje ante opción inválida.
                    System.out.println("\n❌ Opción no válida. Intente nuevamente.");
            }
        }
    }

    /**
     * Solicita la información necesaria para construir un nuevo objeto Conductor.
     * 
     * @return Instancia de Conductor poblada con las entradas de la consola.
     */
    public Conductor solicitarDatosConductor() {
        // Instancia un nuevo objeto dominio de Conductor.
        Conductor conductor = new Conductor();

        // Solicita e ingresa el documento de identidad.
        System.out.print("Ingrese número de identificación / cédula: ");
        conductor.setNumeroIdentificacion(scanner.nextLine().trim());

        // Solicita e ingresa el nombre completo.
        System.out.print("Ingrese nombre completo: ");
        conductor.setNombre(scanner.nextLine().trim());

        // Solicita e ingresa la categoría de la licencia de conducción.
        System.out.print("Ingrese tipo de licencia (ej. C1, C2, C3): ");
        conductor.setTipoLicencia(scanner.nextLine().trim().toUpperCase());

        // Solicita e ingresa el teléfono o medio de contacto.
        System.out.print("Ingrese teléfono / contacto: ");
        conductor.setContacto(scanner.nextLine().trim());

        // Establece el estado inicial por defecto como DISPONIBLE.
        conductor.setEstado(EstadoConductor.DISPONIBLE);

        return conductor;
    }

    /**
     * Captura el número de identificación único de un conductor.
     * 
     * @return Cadena con el número de cédula o documento.
     */
    public String solicitarNumeroIdentificacion() {
        // Solicita la clave primaria de consulta.
        System.out.print("Ingrese el número de identificación del conductor: ");
        return scanner.nextLine().trim();
    }

    /**
     * Despliega en pantalla los atributos de un conductor específico.
     * 
     * @param c Instancia del objeto Conductor a mostrar.
     */
    public void mostrarConductor(Conductor c) {
        // Valida si el objeto es nulo antes de imprimir.
        if (c == null) {
            System.out.println("\n⚠️ No se encontró información del conductor especificado.");
            return;
        }

        // Formatea y muestra los detalles del registro.
        System.out.println("\n────────────────────────────────────────");
        System.out.println("ID Base Datos : " + c.getIdConductor());
        System.out.println("Identificación: " + c.getNumeroIdentificacion());
        System.out.println("Nombre        : " + c.getNombre());
        System.out.println("Licencia      : " + c.getTipoLicencia());
        System.out.println("Contacto      : " + c.getContacto());
        System.out.println("Estado        : " + (c.getEstado() != null ? c.getEstado() : "N/A"));
        System.out.println("────────────────────────────────────────");
    }

    /**
     * Recibe una colección de conductores e imprime su contenido estructurado.
     * 
     * @param lista Lista de objetos Conductor recuperados de la base de datos.
     */
    public void mostrarListaConductores(List<Conductor> lista) {
        // Controla el caso de colecciones vacías o no inicializadas.
        if (lista == null || lista.isEmpty()) {
            System.out.println("\n⚠️ No hay conductores registrados en el sistema.");
            return;
        }

        // Imprime la cabecera de la tabla de resultados.
        System.out.println("\n═════════════════════════════════════════════════════════════════════");
        System.out.printf("%-15s %-25s %-10s %-12s %-10s\n", "IDENTIFICACIÓN", "NOMBRE", "LICENCIA", "CONTACTO", "ESTADO");
        System.out.println("═════════════════════════════════════════════════════════════════════");

        // Itera sobre cada registro formateando las columnas.
        for (Conductor c : lista) {
            System.out.printf("%-15s %-25s %-10s %-12s %-10s\n",
                    c.getNumeroIdentificacion(),
                    c.getNombre(),
                    c.getTipoLicencia(),
                    c.getContacto(),
                    c.getEstado() != null ? c.getEstado().name() : "N/A");
        }
        System.out.println("═════════════════════════════════════════════════════════════════════");
    }

    // ── MÉTODOS PRIVADOS AUXILIARES DE FLUJO Y COMUNICACIÓN CON SERVICIO ──

    /**
     * Maneja el proceso interactivo de creación y guardado de un conductor.
     */
    private void registrarConductor() {
        System.out.println("\n--- REGISTRO DE NUEVO CONDUCTOR ---");
        // Solicita los datos requeridos.
        Conductor nuevo = solicitarDatosConductor();
        try {
            // Invoca la regla de negocio para registrar.
            conductorService.registrarConductor(nuevo);
            System.out.println("\n✅ Conductor registrado correctamente.");
        } catch (DataBaseException | IllegalArgumentException e) {
            // Muestra mensajes de error en la capa de persistencia o validación.
            System.out.println("\n❌ Error al registrar conductor: " + e.getMessage());
        }
    }

    /**
     * Realiza la búsqueda de un conductor por documento e imprime el resultado.
     */
    private void buscarConductor() {
        System.out.println("\n--- BÚSQUEDA DE CONDUCTOR ---");
        // Pide la identificación de consulta.
        String id = solicitarNumeroIdentificacion();
        try {
            // Consulta el repositorio mediante la capa de servicio.
            Conductor c = conductorService.buscarPorIdentificacion(id);
            // Muestra los datos obtenidos.
            mostrarConductor(c);
        } catch (DataBaseException | IllegalArgumentException e) {
            System.out.println("\n❌ Error al buscar conductor: " + e.getMessage());
        }
    }

    /**
     * Consulta y muestra la lista completa de conductores.
     */
    private void listarConductores() {
        System.out.println("\n--- LISTADO GENERAL DE CONDUCTORES ---");
        try {
            // Obtiene todos los conductores.
            List<Conductor> lista = conductorService.listarTodos();
            // Despliega la colección formatada.
            mostrarListaConductores(lista);
        } catch (DataBaseException e) {
            System.out.println("\n❌ Error al consultar lista de conductores: " + e.getMessage());
        }
    }

    /**
     * Permite editar los datos de contacto o tipo de licencia de un conductor.
     */
    private void actualizarConductor() {
        System.out.println("\n--- ACTUALIZAR INFORMACIÓN DE CONDUCTOR ---");
        // Solicita el identificador base.
        String numIdentificacion = solicitarNumeroIdentificacion();
        try {
            // Valida la existencia previa del registro.
            Conductor c = conductorService.buscarPorIdentificacion(numIdentificacion);
            if (c != null) {
                // Solicita los nuevos valores.
                System.out.print("Nuevo teléfono/contacto (actual: " + c.getContacto() + "): ");
                String nuevoContacto = scanner.nextLine().trim();
                System.out.print("Nuevo tipo de licencia (actual: " + c.getTipoLicencia() + "): ");
                String nuevaLicencia = scanner.nextLine().trim().toUpperCase();

                // Asigna los cambios si los campos no se dejaron en blanco.
                if (!nuevoContacto.isEmpty()) c.setContacto(nuevoContacto);
                if (!nuevaLicencia.isEmpty()) c.setTipoLicencia(nuevaLicencia);

                // Invoca la actualización en la base de datos.
                conductorService.actualizarConductor(c);
                System.out.println("\n✅ Conductor actualizado exitosamente.");
            } else {
                System.out.println("\n⚠️ El conductor con identificación " + numIdentificacion + " no existe.");
            }
        } catch (DataBaseException | IllegalArgumentException e) {
            System.out.println("\n❌ Error al actualizar conductor: " + e.getMessage());
        }
    }

    /**
     * Cambia el estado operativo de un conductor a INACTIVO.
     */
    private void inactivarConductor() {
        System.out.println("\n--- DAR DE BAJA CONDUCTOR ---");
        // Captura el documento.
        String id = solicitarNumeroIdentificacion();
        try {
            // Ejecuta el cambio de estado mediante la lógica de servicio.
            conductorService.cambiarEstado(id, EstadoConductor.INACTIVO);
            System.out.println("\n✅ El conductor ha sido inactivado correctamente.");
        } catch (DataBaseException | IllegalArgumentException e) {
            System.out.println("\n❌ Error al inactivar conductor: " + e.getMessage());
        }
    }
}