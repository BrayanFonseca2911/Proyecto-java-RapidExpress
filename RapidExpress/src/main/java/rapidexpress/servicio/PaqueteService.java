/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.servicio;

// Importación para la gestión de fechas del sistema.
import java.util.Date;
// Importación para el retorno de listas de paquetes.
import java.util.List;

// Importación del enum de estados del paquete.
import rapidexpress.dominio.EstadoPaquete;
// Importación del modelo de entidad Paquete.
import rapidexpress.dominio.Paquete;
// Importación de la excepción personalizada de persistencia.
import rapidexpress.excepciones.DataBaseException;
// Importación de la capa de acceso a datos para paquetes.
import rapidexpress.repositorio.PaqueteDAO;

/**
 * Propósito: Gestionar la lógica de negocio, ciclo de vida, transiciones de estado, 
 * rastreo por tracking y auditoría de encomiendas dentro del sistema RapidExpress.
 * 
 * @author User
 */
public class PaqueteService {

    // Repositorio de persistencia para realizar operaciones CRUD sobre los paquetes.
    private final PaqueteDAO paqueteDAO;
    // Servicio auxiliar para registrar la traza y auditoría de eventos.
    private final AuditoriaService auditoriaService;

    // Constructor que inicializa los objetos DAO y servicios requeridos.
    public PaqueteService() {
        // Instancia el DAO de paquetes.
        this.paqueteDAO = new PaqueteDAO();
        // Instancia el servicio de auditoría.
        this.auditoriaService = new AuditoriaService();
    }

    /**
     * Genera el ID de rastreo, asigna fecha de ingreso, establece el estado EN_BODEGA
     * y guarda el paquete registrando el evento en auditoría.
     * 
     * @param p Objeto paquete con los datos iniciales.
     * @return true si el paquete fue registrado correctamente.
     * @throws DataBaseException Si ocurre un error en la base de datos.
     */
    public boolean registrarPaquete(Paquete p) throws DataBaseException {
        // Valida que el paquete recibido no sea nulo.
        if (p == null) {
            throw new IllegalArgumentException("El paquete no puede ser nulo.");
        }

        // Genera automáticamente el código único de seguimiento.
        p.generarTrackingId();
        // Asigna la fecha actual del sistema como fecha de registro.
        p.setFechaRegistro(new Date());
        // Establece el estado inicial del paquete como EN_BODEGA.
        p.setEstado(EstadoPaquete.EN_BODEGA);

        // Persiste el objeto paquete en la base de datos a través del DAO.
        boolean guardado = paqueteDAO.guardar(p);

        // Si el guardado fue exitoso, registra la acción en el sistema de auditoría.
        if (guardado) {
            auditoriaService.registrarAccion("CREATE", "PAQUETE", "Tracking: " + p.getTrackingId());
        }

        // Retorna el resultado de la operación de guardado.
        return guardado;
    }

    /**
     * Búsqueda de un paquete registrado utilizando su código único de rastreo.
     * 
     * @param trackingId Código de rastreo del paquete.
     * @return El paquete correspondiente o null si no se encuentra.
     * @throws DataBaseException Si ocurre un error en la consulta SQL.
     */
    public Paquete buscarPorTracking(String trackingId) throws DataBaseException {
        // Valida que el identificador de tracking no sea nulo ni esté vacío.
        if (trackingId == null || trackingId.trim().isEmpty()) {
            return null;
        }
        // Solicita al DAO la búsqueda por el tracking especificado.
        return paqueteDAO.buscarPorTrackingId(trackingId);
    }

    /**
     * Valida las reglas de transición de estados y actualiza el estado del paquete.
     * 
     * @param trackingId Código de rastreo del paquete.
     * @param nuevoEstado Nuevo estado al que pasará la encomienda.
     * @return true si la actualización fue exitosa.
     * @throws DataBaseException Si ocurre un fallo en el acceso a datos.
     */
    public boolean actualizarEstado(String trackingId, EstadoPaquete nuevoEstado) throws DataBaseException {
        // Valida que el parámetro de estado no sea nulo.
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo.");
        }

        // Busca el paquete actual en la base de datos para verificar su estado previo.
        Paquete p = buscarPorTracking(trackingId);
        if (p == null) {
            throw new IllegalArgumentException("No se encontró el paquete con tracking: " + trackingId);
        }

        // Recupera el estado actual del paquete.
        EstadoPaquete estadoActual = p.getEstado();

        // REGLA DE NEGOCIO: Un paquete entregado o cancelado no puede regresar a estado EN_BODEGA.
        if ((estadoActual == EstadoPaquete.ENTREGADO || estadoActual == EstadoPaquete.CANCELADO) 
                && nuevoEstado == EstadoPaquete.EN_BODEGA) {
            throw new IllegalStateException("Transición de estado no válida: No se puede mover a BODEGA un paquete " + estadoActual);
        }

        // Si el estado a actualizar es ENTREGADO, registra la fecha y hora exacta de entrega.
        if (nuevoEstado == EstadoPaquete.ENTREGADO) {
            p.setFechaEntrega(new Date());
        }

        // Actualiza el nuevo estado en el objeto local.
        p.setEstado(nuevoEstado);

        // Actualiza el registro mediante el DAO en la base de datos.
        return paqueteDAO.actualizarEstado(trackingId, nuevoEstado);
    }

    /**
     * Recupera todos los paquetes cuyo estado actual sea EN_BODEGA.
     * 
     * @return Lista de paquetes en bodega.
     * @throws DataBaseException Si ocurre un fallo en la base de datos.
     */
    public List<Paquete> listarEnBodega() throws DataBaseException {
        // Invoca al método especializado del repositorio.
        return paqueteDAO.listarEnBodega();
    }

    /**
     * Filtra y lista los paquetes según un estado en específico.
     * 
     * @param estado Estado por el cual se desea filtrar.
     * @return Lista de paquetes que cumplen con el criterio.
     * @throws DataBaseException Si ocurre un error de consulta.
     */
    public List<Paquete> listarPorEstado(EstadoPaquete estado) throws DataBaseException {
        // Valida que el estado especificado no sea nulo.
        if (estado == null) {
            throw new IllegalArgumentException("El estado especificado no puede ser nulo.");
        }
        // Invoca la consulta parametrizada en el DAO.
        return paqueteDAO.listarPorEstado(estado);
    }

    /**
     * Completa el proceso de entrega de un paquete, cambiando su estado a ENTREGADO
     * y registrando la acción en el log de auditoría.
     * 
     * @param trackingId Código de rastreo del paquete a entregar.
     * @return true si el paquete fue marcado como entregado correctamente.
     * @throws DataBaseException Si ocurre una falla en el almacenamiento.
     */
    public boolean entregarPaquete(String trackingId) throws DataBaseException {
        // Ejecuta la actualización del estado al Enum ENTREGADO.
        boolean exito = actualizarEstado(trackingId, EstadoPaquete.ENTREGADO);

        // Si la actualización fue exitosa, genera la traza en la auditoría.
        if (exito) {
            auditoriaService.registrarAccion("UPDATE", "PAQUETE", "Entregado: " + trackingId);
        }

        // Retorna el resultado final de la entrega.
        return exito;
    }
}