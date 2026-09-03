package rapidexpress.servicio;

import rapidexpress.dominio.Ruta;
import rapidexpress.dominio.Vehiculo;
import rapidexpress.dominio.Conductor;
import rapidexpress.dominio.Paquete;
import rapidexpress.enums.EstadoRuta;
import rapidexpress.enums.EstadoVehiculo;
import rapidexpress.enums.EstadoConductor;
import rapidexpress.enums.EstadoPaquete;
import rapidexpress.excepciones.CapacidadExcedidaException;
import rapidexpress.excepciones.ConductorNoDisponibleException;
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.excepciones.RutaInvalidaException;
import rapidexpress.excepciones.VehiculoNoDisponibleException;
import rapidexpress.repositorio.RutaDAO;
import rapidexpress.repositorio.VehiculoDAO;
import rapidexpress.repositorio.ConductorDAO;
import rapidexpress.repositorio.PaqueteDAO;
import rapidexpress.auditoria.Auditoria;
import rapidexpress.auditoria.AuditLogger;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import rapidexpress.util.DBConnection;

/**
 * Servicio para la planificación y seguimiento de rutas.
 * Este servicio maneja transacciones complejas.
 * 
 * @author User
 */
public class RutaService {
    
    private final RutaDAO rutaDAO;
    private final VehiculoDAO vehiculoDAO;
    private final ConductorDAO conductorDAO;
    private final PaqueteDAO paqueteDAO;
    private final AuditLogger auditLogger;
    
    public RutaService() {
        this.rutaDAO = new RutaDAO();
        this.vehiculoDAO = new VehiculoDAO();
        this.conductorDAO = new ConductorDAO();
        this.paqueteDAO = new PaqueteDAO();
        this.auditLogger = AuditLogger.getInstance();
    }
    
    /**
     * Crea una nueva ruta
     * @param vehiculoId ID del vehículo
     * @param conductorId ID del conductor
     * @param paqueteIds Lista de IDs de paquetes
     * @return ID de la ruta creada
     * @throws DataBaseException Si ocurre un error de base de datos
     * @throws VehiculoNoDisponibleException Si el vehículo no está disponible
     * @throws ConductorNoDisponibleException Si el conductor no está disponible
     * @throws CapacidadExcedidaException Si se excede la capacidad del vehículo
     * @throws RutaInvalidaException Si la ruta tiene datos inválidos
     */
    public Integer crearRuta(Integer vehiculoId, Integer conductorId, List<Integer> paqueteIds) 
            throws DataBaseException, VehiculoNoDisponibleException, 
                   ConductorNoDisponibleException, CapacidadExcedidaException, RutaInvalidaException {
        
        // Validar vehículo
        Vehiculo vehiculo = vehiculoDAO.buscarPorId(vehiculoId);
        if (vehiculo == null) {
            throw new VehiculoNoDisponibleException("No existe vehículo con ID: " + vehiculoId);
        }
        if (vehiculo.getEstado() != EstadoVehiculo.DISPONIBLE) {
            throw new VehiculoNoDisponibleException(vehiculo.getPlaca());
        }
        
        // Validar conductor
        Conductor conductor = conductorDAO.buscarPorId(conductorId);
        if (conductor == null) {
            throw new ConductorNoDisponibleException("No existe conductor con ID: " + conductorId);
        }
        if (conductor.getEstado() != EstadoConductor.ACTIVO) {
            throw new ConductorNoDisponibleException(conductor.getNombre());
        }
        if (conductor.getVehiculoAsignado() != null) {
            throw new ConductorNoDisponibleException("El conductor ya tiene vehículo asignado");
        }
        
        // Validar paquetes y calcular peso total
        double pesoTotal = 0.0;
        for (Integer paqueteId : paqueteIds) {
            Paquete paquete = paqueteDAO.buscarPorId(paqueteId);
            if (paquete == null) {
                throw new RutaInvalidaException("No existe paquete con ID: " + paqueteId);
            }
            if (paquete.getEstado() != EstadoPaquete.EN_BODEGA) {
                throw new RutaInvalidaException("El paquete " + paquete.getTrackingId() + 
                                               " no está en bodega");
            }
            pesoTotal += paquete.getPeso();
        }
        
        // Validar capacidad del vehículo
        if (pesoTotal > vehiculo.getCapacidadCarga()) {
            throw new CapacidadExcedidaException(vehiculo.getCapacidadCarga(), pesoTotal);
        }
        
        // Crear ruta
        Ruta ruta = new Ruta(vehiculo, conductor);
        rutaDAO.guardar(ruta);
        
        // Asignar paquetes a la ruta
        for (Integer paqueteId : paqueteIds) {
            rutaDAO.asignarPaqueteARuta(ruta.getId(), paqueteId);
            
            // Actualizar estado del paquete
            paqueteDAO.actualizarEstado(paqueteDAO.buscarPorId(paqueteId).getTrackingId(), 
                                       EstadoPaquete.ASIGNADO_A_RUTA);
        }
        
        // Registrar auditoría
        registrarAuditoria("CREATE", "RUTA", String.valueOf(ruta.getId()), 
                          "Se creó ruta con " + paqueteIds.size() + " paquetes");
        
        return ruta.getId();
    }
    
