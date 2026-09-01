/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.repositorio;

// Importación para gestionar la conexión a la base de datos MySQL.
import java.sql.Connection;
// Importación para ejecutar consultas SQL parametrizadas de forma segura.
import java.sql.PreparedStatement;
// Importación para procesar los resultados devueltos por la base de datos.
import java.sql.ResultSet;
// Importación para la gestión de errores de la capa JDBC.
import java.sql.SQLException;
// Importación para manejar tipos de datos nulos en MySQL (Types.INTEGER).
import java.sql.Types;
// Importación para crear colecciones dinámicas de datos.
import java.util.ArrayList;
// Importación para definir el tipo de retorno en métodos de listas.
import java.util.List;

// Importación del modelo de dominio Conductor.
import rapidexpress.dominio.Conductor;
// Importación del patrón Singleton para la conexión de base de datos.
import rapidexpress.util.DBConnection;

/**
 * Propósito: Administrar la persistencia de los conductores, gestionando las
 * operaciones CRUD básicas y la asignación/liberación de vehículos de la flota.
 * 
 * @author User
 */
public class ConductorDAO implements IDAO<Conductor, Integer> {

    // --- SENTENCIAS SQL CRUD BÁSICAS ---

    // Inserta un nuevo conductor en la base de datos.
    private static final String SQL_INSERT = 
        "INSERT INTO conductores (nombre, licencia, numero_identificacion, estado, id_vehiculo) VALUES (?, ?, ?, ?, ?)";
    // Selecciona un conductor filtrando por su clave primaria (ID).
    private static final String SQL_SELECT_BY_ID = 
        "SELECT id, nombre, licencia, numero_identificacion, estado, id_vehiculo FROM conductores WHERE id = ?";
    // Selecciona todos los conductores registrados.
    private static final String SQL_SELECT_ALL = 
        "SELECT id, nombre, licencia, numero_identificacion, estado, id_vehiculo FROM conductores";
    // Actualiza la información completa de un conductor existente.
    private static final String SQL_UPDATE = 
        "UPDATE conductores SET nombre = ?, licencia = ?, numero_identificacion = ?, estado = ?, id_vehiculo = ? WHERE id = ?";
    // Elimina un registro de conductor por su ID.
    private static final String SQL_DELETE = 
        "DELETE FROM conductores WHERE id = ?";

    // --- SENTENCIAS SQL ESPECIALIZADAS ---

    // Busca un conductor a partir de su documento o número de identificación único.
    private static final String SQL_SELECT_BY_NUMERO_ID = 
        "SELECT id, nombre, licencia, numero_identificacion, estado, id_vehiculo FROM conductores WHERE numero_identificacion = ?";
    // Filtra conductores basándose en su estado operativo actual.
    private static final String SQL_SELECT_BY_ESTADO = 
        "SELECT id, nombre, licencia, numero_identificacion, estado, id_vehiculo FROM conductores WHERE estado = ?";
    // Consulta conductores que estén en estado 'ACTIVO' y que no tengan un vehículo asignado.
    private static final String SQL_SELECT_DISPONIBLES = 
        "SELECT id, nombre, licencia, numero_identificacion, estado, id_vehiculo FROM conductores WHERE estado = 'ACTIVO' AND id_vehiculo IS NULL";
    // Asigna un vehículo a un conductor específico.
    private static final String SQL_ASIGNAR_VEHICULO = 
        "UPDATE conductores SET id_vehiculo = ? WHERE id = ?";
    // Desvincula el vehículo asignado a un conductor (asigna NULL a la clave foránea).
    private static final String SQL_LIBERAR_VEHICULO = 
        "UPDATE conductores SET id_vehiculo = NULL WHERE id = ?";

    // =================================================================================
    // MÉTODOS CRUD IMPLEMENTADOS DE LA INTERFAZ IDAO
    // =================================================================================

