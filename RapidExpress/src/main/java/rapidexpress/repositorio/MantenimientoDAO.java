/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.repositorio;

// Importación de Connection para gestionar la sesión con la base de datos MySQL.
import java.sql.Connection;
// Importación de Date para mapear las fechas de mantenimiento en SQL.
import java.sql.Date;
// Importación de PreparedStatement para ejecutar sentencias SQL preparadas y seguras.
import java.sql.PreparedStatement;
// Importación de ResultSet para procesar el conjunto de resultados obtenidos.
import java.sql.ResultSet;
// Importación de SQLException para el tratamiento de errores de acceso a datos.
import java.sql.SQLException;
// Importación de LocalDate para el manejo moderno de fechas en Java.
import java.time.LocalDate;
// Importación de ArrayList para construir listas de mantenimientos.
import java.util.ArrayList;
// Importación de la interfaz List para definir las colecciones de retorno.
import java.util.List;

// Importación del modelo de dominio Mantenimiento.
import rapidexpress.dominio.Mantenimiento;
// Importación de DBConnection para utilizar la conexión singleton.
import rapidexpress.util.DBConnection;

/**
 * Propósito: Administrar la persistencia de los mantenimientos preventivos y correctivos
 * asignados a los vehículos de la flota en la base de datos.
 * 
 * @author User
 */
public class MantenimientoDAO implements IDAO<Mantenimiento, Integer> {

    // Consulta SQL para registrar un nuevo mantenimiento.
    private static final String SQL_INSERT = 
        "INSERT INTO mantenimientos (id_vehiculo, fecha, descripcion, costo, tipo) VALUES (?, ?, ?, ?, ?)";

    // Consulta SQL para buscar un mantenimiento por su clave primaria.
    private static final String SQL_SELECT_BY_ID = 
        "SELECT id, id_vehiculo, fecha, descripcion, costo, tipo FROM mantenimientos WHERE id = ?";

    // Consulta SQL para obtener todos los registros de mantenimiento.
    private static final String SQL_SELECT_ALL = 
        "SELECT id, id_vehiculo, fecha, descripcion, costo, tipo FROM mantenimientos";

    // Consulta SQL para actualizar los datos de un mantenimiento existente.
    private static final String SQL_UPDATE = 
        "UPDATE mantenimientos SET id_vehiculo = ?, fecha = ?, descripcion = ?, costo = ?, tipo = ? WHERE id = ?";

    // Consulta SQL para borrar un registro de mantenimiento por ID.
    private static final String SQL_DELETE = 
        "DELETE FROM mantenimientos WHERE id = ?";

    // Consulta SQL personalizada para obtener mantenimientos asociados a un vehículo específico.
    private static final String SQL_SELECT_BY_VEHICULO = 
        "SELECT id, id_vehiculo, fecha, descripcion, costo, tipo FROM mantenimientos WHERE id_vehiculo = ? ORDER BY fecha DESC";

    // Consulta SQL personalizada para filtrar mantenimientos dentro de un rango de fechas.
    private static final String SQL_SELECT_BY_RANGO_FECHAS = 
        "SELECT id, id_vehiculo, fecha, descripcion, costo, tipo FROM mantenimientos WHERE fecha BETWEEN ? AND ? ORDER BY fecha DESC";

