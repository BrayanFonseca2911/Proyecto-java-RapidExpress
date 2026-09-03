package rapidexpress.repositorio;

import rapidexpress.dominio.Conductor;
import rapidexpress.enums.EstadoConductor;
import rapidexpress.excepciones.DataBaseException;

import java.util.List;

/**
 * Contrato de persistencia especifico para Conductor.
 */
public interface IConductorDAO extends IDAO<Conductor, Integer> {

    Conductor buscarPorNumeroIdentificacion(String numeroIdentificacion) throws DataBaseException;

    List<Conductor> listarPorEstado(EstadoConductor estado) throws DataBaseException;

    List<Conductor> listarDisponibles() throws DataBaseException;

    boolean asignarVehiculo(Integer conductorId, Integer vehiculoId) throws DataBaseException;

    boolean liberarVehiculo(Integer conductorId) throws DataBaseException;
}
