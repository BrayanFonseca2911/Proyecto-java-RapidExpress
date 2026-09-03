package rapidexpress.servicio;

import rapidexpress.dominio.Paquete;
import rapidexpress.dominio.Ruta;
import rapidexpress.dominio.Vehiculo;
import rapidexpress.enums.EstadoPaquete;
import rapidexpress.enums.EstadoVehiculo;
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.repositorio.PaqueteDAO;
import rapidexpress.repositorio.RutaDAO;
import rapidexpress.repositorio.VehiculoDAO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para la generación de reportes.
 * 
 * @author User
 */
public class ReporteService {
    
    private final PaqueteDAO paqueteDAO;
    private final RutaDAO rutaDAO;
    private final VehiculoDAO vehiculoDAO;
    
    public ReporteService() {
        this.paqueteDAO = new PaqueteDAO();
        this.rutaDAO = new RutaDAO();
        this.vehiculoDAO = new VehiculoDAO();
    }
    
    /**
     * Obtiene un resumen de paquetes por estado
     * @return Mapa con estado y cantidad
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public Map<String, Integer> getResumenPaquetesPorEstado() throws DataBaseException {
        Map<String, Integer> resumen = new HashMap<>();
        
        for (EstadoPaquete estado : EstadoPaquete.values()) {
            List<Paquete> paquetes = paqueteDAO.listarPorEstado(estado);
            resumen.put(estado.getDescripcion(), paquetes.size());
        }
        
        return resumen;
    }
    
    /**
     * Obtiene vehículos en mantenimiento
     * @return Lista de vehículos en mantenimiento
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<Vehiculo> getVehiculosEnMantenimiento() throws DataBaseException {
        return vehiculoDAO.listarPorEstado(EstadoVehiculo.EN_MANTENIMIENTO);
    }
    
    /**
     * Obtiene historial de rutas de un vehículo por placa
     * @param placa Placa del vehículo
     * @return Lista de descripciones de rutas
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public List<String> getHistorialRutasVehiculo(String placa) throws DataBaseException {
        List<String> historial = new ArrayList<>();
        
        // Buscar vehículo por placa
        Vehiculo vehiculo = vehiculoDAO.buscarPorPlaca(placa);
        if (vehiculo == null) {
            throw new DataBaseException("No existe vehículo con placa: " + placa);
        }
        
        // Obtener rutas completadas del vehículo
        List<Ruta> rutas = rutaDAO.listarHistorialVehiculo(vehiculo.getId());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        for (Ruta ruta : rutas) {
            String descripcion = String.format("Ruta #%d | %s | %d paquetes | %.2f kg",
                ruta.getId(),
                ruta.getFecha().format(formatter),
                ruta.getPaquetes() != null ? ruta.getPaquetes().size() : 0,
                ruta.getPesoTotal());
            historial.add(descripcion);
        }
        
        return historial;
    }
    
    /**
     * Obtiene entregas por conductor en un rango de fechas
     * @param fechaInicioStr Fecha de inicio en formato dd/MM/yyyy
     * @param fechaFinStr Fecha de fin en formato dd/MM/yyyy
     * @return Mapa con nombre del conductor y lista de entregas
     * @throws DataBaseException Si ocurre un error de base de datos
     */
    public Map<String, List<String>> getEntregasPorConductor(String fechaInicioStr, String fechaFinStr) 
            throws DataBaseException {
        
        Map<String, List<String>> entregas = new HashMap<>();
        
        try {
            // Parsear fechas
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr.trim(), formatter);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr.trim(), formatter);
            
            LocalDateTime inicio = fechaInicio.atStartOfDay();
            LocalDateTime fin = fechaFin.plusDays(1).atStartOfDay();
            
            // Obtener todas las rutas completadas en el rango
            List<Ruta> todasRutas = rutaDAO.listarTodos();
            
            for (Ruta ruta : todasRutas) {
                // Filtrar por fecha y estado
                if (ruta.getFecha().isAfter(inicio) && 
                    ruta.getFecha().isBefore(fin) &&
                    ruta.getEstado().toString().equals("COMPLETADA")) {
                    
                    String conductorNombre = ruta.getConductor() != null 
                        ? ruta.getConductor().getNombre() 
                        : "Desconocido";
                    
                    // Crear lista si no existe
                    entregas.putIfAbsent(conductorNombre, new ArrayList<>());
                    
                    // Agregar descripción de la entrega
                    String entregaDesc = String.format("Ruta #%d | %s | %d paquetes",
                        ruta.getId(),
                        ruta.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                        ruta.getPaquetes() != null ? ruta.getPaquetes().size() : 0);
                    
                    entregas.get(conductorNombre).add(entregaDesc);
                }
            }
            
        } catch (Exception e) {
            throw new DataBaseException("Error al procesar fechas: " + e.getMessage(), e);
        }
        
        return entregas;
    }
}