    /**
     * Inicia una ruta planificada
     * @param rutaId ID de la ruta
     * @throws DataBaseException Si ocurre un error de base de datos
     * @throws RutaInvalidaException Si la ruta no puede ser iniciada
     */
    public void iniciarRuta(Integer rutaId) throws DataBaseException, RutaInvalidaException {
        // Buscar ruta
        Ruta ruta = rutaDAO.buscarPorId(rutaId);
        if (ruta == null) {
            throw new RutaInvalidaException("No existe ruta con ID: " + rutaId);
        }
        
        // Validar que esté planificada
        if (ruta.getEstado() != EstadoRuta.PLANIFICADA) {
            throw new RutaInvalidaException(rutaId, "La ruta no está planificada");
        }
        
        // Iniciar transacción
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);
            
            // Cambiar estado de la ruta
            rutaDAO.iniciarRuta(rutaId);
            
            // Cambiar estado del vehículo
            ruta.getVehiculo().setEstado(EstadoVehiculo.EN_RUTA);
            vehiculoDAO.actualizar(ruta.getVehiculo());
            
            // Cambiar estado del conductor
            ruta.getConductor().setEstado(EstadoConductor.EN_RUTA);
            conductorDAO.actualizar(ruta.getConductor());
            
            // Cambiar estado de todos los paquetes a EN_TRANSITO
            for (Paquete paquete : ruta.getPaquetes()) {
                paqueteDAO.actualizarEstado(paquete.getTrackingId(), EstadoPaquete.EN_TRANSITO);
            }
            
            // Confirmar transacción
            conn.commit();
            
            // Registrar auditoría
            registrarAuditoria("UPDATE", "RUTA", String.valueOf(rutaId), 
                              "Se inició ruta con ID: " + rutaId);
            
        } catch (SQLException e) {
            // Revertir transacción en caso de error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DataBaseException("Error al revertir transacción", ex);
                }
            }
            throw new DataBaseException("Error al iniciar ruta", e);
        } finally {
            // Restaurar auto-commit
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    // Ignorar
                }
            }
        }
    }
    
    /**
     * Completa una ruta en curso
     * @param rutaId ID de la ruta
     * @throws DataBaseException Si ocurre un error de base de datos
     * @throws RutaInvalidaException Si la ruta no puede ser completada
     */
    public void completarRuta(Integer rutaId) throws DataBaseException, RutaInvalidaException {
        // Buscar ruta
        Ruta ruta = rutaDAO.buscarPorId(rutaId);
        if (ruta == null) {
            throw new RutaInvalidaException("No existe ruta con ID: " + rutaId);
        }
        
        // Validar que esté en curso
        if (ruta.getEstado() != EstadoRuta.EN_CURSO) {
            throw new RutaInvalidaException(rutaId, "La ruta no está en curso");
        }
        
        // Completar transacción
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);
            
            // Cambiar estado de la ruta
            rutaDAO.completarRuta(rutaId);
            
            // Liberar vehículo
            ruta.getVehiculo().setEstado(EstadoVehiculo.DISPONIBLE);
            vehiculoDAO.actualizar(ruta.getVehiculo());
            
            // Liberar conductor
            ruta.getConductor().setEstado(EstadoConductor.ACTIVO);
            conductorDAO.actualizar(ruta.getConductor());
            
            // Confirmar transacción
            conn.commit();
            
            // Registrar auditoría
            registrarAuditoria("UPDATE", "RUTA", String.valueOf(rutaId), 
                              "Se completó ruta con ID: " + rutaId);
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DataBaseException("Error al revertir transacción", ex);
                }
            }
            throw new DataBaseException("Error al completar ruta", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    // Ignorar
                }
            }
        }
    }
    
    /**
     * Lista rutas activas (en curso)
     * @return Lista de rutas en curso
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Ruta> monitorearRutasActivas() throws DataBaseException {
        return rutaDAO.listarActivas();
    }
    
    /**
     * Actualiza el estado de un paquete en una ruta activa
     * @param rutaId ID de la ruta
     * @param trackingId ID de tracking del paquete
     * @param nuevoEstado Nuevo estado del paquete
     * @throws DataBaseException Si ocurre un error de base de datos
     * @throws RutaInvalidaException Si la ruta no está activa o el paquete no pertenece
     */
    public void actualizarPaqueteEnRuta(Integer rutaId, String trackingId, EstadoPaquete nuevoEstado) 
            throws DataBaseException, RutaInvalidaException {
        
        // Validar que la ruta esté en curso
        Ruta ruta = rutaDAO.buscarPorId(rutaId);
        if (ruta == null || ruta.getEstado() != EstadoRuta.EN_CURSO) {
            throw new RutaInvalidaException("La ruta no está activa");
        }
        
        // Validar que el paquete pertenezca a la ruta
        boolean pertenece = false;
        for (Paquete paquete : ruta.getPaquetes()) {
            if (paquete.getTrackingId().equals(trackingId)) {
                pertenece = true;
                break;
            }
        }
        
        if (!pertenece) {
            throw new RutaInvalidaException("El paquete no pertenece a esta ruta");
        }
        
        // Actualizar estado del paquete
        paqueteDAO.actualizarEstado(trackingId, nuevoEstado);
        
        // Registrar auditoría
        registrarAuditoria("UPDATE", "PAQUETE", trackingId, 
                          "Se actualizó estado en ruta " + rutaId + " a: " + nuevoEstado);
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