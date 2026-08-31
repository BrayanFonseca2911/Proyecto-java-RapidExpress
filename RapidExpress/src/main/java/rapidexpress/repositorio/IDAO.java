package rapidexpress.repositorio;

import java.util.List;
import rapidexpress.excepciones.DataBaseException;  // ← AGREGAR ESTA LÍNEA

/**
 * Interfaz genérica para operaciones CRUD de acceso a datos
 * 
 * @author User
 * @param <T> Tipo de entidad
 * @param <ID> Tipo del identificador de la entidad
 */
public interface IDAO<T, ID> {
    
    /**
     * Busca una entidad por su ID
     * @param id Identificador de la entidad
     * @return La entidad encontrada o null si no existe
     * @throws DataBaseException Si hay error de base de datos
     */
    T buscarPorId(ID id) throws DataBaseException;
    
    /**
     * Lista todas las entidades
     * @return Lista de entidades
     * @throws DataBaseException Si hay error de base de datos
     */
    List<T> listarTodos() throws DataBaseException;
    
    /**
     * Guarda una nueva entidad
     * @param entidad Entidad a guardar
     * @return true si se guardó correctamente
     * @throws DataBaseException Si hay error de base de datos
     */
    boolean guardar(T entidad) throws DataBaseException;
    
    /**
     * Actualiza una entidad existente
     * @param entidad Entidad con datos actualizados
     * @return true si se actualizó correctamente
     * @throws DataBaseException Si hay error de base de datos
     */
    boolean actualizar(T entidad) throws DataBaseException;
    
    /**
     * Elimina una entidad por su ID
     * @param id Identificador de la entidad a eliminar
     * @return true si se eliminó correctamente
     * @throws DataBaseException Si hay error de base de datos
     */
    boolean eliminar(ID id) throws DataBaseException;
}