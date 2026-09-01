/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.servicio;

// Importación para el manejo de listas de conductores.
import java.util.List;

// Importación de los modelos de dominio involucrados.
import rapidexpress.dominio.Conductor;
import rapidexpress.dominio.Vehiculo;
// Importación de la excepción de persistencia personalizada.
import rapidexpress.excepciones.DataBaseException;
// Importación de los DAOs de la capa de acceso a datos.
import rapidexpress.repositorio.ConductorDAO;
import rapidexpress.repositorio.VehiculoDAO;

/**
 * Propósito: Gestionar la lógica de negocio para conductores, incluyendo su registro,
 * actualización, consulta de disponibilidad y asignación/liberación de vehículos.
 * 
 * @author User
 */
public class ConductorService {

    // Repositorio para realizar operaciones CRUD sobre conductores.
    private final ConductorDAO conductorDAO;
    // Repositorio para actualizar y consultar el estado de los vehículos asignados.
    private final VehiculoDAO vehiculoDAO;

    // Constructor que inicializa los DAOs de acceso a datos.
    public ConductorService() {
        // Instancia el DAO de conductores.
        this.conductorDAO = new ConductorDAO();
        // Instancia el DAO de vehículos.
        this.vehiculoDAO = new VehiculoDAO();
    }

    /**
     * Registra un nuevo conductor previa validación de número de identificación único.
     * 
     * @param c Objeto Conductor a guardar.
     * @return true si el registro fue exitoso.
     * @throws DataBaseException Si ocurre un error en la base de datos.
     * @throws IllegalArgumentException Si el conductor o su identificación son inválidos o ya existen.
     */
    public boolean registrarConductor(Conductor c) throws DataBaseException {
        // Valida que el objeto no sea nulo.
        if (c == null) {
            throw new IllegalArgumentException("El conductor no puede ser nulo.");
        }
        // Valida que el número de identificación no esté vacío.
        if (c.getNumeroId() == null || c.getNumeroId().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de identificación es obligatorio.");
        }
        // Verifica que no exista un conductor registrado previamente con el mismo numeroId.
        if (conductorDAO.buscarPorNumeroId(c.getNumeroId()) != null) {
            throw new IllegalArgumentException("Ya existe un conductor registrado con el ID: " + c.getNumeroId());
        }

        // Ejecuta el guardado a través del DAO de conductores.
        return conductorDAO.guardar(c);
    }

    /**
     * Actualiza la información de un conductor existente.
     * 
     * @param c Conductor con los campos actualizados.
     * @return true si la actualización fue exitosa en la base de datos.
     * @throws DataBaseException Si ocurre un fallo en el acceso a datos.
     */
    public boolean actualizarConductor(Conductor c) throws DataBaseException {
        // Valida que el conductor no sea nulo y tenga un ID válido.
        if (c == null || c.getId() <= 0) {
            throw new IllegalArgumentException("El conductor a actualizar no es válido.");
        }
        // Ejecuta la modificación en el repositorio.
        return conductorDAO.actualizar(c);
    }

    /**
     * Asigna un vehículo disponible a un conductor activo.
     * 
     * @param numeroId Número de identificación del conductor.
     * @param placa Placa del vehículo a asignar.
     * @return true si la asignación completó con éxito en ambos registros.
     * @throws DataBaseException Si ocurre un error de persistencia.
     */
    public boolean asignarVehiculo(String numeroId, String placa) throws DataBaseException {
        // Busca el conductor por su número de identificación.
        Conductor conductor = conductorDAO.buscarPorNumeroId(numeroId);
        if (conductor == null) {
            throw new IllegalArgumentException("No se encontró al conductor con ID: " + numeroId);
        }

        // Verifica que el estado del conductor sea ACTIVO.
        if (!"ACTIVO".equalsIgnoreCase(conductor.getEstado())) {
            throw new IllegalStateException("El conductor debe estar en estado ACTIVO para asignarle un vehículo.");
        }

        // Busca el vehículo objetivo por su placa.
        Vehiculo vehiculo = vehiculoDAO.buscarPorPlaca(placa);
        if (vehiculo == null) {
            throw new IllegalArgumentException("No se encontró el vehículo con placa: " + placa);
        }

        // Verifica que el estado del vehículo sea DISPONIBLE.
        if (!"DISPONIBLE".equalsIgnoreCase(vehiculo.getEstado())) {
            throw new IllegalStateException("El vehículo con placa " + placa + " no está DISPONIBLE.");
        }

        // Vincula el vehículo con el conductor mediante el DAO de conductores.
        boolean asignado = conductorDAO.asignarVehiculo(conductor.getId(), vehiculo.getId());

        // Si la asignación en BD fue exitosa, actualiza la entidad vehículo y su estado en BD.
        if (asignado) {
            // Asigna la referencia del conductor en el objeto vehículo.
            vehiculo.setConductorAsignado(conductor);
            // Actualiza el estado operativo del vehículo a OCUPADO.
            vehiculo.setEstado("OCUPADO");
            // Persiste los cambios del vehículo en la base de datos.
            vehiculoDAO.actualizar(vehiculo);
        }

        // Retorna verdadero si la operación se completó correctamente.
        return asignado;
    }

    /**
     * Desvincula el vehículo asignado a un conductor y vuelve a poner el vehículo como DISPONIBLE.
     * 
     * @param numeroId Número de identificación del conductor a liberar.
     * @return true si la desvinculación se realizó exitosamente.
     * @throws DataBaseException Si ocurre una falla en MySQL.
     */
    public boolean liberarVehiculo(String numeroId) throws DataBaseException {
        // Busca al conductor por su número de identificación.
        Conductor conductor = conductorDAO.buscarPorNumeroId(numeroId);
        if (conductor == null) {
            throw new IllegalArgumentException("No existe un conductor con el ID: " + numeroId);
        }

        // Obtiene el vehículo actualmente asociado al conductor si existe.
        Vehiculo vehiculoAsociado = vehiculoDAO.buscarPorConductorId(conductor.getId());

        // Ejecuta la desvinculación en el repositorio de conductores.
        boolean liberado = conductorDAO.liberarVehiculo(conductor.getId());

        // Si la desvinculación fue exitosa y existía un vehículo asociado, actualiza el vehículo.
        if (liberado && vehiculoAsociado != null) {
            // Remueve la referencia del conductor en el vehículo.
            vehiculoAsociado.setConductorAsignado(null);
            // Restablece el estado del vehículo a DISPONIBLE.
            vehiculoAsociado.setEstado("DISPONIBLE");
            // Persiste el cambio de estado del vehículo en la BD.
            vehiculoDAO.actualizar(vehiculoAsociado);
        }

        // Retorna la confirmación de la liberación.
        return liberado;
    }

    /**
     * Obtiene el listado de conductores que se encuentran disponibles para asignación.
     * 
     * @return Lista de conductores en estado disponible.
     * @throws DataBaseException Si ocurre un error en la consulta.
     */
    public List<Conductor> listarDisponibles() throws DataBaseException {
        // Invoca el método correspondiente en el repositorio de conductores.
        return conductorDAO.listarDisponibles();
    }
}