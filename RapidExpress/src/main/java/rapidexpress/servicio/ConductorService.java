package rapidexpress.servicio;

import rapidexpress.dominio.Conductor;
import rapidexpress.dominio.Vehiculo;
import rapidexpress.enums.EstadoConductor;
import rapidexpress.enums.EstadoVehiculo;
import rapidexpress.excepciones.ConductorNoDisponibleException;
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.excepciones.VehiculoNoDisponibleException;
import rapidexpress.repositorio.ConductorDAO;
import rapidexpress.repositorio.IConductorDAO;
import rapidexpress.repositorio.IVehiculoDAO;
import rapidexpress.repositorio.VehiculoDAO;
import rapidexpress.auditoria.Auditoria;
import rapidexpress.auditoria.AuditLogger;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para la gestión de conductores.
 * Depende de las abstracciones {@link IConductorDAO} e {@link IVehiculoDAO}
 * (Principio de Inversión de Dependencias) en lugar de las implementaciones
 * JDBC concretas, lo que permite sustituirlas por dobles de prueba.
 *
 * @author User
 */
public class ConductorService {

    private final IConductorDAO conductorDAO;
    private final IVehiculoDAO vehiculoDAO;
    private final AuditLogger auditLogger;

    public ConductorService() {
        this(new ConductorDAO(), new VehiculoDAO());
    }

    public ConductorService(IConductorDAO conductorDAO, IVehiculoDAO vehiculoDAO) {
        this.conductorDAO = conductorDAO;
        this.vehiculoDAO = vehiculoDAO;
        this.auditLogger = AuditLogger.getInstance();
    }
    
    /**
     * Registra un nuevo conductor
     * @param conductor Conductor a registrar
     * @throws DataBaseException Si ocurre un error de base de datos
     * @throws ConductorNoDisponibleException Si el número de identificación ya existe
     */
    public void registrarConductor(Conductor conductor) throws DataBaseException, ConductorNoDisponibleException {
        // Validar que el número de identificación no exista
        Conductor existente = conductorDAO.buscarPorNumeroIdentificacion(conductor.getNumeroIdentificacion());
        if (existente != null) {
            throw new ConductorNoDisponibleException("Ya existe un conductor con identificacion: " + 
                                                     conductor.getNumeroIdentificacion());
        }
        
        // Validar datos
        validarConductor(conductor);
        
        // Guardar en base de datos
        conductorDAO.guardar(conductor);
        
        // Registrar auditoría
        registrarAuditoria("CREATE", "CONDUCTOR", String.valueOf(conductor.getId()), 
                          "Se registro conductor: " + conductor.getNombre());
    }
    
