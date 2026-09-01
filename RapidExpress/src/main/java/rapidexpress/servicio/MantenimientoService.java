/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.servicio;

// Importación para el manejo de listas de mantenimientos.
import java.util.List;

// Importación del enum para el control de estados del vehículo.
import rapidexpress.dominio.EstadoVehiculo;
// Importaciones de los modelos del dominio requeridos.
import rapidexpress.dominio.Mantenimiento;
import rapidexpress.dominio.Vehiculo;
// Importación de la excepción personalizada de la base de datos.
import rapidexpress.excepciones.DataBaseException;
// Importación de la capa DAO de mantenimientos.
import rapidexpress.repositorio.MantenimientoDAO;

/**
 * Propósito: Gestionar la lógica de negocio para los mantenimientos preventivos y correctivos,
 * coordinando los cambios de estado operativos en la flota de vehículos a través de VehiculoService.
 * 
 * @author User
 */
public class MantenimientoService {

    // Repositorio para la persistencia y consulta de mantenimientos.
    private final MantenimientoDAO mantenimientoDAO;
    // Servicio de vehículos para consultar información y cambiar estados operativos.
    private final VehiculoService vehiculoService;

    // Constructor que inicializa las dependencias requeridas.
    public MantenimientoService() {
        // Instancia el DAO de mantenimientos.
        this.mantenimientoDAO = new MantenimientoDAO();
        // Instancia el servicio de vehículos para mantener la cohesión de negocio.
        this.vehiculoService = new VehiculoService();
    }

    /**
     * Programa un mantenimiento para un vehículo, cambiando su estado operativo a EN_MANTENIMIENTO.
     * 
     * @param placa Placa del vehículo a ingresar a mantenimiento.
     * @param m Objeto mantenimiento con las observaciones y detalles.
     * @return true si la actualización de estado y el guardado fueron exitosos.
     * @throws DataBaseException Si ocurre un fallo en la base de datos.
     */
    public boolean programarMantenimiento(String placa, Mantenimiento m) throws DataBaseException {
        // Valida que el objeto mantenimiento no sea nulo.
        if (m == null) {
            throw new IllegalArgumentException("El objeto mantenimiento no puede ser nulo.");
        }
        // Valida que la placa ingresada no sea vacía.
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa del vehículo es obligatoria.");
        }

        // Busca el vehículo por la placa proporcionada mediante el servicio de vehículos.
        Vehiculo vehiculo = vehiculoService.buscarPorPlaca(placa);
        if (vehiculo == null) {
            throw new IllegalArgumentException("No se encontró ningún vehículo con la placa: " + placa);
        }

        // Asigna el ID del vehículo hallado al objeto mantenimiento.
        m.setIdVehiculo(vehiculo.getId());

        // Cambia el estado del vehículo a EN_MANTENIMIENTO a través del VehiculoService.
        boolean estadoCambiado = vehiculoService.cambiarEstado(placa, EstadoVehiculo.EN_MANTENIMIENTO);

        // Si el cambio de estado fue exitoso, persiste el registro de mantenimiento en la BD.
        if (estadoCambiado) {
            return mantenimientoDAO.guardar(m);
        }

        // Retorna false si no se pudo cambiar el estado del vehículo.
        return false;
    }

    /**
     * Registra directamente una entrada de mantenimiento en la base de datos.
     * 
     * @param m Objeto mantenimiento a registrar.
     * @return true si el registro fue guardado correctamente.
     * @throws DataBaseException Si ocurre un error en la persistencia.
     */
    public boolean registrarMantenimiento(Mantenimiento m) throws DataBaseException {
        // Valida que la entidad de mantenimiento no sea nula.
        if (m == null) {
            throw new IllegalArgumentException("El mantenimiento a registrar no puede ser nulo.");
        }
        // Persiste la información en la base de datos a través del DAO.
        return mantenimientoDAO.guardar(m);
    }

    /**
     * Obtiene el historial completo de mantenimientos realizados a un vehículo específico.
     * 
     * @param placa Placa del vehículo del cual se desea el historial.
     * @return Lista de mantenimientos asociados al vehículo.
     * @throws DataBaseException Si ocurre un fallo en la consulta SQL.
     */
    public List<Mantenimiento> listarHistorialVehiculo(String placa) throws DataBaseException {
        // Valida que la placa no esté vacía.
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar una placa válida.");
        }

        // Busca el vehículo por su placa.
        Vehiculo vehiculo = vehiculoService.buscarPorPlaca(placa);
        if (vehiculo == null) {
            throw new IllegalArgumentException("No existe un vehículo registrado con la placa: " + placa);
        }

        // Retorna la lista de mantenimientos filtrados por el ID primario del vehículo.
        return mantenimientoDAO.listarPorVehiculo(vehiculo.getId());
    }

    /**
     * Recupera el listado de todos los mantenimientos pendientes o activos.
     * 
     * @return Lista de mantenimientos pertenecientes a vehículos en estado EN_MANTENIMIENTO.
     * @throws DataBaseException Si ocurre un error en la base de datos.
     */
    public List<Mantenimiento> listarMantenimientosPendientes() throws DataBaseException {
        // Consulta y retorna los mantenimientos con estado pendiente en la BD.
        return mantenimientoDAO.listarMantenimientosPendientes();
    }
}