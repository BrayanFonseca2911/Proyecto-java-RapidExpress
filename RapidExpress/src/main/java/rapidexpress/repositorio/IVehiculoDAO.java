package rapidexpress.repositorio;

import rapidexpress.dominio.Vehiculo;
import rapidexpress.enums.EstadoVehiculo;
import rapidexpress.excepciones.DataBaseException;

import java.util.List;

/**
 * Contrato de persistencia especifico para Vehiculo.
 * Extiende las operaciones CRUD genericas de {@link IDAO} con las
 * consultas propias del dominio de flota.
 */
public interface IVehiculoDAO extends IDAO<Vehiculo, Integer> {

    Vehiculo buscarPorPlaca(String placa) throws DataBaseException;

    List<Vehiculo> listarPorEstado(EstadoVehiculo estado) throws DataBaseException;

    List<Vehiculo> listarDisponibles() throws DataBaseException;
}
