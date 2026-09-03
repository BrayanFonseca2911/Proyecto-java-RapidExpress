package rapidexpress.repositorio;

import rapidexpress.dominio.Paquete;
import rapidexpress.dominio.Remitente;
import rapidexpress.dominio.Destinatario;
import rapidexpress.enums.EstadoPaquete;
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos para la entidad Paquete.
 * 
 * @author User
 */
public class PaqueteDAO implements IPaqueteDAO {
    
    private final DBConnection dbConnection;
    
    public PaqueteDAO() {
    try {
        this.dbConnection = DBConnection.getInstance();
    } catch (DataBaseException e) {
        System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
        throw new RuntimeException("No se pudo inicializar PaqueteDAO", e);
    }
}
    
    @Override
    public Paquete buscarPorId(Integer id) throws DataBaseException {
        String sql = "SELECT p.*, r.nombre as remitente_nombre, r.direccion as remitente_direccion, " +
                     "r.telefono as remitente_telefono, d.nombre as destinatario_nombre, " +
                     "d.direccion as destinatario_direccion, d.telefono as destinatario_telefono " +
                     "FROM paquete p " +
                     "LEFT JOIN remitente r ON p.remitente_id = r.id " +
                     "LEFT JOIN destinatario d ON p.destinatario_id = d.id " +
                     "WHERE p.id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearPaquete(rs);
            }
            return null;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "paquete", e);
        }
    }
    
    /**
     * Busca un paquete por su tracking ID
     * @param trackingId ID de tracking del paquete
     * @return Paquete encontrado o null
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public Paquete buscarPorTrackingId(String trackingId) throws DataBaseException {
        String sql = "SELECT p.*, r.nombre as remitente_nombre, r.direccion as remitente_direccion, " +
                     "r.telefono as remitente_telefono, d.nombre as destinatario_nombre, " +
                     "d.direccion as destinatario_direccion, d.telefono as destinatario_telefono " +
                     "FROM paquete p " +
                     "LEFT JOIN remitente r ON p.remitente_id = r.id " +
                     "LEFT JOIN destinatario d ON p.destinatario_id = d.id " +
                     "WHERE p.tracking_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, trackingId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearPaquete(rs);
            }
            return null;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "paquete", e);
        }
    }
    
    @Override
    public List<Paquete> listarTodos() throws DataBaseException {
        String sql = "SELECT p.*, r.nombre as remitente_nombre, r.direccion as remitente_direccion, " +
                     "r.telefono as remitente_telefono, d.nombre as destinatario_nombre, " +
                     "d.direccion as destinatario_direccion, d.telefono as destinatario_telefono " +
                     "FROM paquete p " +
                     "LEFT JOIN remitente r ON p.remitente_id = r.id " +
                     "LEFT JOIN destinatario d ON p.destinatario_id = d.id " +
                     "ORDER BY p.fecha_registro DESC";
        List<Paquete> paquetes = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                paquetes.add(mapearPaquete(rs));
            }
            return paquetes;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "paquete", e);
        }
    }
    
    /**
     * Lista paquetes por estado
     * @param estado Estado a filtrar
     * @return Lista de paquetes con el estado especificado
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Paquete> listarPorEstado(EstadoPaquete estado) throws DataBaseException {
        String sql = "SELECT p.*, r.nombre as remitente_nombre, r.direccion as remitente_direccion, " +
                     "r.telefono as remitente_telefono, d.nombre as destinatario_nombre, " +
                     "d.direccion as destinatario_direccion, d.telefono as destinatario_telefono " +
                     "FROM paquete p " +
                     "LEFT JOIN remitente r ON p.remitente_id = r.id " +
                     "LEFT JOIN destinatario d ON p.destinatario_id = d.id " +
                     "WHERE p.estado = ? ORDER BY p.fecha_registro DESC";
        List<Paquete> paquetes = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                paquetes.add(mapearPaquete(rs));
            }
            return paquetes;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "paquete", e);
        }
    }
    
    /**
     * Lista paquetes en bodega (disponibles para asignar)
     * @return Lista de paquetes en bodega
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Paquete> listarEnBodega() throws DataBaseException {
        return listarPorEstado(EstadoPaquete.EN_BODEGA);
    }
    
    @Override
    public boolean guardar(Paquete paquete) throws DataBaseException {
        String sql = "INSERT INTO paquete (tracking_id, descripcion, peso, dimensiones, " +
                     "origen, destino, estado, remitente_id, destinatario_id, fecha_registro) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, paquete.getTrackingId());
            stmt.setString(2, paquete.getDescripcion());
            stmt.setDouble(3, paquete.getPeso());
            stmt.setString(4, paquete.getDimensiones());
            stmt.setString(5, paquete.getOrigen());
            stmt.setString(6, paquete.getDestino());
            stmt.setString(7, paquete.getEstado().name());
            stmt.setInt(8, paquete.getRemitente().getId());
            stmt.setInt(9, paquete.getDestinatario().getId());
            stmt.setTimestamp(10, Timestamp.valueOf(paquete.getFechaRegistro()));
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    paquete.setId(keys.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            throw new DataBaseException("INSERT", "paquete", e);
        }
    }
    
    @Override
    public boolean actualizar(Paquete paquete) throws DataBaseException {
        String sql = "UPDATE paquete SET descripcion = ?, peso = ?, dimensiones = ?, " +
                     "origen = ?, destino = ?, estado = ?, remitente_id = ?, destinatario_id = ?, " +
                     "fecha_entrega = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, paquete.getDescripcion());
            stmt.setDouble(2, paquete.getPeso());
            stmt.setString(3, paquete.getDimensiones());
            stmt.setString(4, paquete.getOrigen());
            stmt.setString(5, paquete.getDestino());
            stmt.setString(6, paquete.getEstado().name());
            stmt.setInt(7, paquete.getRemitente().getId());
            stmt.setInt(8, paquete.getDestinatario().getId());
            
            if (paquete.getFechaEntregada() != null) {
                stmt.setTimestamp(9, Timestamp.valueOf(paquete.getFechaEntregada()));
            } else {
                stmt.setNull(9, Types.TIMESTAMP);
            }
            
            stmt.setInt(10, paquete.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DataBaseException("UPDATE", "paquete", e);
        }
    }
    
    @Override
    public boolean eliminar(Integer id) throws DataBaseException {
        String sql = "DELETE FROM paquete WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DataBaseException("DELETE", "paquete", e);
        }
    }
    
    /**
     * Actualiza el estado de un paquete por tracking ID
     * @param trackingId ID de tracking del paquete
     * @param estado Nuevo estado
     * @return true si se actualizó correctamente
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public boolean actualizarEstado(String trackingId, EstadoPaquete estado) throws DataBaseException {
        String sql = "UPDATE paquete SET estado = ? WHERE tracking_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado.name());
            stmt.setString(2, trackingId);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DataBaseException("UPDATE", "paquete", e);
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto Paquete
     */
    private Paquete mapearPaquete(ResultSet rs) throws SQLException {
        Paquete paquete = new Paquete();
        paquete.setId(rs.getInt("id"));
        paquete.setTrackingId(rs.getString("tracking_id"));
        paquete.setDescripcion(rs.getString("descripcion"));
        paquete.setPeso(rs.getDouble("peso"));
        paquete.setDimensiones(rs.getString("dimensiones"));
        paquete.setOrigen(rs.getString("origen"));
        paquete.setDestino(rs.getString("destino"));
        paquete.setEstado(EstadoPaquete.valueOf(rs.getString("estado")));
        
        Timestamp fechaRegistro = rs.getTimestamp("fecha_registro");
        if (fechaRegistro != null) {
            paquete.setFechaRegistro(fechaRegistro.toLocalDateTime());
        }
        
        Timestamp fechaEntrega = rs.getTimestamp("fecha_entrega");
        if (fechaEntrega != null) {
            paquete.setFechaEntregada(fechaEntrega.toLocalDateTime());
        }
        
        // Mapear remitente
        Remitente remitente = new Remitente();
        remitente.setId(rs.getInt("remitente_id"));
        remitente.setNombre(rs.getString("remitente_nombre"));
        remitente.setDireccion(rs.getString("remitente_direccion"));
        remitente.setTelefono(rs.getString("remitente_telefono"));
        paquete.setRemitente(remitente);
        
        // Mapear destinatario
        Destinatario destinatario = new Destinatario();
        destinatario.setId(rs.getInt("destinatario_id"));
        destinatario.setNombre(rs.getString("destinatario_nombre"));
        destinatario.setDireccion(rs.getString("destinatario_direccion"));
        destinatario.setTelefono(rs.getString("destinatario_telefono"));
        paquete.setDestinatario(destinatario);
        
        return paquete;
    }
}