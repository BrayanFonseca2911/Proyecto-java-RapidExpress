package rapidexpress.repositorio;

import rapidexpress.dominio.Mantenimiento;
import rapidexpress.excepciones.DataBaseException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrato de persistencia especifico para Mantenimiento.
 */
public interface IMantenimientoDAO extends IDAO<Mantenimiento, Integer> {

    List<Mantenimiento> listarPorVehiculo(Integer vehiculoId) throws DataBaseException;

    List<Mantenimiento> listarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) throws DataBaseException;
}
