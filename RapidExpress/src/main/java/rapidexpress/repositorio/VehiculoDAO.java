package rapidexpress.repositorio;

import rapidexpress.dominio.Vehiculo;
import rapidexpress.enums.EstadoVehiculo;
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos para la entidad Vehiculo.
 * Implementa operaciones CRUD y consultas específicas.
 * 
 * @author User
 */
public class VehiculoDAO implements IVehiculoDAO {
    
    private final DBConnection dbConnection;
    
    /**
     * Constructor que inicializa la conexión a base de datos
     */
    public VehiculoDAO() {
    try {
        this.dbConnection = DBConnection.getInstance();
    } catch (DataBaseException e) {
        System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
        throw new RuntimeException("No se pudo inicializar VehiculoDAO", e);
    }
}
    
    
    @Override
    public Vehiculo buscarPorId(Integer id) throws DataBaseException {
        String sql = "SELECT * FROM vehiculo WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearVehiculo(rs);
            }
            return null;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "vehiculo", e);
        }
    }
    
    /**
     * Busca un vehículo por su placa
     * @param placa Placa del vehículo
     * @return Vehiculo encontrado o null
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public Vehiculo buscarPorPlaca(String placa) throws DataBaseException {
        String sql = "SELECT * FROM vehiculo WHERE placa = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, placa);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearVehiculo(rs);
            }
            return null;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "vehiculo", e);
        }
    }
    
    @Override
    public List<Vehiculo> listarTodos() throws DataBaseException {
        String sql = "SELECT * FROM vehiculo ORDER BY placa";
        List<Vehiculo> vehiculos = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                vehiculos.add(mapearVehiculo(rs));
            }
            return vehiculos;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "vehiculo", e);
        }
    }
    
    /**
     * Lista vehículos por estado
     * @param estado Estado a filtrar
     * @return Lista de vehículos con el estado especificado
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Vehiculo> listarPorEstado(EstadoVehiculo estado) throws DataBaseException {
        String sql = "SELECT * FROM vehiculo WHERE estado = ? ORDER BY placa";
        List<Vehiculo> vehiculos = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                vehiculos.add(mapearVehiculo(rs));
            }
            return vehiculos;
            
        } catch (SQLException e) {
            throw new DataBaseException("SELECT", "vehiculo", e);
        }
    }
    
    /**
     * Lista solo los vehículos disponibles
     * @return Lista de vehículos disponibles
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Vehiculo> listarDisponibles() throws DataBaseException {
        return listarPorEstado(EstadoVehiculo.DISPONIBLE);
    }
    
    @Override
    public boolean guardar(Vehiculo vehiculo) throws DataBaseException {
        String sql = "INSERT INTO vehiculo (placa, marca, modelo, anio, capacidad_carga, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, vehiculo.getPlaca());
            stmt.setString(2, vehiculo.getMarca());
            stmt.setString(3, vehiculo.getModelo());
            stmt.setInt(4, vehiculo.getYear());
            stmt.setDouble(5, vehiculo.getCapacidadCarga());
            stmt.setString(6, vehiculo.getEstado().name());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    vehiculo.setId(keys.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            throw new DataBaseException("INSERT", "vehiculo", e);
        }
    }
    
    @Override
    public boolean actualizar(Vehiculo vehiculo) throws DataBaseException {
        String sql = "UPDATE vehiculo SET marca = ?, modelo = ?, anio = ?, " +
                     "capacidad_carga = ?, estado = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, vehiculo.getMarca());
            stmt.setString(2, vehiculo.getModelo());
            stmt.setInt(3, vehiculo.getYear());
            stmt.setDouble(4, vehiculo.getCapacidadCarga());
            stmt.setString(5, vehiculo.getEstado().name());
            stmt.setInt(6, vehiculo.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DataBaseException("UPDATE", "vehiculo", e);
        }
    }
    
    @Override
    public boolean eliminar(Integer id) throws DataBaseException {
        String sql = "DELETE FROM vehiculo WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            throw new DataBaseException("DELETE", "vehiculo", e);
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto Vehiculo
     * @param rs ResultSet con los datos del vehículo
     * @return Objeto Vehiculo mapeado
     * @throws SQLException Si ocurre un error al leer el ResultSet
     */
    private Vehiculo mapearVehiculo(ResultSet rs) throws SQLException {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(rs.getInt("id"));
        vehiculo.setPlaca(rs.getString("placa"));
        vehiculo.setMarca(rs.getString("marca"));
        vehiculo.setModelo(rs.getString("modelo"));
        vehiculo.setYear(rs.getInt("anio"));
        vehiculo.setCapacidadCarga(rs.getDouble("capacidad_carga"));
        vehiculo.setEstado(EstadoVehiculo.valueOf(rs.getString("estado")));
        return vehiculo;
    }
}