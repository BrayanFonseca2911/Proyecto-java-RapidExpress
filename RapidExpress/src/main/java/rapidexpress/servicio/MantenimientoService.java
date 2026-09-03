package rapidexpress.servicio;

import rapidexpress.dominio.Mantenimiento;
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.repositorio.MantenimientoDAO;
import rapidexpress.repositorio.IMantenimientoDAO;
import rapidexpress.auditoria.Auditoria;
import rapidexpress.auditoria.AuditLogger;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para la gestión de mantenimientos.
 * Depende de la abstracción {@link IMantenimientoDAO} en lugar de la
 * implementación JDBC concreta (DIP).
 *
 * @author User
 */
public class MantenimientoService {

    private final IMantenimientoDAO mantenimientoDAO;
    private final AuditLogger auditLogger;

    public MantenimientoService() {
        this(new MantenimientoDAO());
    }

    public MantenimientoService(IMantenimientoDAO mantenimientoDAO) {
        this.mantenimientoDAO = mantenimientoDAO;
        this.auditLogger = AuditLogger.getInstance();
    }
    
    /**
     * Registra un mantenimiento
     * @param mantenimiento Mantenimiento a registrar
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public void registrarMantenimiento(Mantenimiento mantenimiento) throws DataBaseException {
        if (mantenimiento.getFecha() == null) {
            mantenimiento.setFecha(LocalDateTime.now());
        }
        
        mantenimientoDAO.guardar(mantenimiento);
        
        registrarAuditoria("CREATE", "MANTENIMIENTO", String.valueOf(mantenimiento.getId()), 
                          "Se registro mantenimiento para vehiculo " + 
                          mantenimiento.getVehiculo().getPlaca());
    }
    
    /**
     * Lista mantenimientos por vehículo
     * @param vehiculoId ID del vehículo
     * @return Lista de mantenimientos
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Mantenimiento> listarPorVehiculo(Integer vehiculoId) throws DataBaseException {
        return mantenimientoDAO.listarPorVehiculo(vehiculoId);
    }
    
    /**
     * Lista mantenimientos en un rango de fechas
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Lista de mantenimientos
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Mantenimiento> listarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) 
            throws DataBaseException {
        return mantenimientoDAO.listarPorRangoFechas(fechaInicio, fechaFin);
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