    /**
     * Actualiza un conductor existente
     * @param conductor Conductor con datos actualizados
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public void actualizarConductor(Conductor conductor) throws DataBaseException {
        validarConductor(conductor);
        conductorDAO.actualizar(conductor);
        
        registrarAuditoria("UPDATE", "CONDUCTOR", String.valueOf(conductor.getId()), 
                          "Se actualizo conductor: " + conductor.getNombre());
    }
    
    /**
     * Busca un conductor por número de identificación
     * @param numeroIdentificacion Número de identificación
     * @return Conductor encontrado o null
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public Conductor buscarPorNumeroIdentificacion(String numeroIdentificacion) throws DataBaseException {
        return conductorDAO.buscarPorNumeroIdentificacion(numeroIdentificacion);
    }
    
    /**
     * Lista todos los conductores
     * @return Lista de conductores
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Conductor> listarTodos() throws DataBaseException {
        return conductorDAO.listarTodos();
    }
    
    /**
     * Lista conductores disponibles (activos sin vehículo asignado)
     * @return Lista de conductores disponibles
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Conductor> listarDisponibles() throws DataBaseException {
        return conductorDAO.listarDisponibles();
    }
    
    /**
     * Asigna un vehículo a un conductor
     * @param numeroIdentificacion Número de identificación del conductor
     * @param placa Placa del vehículo
     * @throws DataBaseException Si ocurre un error de base de datos
     * @throws ConductorNoDisponibleException Si el conductor no está disponible
     * @throws VehiculoNoDisponibleException Si el vehículo no está disponible
     */
    public void asignarVehiculo(String numeroIdentificacion, String placa) 
            throws DataBaseException, ConductorNoDisponibleException, VehiculoNoDisponibleException {
        
        // Buscar conductor
        Conductor conductor = conductorDAO.buscarPorNumeroIdentificacion(numeroIdentificacion);
        if (conductor == null) {
            throw new ConductorNoDisponibleException("No existe conductor con identificacion: " + numeroIdentificacion);
        }
        
        // Validar que el conductor esté activo
        if (conductor.getEstado() != EstadoConductor.ACTIVO) {
            throw new ConductorNoDisponibleException("El conductor no esta activo");
        }
        
        // Validar que no tenga vehículo asignado
        if (conductor.getVehiculoAsignado() != null) {
            throw new ConductorNoDisponibleException("El conductor ya tiene un vehiculo asignado");
        }
        
        // Buscar vehículo
        Vehiculo vehiculo = vehiculoDAO.buscarPorPlaca(placa);
        if (vehiculo == null) {
            throw new VehiculoNoDisponibleException("No existe vehiculo con placa: " + placa);
        }
        
        // Validar que el vehículo esté disponible
        if (vehiculo.getEstado() != EstadoVehiculo.DISPONIBLE) {
            throw new VehiculoNoDisponibleException("El vehiculo no está disponible");
        }
        
        // Asignar vehículo al conductor
        conductor.setVehiculoAsignado(vehiculo);
        conductorDAO.asignarVehiculo(conductor.getId(), vehiculo.getId());
        
        // Actualizar estado del vehículo (opcional, depende del diseño)
        // vehiculo.setConductorAsignado(conductor);
        // vehiculoDAO.actualizar(vehiculo);
        
        // Registrar auditoría
        registrarAuditoria("UPDATE", "CONDUCTOR", String.valueOf(conductor.getId()), 
                          "Se asigno vehiculo " + placa + " al conductor " + numeroIdentificacion);
    }
    
    /**
     * Libera el vehículo asignado a un conductor
     * @param numeroIdentificacion Número de identificación del conductor
     * @throws DataBaseException Si ocurre un error de base de datos
     * @throws ConductorNoDisponibleException Si el conductor no existe
     */
    public void liberarVehiculo(String numeroIdentificacion) 
            throws DataBaseException, ConductorNoDisponibleException {
        
        // Buscar conductor
        Conductor conductor = conductorDAO.buscarPorNumeroIdentificacion(numeroIdentificacion);
        if (conductor == null) {
            throw new ConductorNoDisponibleException("No existe conductor con identificacion: " + numeroIdentificacion);
        }
        
        // Validar que tenga vehículo asignado
        if (conductor.getVehiculoAsignado() == null) {
            throw new ConductorNoDisponibleException("El conductor no tiene vehiculo asignado");
        }
        
        // Liberar vehículo
        conductorDAO.liberarVehiculo(conductor.getId());
        conductor.setVehiculoAsignado(null);
        
        // Registrar auditoría
        registrarAuditoria("UPDATE", "CONDUCTOR", String.valueOf(conductor.getId()), 
                          "Se libero vehiculo del conductor " + numeroIdentificacion);
    }
    
    /**
     * Valida que el conductor tenga datos correctos
     */
    private void validarConductor(Conductor conductor) throws DataBaseException {
        if (conductor.getNumeroIdentificacion() == null || conductor.getNumeroIdentificacion().trim().isEmpty()) {
            throw new DataBaseException("El numero de identificacion es obligatorio");
        }
        
        if (conductor.getNombre() == null || conductor.getNombre().trim().isEmpty()) {
            throw new DataBaseException("El nombre del conductor es obligatorio");
        }
        
        if (conductor.getTipoLicencia() == null || conductor.getTipoLicencia().trim().isEmpty()) {
            throw new DataBaseException("El tipo de licencia es obligatorio");
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