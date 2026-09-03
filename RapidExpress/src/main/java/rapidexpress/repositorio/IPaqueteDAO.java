package rapidexpress.repositorio;

import rapidexpress.dominio.Paquete;
import rapidexpress.enums.EstadoPaquete;
import rapidexpress.excepciones.DataBaseException;

import java.util.List;

/**
 * Contrato de persistencia especifico para Paquete.
 */
public interface IPaqueteDAO extends IDAO<Paquete, Integer> {

    Paquete buscarPorTrackingId(String trackingId) throws DataBaseException;

    List<Paquete> listarPorEstado(EstadoPaquete estado) throws DataBaseException;

    List<Paquete> listarEnBodega() throws DataBaseException;

    boolean actualizarEstado(String trackingId, EstadoPaquete estado) throws DataBaseException;
}
