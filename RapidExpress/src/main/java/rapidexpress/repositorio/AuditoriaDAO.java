package rapidexpress.repositorio;

import rapidexpress.auditoria.Auditoria;
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos para la entidad Auditoria.
 * 
 * @author User
 */
public class AuditoriaDAO {
    
    private final DBConnection dbConnection;
    
    public AuditoriaDAO() {
        try {
            this.dbConnection = DBConnection.getInstance();
        } catch (DataBaseException e) {
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo inicializar AuditoriaDAO", e);
        }
    }
    
    /**
     * Guarda un registro de auditoría
     * @param auditoria Registro a guardar
     * @return true si se guardó correctamente
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public boolean guardar(Auditoria auditoria) throws DataBaseException {
        String sql = "INSERT INTO auditoria (fecha, usuario, accion, tabla_afectada, " +
                     "registro_id, detalle, ip) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(auditoria.getFecha()));
            stmt.setString(2, auditoria.getUsuario());
            stmt.setString(3, auditoria.getAccion());
            stmt.setString(4, auditoria.getTablaAfectada());
            stmt.setString(5, auditoria.getRegistroId());
            stmt.setString(6, auditoria.getDetalle());
            stmt.setString(7, auditoria.getIp());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    auditoria.setId(keys.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            throw new DataBaseException("INSERT", "auditoria", e);
        }
    }
    
    /**
     * Lista registros de auditoría por fecha
     * @param fecha Fecha a filtrar
     * @return Lista de registros de auditoría
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Auditoria> listarPorFecha(LocalDateTime fecha) throws DataBaseException {
        String sql = "SELECT * FROM auditoria WHERE DATE(fecha) = DATE(?) ORDER BY fecha DESC";
        List<Auditoria> auditorias = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(fecha));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                auditorias.add(mapearAuditoria(rs));
            }
            return auditorias;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "auditoria", e);
        }
    }
    
    /**
     * Lista registros de auditoría por acción
     * @param accion Acción a filtrar
     * @return Lista de registros de auditoría
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Auditoria> listarPorAccion(String accion) throws DataBaseException {
        String sql = "SELECT * FROM auditoria WHERE accion = ? ORDER BY fecha DESC";
        List<Auditoria> auditorias = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, accion);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                auditorias.add(mapearAuditoria(rs));
            }
            return auditorias;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "auditoria", e);
        }
    }
    
    /**
     * Lista todos los registros de auditoría
     * @return Lista de todos los registros
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Auditoria> listarTodos() throws DataBaseException {
        String sql = "SELECT * FROM auditoria ORDER BY fecha DESC LIMIT 100";
        List<Auditoria> auditorias = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                auditorias.add(mapearAuditoria(rs));
            }
            return auditorias;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "auditoria", e);
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto Auditoria
     */
    private Auditoria mapearAuditoria(ResultSet rs) throws SQLException {
        Auditoria auditoria = new Auditoria();
        auditoria.setId(rs.getInt("id"));
        
        Timestamp fecha = rs.getTimestamp("fecha");
        if (fecha != null) {
            auditoria.setFecha(fecha.toLocalDateTime());
        }
        
        auditoria.setUsuario(rs.getString("usuario"));
        auditoria.setAccion(rs.getString("accion"));
        auditoria.setTablaAfectada(rs.getString("tabla_afectada"));
        auditoria.setRegistroId(rs.getString("registro_id"));
        auditoria.setDetalle(rs.getString("detalle"));
        auditoria.setIp(rs.getString("ip"));
        
        return auditoria;
    }
}