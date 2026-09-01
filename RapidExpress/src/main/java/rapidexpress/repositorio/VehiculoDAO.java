/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.repositorio;

// Importación de la interfaz Connection para administrar las sesiones JDBC con MySQL.
import java.sql.Connection;
// Importación de PreparedStatement para ejecutar consultas parametrizadas de forma segura.
import java.sql.PreparedStatement;
// Importación de ResultSet para procesar los datos devueltos por la base de datos.
import java.sql.ResultSet;
// Importación de SQLException para la gestión y captura de errores de la capa de persistencia.
import java.sql.SQLException;
// Importación de ArrayList para almacenar la lista de objetos de retorno.
import java.util.ArrayList;
// Importación de la interfaz List para definir los tipos de retorno en las colecciones.
import java.util.List;

// Importación del modelo de dominio Vehiculo.
import rapidexpress.dominio.Vehiculo;
// Importación del singleton DBConnection para obtener la conexión a MySQL.
import rapidexpress.util.DBConnection;

/**
 * Propósito: Implementar el acceso a datos y las operaciones CRUD para los vehículos
 * de la flota dentro del sistema RapidExpress.
 * 
 * @author User
 */
public class VehiculoDAO implements IDAO<Vehiculo, Integer> {

    // Consulta SQL para insertar un nuevo vehículo en la base de datos.
    private static final String SQL_INSERT = 
        "INSERT INTO vehiculos (placa, modelo, capacidad_kg, estado) VALUES (?, ?, ?, ?)";

    // Consulta SQL para buscar un vehículo por su identificador primario.
    private static final String SQL_SELECT_BY_ID = 
        "SELECT id, placa, modelo, capacidad_kg, estado FROM vehiculos WHERE id = ?";

    // Consulta SQL para obtener todos los vehículos registrados.
    private static final String SQL_SELECT_ALL = 
        "SELECT id, placa, modelo, capacidad_kg, estado FROM vehiculos";

    // Consulta SQL para actualizar los atributos de un vehículo existente.
    private static final String SQL_UPDATE = 
        "UPDATE vehiculos SET placa = ?, modelo = ?, capacidad_kg = ?, estado = ? WHERE id = ?";

    // Consulta SQL para eliminar un registro de vehículo mediante su ID.
    private static final String SQL_DELETE = 
        "DELETE FROM vehiculos WHERE id = ?";

    @Override
    public boolean crear(Vehiculo vehiculo) {
        // Inicia el try-with-resources garantizando el cierre de la conexión y sentencia.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {

            // Asigna la placa del vehículo al primer parámetro de la consulta.
            stmt.setString(1, vehiculo.getPlaca());
            // Asigna el modelo o descripción del vehículo.
            stmt.setString(2, vehiculo.getModelo());
            // Asigna la capacidad de carga en kilogramos como valor decimal.
            stmt.setDouble(3, vehiculo.getCapacidadKg());
            // Asigna el estado operativo del vehículo (ej. ACTIVO, MANTENIMIENTO).
            stmt.setString(4, vehiculo.getEstado());

            // Ejecuta la inserción y retorna verdadero si se modificó al menos un registro.
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Registra el error en la consola si falla la operación en MySQL.
            System.err.println("Error al crear vehículo: " + e.getMessage());
            // Retorna falso notificando que la operación no fue exitosa.
            return false;
        }
    }

    @Override
    public Vehiculo buscarPorId(Integer id) {
        // Instancia la variable donde se almacenará el resultado encontrado.
        Vehiculo vehiculo = null;

        // Abre la conexión y prepara la consulta de búsqueda por identificador.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            // Pasa el ID recibido como parámetro a la consulta SQL.
            stmt.setInt(1, id);

            // Ejecuta el query y procesa el ResultSet devuelto.
            try (ResultSet rs = stmt.executeQuery()) {
                // Si la consulta arroja una fila, procede a transformarla a objeto Java.
                if (rs.next()) {
                    vehiculo = mapearResultSetAVehiculo(rs);
                }
            }

        } catch (SQLException e) {
            // Captura la excepción y reporta el mensaje de error.
            System.err.println("Error al buscar vehículo por ID: " + e.getMessage());
        }

        // Retorna el objeto poblado o nulo si no existía el registro.
        return vehiculo;
    }

    @Override
    public List<Vehiculo> listarTodos() {
        // Inicializa la lista receptora para acumular todos los vehículos.
        List<Vehiculo> lista = new ArrayList<>();

        // Intenta establecer la conexión y consulta todas las filas de la tabla.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            // Recorre secuencialmente cada una de las filas recuperadas.
            while (rs.next()) {
                // Transforma la fila actual y la añade a la lista de respuesta.
                lista.add(mapearResultSetAVehiculo(rs));
            }

        } catch (SQLException e) {
            // Muestra en la consola de error la falla ocurrida.
            System.err.println("Error al listar vehículos: " + e.getMessage());
        }

        // Retorna la colección completa de vehículos.
        return lista;
    }

    @Override
    public boolean actualizar(Vehiculo vehiculo) {
        // Abre la conexión y prepara la sentencia UPDATE.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

            // Asigna los parámetros correspondientes a los nuevos valores.
            stmt.setString(1, vehiculo.getPlaca());
            stmt.setString(2, vehiculo.getModelo());
            stmt.setDouble(3, vehiculo.getCapacidadKg());
            stmt.setString(4, vehiculo.getEstado());
            // Asigna el identificador del vehículo en la cláusula WHERE.
            stmt.setInt(5, vehiculo.getId());

            // Retorna verdadero si la actualización afectó una fila.
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Reporta la falla al actualizar en consola.
            System.err.println("Error al actualizar vehículo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        // Inicia el bloque try-with-resources con la sentencia de borrado.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {

            // Indica la clave primaria del registro que se desea eliminar.
            stmt.setInt(1, id);
            // Retorna verdadero si el borrado afectó una fila en MySQL.
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Reporta la excepción producida en la base de datos.
            System.err.println("Error al eliminar vehículo: " + e.getMessage());
            return false;
        }
    }

    // Método privado auxiliar para mapear una fila de ResultSet a un objeto Vehiculo.
    private Vehiculo mapearResultSetAVehiculo(ResultSet rs) throws SQLException {
        // Instancia un nuevo objeto de la entidad Vehiculo.
        Vehiculo v = new Vehiculo();
        // Asigna la clave primaria recuperada.
        v.setId(rs.getInt("id"));
        // Mapea la columna de la placa vehicular.
        v.setPlaca(rs.getString("placa"));
        // Mapea el modelo o marca del vehículo.
        v.setModelo(rs.getString("modelo"));
        // Mapea el peso máximo soportado en kilogramos.
        v.setCapacidadKg(rs.getDouble("capacidad_kg"));
        // Mapea la cadena que representa el estado del vehículo.
        v.setEstado(rs.getString("estado"));
        
        // Retorna el objeto mapeado.
        return v;
    }
}