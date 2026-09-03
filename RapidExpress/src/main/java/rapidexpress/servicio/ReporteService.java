package rapidexpress.servicio;

import rapidexpress.dominio.Paquete;
import rapidexpress.dominio.Ruta;
import rapidexpress.dominio.Vehiculo;
import rapidexpress.enums.EstadoPaquete;
import rapidexpress.enums.EstadoRuta;
import rapidexpress.enums.EstadoVehiculo;
import rapidexpress.excepciones.DataBaseException;
import rapidexpress.repositorio.PaqueteDAO;
import rapidexpress.repositorio.RutaDAO;
import rapidexpress.repositorio.VehiculoDAO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        // Se trae la lista completa una sola vez y se agrupa en memoria con
        // Stream API, en vez de consultar la base de datos una vez por estado.
        Map<EstadoPaquete, Long> conteoPorEstado = paqueteDAO.listarTodos().stream()
                .collect(Collectors.groupingBy(Paquete::getEstado, Collectors.counting()));

        return Arrays.stream(EstadoPaquete.values())
                .collect(Collectors.toMap(
                        EstadoPaquete::getDescripcion,
                        estado -> conteoPorEstado.getOrDefault(estado, 0L).intValue(),
                        (a, b) -> a,
                        LinkedHashMap::new));
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
        // Buscar vehículo por placa
        Vehiculo vehiculo = vehiculoDAO.buscarPorPlaca(placa);
        if (vehiculo == null) {
            throw new DataBaseException("No existe vehículo con placa: " + placa);
        }

        // Obtener rutas completadas del vehículo
        List<Ruta> rutas = rutaDAO.listarHistorialVehiculo(vehiculo.getId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        return rutas.stream()
                .map(ruta -> String.format("Ruta #%d | %s | %d paquetes | %.2f kg",
                        ruta.getId(),
                        ruta.getFecha().format(formatter),
                        ruta.getPaquetes() != null ? ruta.getPaquetes().size() : 0,
                        ruta.getPesoTotal()))
                .collect(Collectors.toList());
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

        try {
            // Parsear fechas
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr.trim(), formatter);
            LocalDate fechaFin = LocalDate.parse(fechaFinStr.trim(), formatter);

            LocalDateTime inicio = fechaInicio.atStartOfDay();
            LocalDateTime fin = fechaFin.plusDays(1).atStartOfDay();
            DateTimeFormatter formatterCompleto = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // Filtrar rutas completadas en el rango y agruparlas por conductor
            return rutaDAO.listarTodos().stream()
                    .filter(ruta -> ruta.getFecha().isAfter(inicio)
                            && ruta.getFecha().isBefore(fin)
                            && ruta.getEstado() == EstadoRuta.COMPLETADA)
                    .collect(Collectors.groupingBy(
                            ruta -> ruta.getConductor() != null ? ruta.getConductor().getNombre() : "Desconocido",
                            Collectors.mapping(
                                    ruta -> String.format("Ruta #%d | %s | %d paquetes",
                                            ruta.getId(),
                                            ruta.getFecha().format(formatterCompleto),
                                            ruta.getPaquetes() != null ? ruta.getPaquetes().size() : 0),
                                    Collectors.toList())));

        } catch (DataBaseException e) {
            throw e;
        } catch (Exception e) {
            throw new DataBaseException("Error al procesar fechas: " + e.getMessage(), e);
        }
    }
}