package rapidexpress.repositorio;

import rapidexpress.dominio.Conductor;
import rapidexpress.enums.EstadoConductor;
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos para la entidad Conductor.
 * 
 * @author User
 */
public class ConductorDAO implements IConductorDAO {
    
    private final DBConnection dbConnection;
    
    public ConductorDAO() {
    try {
        this.dbConnection = DBConnection.getInstance();
    } catch (DataBaseException e) {
        System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        throw new RuntimeException("No se pudo inicializar ConductorDAO", e);
    }
}
    
    @Override
    public Conductor buscarPorId(Integer id) throws DataBaseException {
        String sql = "SELECT * FROM conductor WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearConductor(rs);
            }
            return null;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "conductor", e);
        }
    }
    
    /**
     * Busca un conductor por su número de identificación
     * @param numeroIdentificacion Número de identificación
     * @return Conductor encontrado o null
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public Conductor buscarPorNumeroIdentificacion(String numeroIdentificacion) throws DataBaseException {
        String sql = "SELECT * FROM conductor WHERE numero_identificacion = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, numeroIdentificacion);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearConductor(rs);
            }
            return null;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "conductor", e);
        }
    }
    
    @Override
    public List<Conductor> listarTodos() throws DataBaseException {
        String sql = "SELECT * FROM conductor ORDER BY nombre";
        List<Conductor> conductores = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                conductores.add(mapearConductor(rs));
            }
            return conductores;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "conductor", e);
        }
    }
    
    /**
     * Lista conductores por estado
     * @param estado Estado a filtrar
     * @return Lista de conductores con el estado especificado
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Conductor> listarPorEstado(EstadoConductor estado) throws DataBaseException {
        String sql = "SELECT * FROM conductor WHERE estado = ? ORDER BY nombre";
        List<Conductor> conductores = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                conductores.add(mapearConductor(rs));
            }
            return conductores;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "conductor", e);
        }
    }
    
    /**
     * Lista conductores disponibles (activos sin vehículo asignado)
     * @return Lista de conductores disponibles
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Conductor> listarDisponibles() throws DataBaseException {
        String sql = "SELECT * FROM conductor WHERE estado = 'ACTIVO' AND vehiculo_id IS NULL ORDER BY nombre";
        List<Conductor> conductores = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                conductores.add(mapearConductor(rs));
            }
            return conductores;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "conductor", e);
        }
    }
    
    @Override
    public boolean guardar(Conductor conductor) throws DataBaseException {
        String sql = "INSERT INTO conductor (numero_identificacion, nombre, tipo_licencia, contacto, estado) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, conductor.getNumeroIdentificacion());
            stmt.setString(2, conductor.getNombre());
            stmt.setString(3, conductor.getTipoLicencia());
            stmt.setString(4, conductor.getContacto());
            stmt.setString(5, conductor.getEstado().name());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    conductor.setId(keys.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            throw new DataBaseException("INSERT", "conductor", e);
        }
    }
    
    @Override
    public boolean actualizar(Conductor conductor) throws DataBaseException {
        String sql = "UPDATE conductor SET nombre = ?, tipo_licencia = ?, " +
                     "contacto = ?, estado = ?, vehiculo_id = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, conductor.getNombre());
            stmt.setString(2, conductor.getTipoLicencia());
            stmt.setString(3, conductor.getContacto());
            stmt.setString(4, conductor.getEstado().name());
            
            if (conductor.getVehiculoAsignado() != null) {
                stmt.setInt(5, conductor.getVehiculoAsignado().getId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            
            stmt.setInt(6, conductor.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DataBaseException("UPDATE", "conductor", e);
        }
    }
    
    @Override
    public boolean eliminar(Integer id) throws DataBaseException {
        String sql = "DELETE FROM conductor WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DataBaseException("DELETE", "conductor", e);
        }
    }
    
    /**
     * Asigna un vehículo a un conductor
     * @param conductorId ID del conductor
     * @param vehiculoId ID del vehículo
     * @return true si se asignó correctamente
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public boolean asignarVehiculo(Integer conductorId, Integer vehiculoId) throws DataBaseException {
        String sql = "UPDATE conductor SET vehiculo_id = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, vehiculoId);
            stmt.setInt(2, conductorId);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DataBaseException("UPDATE", "conductor", e);
        }
    }
    
    /**
     * Libera el vehículo asignado a un conductor
     * @param conductorId ID del conductor
     * @return true si se liberó correctamente
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public boolean liberarVehiculo(Integer conductorId) throws DataBaseException {
        String sql = "UPDATE conductor SET vehiculo_id = NULL WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, conductorId);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DataBaseException("UPDATE", "conductor", e);
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto Conductor
     */
    private Conductor mapearConductor(ResultSet rs) throws SQLException {
        Conductor conductor = new Conductor();
        conductor.setId(rs.getInt("id"));
        conductor.setNumeroIdentificacion(rs.getString("numero_identificacion"));
        conductor.setNombre(rs.getString("nombre"));
        conductor.setTipoLicencia(rs.getString("tipo_licencia"));
        conductor.setContacto(rs.getString("contacto"));
        conductor.setEstado(EstadoConductor.valueOf(rs.getString("estado")));
        return conductor;
    }
}