    @Override
    public boolean crear(Conductor conductor) {
        // Inicia sesión en la base de datos y prepara la sentencia de inserción.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {

            // Asigna el nombre completo del conductor.
            stmt.setString(1, conductor.getNombre());
            // Asigna la categoría o número de licencia de conducción.
            stmt.setString(2, conductor.getLicencia());
            // Asigna el número de identificación único.
            stmt.setString(3, conductor.getNumeroIdentificacion());
            // Asigna el estado actual (ej. ACTIVO, INACTIVO).
            stmt.setString(4, conductor.getEstado());

            // Si el vehículo asignado no es nulo, pasa el id_vehiculo; de lo contrario, envía NULL a la BD.
            if (conductor.getIdVehiculo() != null && conductor.getIdVehiculo() > 0) {
                stmt.setInt(5, conductor.getIdVehiculo());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            // Retorna verdadero si la fila fue insertada exitosamente.
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Imprime en consola cualquier falla producida durante la inserción.
            System.err.println("Error al crear conductor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Conductor buscarPorId(Integer id) {
        // Variable contenedora para el objeto resultante.
        Conductor conductor = null;

        // Abre conexión e inicia la consulta filtrando por ID.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            // Asigna el ID del parámetro.
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                // Si la consulta arroja una fila, mapea los valores al objeto Conductor.
                if (rs.next()) {
                    conductor = mapearResultSetAConductor(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar conductor por ID: " + e.getMessage());
        }

        // Retorna el objeto Conductor encontrado o nulo.
        return conductor;
    }

    @Override
    public List<Conductor> listarTodos() {
        // Inicializa la lista receptora para acumular los conductores.
        List<Conductor> lista = new ArrayList<>();

        // Consulta general a la tabla conductores.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            // Recorre secuencialmente cada uno de los registros.
            while (rs.next()) {
                lista.add(mapearResultSetAConductor(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar conductores: " + e.getMessage());
        }

        // Retorna la colección completa de conductores.
        return lista;
    }

    @Override
    public boolean actualizar(Conductor conductor) {
        // Prepara la sentencia UPDATE dentro del bloque try-with-resources.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

            // Asigna los valores actualizados de los campos.
            stmt.setString(1, conductor.getNombre());
            stmt.setString(2, conductor.getLicencia());
            stmt.setString(3, conductor.getNumeroIdentificacion());
            stmt.setString(4, conductor.getEstado());

            // Manejo de valor nulo para la clave foránea del vehículo.
            if (conductor.getIdVehiculo() != null && conductor.getIdVehiculo() > 0) {
                stmt.setInt(5, conductor.getIdVehiculo());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            // Pasa el ID del conductor para la cláusula WHERE.
            stmt.setInt(6, conductor.getId());

            // Retorna verdadero si se modificó el registro.
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar conductor: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        // Prepara el borrado físico del registro en MySQL.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {

            // Asigna el ID numérico a eliminar.
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar conductor: " + e.getMessage());
            return false;
        }
    }

    // =================================================================================
    // MÉTODOS ESPECIALIZADOS DE GESTIÓN DE CONDUCTORES
    // =================================================================================

    /**
     * Busca un conductor de forma única mediante su número de identificación.
     * 
     * @param numeroId Documento de identidad del conductor.
     * @return Objeto Conductor coincidente o null si no existe.
     */
    public Conductor buscarPorNumeroIdentificacion(String numeroId) {
        Conductor conductor = null;

        // Ejecuación de consulta filtrada por la columna numero_identificacion.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_NUMERO_ID)) {

            // Pasa la cédula/documento a la consulta SQL.
            stmt.setString(1, numeroId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    conductor = mapearResultSetAConductor(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar por número de identificación: " + e.getMessage());
        }

        return conductor;
    }

    /**
     * Filtra la lista de conductores basándose en su estado actual en la empresa.
     * 
     * @param estado Estado del conductor (ej. "ACTIVO", "INACTIVO", "VACACIONES").
     * @return Lista de conductores pertenecientes a ese estado.
     */
    public List<Conductor> listarPorEstado(String estado) {
        List<Conductor> lista = new ArrayList<>();

        // Abre la conexión y prepara la lectura por estado.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ESTADO)) {

            // Pasa el valor del estado a consultar.
            stmt.setString(1, estado);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearResultSetAConductor(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar conductores por estado: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Obtiene la lista de conductores que están habilitados (ACTIVO) y listos para 
     * tomar una asignación, al no poseer ningún vehículo a su cargo actualmente.
     * 
     * @return Lista de conductores activos sin vehículo asignado.
     */
    public List<Conductor> listarDisponibles() {
        List<Conductor> lista = new ArrayList<>();

        // Consulta constante que verifica estado = 'ACTIVO' y id_vehiculo IS NULL.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_DISPONIBLES);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearResultSetAConductor(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar conductores disponibles: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Vincula un vehículo específico a un conductor en la base de datos.
     * 
     * @param conductorId ID del conductor que recibirá el vehículo.
     * @param vehiculoId ID del vehículo que se le va a asignar.
     * @return Verdadero si la operación actualizó la fila correctamente.
     */
    public boolean asignarVehiculo(Integer conductorId, Integer vehiculoId) {
        // Prepara el comando UPDATE de asignación directa.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_ASIGNAR_VEHICULO)) {

            // Asigna la clave del vehículo a actualizar.
            stmt.setInt(1, vehiculoId);
            // Asigna el ID del conductor en la cláusula WHERE.
            stmt.setInt(2, conductorId);

            // Retorna verdadero si se modificó el registro.
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al asignar vehículo al conductor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Desvincula cualquier vehículo que tenga a cargo el conductor especificado.
     * 
     * @param conductorId ID del conductor al cual se le retirará el vehículo.
     * @return Verdadero si se liberó el vehículo con éxito.
     */
    public boolean liberarVehiculo(Integer conductorId) {
        // Prepara el comando UPDATE para limpiar el valor de id_vehiculo a NULL.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_LIBERAR_VEHICULO)) {

            // Pasa el ID del conductor a la cláusula WHERE.
            stmt.setInt(1, conductorId);

            // Retorna verdadero si la operación actualizó la fila.
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al liberar el vehículo del conductor: " + e.getMessage());
            return false;
        }
    }

    // =================================================================================
    // MÉTODOS AUXILIARES DE MAPEO
    // =================================================================================

    // Transforma una fila obtenida de un ResultSet a un objeto de la entidad Conductor.
    private Conductor mapearResultSetAConductor(ResultSet rs) throws SQLException {
        // Instancia un nuevo objeto Conductor.
        Conductor c = new Conductor();
        // Mapea la clave primaria.
        c.setId(rs.getInt("id"));
        // Mapea el nombre del conductor.
        c.setNombre(rs.getString("nombre"));
        // Mapea la información de la licencia.
        c.setLicencia(rs.getString("licencia"));
        // Mapea la cédula / documento de identidad.
        c.setNumeroIdentificacion(rs.getString("numero_identificacion"));
        // Mapea la cadena representativa del estado.
        c.setEstado(rs.getString("estado"));
        
        // Maneja la lectura de la FK id_vehiculo cuando la columna puede ser NULL en la BD.
        int idVehiculo = rs.getInt("id_vehiculo");
        if (!rs.wasNull()) {
            c.setIdVehiculo(idVehiculo);
        } else {
            c.setIdVehiculo(null);
        }
        
        // Retorna la instancia de Conductor poblada.
        return c;
    }
}