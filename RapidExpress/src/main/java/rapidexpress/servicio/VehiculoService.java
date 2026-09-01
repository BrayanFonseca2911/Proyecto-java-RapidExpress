/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.servicio;

// Importación para retornar colecciones de vehículos.
import java.util.List;

// Importación de los modelos de dominio involucrados.
import rapidexpress.dominio.Mantenimiento;
import rapidexpress.dominio.Vehiculo;
// Importación de las excepciones personalizadas del sistema.
import rapidexpress.excepciones.DataBaseException;
// Importación de la capa de persistencia para acceso a datos.
import rapidexpress.repositorio.MantenimientoDAO;
import rapidexpress.repositorio.VehiculoDAO;

/**
 * Propósito: Coordinar las reglas de negocio, validaciones y auditoría 
 * aplicables a la gestión de vehículos de la flota dentro del sistema RapidExpress.
 * 
 * @author User
 */
public class VehiculoService {

    // Repositorio para operaciones CRUD de vehículos.
    private final VehiculoDAO vehiculoDAO;
    // Repositorio para registrar los mantenimientos de los vehículos.
    private final MantenimientoDAO mantenimientoDAO;
    // Servicio para el registro de traza y auditoría de acciones.
    private final AuditoriaService auditoriaService;

    // Constructor que inicializa las dependencias de datos y servicios auxiliares.
    public VehiculoService() {
        // Instancia el DAO de vehículos.
        this.vehiculoDAO = new VehiculoDAO();
        // Instancia el DAO de mantenimientos.
        this.mantenimientoDAO = new MantenimientoDAO();
        // Instancia el servicio de auditoría.
        this.auditoriaService = new AuditoriaService();
    }

    /**
     * Registra un nuevo vehículo tras validar duplicidad de placa y coherencia de datos.
     * 
     * @param v Objeto vehículo a registrar.
     * @return true si el proceso de guardado y auditoría fue exitoso.
     * @throws DataBaseException Si ocurre una falla en el acceso a datos.
     * @throws IllegalArgumentException Si la placa ya existe o los datos son inválidos.
     */
    public boolean registrarVehiculo(Vehiculo v) throws DataBaseException {
        // Valida que el objeto no sea nulo.
        if (v == null) {
            throw new IllegalArgumentException("El vehículo no puede ser nulo.");
        }
        // Valida que la placa no sea vacía o nula.
        if (v.getPlaca() == null || v.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("La placa del vehículo es obligatoria.");
        }
        // Valida que no exista un vehículo registrado con la misma placa.
        if (buscarPorPlaca(v.getPlaca()) != null) {
            throw new IllegalArgumentException("Ya existe un vehículo registrado con la placa: " + v.getPlaca());
        }
        // Valida que la capacidad de carga (peso) sea un valor positivo.
        if (v.getCapacidadKg() <= 0) {
            throw new IllegalArgumentException("La capacidad de carga debe ser mayor a 0 kg.");
        }
        // Valida que el modelo/año sea un valor razonable.
        if (v.getModelo() == null || v.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("El modelo o año del vehículo debe ser especificado.");
        }

        // Guarda el registro en la base de datos a través del DAO.
        boolean guardado = vehiculoDAO.guardar(v);

        // Si se guardó correctamente, registra la traza de auditoría.
        if (guardado) {
            auditoriaService.registrarAccion("CREATE", "VEHICULO", "Placa: " + v.getPlaca());
        }

        // Retorna el resultado de la operación.
        return guardado;
    }

    /**
     * Actualiza los datos de un vehículo existente tras verificar su presencia en la BD.
     * 
     * @param v Vehiculo con la información modificada.
     * @return true si se actualizó correctamente.
     * @throws DataBaseException Si hay error en MySQL.
     */
    public boolean actualizarVehiculo(Vehiculo v) throws DataBaseException {
        // Valida la existencia del objeto y su ID.
        if (v == null || v.getId() <= 0) {
            throw new IllegalArgumentException("El vehículo a actualizar no es válido.");
        }
        // Verifica que el vehículo realmente exista previamente en la base de datos.
        Vehiculo vehiculoExistente = vehiculoDAO.buscarPorId(v.getId());
        if (vehiculoExistente == null) {
            throw new IllegalArgumentException("No existe el vehículo especificado para actualizar.");
        }

        // Ejecuta la actualización de la entidad en la base de datos.
        boolean actualizado = vehiculoDAO.actualizar(v);

        // Si fue exitoso, envía la auditoría.
        if (actualizado) {
            auditoriaService.registrarAccion("UPDATE", "VEHICULO", "ID: " + v.getId());
        }

        // Retorna el resultado.
        return actualizado;
    }

