package rapidexpress.repositorio;

import rapidexpress.dominio.Ruta;
import rapidexpress.dominio.Vehiculo;
import rapidexpress.dominio.Conductor;
import rapidexpress.dominio.Paquete;
import rapidexpress.enums.EstadoRuta;
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos para la entidad Ruta.
 * 
 * @author User
 */
public class RutaDAO implements IRutaDAO {
    
    private final DBConnection dbConnection;
    private final VehiculoDAO vehiculoDAO;
    private final ConductorDAO conductorDAO;
    private final PaqueteDAO paqueteDAO;
    
    public RutaDAO(){
    try {
        this.dbConnection = DBConnection.getInstance();
        this.vehiculoDAO = new VehiculoDAO();
        this.conductorDAO = new ConductorDAO();
        this.paqueteDAO = new PaqueteDAO();
    } catch (DataBaseException e) {
        System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
        throw new RuntimeException("No se pudo inicializar RutaDAO", e);
    }
}
     
    @Override
    public Ruta buscarPorId(Integer id) throws DataBaseException {
        String sql = "SELECT * FROM ruta WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearRuta(rs);
            }
            return null;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "ruta", e);
        }
    }
    
    @Override
    public List<Ruta> listarTodos() throws DataBaseException {
        String sql = "SELECT * FROM ruta ORDER BY fecha DESC";
        List<Ruta> rutas = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                rutas.add(mapearRuta(rs));
            }
            return rutas;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "ruta", e);
        }
    }
    
    /**
     * Lista rutas activas (en curso)
     * @return Lista de rutas en curso
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Ruta> listarActivas() throws DataBaseException {
        String sql = "SELECT * FROM ruta WHERE estado = 'EN_CURSO' ORDER BY fecha DESC";
        List<Ruta> rutas = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                rutas.add(mapearRuta(rs));
            }
            return rutas;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "ruta", e);
        }
    }
    
    /**
     * Lista rutas por vehículo
     * @param vehiculoId ID del vehículo
     * @return Lista de rutas del vehículo
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Ruta> listarPorVehiculo(Integer vehiculoId) throws DataBaseException {
        String sql = "SELECT * FROM ruta WHERE vehiculo_id = ? ORDER BY fecha DESC";
        List<Ruta> rutas = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, vehiculoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                rutas.add(mapearRuta(rs));
            }
            return rutas;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "ruta", e);
        }
    }
    
    /**
     * Lista historial de rutas completadas de un vehículo
     * @param vehiculoId ID del vehículo
     * @return Lista de rutas completadas
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Ruta> listarHistorialVehiculo(Integer vehiculoId) throws DataBaseException {
        String sql = "SELECT * FROM ruta WHERE vehiculo_id = ? AND estado = 'COMPLETADA' ORDER BY fecha DESC";
        List<Ruta> rutas = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, vehiculoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                rutas.add(mapearRuta(rs));
            }
            return rutas;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "ruta", e);
        }
    }
    
    @Override
    public boolean guardar(Ruta ruta) throws DataBaseException {
        String sql = "INSERT INTO ruta (vehiculo_id, conductor_id, fecha, estado, peso_total) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, ruta.getVehiculo().getId());
            stmt.setInt(2, ruta.getConductor().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(ruta.getFecha()));
            stmt.setString(4, ruta.getEstado().name());
            stmt.setDouble(5, ruta.getPesoTotal());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    ruta.setId(keys.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            throw new DataBaseException("INSERT", "ruta", e);
        }
    }
    
    @Override
    public boolean actualizar(Ruta ruta) throws DataBaseException {
        String sql = "UPDATE ruta SET vehiculo_id = ?, conductor_id = ?, " +
                     "fecha = ?, estado = ?, peso_total = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ruta.getVehiculo().getId());
            stmt.setInt(2, ruta.getConductor().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(ruta.getFecha()));
            stmt.setString(4, ruta.getEstado().name());
            stmt.setDouble(5, ruta.getPesoTotal());
            stmt.setInt(6, ruta.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DataBaseException("UPDATE", "ruta", e);
        }
    }
    
    @Override
    public boolean eliminar(Integer id) throws DataBaseException {
        String sql = "DELETE FROM ruta WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DataBaseException("DELETE", "ruta", e);
        }
    }
    
    /**
     * Asigna un paquete a una ruta
     * @param rutaId ID de la ruta
     * @param paqueteId ID del paquete
     * @return true si se asignó correctamente
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public boolean asignarPaqueteARuta(Integer rutaId, Integer paqueteId) throws DataBaseException {
        String sql = "INSERT INTO ruta_paquete (ruta_id, paquete_id) VALUES (?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, rutaId);
            stmt.setInt(2, paqueteId);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DataBaseException("INSERT", "ruta_paquete", e);
        }
    }
    
    /**
     * Obtiene los paquetes asignados a una ruta
     * @param rutaId ID de la ruta
     * @return Lista de paquetes de la ruta
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Paquete> obtenerPaquetesDeRuta(Integer rutaId) throws DataBaseException {
        String sql = "SELECT p.* FROM paquete p " +
                     "INNER JOIN ruta_paquete rp ON p.id = rp.paquete_id " +
                     "WHERE rp.ruta_id = ?";
        List<Paquete> paquetes = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, rutaId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                paquetes.add(paqueteDAO.buscarPorId(rs.getInt("id")));
            }
            return paquetes;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "ruta_paquete", e);
        }
    }
    
    /**
     * Inicia una ruta (cambia estado a EN_CURSO)
     * @param rutaId ID de la ruta
     * @return true si se inició correctamente
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public boolean iniciarRuta(Integer rutaId) throws DataBaseException {
        String sql = "UPDATE ruta SET estado = 'EN_CURSO' WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, rutaId);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DataBaseException("UPDATE", "ruta", e);
        }
    }
    
    /**
     * Completa una ruta (cambia estado a COMPLETADA)
     * @param rutaId ID de la ruta
     * @return true si se completó correctamente
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public boolean completarRuta(Integer rutaId) throws DataBaseException {
        String sql = "UPDATE ruta SET estado = 'COMPLETADA' WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, rutaId);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DataBaseException("UPDATE", "ruta", e);
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto Ruta
     */
    private Ruta mapearRuta(ResultSet rs) throws SQLException, DataBaseException {
        Ruta ruta = new Ruta();
        ruta.setId(rs.getInt("id"));
        
        // Mapear vehículo
        Vehiculo vehiculo = vehiculoDAO.buscarPorId(rs.getInt("vehiculo_id"));
        ruta.setVehiculo(vehiculo);
        
        // Mapear conductor
        Conductor conductor = conductorDAO.buscarPorId(rs.getInt("conductor_id"));
        ruta.setConductor(conductor);
        
        Timestamp fecha = rs.getTimestamp("fecha");
        if (fecha != null) {
            ruta.setFecha(fecha.toLocalDateTime());
        }
        
        ruta.setEstado(EstadoRuta.valueOf(rs.getString("estado")));
        ruta.setPesoTotal(rs.getDouble("peso_total"));
        
        // Cargar paquetes de la ruta
        List<Paquete> paquetes = obtenerPaquetesDeRuta(ruta.getId());
        ruta.setPaquetes(paquetes);
        
        return ruta;
    }
}