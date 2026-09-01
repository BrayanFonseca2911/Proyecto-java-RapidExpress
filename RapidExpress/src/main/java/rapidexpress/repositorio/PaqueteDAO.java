/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.repositorio;

// Importación para administrar la conexión a la base de datos MySQL.
import java.sql.Connection;
// Importación para mapear fechas en consultas SQL.
import java.sql.Date;
// Importación para ejecutar sentencias SQL preparadas y seguras.
import java.sql.PreparedStatement;
// Importación para procesar los resultados tabulares devueltos por la base de datos.
import java.sql.ResultSet;
// Importación para el manejo de excepciones de bases de datos.
import java.sql.SQLException;
// Importación para el manejo moderno de fechas introducido en Java 8.
import java.time.LocalDate;
// Importación para instanciar listas dinámicas.
import java.util.ArrayList;
// Importación para definir las firmas de retorno de las colecciones.
import java.util.List;

// Importación de la entidad Paquete del modelo de dominio.
import rapidexpress.dominio.Paquete;
// Importación del administrador de conexiones Singleton.
import rapidexpress.util.DBConnection;

/**
 * Propósito: Implementar el acceso a datos para la entidad Paquete, incluyendo
 * operaciones CRUD básicas y consultas analíticas de logística y estado.
 * 
 * @author User
 */
public class PaqueteDAO implements IDAO<Paquete, Integer> {

    // --- CONSULTAS CRUD BÁSICAS ---
    
    // Inserta un nuevo paquete en la tabla paquetes.
    private static final String SQL_INSERT = 
        "INSERT INTO paquetes (numero_guia, peso_kg, destinatario, direccion_destino, estado, id_ruta, fecha_registro, fecha_entrega) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    // Selecciona un paquete por su clave primaria (ID).
    private static final String SQL_SELECT_BY_ID = 
        "SELECT id, numero_guia, peso_kg, destinatario, direccion_destino, estado, id_ruta, fecha_registro, fecha_entrega FROM paquetes WHERE id = ?";
    // Selecciona todos los paquetes registrados.
    private static final String SQL_SELECT_ALL = 
        "SELECT id, numero_guia, peso_kg, destinatario, direccion_destino, estado, id_ruta, fecha_registro, fecha_entrega FROM paquetes";
    // Actualiza los datos completos de un paquete existente.
    private static final String SQL_UPDATE = 
        "UPDATE paquetes SET numero_guia = ?, peso_kg = ?, destinatario = ?, direccion_destino = ?, estado = ?, id_ruta = ?, fecha_registro = ?, fecha_entrega = ? WHERE id = ?";
    // Elimina un registro de paquete según su ID.
    private static final String SQL_DELETE = 
        "DELETE FROM paquetes WHERE id = ?";

    // --- CONSULTAS ESPECÍFICAS DE LOGÍSTICA ---
    
    // Busca un paquete utilizando su número de guía (tracking ID).
    private static final String SQL_SELECT_BY_TRACKING = 
        "SELECT id, numero_guia, peso_kg, destinatario, direccion_destino, estado, id_ruta, fecha_registro, fecha_entrega FROM paquetes WHERE numero_guia = ?";
    // Lista paquetes filtrados por un estado específico.
    private static final String SQL_SELECT_BY_ESTADO = 
        "SELECT id, numero_guia, peso_kg, destinatario, direccion_destino, estado, id_ruta, fecha_registro, fecha_entrega FROM paquetes WHERE estado = ?";
    // Lista exclusivamente los paquetes que se encuentran almacenados en bodega.
    private static final String SQL_SELECT_EN_BODEGA = 
        "SELECT id, numero_guia, peso_kg, destinatario, direccion_destino, estado, id_ruta, fecha_registro, fecha_entrega FROM paquetes WHERE estado = 'EN_BODEGA'";
    // Filtra los paquetes según su fecha de registro en el sistema.
    private static final String SQL_SELECT_BY_RANGO_FECHAS = 
        "SELECT id, numero_guia, peso_kg, destinatario, direccion_destino, estado, id_ruta, fecha_registro, fecha_entrega FROM paquetes WHERE fecha_registro BETWEEN ? AND ?";
    // Consulta con JOIN para obtener los paquetes entregados por un conductor específico en un rango de fechas.
    private static final String SQL_SELECT_ENTREGAS_CONDUCTOR = 
        "SELECT p.id, p.numero_guia, p.peso_kg, p.destinatario, p.direccion_destino, p.estado, p.id_ruta, p.fecha_registro, p.fecha_entrega " +
        "FROM paquetes p " +
        "INNER JOIN rutas r ON p.id_ruta = r.id " +
        "WHERE p.estado = 'ENTREGADO' AND r.id_conductor = ? AND p.fecha_entrega BETWEEN ? AND ?";
    // Actualiza únicamente el estado operativo y la fecha de entrega de un paquete.
    private static final String SQL_UPDATE_ESTADO = 
        "UPDATE paquetes SET estado = ?, fecha_entrega = ? WHERE numero_guia = ?";

