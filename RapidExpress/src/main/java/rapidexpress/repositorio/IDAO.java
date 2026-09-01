/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package rapidexpress.repositorio;

// Importación de la interfaz List para definir los retornos de colecciones de datos.
import java.util.List;
// Importación de la excepción personalizada para la captura de errores en la capa de datos.
import rapidexpress.excepciones.DataBaseException;

/**
 * Interfaz genérica para operaciones CRUD de acceso a datos
 * 
 * @author User
 * @param <T> Tipo de entidad de dominio
 * @param <ID> Tipo del identificador clave de la entidad
 */
public interface IDAO<T, ID> {
    
    /**
     * Busca una entidad por su ID
     * @param id Identificador de la entidad
     * @return La entidad encontrada o null si no existe
     * @throws DataBaseException Si hay error de base de datos
     */
    // Firma del método para consultar un único registro según su clave primaria.
    T buscarPorId(ID id) throws DataBaseException;
    
    /**
     * Lista todas las entidades
     * @return Lista de entidades
     * @throws DataBaseException Si hay error de base de datos
     */
    // Firma del método para obtener el conjunto completo de registros de la tabla.
    List<T> listarTodos() throws DataBaseException;
    
    /**
     * Guarda una nueva entidad
     * @param entidad Entidad a guardar
     * @return true si se guardó correctamente
     * @throws DataBaseException Si hay error de base de datos
     */
    // Firma del método para insertar un nuevo registro en la base de datos.
    boolean guardar(T entidad) throws DataBaseException;
    
    /**
     * Actualiza una entidad existente
     * @param entidad Entidad con datos actualizados
     * @return true si se actualizó correctamente
     * @throws DataBaseException Si hay error de base de datos
     */
    // Firma del método para modificar la información de un registro existente.
    boolean actualizar(T entidad) throws DataBaseException;
    
    /**
     * Elimina una entidad por su ID
     * @param id Identificador de la entidad a eliminar
     * @return true si se eliminó correctamente
     * @throws DataBaseException Si hay error de base de datos
     */
    // Firma del método para realizar el borrado físico de una entidad por su ID.
    boolean eliminar(ID id) throws DataBaseException;
}