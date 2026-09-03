package rapidexpress.repositorio;

import rapidexpress.dominio.Mantenimiento;
import rapidexpress.dominio.Vehiculo;
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos para la entidad Mantenimiento.
 * 
 * @author User
 */
public class MantenimientoDAO implements IDAO<Mantenimiento, Integer> {
    
    private final DBConnection dbConnection;
    private final VehiculoDAO vehiculoDAO;
    
    public MantenimientoDAO() {
    try {
        this.dbConnection = DBConnection.getInstance();
        this.vehiculoDAO = new VehiculoDAO();
    } catch (DataBaseException e) {
        System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
        throw new RuntimeException("No se pudo inicializar MantenimientoDAO", e);
    }
}
    
    @Override
    public Mantenimiento buscarPorId(Integer id) throws DataBaseException {
        String sql = "SELECT * FROM mantenimiento WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearMantenimiento(rs);
            }
            return null;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "mantenimiento", e);
        }
    }
    
    @Override
    public List<Mantenimiento> listarTodos() throws DataBaseException {
        String sql = "SELECT * FROM mantenimiento ORDER BY fecha DESC";
        List<Mantenimiento> mantenimientos = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                mantenimientos.add(mapearMantenimiento(rs));
            }
            return mantenimientos;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "mantenimiento", e);
        }
    }
    
    /**
     * Lista mantenimientos por vehículo
     * @param vehiculoId ID del vehículo
     * @return Lista de mantenimientos del vehículo
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Mantenimiento> listarPorVehiculo(Integer vehiculoId) throws DataBaseException {
        String sql = "SELECT * FROM mantenimiento WHERE vehiculo_id = ? ORDER BY fecha DESC";
        List<Mantenimiento> mantenimientos = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, vehiculoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                mantenimientos.add(mapearMantenimiento(rs));
            }
            return mantenimientos;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "mantenimiento", e);
        }
    }
    
    /**
     * Lista mantenimientos en un rango de fechas
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Lista de mantenimientos en el rango
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Mantenimiento> listarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) throws DataBaseException {
        String sql = "SELECT * FROM mantenimiento WHERE fecha BETWEEN ? AND ? ORDER BY fecha DESC";
        List<Mantenimiento> mantenimientos = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(fechaInicio));
            stmt.setTimestamp(2, Timestamp.valueOf(fechaFin));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                mantenimientos.add(mapearMantenimiento(rs));
            }
            return mantenimientos;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "mantenimiento", e);
        }
    }
    
    @Override
    public boolean guardar(Mantenimiento mantenimiento) throws DataBaseException {
        String sql = "INSERT INTO mantenimiento (vehiculo_id, fecha, descripcion, costo, kilometraje) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, mantenimiento.getVehiculo().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(mantenimiento.getFecha()));
            stmt.setString(3, mantenimiento.getDescripcion());
            stmt.setDouble(4, mantenimiento.getCosto());
            stmt.setInt(5, mantenimiento.getKilometraje());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    mantenimiento.setId(keys.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            throw new DataBaseException("INSERT", "mantenimiento", e);
        }
    }
    
    @Override
    public boolean actualizar(Mantenimiento mantenimiento) throws DataBaseException {
        String sql = "UPDATE mantenimiento SET vehiculo_id = ?, fecha = ?, " +
                     "descripcion = ?, costo = ?, kilometraje = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, mantenimiento.getVehiculo().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(mantenimiento.getFecha()));
            stmt.setString(3, mantenimiento.getDescripcion());
            stmt.setDouble(4, mantenimiento.getCosto());
            stmt.setInt(5, mantenimiento.getKilometraje());
            stmt.setInt(6, mantenimiento.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DataBaseException("UPDATE", "mantenimiento", e);
        }
    }
    
    @Override
    public boolean eliminar(Integer id) throws DataBaseException {
        String sql = "DELETE FROM mantenimiento WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DataBaseException("DELETE", "mantenimiento", e);
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto Mantenimiento
     */
    private Mantenimiento mapearMantenimiento(ResultSet rs) throws SQLException, DataBaseException {
        Mantenimiento mantenimiento = new Mantenimiento();
        mantenimiento.setId(rs.getInt("id"));
        
        Vehiculo vehiculo = vehiculoDAO.buscarPorId(rs.getInt("vehiculo_id"));
        mantenimiento.setVehiculo(vehiculo);
        
        Timestamp fecha = rs.getTimestamp("fecha");
        if (fecha != null) {
            mantenimiento.setFecha(fecha.toLocalDateTime());
        }
        
        mantenimiento.setDescripcion(rs.getString("descripcion"));
        mantenimiento.setCosto(rs.getDouble("costo"));
        mantenimiento.setKilometraje(rs.getInt("kilometraje"));
        
        return mantenimiento;
    }
}