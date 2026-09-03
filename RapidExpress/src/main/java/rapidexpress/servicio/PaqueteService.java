package rapidexpress.servicio;

import rapidexpress.dominio.Paquete;
import rapidexpress.enums.EstadoPaquete;
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.excepciones.PaqueteNotFoundException;
import rapidexpress.repositorio.PaqueteDAO;
import rapidexpress.auditoria.Auditoria;
import rapidexpress.auditoria.AuditLogger;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para la gestión de paquetes.
 * 
 * @author User
 */
public class PaqueteService {
    
    private final PaqueteDAO paqueteDAO;
    private final AuditLogger auditLogger;
    
    public PaqueteService() {
        this.paqueteDAO = new PaqueteDAO();
        this.auditLogger = AuditLogger.getInstance();
    }
    
    /**
     * Registra un nuevo paquete
     * @param paquete Paquete a registrar
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public void registrarPaquete(Paquete paquete) throws DataBaseException {
        // Validar datos
        validarPaquete(paquete);
        
        // Generar tracking ID si no existe
        if (paquete.getTrackingId() == null || paquete.getTrackingId().trim().isEmpty()) {
            paquete.setTrackingId(paquete.generarTrackingId());
        }
        
        // Establecer fecha de registro
        if (paquete.getFechaRegistro() == null) {
            paquete.setFechaRegistro(LocalDateTime.now());
        }
        
        // Establecer estado inicial
        if (paquete.getEstado() == null) {
            paquete.setEstado(EstadoPaquete.EN_BODEGA);
        }
        
        // Guardar en base de datos
        paqueteDAO.guardar(paquete);
        
        // Registrar auditoría
        registrarAuditoria("CREATE", "PAQUETE", paquete.getTrackingId(), 
                          "Se registró paquete con tracking ID: " + paquete.getTrackingId());
    }
    
    /**
     * Busca un paquete por tracking ID
     * @param trackingId ID de tracking
     * @return Paquete encontrado o null
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public Paquete buscarPorTracking(String trackingId) throws DataBaseException {
        return paqueteDAO.buscarPorTrackingId(trackingId);
    }
    
    /**
     * Lista todos los paquetes
     * @return Lista de paquetes
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Paquete> listarTodos() throws DataBaseException {
        return paqueteDAO.listarTodos();
    }
    
    /**
     * Lista paquetes en bodega
     * @return Lista de paquetes en bodega
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Paquete> listarEnBodega() throws DataBaseException {
        return paqueteDAO.listarEnBodega();
    }
    
    /**
     * Lista paquetes por estado
     * @param estado Estado a filtrar
     * @return Lista de paquetes con el estado especificado
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Paquete> listarPorEstado(EstadoPaquete estado) throws DataBaseException {
        return paqueteDAO.listarPorEstado(estado);
    }
    
    /**
     * Actualiza el estado de un paquete
     * @param trackingId ID de tracking del paquete
     * @param nuevoEstado Nuevo estado
     * @throws DataBaseException Si ocurre un error de base de datos
     * @throws PaqueteNotFoundException Si el paquete no existe
     */
    public void actualizarEstado(String trackingId, EstadoPaquete nuevoEstado) 
            throws DataBaseException, PaqueteNotFoundException {
        
        // Buscar paquete
        Paquete paquete = paqueteDAO.buscarPorTrackingId(trackingId);
        if (paquete == null) {
            throw new PaqueteNotFoundException(trackingId);
        }
        
        // Validar transición de estado
        if (!paquete.getEstado().puedeTransicionarA(nuevoEstado)) {
            throw new DataBaseException("No se puede cambiar el estado de " + 
                                       paquete.getEstado() + " a " + nuevoEstado);
        }
        
        // Actualizar estado
        paquete.setEstado(nuevoEstado);
        
        // Si se entrega, registrar fecha
        if (nuevoEstado == EstadoPaquete.ENTREGADO) {
            paquete.entregar();
        }
        
        // Guardar cambios
        paqueteDAO.actualizar(paquete);
        
        // Registrar auditoría
        registrarAuditoria("UPDATE", "PAQUETE", trackingId, 
                          "Se actualizó estado del paquete a: " + nuevoEstado);
    }
    
    /**
     * Valida que el paquete tenga datos correctos
     */
    private void validarPaquete(Paquete paquete) throws DataBaseException {
        if (paquete.getDescripcion() == null || paquete.getDescripcion().trim().isEmpty()) {
            throw new DataBaseException("La descripción del paquete es obligatoria");
        }
        
        if (paquete.getPeso() <= 0) {
            throw new DataBaseException("El peso del paquete debe ser mayor a cero");
        }
        
        if (paquete.getOrigen() == null || paquete.getOrigen().trim().isEmpty()) {
            throw new DataBaseException("La dirección de origen es obligatoria");
        }
        
        if (paquete.getDestino() == null || paquete.getDestino().trim().isEmpty()) {
            throw new DataBaseException("La dirección de destino es obligatoria");
        }
        
        if (paquete.getRemitente() == null) {
            throw new DataBaseException("El remitente es obligatorio");
        }
        
        if (paquete.getDestinatario() == null) {
            throw new DataBaseException("El destinatario es obligatorio");
        }
    }
    
    /**
     * Registra una acción en el sistema de auditoría
     */
    private void registrarAuditoria(String accion, String tabla, String registroId, String detalle) {
        Auditoria auditoria = new Auditoria();
        auditoria.setFecha(LocalDateTime.now());
        auditoria.setUsuario("system");
        auditoria.setAccion(accion);
        auditoria.setTablaAfectada(tabla);
        auditoria.setRegistroId(registroId);
        auditoria.setDetalle(detalle);
        auditoria.setIp("localhost");
        
        auditLogger.log(auditoria);
    }
}