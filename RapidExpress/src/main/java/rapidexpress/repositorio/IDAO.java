package rapidexpress.repositorio;

import rapidexpress.excepciones.DataBaseException;
import java.util.List;

/**
 * Interfaz genérica que define las operaciones CRUD básicas
 * para todas las entidades del sistema.
 * 
 * @param <T> Tipo de entidad (Vehiculo, Conductor, Paquete, etc.)
 * @param <ID> Tipo del identificador de la entidad (Integer, String, etc.)
 * @author User
 */
public interface IDAO<T, ID> {
    
    /**
     * Busca una entidad por su identificador único
     * @param id Identificador de la entidad
     * @return La entidad encontrada o null si no existe
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    T buscarPorId(ID id) throws DataBaseException;
    
    /**
     * Lista todas las entidades de este tipo
     * @return Lista de todas las entidades
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    List<T> listarTodos() throws DataBaseException;
    
    /**
     * Guarda una nueva entidad en la base de datos
     * @param entidad Entidad a guardar
     * @return true si se guardó correctamente, false en caso contrario
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    boolean guardar(T entidad) throws DataBaseException;
    
    /**
     * Actualiza una entidad existente en la base de datos
     * @param entidad Entidad con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    boolean actualizar(T entidad) throws DataBaseException;
    
    /**
     * Elimina una entidad por su identificador
     * @param id Identificador de la entidad a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    boolean eliminar(ID id) throws DataBaseException;
}package rapidexpress.repositorio;

import rapidexpress.excepciones.DataBaseException;
import java.util.List;

/**
 * Interfaz genérica que define las operaciones CRUD básicas
 * para todas las entidades del sistema.
 * 
 * @param <T> Tipo de entidad (Vehiculo, Conductor, Paquete, etc.)
 * @param <ID> Tipo del identificador de la entidad (Integer, String, etc.)
 * @author User
 */
public interface IDAO<T, ID> {
    
    /**
     * Busca una entidad por su identificador único
     * @param id Identificador de la entidad
     * @return La entidad encontrada o null si no existe
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    T buscarPorId(ID id) throws DataBaseException;
    
    /**
     * Lista todas las entidades de este tipo
     * @return Lista de todas las entidades
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    List<T> listarTodos() throws DataBaseException;
    
    /**
     * Guarda una nueva entidad en la base de datos
     * @param entidad Entidad a guardar
     * @return true si se guardó correctamente, false en caso contrario
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    boolean guardar(T entidad) throws DataBaseException;
    
    /**
     * Actualiza una entidad existente en la base de datos
     * @param entidad Entidad con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    boolean actualizar(T entidad) throws DataBaseException;
    
    /**
     * Elimina una entidad por su identificador
     * @param id Identificador de la entidad a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    boolean eliminar(ID id) throws DataBaseException;
}