    /**
     * Elimina un vehículo mediante su número de placa tras validar reglas de negocio.
     * 
     * @param placa Placa única del vehículo.
     * @return true si el borrado fue exitoso.
     * @throws DataBaseException Si falla la eliminación en BD.
     */
    public boolean eliminarVehiculo(String placa) throws DataBaseException {
        // Busca el vehículo en la base de datos a partir de su placa.
        Vehiculo v = buscarPorPlaca(placa);
        if (v == null) {
            throw new IllegalArgumentException("No se encontró ningún vehículo con la placa: " + placa);
        }

        // (Aquí se podría invocar validación con RutaDAO/RutaService para verificar que no tenga rutas activas).

        // Ejecuta la eliminación física en la base de datos mediante el ID recuperado.
        boolean eliminado = vehiculoDAO.eliminar(v.getId());

        // Si se eliminó correctamente, registra la traza de auditoría.
        if (eliminado) {
            auditoriaService.registrarAccion("DELETE", "VEHICULO", "Placa: " + placa);
        }

        // Retorna la confirmación de la eliminación.
        return eliminado;
    }

    /**
     * Busca un vehículo registrado utilizando su placa.
     * 
     * @param placa Placa del vehículo.
     * @return El objeto Vehiculo encontrado o null.
     * @throws DataBaseException Si ocurre un error en la consulta.
     */
    public Vehiculo buscarPorPlaca(String placa) throws DataBaseException {
        // Valida que la placa ingresada contenga texto.
        if (placa == null || placa.trim().isEmpty()) {
            return null;
        }
        // Invoca el método especializado del DAO de vehículos.
        return vehiculoDAO.buscarPorPlaca(placa);
    }

    /**
     * Obtiene el listado completo de vehículos.
     * 
     * @return Lista de vehículos de la flota.
     * @throws DataBaseException Si hay error al listar en la BD.
     */
    public List<Vehiculo> listarTodos() throws DataBaseException {
        // Retorna la colección completa provista por el DAO.
        return vehiculoDAO.listarTodos();
    }

    /**
     * Obtiene la lista de vehículos disponibles para ser asignados a rutas.
     * 
     * @return Lista de vehículos en estado disponible.
     * @throws DataBaseException Si ocurre un fallo en la base de datos.
     */
    public List<Vehiculo> listarDisponibles() throws DataBaseException {
        // Retorna los vehículos filtrados por disponibilidad desde el DAO.
        return vehiculoDAO.listarDisponibles();
    }

    /**
     * Registra una orden de mantenimiento para un vehículo y actualiza su estado operativo.
     * 
     * @param placa Placa del vehículo a intervenir.
     * @param m Objeto Mantenimiento con la descripción y costos.
     * @return true si la operación completó el registro de mantenimiento y cambio de estado.
     * @throws DataBaseException Si ocurre un fallo en la persistencia.
     */
    public boolean programarMantenimiento(String placa, Mantenimiento m) throws DataBaseException {
        // Busca el vehículo objetivo por su placa.
        Vehiculo v = buscarPorPlaca(placa);
        if (v == null) {
            throw new IllegalArgumentException("No existe el vehículo con placa: " + placa);
        }
        // Valida que el objeto de mantenimiento no sea nulo.
        if (m == null) {
            throw new IllegalArgumentException("Los datos de mantenimiento son obligatorios.");
        }

        // Modifica el estado del vehículo a 'EN_MANTENIMIENTO'.
        v.setEstado("EN_MANTENIMIENTO");
        // Actualiza el estado del vehículo en la BD.
        vehiculoDAO.actualizar(v);

        // Asigna el ID del vehículo al objeto mantenimiento.
        m.setIdVehiculo(v.getId());

        // Guarda el registro de mantenimiento en la base de datos.
        boolean guardado = mantenimientoDAO.guardar(m);

        // Si fue exitoso, audita el evento.
        if (guardado) {
            auditoriaService.registrarAccion("UPDATE", "VEHICULO_MANTENIMIENTO", "Placa: " + placa);
        }

        // Retorna la confirmación del guardado.
        return guardado;
    }

    /**
     * Valida si un vehículo tiene capacidad de carga suficiente para un peso determinado.
     * 
     * @param placa Placa del vehículo.
     * @param peso Peso a validar en kg.
     * @return true si la capacidad de carga del vehículo es mayor o igual al peso.
     * @throws DataBaseException Si ocurre un fallo de lectura en la BD.
     */
    public boolean validarCapacidad(String placa, double peso) throws DataBaseException {
        // Busca el vehículo para validar sus especificaciones.
        Vehiculo v = buscarPorPlaca(placa);
        if (v == null) {
            throw new IllegalArgumentException("Vehículo no encontrado.");
        }
        // Compara si la capacidad de carga soportada es suficiente.
        return v.getCapacidadKg() >= peso;
    }
}