package rapidexpress.servicio;

import rapidexpress.auditoria.Auditoria;
import rapidexpress.auditoria.AuditLogger;
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.repositorio.AuditoriaDAO;
import rapidexpress.repositorio.IAuditoriaDAO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para la gestión de auditoría.
 * Depende de la abstracción {@link IAuditoriaDAO} en lugar de la
 * implementación JDBC concreta (DIP).
 *
 * @author User
 */
public class AuditoriaService {

    private final IAuditoriaDAO auditoriaDAO;
    private final AuditLogger auditLogger;

    public AuditoriaService() {
        this(new AuditoriaDAO());
    }

    public AuditoriaService(IAuditoriaDAO auditoriaDAO) {
        this.auditoriaDAO = auditoriaDAO;
        this.auditLogger = AuditLogger.getInstance();
    }
    
    /**
     * Registra una acción en la auditoría
     * @param accion Tipo de acción
     * @param tabla Tabla afectada
     * @param registroId ID del registro
     * @param detalle Descripción del cambio
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public void registrarAccion(String accion, String tabla, String registroId, String detalle) 
            throws DataBaseException {
        
        Auditoria auditoria = new Auditoria();
        auditoria.setFecha(LocalDateTime.now());
        auditoria.setUsuario("system");
        auditoria.setAccion(accion);
        auditoria.setTablaAfectada(tabla);
        auditoria.setRegistroId(registroId);
        auditoria.setDetalle(detalle);
        auditoria.setIp("localhost");
        
        auditoriaDAO.guardar(auditoria);
        auditLogger.log(auditoria);
    }
    
    /**
     * Lista registros de auditoría por fecha
     * @param fecha Fecha a filtrar
     * @return Lista de registros
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Auditoria> getLogsPorFecha(LocalDateTime fecha) throws DataBaseException {
        return auditoriaDAO.listarPorFecha(fecha);
    }
    
    /**
     * Lista registros de auditoría por acción
     * @param accion Acción a filtrar
     * @return Lista de registros
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Auditoria> getLogsPorAccion(String accion) throws DataBaseException {
        return auditoriaDAO.listarPorAccion(accion);
    }
}