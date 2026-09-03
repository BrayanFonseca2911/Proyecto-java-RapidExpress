package rapidexpress.repositorio;

import rapidexpress.dominio.Paquete;
import rapidexpress.dominio.Ruta;
import rapidexpress.excepciones.DataBaseException;

import java.util.List;

/**
 * Contrato de persistencia especifico para Ruta.
 */
public interface IRutaDAO extends IDAO<Ruta, Integer> {

    List<Ruta> listarActivas() throws DataBaseException;

    List<Ruta> listarPorVehiculo(Integer vehiculoId) throws DataBaseException;

    List<Ruta> listarHistorialVehiculo(Integer vehiculoId) throws DataBaseException;

    boolean asignarPaqueteARuta(Integer rutaId, Integer paqueteId) throws DataBaseException;

    List<Paquete> obtenerPaquetesDeRuta(Integer rutaId) throws DataBaseException;

    boolean iniciarRuta(Integer rutaId) throws DataBaseException;

    boolean completarRuta(Integer rutaId) throws DataBaseException;
}
