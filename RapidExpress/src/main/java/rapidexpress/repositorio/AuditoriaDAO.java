/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.repositorio;

// Importación de la interfaz Connection para manejar la sesión con MySQL.
import java.sql.Connection;
// Importación de Date para manejar filtros de búsqueda por fecha en SQL.
import java.sql.Date;
// Importación de PreparedStatement para ejecutar consultas SQL parametrizadas de forma segura.
import java.sql.PreparedStatement;
// Importación de ResultSet para iterar sobre los resultados devueltos por MySQL.
import java.sql.ResultSet;
// Importación de SQLException para capturar y gestionar errores de acceso a datos.
import java.sql.SQLException;
// Importación de Timestamp para convertir fechas con hora exacta a formato SQL.
import java.sql.Timestamp;
// Importación de LocalDate para permitir el paso de fechas desde Java 8+.
import java.time.LocalDate;
// Importación de ArrayList para instanciar listas dinámicas de objetos.
import java.util.ArrayList;
// Importación de la interfaz List para definir el retorno de colecciones.
import java.util.List;

// Importación del modelo de dominio Auditoria desde el paquete correspondiente.
import rapidexpress.auditoria.Auditoria;
// Importación de la utilidad DBConnection para obtener conexiones activas con MySQL.
import rapidexpress.util.DBConnection;

/**
 * Propósito: Administrar el acceso y la persistencia de datos de la entidad Auditoría,
 * registrando eventos del sistema y permitiendo consultas filtradas por fecha o acción.
 * 
 * @author User
 */
public class AuditoriaDAO {

    // Sentencia SQL estática encargada de insertar un nuevo registro de auditoría.
    private static final String SQL_INSERT = 
        "INSERT INTO auditoria (fecha, usuario, accion, tabla_afectada, registro_id, detalle) VALUES (?, ?, ?, ?, ?, ?)";

    // Sentencia SQL para filtrar y obtener auditorías cuya fecha coincida con la consultada.
    private static final String SQL_SELECT_BY_FECHA = 
        "SELECT id, fecha, usuario, accion, tabla_afectada, registro_id, detalle FROM auditoria WHERE DATE(fecha) = ? ORDER BY fecha DESC";

    // Sentencia SQL para filtrar registros según el tipo de acción ejecutada.
    private static final String SQL_SELECT_BY_ACCION = 
        "SELECT id, fecha, usuario, accion, tabla_afectada, registro_id, detalle FROM auditoria WHERE accion = ? ORDER BY fecha DESC";

    // Guarda de forma permanente una entidad Auditoria en la base de datos.
    public boolean guardar(Auditoria a) {
        // Abre la conexión y prepara la sentencia SQL dentro de un try-with-resources para cierre automático.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {

            // Asigna la fecha/hora actual convirtiendo LocalDateTime o asignando el tiempo del sistema si es nulo.
            stmt.setTimestamp(1, a.getFecha() != null ? Timestamp.valueOf(a.getFecha()) : new Timestamp(System.currentTimeMillis()));
            // Establece el nombre del usuario responsable de la acción.
            stmt.setString(2, a.getUsuario());
            // Establece el identificador o nombre de la acción realizada.
            stmt.setString(3, a.getAccion());
            // Establece el nombre de la tabla de la base de datos impactada.
            stmt.setString(4, a.getTablaAfectada());
            // Establece la clave primaria del registro afectado.
            stmt.setInt(5, a.getRegistroId());
            // Establece el detalle explicativo de la operación.
            stmt.setString(6, a.getDetalle());

            // Ejecuta la modificación en MySQL y retorna verdadero si afectó al menos 1 fila.
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Imprime en el canal de errores la excepción de SQL capturada.
            System.err.println("Error al guardar registro de auditoría: " + e.getMessage());
            // Retorna falso para notificar que la inserción no fue exitosa.
            return false;
        }
    }

    // Consulta los eventos de auditoría filtrando por una fecha SQL específica.
    public List<Auditoria> listarPorFecha(Date fecha) {
        // Instancia la lista receptora donde se agregarán las auditorías encontradas.
        List<Auditoria> lista = new ArrayList<>();

        // Inicia la conexión y prepara la consulta por fecha.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_FECHA)) {

            // Asigna la fecha al parámetro dinámico de la consulta.
            stmt.setDate(1, fecha);

            // Ejecuta la consulta y lee el cursor de resultados.
            try (ResultSet rs = stmt.executeQuery()) {
                // Itera mientras existan filas por procesar en el conjunto de resultados.
                while (rs.next()) {
                    // Mapea la fila actual a un objeto Auditoria y lo agrega a la lista.
                    lista.add(mapearResultSetAAuditoria(rs));
                }
            }

        } catch (SQLException e) {
            // Reporta la falla producida durante la lectura.
            System.err.println("Error al listar auditorías por fecha: " + e.getMessage());
        }

        // Retorna la lista con los registros procesados.
        return lista;
    }

    // Sobrecarga conveniente del método para recibir objetos de tipo LocalDate.
    public List<Auditoria> listarPorFecha(LocalDate fecha) {
        // Convierte el objeto LocalDate al Date de SQL y ejecuta el método de búsqueda principal.
        return fecha != null ? listarPorFecha(Date.valueOf(fecha)) : new ArrayList<>();
    }

    // Consulta los eventos de auditoría filtrando por el identificador de la acción.
    public List<Auditoria> listarPorAccion(String accion) {
        // Instancia la lista para almacenar los registros resultantes.
        List<Auditoria> lista = new ArrayList<>();

        // Abre la sesión con la base de datos y prepara la consulta por acción.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ACCION)) {

            // Asigna la cadena de texto con el nombre de la acción al parámetro SQL.
            stmt.setString(1, accion);

            // Obtiene el conjunto de resultados ejecutando la consulta SELECT.
            try (ResultSet rs = stmt.executeQuery()) {
                // Recorre cada uno de los registros devueltos por MySQL.
                while (rs.next()) {
                    // Transforma la fila en objeto Java y lo incluye en la colección.
                    lista.add(mapearResultSetAAuditoria(rs));
                }
            }

        } catch (SQLException e) {
            // Informa el error en consola en caso de fallo.
            System.err.println("Error al listar auditorías por acción: " + e.getMessage());
        }

        // Retorna la lista resultante.
        return lista;
    }

    // Método auxiliar privado que transforma un ResultSet en una instancia de Auditoria.
    private Auditoria mapearResultSetAAuditoria(ResultSet rs) throws SQLException {
        // Crea una nueva instancia de la clase de modelo Auditoria.
        Auditoria a = new Auditoria();
        // Asigna el identificador numérico recuperado.
        a.setId(rs.getInt("id"));
        
        // Extrae el dato Timestamp de la columna fecha.
        Timestamp ts = rs.getTimestamp("fecha");
        // Verifica que no sea nulo antes de transformar el tipo.
        if (ts != null) {
            // Convierte Timestamp a LocalDateTime para el atributo del modelo.
            a.setFecha(ts.toLocalDateTime());
        }
        
        // Mapea la columna usuario.
        a.setUsuario(rs.getString("usuario"));
        // Mapea la columna accion.
        a.setAccion(rs.getString("accion"));
        // Mapea la columna tabla_afectada.
        a.setTablaAfectada(rs.getString("tabla_afectada"));
        // Mapea la columna registro_id.
        a.setRegistroId(rs.getInt("registro_id"));
        // Mapea el detalle explicativo.
        a.setDetalle(rs.getString("detalle"));
        
        // Retorna el objeto completamente construido.
        return a;
    }
}