    @Override
    public boolean crear(Mantenimiento mantenimiento) {
        // Abre conexión e instancia el PreparedStatement dentro del bloque try-with-resources.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {

            // Asigna la clave foránea del vehículo asociado al mantenimiento.
            stmt.setInt(1, mantenimiento.getIdVehiculo());
            // Convierte la fecha de LocalDate a java.sql.Date para enviarla a MySQL.
            stmt.setDate(2, mantenimiento.getFecha() != null ? Date.valueOf(mantenimiento.getFecha()) : null);
            // Asigna la descripción del servicio realizado.
            stmt.setString(3, mantenimiento.getDescripcion());
            // Asigna el costo total del mantenimiento.
            stmt.setDouble(4, mantenimiento.getCosto());
            // Asigna el tipo de servicio (ej. PREVENTIVO, CORRECTIVO).
            stmt.setString(5, mantenimiento.getTipo());

            // Retorna verdadero si la fila fue insertada exitosamente.
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Imprime el detalle de la excepción capturada en la consola de errores.
            System.err.println("Error al registrar mantenimiento: " + e.getMessage());
            // Retorna falso indicando que la operación falló.
            return false;
        }
    }

    @Override
    public Mantenimiento buscarPorId(Integer id) {
        // Instancia la variable donde se almacenará el objeto recuperado.
        Mantenimiento mantenimiento = null;

        // Establece conexión e inicia la consulta filtrada por ID.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            // Asigna el identificador clave a la consulta.
            stmt.setInt(1, id);

            // Ejecuta el query y procesa el ResultSet devuelto.
            try (ResultSet rs = stmt.executeQuery()) {
                // Si existe el registro, mapea las columnas al objeto Mantenimiento.
                if (rs.next()) {
                    mantenimiento = mapearResultSetAMantenimiento(rs);
                }
            }

        } catch (SQLException e) {
            // Notifica la falla de lectura en la consola.
            System.err.println("Error al buscar mantenimiento por ID: " + e.getMessage());
        }

        // Retorna el objeto poblado o null si no existe.
        return mantenimiento;
    }

    @Override
    public List<Mantenimiento> listarTodos() {
        // Inicializa la lista receptora para acumular los mantenimientos.
        List<Mantenimiento> lista = new ArrayList<>();

        // Consulta la totalidad de filas registradas en la tabla mantenimientos.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            // Recorre secuencialmente cada registro retornado por MySQL.
            while (rs.next()) {
                // Transforma la fila actual y la añade a la lista.
                lista.add(mapearResultSetAMantenimiento(rs));
            }

        } catch (SQLException e) {
            // Reporta la excepción surgida durante el listado general.
            System.err.println("Error al listar mantenimientos: " + e.getMessage());
        }

        // Retorna la colección completa de mantenimientos.
        return lista;
    }

    @Override
    public boolean actualizar(Mantenimiento mantenimiento) {
        // Inicia el bloque try para ejecutar la sentencia UPDATE.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

            // Asigna los valores actualizados de la entidad.
            stmt.setInt(1, mantenimiento.getIdVehiculo());
            stmt.setDate(2, mantenimiento.getFecha() != null ? Date.valueOf(mantenimiento.getFecha()) : null);
            stmt.setString(3, mantenimiento.getDescripcion());
            stmt.setDouble(4, mantenimiento.getCosto());
            stmt.setString(5, mantenimiento.getTipo());
            // Asigna el ID del registro a modificar en la cláusula WHERE.
            stmt.setInt(6, mantenimiento.getId());

            // Retorna verdadero si afectó al menos una fila.
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Informa el error de actualización producido.
            System.err.println("Error al actualizar mantenimiento: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        // Prepara la eliminación del registro mediante su ID.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {

            // Pasa el ID del mantenimiento como parámetro.
            stmt.setInt(1, id);
            // Retorna verdadero si el registro fue borrado.
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Imprime el mensaje de error si ocurre un fallo en la base de datos.
            System.err.println("Error al eliminar mantenimiento: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el historial de mantenimientos registrados para un vehículo en específico.
     * 
     * @param vehiculoId ID del vehículo a consultar.
     * @return Lista de mantenimientos ordenados por fecha descendente.
     */
    public List<Mantenimiento> listarPorVehiculo(Integer vehiculoId) {
        // Inicializa la lista receptora de resultados.
        List<Mantenimiento> lista = new ArrayList<>();

        // Ejecuta la consulta filtrada por id_vehiculo.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_VEHICULO)) {

            // Pasa el ID del vehículo a la consulta.
            stmt.setInt(1, vehiculoId);

            try (ResultSet rs = stmt.executeQuery()) {
                // Itera los registros encontrados y los mapea a objetos Java.
                while (rs.next()) {
                    lista.add(mapearResultSetAMantenimiento(rs));
                }
            }

        } catch (SQLException e) {
            // Muestra en consola el error durante el filtrado por vehículo.
            System.err.println("Error al listar mantenimientos por vehículo: " + e.getMessage());
        }

        // Retorna la lista de mantenimientos del vehículo.
        return lista;
    }

    /**
     * Obtiene los mantenimientos ejecutados dentro de un rango de fechas.
     * 
     * @param inicio Fecha inicial del rango.
     * @param fin Fecha final del rango.
     * @return Lista de mantenimientos comprendidos en el intervalo.
     */
    public List<Mantenimiento> listarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        // Inicializa la lista para guardar las coincidencias.
        List<Mantenimiento> lista = new ArrayList<>();

        // Prepara la consulta SQL con la cláusula BETWEEN.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_RANGO_FECHAS)) {

            // Asigna los parámetros de fecha inicio y fin.
            stmt.setDate(1, inicio != null ? Date.valueOf(inicio) : null);
            stmt.setDate(2, fin != null ? Date.valueOf(fin) : null);

            try (ResultSet rs = stmt.executeQuery()) {
                // Recorre y agrega cada elemento resultante a la lista.
                while (rs.next()) {
                    lista.add(mapearResultSetAMantenimiento(rs));
                }
            }

        } catch (SQLException e) {
            // Notifica la excepción surgida durante la lectura por rango de fechas.
            System.err.println("Error al listar mantenimientos por rango de fechas: " + e.getMessage());
        }

        // Retorna los mantenimientos encontrados en el rango especificado.
        return lista;
    }

    // Método privado auxiliar que transforma las columnas de un ResultSet a un objeto Mantenimiento.
    private Mantenimiento mapearResultSetAMantenimiento(ResultSet rs) throws SQLException {
        // Crea una nueva instancia de la clase Mantenimiento.
        Mantenimiento m = new Mantenimiento();
        // Asigna el identificador único.
        m.setId(rs.getInt("id"));
        // Asigna la clave foránea del vehículo.
        m.setIdVehiculo(rs.getInt("id_vehiculo"));
        
        // Extrae el objeto Date de la columna fecha de SQL.
        Date sqlDate = rs.getDate("fecha");
        // Valida que el dato no sea nulo antes de hacer la conversión a LocalDate.
        if (sqlDate != null) {
            m.setFecha(sqlDate.toLocalDate());
        }
        
        // Asigna el detalle textual de las intervenciones.
        m.setDescripcion(rs.getString("descripcion"));
        // Asigna el costo en valor decimal.
        m.setCosto(rs.getDouble("costo"));
        // Asigna la clasificación o tipo del mantenimiento.
        m.setTipo(rs.getString("tipo"));
        
        // Retorna el objeto Mantenimiento completamente mapeado.
        return m;
    }
}