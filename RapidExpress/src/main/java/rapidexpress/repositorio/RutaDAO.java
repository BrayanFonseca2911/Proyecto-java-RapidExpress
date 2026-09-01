/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.repositorio;

// Importación para la gestión de conexiones JDBC con la base de datos MySQL.
import java.sql.Connection;
// Importación para mapear las fechas al formato reconocido por SQL.
import java.sql.Date;
// Importación para crear sentencias preparadas de SQL parametrizadas.
import java.sql.PreparedStatement;
// Importación para procesar las lecturas de filas devueltas por MySQL.
import java.sql.ResultSet;
// Importación para gestionar excepciones nativas de la API JDBC.
import java.sql.SQLException;
// Importación para instanciar listas dinámicas de Java.
import java.util.ArrayList;
// Importación para definir el contrato de retorno de colecciones.
import java.util.List;

// Importación del modelo de dominio Ruta.
import rapidexpress.dominio.Ruta;
// Importación de la excepción de persistencia personalizada.
import rapidexpress.excepciones.DataBaseException;
// Importación del patrón Singleton para obtener conexiones.
import rapidexpress.util.DBConnection;

/**
 * Propósito: Gestionar la persistencia y operaciones de acceso a datos 
 * relativas a las rutas de distribución y logística de la flota.
 * 
 * @author User
 */
public class RutaDAO implements IDAO<Ruta, Integer> {

    // Consulta SQL para insertar una nueva ruta en el sistema.
    private static final String SQL_INSERT = 
        "INSERT INTO rutas (origen, destino, fecha, id_vehiculo, id_conductor, estado) VALUES (?, ?, ?, ?, ?, ?)";

    // Consulta SQL para realizar la búsqueda de una ruta por su ID.
    private static final String SQL_SELECT_BY_ID = 
        "SELECT id, origen, destino, fecha, id_vehiculo, id_conductor, estado FROM rutas WHERE id = ?";

    // Consulta SQL para consultar todas las rutas registradas.
    private static final String SQL_SELECT_ALL = 
        "SELECT id, origen, destino, fecha, id_vehiculo, id_conductor, estado FROM rutas";

    // Consulta SQL para modificar los datos de una ruta existente.
    private static final String SQL_UPDATE = 
        "UPDATE rutas SET origen = ?, destino = ?, fecha = ?, id_vehiculo = ?, id_conductor = ?, estado = ? WHERE id = ?";

    // Consulta SQL para realizar el borrado físico de una ruta.
    private static final String SQL_DELETE = 
        "DELETE FROM rutas WHERE id = ?";

    @Override
    public boolean guardar(Ruta ruta) throws DataBaseException {
        // Inicia el bloque try-with-resources que cierra la conexión y la sentencia automáticamente.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {

            // Asigna la ciudad o dirección de origen.
            stmt.setString(1, ruta.getOrigen());
            // Asigna la ciudad o dirección de destino.
            stmt.setString(2, ruta.getDestino());
            // Realiza la conversión de LocalDate a java.sql.Date validando nulos.
            stmt.setDate(3, ruta.getFecha() != null ? Date.valueOf(ruta.getFecha()) : null);
            // Asigna la clave foránea del vehículo asignado a la ruta.
            stmt.setInt(4, ruta.getIdVehiculo());
            // Asigna la clave foránea del conductor a cargo de la ruta.
            stmt.setInt(5, ruta.getIdConductor());
            // Asigna el estado operativo de la ruta (ej. PROGRAMADA, EN_PROCESO, FINALIZADA).
            stmt.setString(6, ruta.getEstado());

            // Retorna verdadero si se insertó al menos una fila.
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Relanza la excepción encapsulada en DataBaseException.
            throw new DataBaseException("Error al guardar la ruta: " + e.getMessage(), e);
        }
    }

    @Override
    public Ruta buscarPorId(Integer id) throws DataBaseException {
        // Declaración del objeto resultado.
        Ruta ruta = null;

        // Abre la conexión y prepara la consulta filtrada por la clave primaria.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            // Asigna el identificador a la consulta parametrizada.
            stmt.setInt(1, id);

            // Ejecuta el query y procesa los resultados devueltos.
            try (ResultSet rs = stmt.executeQuery()) {
                // Si existe un resultado, transfiere las columnas al objeto entidad.
                if (rs.next()) {
                    ruta = mapearResultSetARuta(rs);
                }
            }

        } catch (SQLException e) {
            // Notifica el error de lectura envolviéndolo en DataBaseException.
            throw new DataBaseException("Error al buscar ruta por ID: " + e.getMessage(), e);
        }

        // Retorna la ruta recuperada o null.
        return ruta;
    }

    @Override
    public List<Ruta> listarTodos() throws DataBaseException {
        // Inicializa la lista receptora para acumular todas las rutas.
        List<Ruta> lista = new ArrayList<>();

        // Consulta masiva de la tabla rutas.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            // Itera secuencialmente sobre el conjunto de resultados.
            while (rs.next()) {
                // Mapea y agrega la ruta a la lista.
                lista.add(mapearResultSetARuta(rs));
            }

        } catch (SQLException e) {
            // Captura el fallo y lanza la excepción del sistema.
            throw new DataBaseException("Error al listar las rutas: " + e.getMessage(), e);
        }

        // Retorna la lista con los registros de rutas.
        return lista;
    }

    @Override
    public boolean actualizar(Ruta ruta) throws DataBaseException {
        // Ejecuta la modificación del registro por ID.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

            // Setea los campos con los nuevos valores recibidos.
            stmt.setString(1, ruta.getOrigen());
            stmt.setString(2, ruta.getDestino());
            stmt.setDate(3, ruta.getFecha() != null ? Date.valueOf(ruta.getFecha()) : null);
            stmt.setInt(4, ruta.getIdVehiculo());
            stmt.setInt(5, ruta.getIdConductor());
            stmt.setString(6, ruta.getEstado());
            // Asigna el ID para especificar la fila en el WHERE.
            stmt.setInt(7, ruta.getId());

            // Retorna verdadero si afectó un registro.
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Propaga la excepción hacia la capa superior.
            throw new DataBaseException("Error al actualizar la ruta: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(Integer id) throws DataBaseException {
        // Prepara la consulta para borrar la fila según su ID.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {

            // Asigna el parámetro ID.
            stmt.setInt(1, id);
            // Ejecuta el borrado y confirma el resultado.
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Notifica la falla de eliminación.
            throw new DataBaseException("Error al eliminar la ruta: " + e.getMessage(), e);
        }
    }

    // Método auxiliar privado que convierte las columnas de una fila en un objeto Ruta.
    private Ruta mapearResultSetARuta(ResultSet rs) throws SQLException {
        // Instancia un nuevo objeto entidad.
        Ruta r = new Ruta();
        // Setea el identificador único.
        r.setId(rs.getInt("id"));
        // Setea el origen.
        r.setOrigen(rs.getString("origen"));
        // Setea el destino.
        r.setDestino(rs.getString("destino"));
        
        // Lee la fecha de la base de datos.
        Date sqlDate = rs.getDate("fecha");
        // Si no es nula, la mapea a LocalDate.
        if (sqlDate != null) {
            r.setFecha(sqlDate.toLocalDate());
        }
        
        // Setea las claves foráneas y el estado.
        r.setIdVehiculo(rs.getInt("id_vehiculo"));
        r.setIdConductor(rs.getInt("id_conductor"));
        r.setEstado(rs.getString("estado"));
        
        // Deuelve el objeto instanciado.
        return r;
    }
}