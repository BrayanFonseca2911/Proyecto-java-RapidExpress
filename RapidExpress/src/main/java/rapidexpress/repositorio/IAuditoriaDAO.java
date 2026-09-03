package rapidexpress.repositorio;

import rapidexpress.auditoria.Auditoria;
import rapidexpress.excepciones.DataBaseException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrato de persistencia para el registro de auditoria.
 * No extiende {@link IDAO} porque los registros de auditoria son de
 * solo creacion y consulta (no se actualizan ni se eliminan).
 */
public interface IAuditoriaDAO {

    boolean guardar(Auditoria auditoria) throws DataBaseException;

    List<Auditoria> listarTodos() throws DataBaseException;

    List<Auditoria> listarPorFecha(LocalDateTime fecha) throws DataBaseException;

    List<Auditoria> listarPorAccion(String accion) throws DataBaseException;
}