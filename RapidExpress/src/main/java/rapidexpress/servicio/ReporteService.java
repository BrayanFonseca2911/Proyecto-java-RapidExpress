/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.servicio;

// Importaciones para el manejo de fechas en filtros de reportes.
import java.util.Date;
// Importaciones de colecciones Map, List y HashMap para estructura de reportes.
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// Importación de stream collectors para el agrupamiento de datos.
import java.util.stream.Collectors;

// Importaciones de los enums y modelos del dominio.
import rapidexpress.dominio.EstadoPaquete;
import rapidexpress.dominio.EstadoVehiculo;
import rapidexpress.dominio.Paquete;
import rapidexpress.dominio.Ruta;
import rapidexpress.dominio.Vehiculo;
// Importación de la excepción de persistencia personalizada.
import rapidexpress.excepciones.DataBaseException;
// Importaciones de los repositorios requeridos para las consultas analíticas.
import rapidexpress.repositorio.PaqueteDAO;
import rapidexpress.repositorio.RutaDAO;
import rapidexpress.repositorio.VehiculoDAO;

/**
 * Propósito: Proveer la lógica de negocio para la consolidación, agrupamiento 
 * y generación de reportes métricos y operativos del sistema RapidExpress.
 * 
 * @author User
 */
public class ReporteService {

    // Repositorio para la consulta y conteo de datos de paquetes.
    private final PaqueteDAO paqueteDAO;
    // Repositorio para el historial de trayectos y rutas.
    private final RutaDAO rutaDAO;
    // Repositorio para la consulta del parque automotor.
    private final VehiculoDAO vehiculoDAO;

    // Constructor que inicializa los DAOs colaboradores.
    public ReporteService() {
        // Instancia el DAO de paquetes.
        this.paqueteDAO = new PaqueteDAO();
        // Instancia el DAO de rutas.
        this.rutaDAO = new RutaDAO();
        // Instancia el DAO de vehículos.
        this.vehiculoDAO = new VehiculoDAO();
    }

    /**
     * Consulta las entregas realizadas por un conductor en un rango de fechas y las agrupa por nombre.
     * 
     * @param conductorId Identificador único del conductor.
     * @param inicio Fecha inicial del periodo.
     * @param fin Fecha final del periodo.
     * @return Mapa con la llave (Nombre del conductor) y el valor (Lista de tracking IDs entregados).
     * @throws DataBaseException Si ocurre un fallo en la consulta SQL.
     */
    public Map<String, List<String>> getEntregasPorConductor(Integer conductorId, Date inicio, Date fin) throws DataBaseException {
        // Valida que el ID del conductor sea válido.
        if (conductorId == null || conductorId <= 0) {
            throw new IllegalArgumentException("El ID del conductor debe ser válido.");
        }
        // Valida que el rango de fechas no sea nulo.
        if (inicio == null || fin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias.");
        }

        // Obtiene la lista de paquetes entregados mediante el DAO.
        List<Paquete> paquetes = paqueteDAO.listarEntregasPorConductor(conductorId, inicio, fin);

        // Agrupa los códigos de rastreo (trackingId) por el nombre del conductor.
        return paquetes.stream().collect(
            Collectors.groupingBy(
                p -> p.getConductor() != null ? p.getConductor().getNombre() : "Conductor Desconocido",
                Collectors.mapping(Paquete::getTrackingId, Collectors.toList())
            )
        );
    }

    /**
     * Consulta el historial completo de rutas recorridas por un vehículo específico.
     * 
     * @param placa Placa del vehículo a consultar.
     * @return Lista de rutas en las que participó el vehículo.
     * @throws DataBaseException Si ocurre un fallo en el acceso a datos.
     */
    public List<Ruta> getHistorialRutasVehiculo(String placa) throws DataBaseException {
        // Valida que la placa no esté vacía.
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa del vehículo es obligatoria.");
        }

        // Busca la entidad del vehículo por su placa.
        Vehiculo vehiculo = vehiculoDAO.buscarPorPlaca(placa);
        if (vehiculo == null) {
            throw new IllegalArgumentException("No se encontró ningún vehículo con la placa: " + placa);
        }

        // Retorna la lista de rutas registradas para la ID del vehículo.
        return rutaDAO.listarHistorialVehiculo(vehiculo.getId());
    }

    /**
     * Genera un resumen estadístico con el conteo global de paquetes según su estado.
     * 
     * @return Mapa asociativo con cada EstadoPaquete y la cantidad total correspondiente.
     * @throws DataBaseException Si ocurre un fallo en la consulta de base de datos.
     */
    public Map<EstadoPaquete, Integer> getResumenPaquetesPorEstado() throws DataBaseException {
        // Inicializa el mapa que contendrá los totales por estado.
        Map<EstadoPaquete, Integer> resumen = new HashMap<>();

        // Recorre cada uno de los valores definidos en el Enum EstadoPaquete.
        for (EstadoPaquete estado : EstadoPaquete.values()) {
            // Cuenta la cantidad de paquetes existentes para cada estado particular.
            int cantidad = paqueteDAO.contarPorEstado(estado);
            // Registra la pareja estado - cantidad en el mapa.
            resumen.put(estado, cantidad);
        }

        // Retorna el resumen analítico generado.
        return resumen;
    }

    /**
     * Obtiene el listado actualizado de los vehículos que se encuentran en mantenimiento.
     * 
     * @return Lista de vehículos con estado EN_MANTENIMIENTO.
     * @throws DataBaseException Si ocurre un error en el repositorio de datos.
     */
    public List<Vehiculo> getVehiculosEnMantenimiento() throws DataBaseException {
        // Ejecuta la consulta filtrando por el Enum EstadoVehiculo.EN_MANTENIMIENTO.
        return vehiculoDAO.listarPorEstado(EstadoVehiculo.EN_MANTENIMIENTO);
    }

    /**
     * Calcula la cantidad de entregas completadas por cada conductor dentro de un rango de fechas.
     * 
     * @param inicio Fecha de inicio del reporte.
     * @param fin Fecha de fin del reporte.
     * @return Mapa relacionando el nombre del conductor con su cantidad de entregas.
     * @throws DataBaseException Si ocurre una falla en el acceso a datos.
     */
    public Map<String, Integer> getRendimientoConductores(Date inicio, Date fin) throws DataBaseException {
        // Valida la obligatoriedad del rango de fechas.
        if (inicio == null || fin == null) {
            throw new IllegalArgumentException("El rango de fechas especificado no es válido.");
        }

        // Invoca al DAO para obtener el mapa con el conteo de entregas por nombre de conductor.
        return paqueteDAO.obtenerConteoEntregasPorConductor(inicio, fin);
    }
}