    // =================================================================================
    // MÉTODOS CRUD IMPLEMENTADOS DE LA INTERFAZ IDAO
    // =================================================================================

    @Override
    public boolean crear(Paquete paquete) {
        // Inicializa la conexión y prepara la consulta de inserción.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {

            // Asigna los parámetros básicos del paquete.
            stmt.setString(1, paquete.getNumeroGuia());
            stmt.setDouble(2, paquete.getPesoKg());
            stmt.setString(3, paquete.getDestinatario());
            stmt.setString(4, paquete.getDireccionDestino());
            stmt.setString(5, paquete.getEstado());
            stmt.setInt(6, paquete.getIdRuta());
            // Asigna la fecha de registro, validando si no es nula.
            stmt.setDate(7, paquete.getFechaRegistro() != null ? Date.valueOf(paquete.getFechaRegistro()) : null);
            // Asigna la fecha de entrega, validando si no es nula.
            stmt.setDate(8, paquete.getFechaEntrega() != null ? Date.valueOf(paquete.getFechaEntrega()) : null);

            // Retorna verdadero si se insertó al menos un registro.
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            // Reporta fallos en la consola de errores.
            System.err.println("Error al registrar paquete: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Paquete buscarPorId(Integer id) {
        // Variable para almacenar el paquete encontrado.
        Paquete paquete = null;

        // Prepara la consulta buscando por identificador primario.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            // Pasa el ID recibido como parámetro.
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                // Si existe el registro, lo mapea a un objeto Paquete.
                if (rs.next()) {
                    paquete = mapearResultSetAPaquete(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar paquete por ID: " + e.getMessage());
        }

        // Retorna el paquete o null.
        return paquete;
    }

    @Override
    public List<Paquete> listarTodos() {
        // Lista receptora para acumular los paquetes.
        List<Paquete> lista = new ArrayList<>();

        // Consulta masiva de todos los paquetes.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            // Itera el ResultSet agregando cada paquete mapeado.
            while (rs.next()) {
                lista.add(mapearResultSetAPaquete(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar paquetes: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public boolean actualizar(Paquete paquete) {
        // Prepara la consulta para actualizar todos los campos de un paquete.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

            // Asigna los parámetros actualizados.
            stmt.setString(1, paquete.getNumeroGuia());
            stmt.setDouble(2, paquete.getPesoKg());
            stmt.setString(3, paquete.getDestinatario());
            stmt.setString(4, paquete.getDireccionDestino());
            stmt.setString(5, paquete.getEstado());
            stmt.setInt(6, paquete.getIdRuta());
            stmt.setDate(7, paquete.getFechaRegistro() != null ? Date.valueOf(paquete.getFechaRegistro()) : null);
            stmt.setDate(8, paquete.getFechaEntrega() != null ? Date.valueOf(paquete.getFechaEntrega()) : null);
            // Asigna el ID al WHERE de la consulta.
            stmt.setInt(9, paquete.getId());

            // Retorna verdadero si modificó el registro.
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar paquete: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        // Prepara la eliminación física del registro.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {

            // Asigna el ID del registro a borrar.
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar paquete: " + e.getMessage());
            return false;
        }
    }

    // =================================================================================
    // MÉTODOS ESPECIALIZADOS DE LOGÍSTICA
    // =================================================================================

    /**
     * Busca un paquete por su número de guía único de rastreo.
     * 
     * @param trackingId Número de guía del paquete.
     * @return El paquete correspondiente o null si no se encuentra.
     */
    public Paquete buscarPorTrackingId(String trackingId) {
        Paquete paquete = null;

        // Prepara la consulta filtrando por numero_guia.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_TRACKING)) {

            // Pasa el string del tracking ID al parámetro SQL.
            stmt.setString(1, trackingId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Mapea y guarda el resultado devuelto.
                    paquete = mapearResultSetAPaquete(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar por tracking ID: " + e.getMessage());
        }

        return paquete;
    }

    /**
     * Lista todos los paquetes que se encuentren en un estado específico.
     * Nota: Se utiliza String para "estado" manteniendo la coherencia de la base de datos.
     * 
     * @param estado Cadena de texto que representa el estado (ej. "EN_RUTA").
     * @return Lista de paquetes filtrada.
     */
    public List<Paquete> listarPorEstado(String estado) {
        List<Paquete> lista = new ArrayList<>();

        // Prepara la consulta filtrando por la columna estado.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ESTADO)) {

            // Asigna el estado a buscar.
            stmt.setString(1, estado);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearResultSetAPaquete(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar por estado: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Obtiene los paquetes que actualmente están almacenados en bodega.
     * 
     * @return Lista de paquetes en estado 'EN_BODEGA'.
     */
    public List<Paquete> listarEnBodega() {
        List<Paquete> lista = new ArrayList<>();

        // Ejecuta la consulta constante que filtra los paquetes en bodega.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_EN_BODEGA);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearResultSetAPaquete(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar paquetes en bodega: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Filtra los paquetes basándose en su fecha de registro en el sistema.
     * 
     * @param inicio Fecha inicial del filtro.
     * @param fin Fecha final del filtro.
     * @return Lista de paquetes registrados en el rango.
     */
    public List<Paquete> listarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        List<Paquete> lista = new ArrayList<>();

        // Prepara la consulta con cláusula BETWEEN.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_RANGO_FECHAS)) {

            // Convierte losLocalDate a java.sql.Date para el PreparedStatement.
            stmt.setDate(1, inicio != null ? Date.valueOf(inicio) : null);
            stmt.setDate(2, fin != null ? Date.valueOf(fin) : null);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearResultSetAPaquete(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar por rango de fechas: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Obtiene las entregas efectivas ('ENTREGADO') realizadas por un conductor en un período.
     * 
     * @param conductorId Identificador clave del conductor.
     * @param inicio Fecha inicial de la entrega.
     * @param fin Fecha final de la entrega.
     * @return Lista de paquetes entregados exitosamente.
     */
    public List<Paquete> listarEntregasPorConductor(Integer conductorId, LocalDate inicio, LocalDate fin) {
        List<Paquete> lista = new ArrayList<>();

        // Ejecuta la consulta compleja con INNER JOIN entre paquetes y rutas.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ENTREGAS_CONDUCTOR)) {

            // Asigna el ID del conductor filtrando desde la tabla rutas.
            stmt.setInt(1, conductorId);
            // Asigna el inicio del intervalo de la fecha de entrega.
            stmt.setDate(2, inicio != null ? Date.valueOf(inicio) : null);
            // Asigna el final del intervalo de la fecha de entrega.
            stmt.setDate(3, fin != null ? Date.valueOf(fin) : null);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearResultSetAPaquete(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al listar entregas por conductor: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Actualiza rápidamente el estado y la fecha de entrega de un paquete en circulación.
     * 
     * @param trackingId Número de guía a actualizar.
     * @param estado Nuevo estado operativo (ej. "ENTREGADO").
     * @return Verdadero si la actualización fue exitosa.
     */
    public boolean actualizarEstado(String trackingId, String estado) {
        // Prepara la consulta UPDATE simplificada.
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_ESTADO)) {

            // Asigna el nuevo estado.
            stmt.setString(1, estado);
            
            // Si el estado es entregado, marca la fecha actual, sino, puede quedar null.
            // (Para simplificar, asignamos la fecha del sistema al momento de actualizar)
            stmt.setDate(2, Date.valueOf(LocalDate.now())); 
            
            // Asigna el número de guía para la cláusula WHERE.
            stmt.setString(3, trackingId);

            // Retorna verdadero si afectó un registro.
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar el estado del paquete: " + e.getMessage());
            return false;
        }
    }

    // =================================================================================
    // MÉTODOS AUXILIARES
    // =================================================================================

    // Convierte un registro del ResultSet a un objeto Paquete, ahora incluyendo las fechas.
    private Paquete mapearResultSetAPaquete(ResultSet rs) throws SQLException {
        // Instancia un nuevo objeto Paquete.
        Paquete p = new Paquete();
        
        // Mapea campos básicos.
        p.setId(rs.getInt("id"));
        p.setNumeroGuia(rs.getString("numero_guia"));
        p.setPesoKg(rs.getDouble("peso_kg"));
        p.setDestinatario(rs.getString("destinatario"));
        p.setDireccionDestino(rs.getString("direccion_destino"));
        p.setEstado(rs.getString("estado"));
        p.setIdRuta(rs.getInt("id_ruta"));
        
        // Mapea la fecha de registro asegurando que no sea nula en la base de datos.
        Date dbFechaRegistro = rs.getDate("fecha_registro");
        if (dbFechaRegistro != null) {
            p.setFechaRegistro(dbFechaRegistro.toLocalDate());
        }

        // Mapea la fecha de entrega asegurando que no sea nula.
        Date dbFechaEntrega = rs.getDate("fecha_entrega");
        if (dbFechaEntrega != null) {
            p.setFechaEntrega(dbFechaEntrega.toLocalDate());
        }

        // Retorna la instancia de Paquete poblada.
        return p;
    }
}