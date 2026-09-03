package rapidexpress.servicio;

import rapidexpress.dominio.Vehiculo;
import rapidexpress.dominio.Mantenimiento;
import rapidexpress.enums.EstadoVehiculo;
import rapidexpress.excepciones.CapacidadExcedidaException;
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.excepciones.VehiculoNoDisponibleException;
import rapidexpress.repositorio.VehiculoDAO;
import rapidexpress.repositorio.IVehiculoDAO;
import rapidexpress.repositorio.MantenimientoDAO;
import rapidexpress.repositorio.IMantenimientoDAO;
import rapidexpress.auditoria.Auditoria;
import rapidexpress.auditoria.AuditLogger;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para la gestión de vehículos.
 * Contiene la lógica de negocio y validaciones.
 * Depende de las abstracciones {@link IVehiculoDAO} e {@link IMantenimientoDAO}
 * en lugar de las implementaciones JDBC concretas (DIP).
 *
 * @author User
 */
public class VehiculoService {

    private final IVehiculoDAO vehiculoDAO;
    private final IMantenimientoDAO mantenimientoDAO;
    private final AuditLogger auditLogger;

    /**
     * Constructor del servicio
     */
    public VehiculoService() {
        this(new VehiculoDAO(), new MantenimientoDAO());
    }

    public VehiculoService(IVehiculoDAO vehiculoDAO, IMantenimientoDAO mantenimientoDAO) {
        this.vehiculoDAO = vehiculoDAO;
        this.mantenimientoDAO = mantenimientoDAO;
        this.auditLogger = AuditLogger.getInstance();
    }
    
    /**
     * Registra un nuevo vehículo en el sistema
     * @param vehiculo Vehículo a registrar
     * @throws DataBaseException Si ocurre un error de base de datos
     * @throws VehiculoNoDisponibleException Si la placa ya existe
     */
    public void registrarVehiculo(Vehiculo vehiculo) throws DataBaseException, VehiculoNoDisponibleException {
        // Validar que la placa no exista
        Vehiculo existente = vehiculoDAO.buscarPorPlaca(vehiculo.getPlaca());
        if (existente != null) {
            throw new VehiculoNoDisponibleException("Ya existe un vehiculo con la placa: " + vehiculo.getPlaca());
        }
        
        // Validar datos
        validarVehiculo(vehiculo);
        
        // Guardar en base de datos
        vehiculoDAO.guardar(vehiculo);
        
        // Registrar auditoría
        registrarAuditoria("CREATE", "VEHICULO", String.valueOf(vehiculo.getId()), 
                          "Se registro vehículo con placa " + vehiculo.getPlaca());
    }
    
    /**
     * Actualiza un vehículo existente
     * @param vehiculo Vehículo con datos actualizados
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public void actualizarVehiculo(Vehiculo vehiculo) throws DataBaseException {
        validarVehiculo(vehiculo);
        vehiculoDAO.actualizar(vehiculo);
        
        registrarAuditoria("UPDATE", "VEHICULO", String.valueOf(vehiculo.getId()), 
                          "Se actualizo vehículo con placa " + vehiculo.getPlaca());
    }
    
    /**
     * Busca un vehículo por su placa
     * @param placa Placa del vehículo
     * @return Vehículo encontrado o null
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public Vehiculo buscarPorPlaca(String placa) throws DataBaseException {
        return vehiculoDAO.buscarPorPlaca(placa);
    }
    
    /**
     * Lista todos los vehículos
     * @return Lista de vehículos
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Vehiculo> listarTodos() throws DataBaseException {
        return vehiculoDAO.listarTodos();
    }
    
    /**
     * Lista vehículos disponibles
     * @return Lista de vehículos disponibles
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Vehiculo> listarDisponibles() throws DataBaseException {
        return vehiculoDAO.listarDisponibles();
    }
    
    /**
     * Lista vehículos por estado
     * @param estado Estado a filtrar
     * @return Lista de vehículos con el estado especificado
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Vehiculo> listarPorEstado(EstadoVehiculo estado) throws DataBaseException {
        return vehiculoDAO.listarPorEstado(estado);
    }
    
    public void cambiarEstado(String placa, EstadoVehiculo nuevoEstado) 
            throws DataBaseException, VehiculoNoDisponibleException {
        
        Vehiculo vehiculo = vehiculoDAO.buscarPorPlaca(placa);
        if (vehiculo == null) {
            throw new VehiculoNoDisponibleException("No existe vehiculo con placa: " + placa);
        }
        
        if (nuevoEstado == null) {
            throw new DataBaseException("El estado no puede ser nulo");
        }
        
        vehiculo.setEstado(nuevoEstado);
        vehiculoDAO.actualizar(vehiculo);
        
        registrarAuditoria("UPDATE", "VEHICULO", String.valueOf(vehiculo.getId()), 
                          "Se cambio el estado del vehiculo " + placa + " a: " + nuevoEstado);
    }
    
    /**
     * Programa un mantenimiento para un vehículo
     * @param placa Placa del vehículo
     * @param mantenimiento Datos del mantenimiento
     * @throws DataBaseException Si ocurre un error de base de datos
     * @throws VehiculoNoDisponibleException Si el vehículo no existe
     */
    public void programarMantenimiento(String placa, Mantenimiento mantenimiento) 
            throws DataBaseException, VehiculoNoDisponibleException {
        
        // Buscar vehículo
        Vehiculo vehiculo = vehiculoDAO.buscarPorPlaca(placa);
        if (vehiculo == null) {
            throw new VehiculoNoDisponibleException("No existe vehiculo con placa: " + placa);
        }
        
        // Validar que no esté en ruta
        if (vehiculo.getEstado() == EstadoVehiculo.EN_RUTA) {
            throw new VehiculoNoDisponibleException("El vehiculo está en ruta y no puede ir a mantenimiento");
        }
        
        // Cambiar estado del vehículo
        vehiculo.setEstado(EstadoVehiculo.EN_MANTENIMIENTO);
        vehiculoDAO.actualizar(vehiculo);
        
        // Guardar mantenimiento
        mantenimiento.setVehiculo(vehiculo);
        mantenimientoDAO.guardar(mantenimiento);
        
        // Registrar auditoría
        registrarAuditoria("UPDATE", "VEHICULO", String.valueOf(vehiculo.getId()), 
                          "Se programo mantenimiento para vehiculo " + placa);
    }
    
    /**
     * Valida que el vehículo tenga datos correctos
     * @param vehiculo Vehículo a validar
     * @throws DataBaseException Si los datos son inválidos
     */
    private void validarVehiculo(Vehiculo vehiculo) throws DataBaseException {
        if (vehiculo.getPlaca() == null || vehiculo.getPlaca().trim().isEmpty()) {
            throw new DataBaseException("La placa del vehiculo es obligatoria");
        }
        
        if (vehiculo.getMarca() == null || vehiculo.getMarca().trim().isEmpty()) {
            throw new DataBaseException("La marca del vehiculo es obligatoria");
        }
        
        if (vehiculo.getModelo() == null || vehiculo.getModelo().trim().isEmpty()) {
            throw new DataBaseException("El modelo del vehiculo es obligatorio");
        }
        
        if (vehiculo.getYear() < 1900 || vehiculo.getYear() > LocalDateTime.now().getYear()) {
            throw new DataBaseException("El año del vehiculo no es válido");
        }
        
        if (vehiculo.getCapacidadCarga() <= 0) {
            throw new DataBaseException("La capacidad de carga debe ser mayor a cero");
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