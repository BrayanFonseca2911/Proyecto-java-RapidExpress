/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.servicio;

// Importación para la gestión de conexiones transaccionales con JDBC.
import java.sql.Connection;
// Importación para la gestión de excepciones de SQL.
import java.sql.SQLException;
// Importación para el manejo de fechas en la creación de rutas.
import java.util.Date;
// Importación para la gestión de colecciones de paquetes y rutas.
import java.util.List;

// Importación de los enums de estado para cada entidad del sistema.
import rapidexpress.dominio.EstadoConductor;
import rapidexpress.dominio.EstadoPaquete;
import rapidexpress.dominio.EstadoRuta;
import rapidexpress.dominio.EstadoVehiculo;
// Importación de los modelos de dominio.
import rapidexpress.dominio.Conductor;
import rapidexpress.dominio.Paquete;
import rapidexpress.dominio.Ruta;
import rapidexpress.dominio.Vehiculo;
// Importación de las excepciones personalizadas del sistema.
import rapidexpress.excepciones.CapacidadExcedidaException;
import rapidexpress.excepciones.DataBaseException;
// Importación de todos los repositorios involucrados en la planificación y ejecución.
import rapidexpress.repositorio.ConductorDAO;
import rapidexpress.repositorio.PaqueteDAO;
import rapidexpress.repositorio.RutaDAO;
import rapidexpress.repositorio.VehiculoDAO;
// Importación del gestor Singleton de conexiones para manejo manual de transacciones.
import rapidexpress.util.DBConnection;

/**
 * Propósito: Gestionar la lógica compleja de planificación, asignación de carga, 
 * control transaccional de inicio/fin y monitoreo en vivo de las rutas de la flota.
 * 
 * @author User
 */
public class RutaService {

    // Repositorio para la persistencia de rutas.
    private final RutaDAO rutaDAO;
    // Repositorio para el control de estados y datos de vehículos.
    private final VehiculoDAO vehiculoDAO;
    // Repositorio para el control de estados y datos de conductores.
    private final ConductorDAO conductorDAO;
    // Repositorio para la actualización y vinculación de paquetes.
    private final PaqueteDAO paqueteDAO;
    // Servicio para la auditoría de eventos del sistema.
    private final AuditoriaService auditoriaService;

    // Constructor que inicializa los repositorios de datos y el servicio de auditoría.
    public RutaService() {
        // Instancia el DAO de rutas.
        this.rutaDAO = new RutaDAO();
        // Instancia el DAO de vehículos.
        this.vehiculoDAO = new VehiculoDAO();
        // Instancia el DAO de conductores.
        this.conductorDAO = new ConductorDAO();
        // Instancia el DAO de paquetes.
        this.paqueteDAO = new PaqueteDAO();
        // Instancia el servicio de auditoría.
        this.auditoriaService = new AuditoriaService();
    }

    /**
     * Planifica y crea una nueva ruta validando disponibilidad de recursos y peso máximo soportado.
     * 
     * @param vehiculoId ID del vehículo asignado.
     * @param conductorId ID del conductor asignado.
     * @param paqueteIds Lista de IDs de los paquetes a incluir en la ruta.
     * @return El ID numérico generado para la nueva ruta.
     * @throws DataBaseException Si ocurre un fallo en el acceso a datos.
     * @throws CapacidadExcedidaException Si la suma del peso supera la capacidad del vehículo.
     */
    public Integer crearRuta(Integer vehiculoId, Integer conductorId, List<Integer> paqueteIds) 
            throws DataBaseException, CapacidadExcedidaException {

        // PASO 1: Busca vehículo y verifica que su estado sea DISPONIBLE.
        Vehiculo vehiculo = vehiculoDAO.buscarPorId(vehiculoId);
        if (vehiculo == null || vehiculo.getEstadoEnum() != EstadoVehiculo.DISPONIBLE) {
            throw new IllegalArgumentException("El vehículo especificado no está DISPONIBLE para ser asignado.");
        }

        // PASO 2: Busca conductor y verifica que su estado sea ACTIVO.
        Conductor conductor = conductorDAO.buscarPorId(conductorId);
        if (conductor == null || conductor.getEstadoEnum() != EstadoConductor.ACTIVO) {
            throw new IllegalArgumentException("El conductor especificado no está ACTIVO para ser asignado.");
        }

        // PASO 3: Obtiene todos los objetos paquetes según la lista de IDs recibida.
        List<Paquete> paquetes = paqueteDAO.obtenerPorIds(paqueteIds);
        if (paquetes == null || paquetes.isEmpty()) {
            throw new IllegalArgumentException("Debe incluir al menos un paquete válido en la ruta.");
        }

        // PASO 4: Calcula pesoTotal sumando los pesos de cada paquete.
        double pesoTotal = 0.0;
        for (Paquete p : paquetes) {
            pesoTotal += p.getPeso();
        }

        // PASO 5: Verifica que el peso total no exceda la capacidad de carga del vehículo.
        if (pesoTotal > vehiculo.getCapacidadCarga()) {
            throw new CapacidadExcedidaException("El peso total (" + pesoTotal + " kg) excede la capacidad del vehículo (" + vehiculo.getCapacidadCarga() + " kg).");
        }

        // PASO 6: Instancia la entidad Ruta con el estado PLANIFICADA y la fecha actual.
        Ruta ruta = new Ruta();
        ruta.setVehiculo(vehiculo);
        ruta.setConductor(conductor);
        ruta.setEstado(EstadoRuta.PLANIFICADA);
        ruta.setFechaCreacion(new Date());

        // PASO 7: Persiste la ruta en la base de datos y recupera el ID autogenerado.
        Integer idGenerado = rutaDAO.guardar(ruta);
        ruta.setId(idGenerado);

        // PASO 8: Asigna la relación de cada paquete con la ruta creada en la BD.
        for (Paquete paquete : paquetes) {
            rutaDAO.asignarPaqueteARuta(ruta.getId(), paquete.getId());
        }

        // PASO 9: Registra el evento en la auditoría.
        auditoriaService.registrarAccion("CREATE", "RUTA", "ID: " + ruta.getId());

        // PASO 10: Retorna el ID numérico de la ruta creada.
        return ruta.getId();
    }

    /**
     * Inicia una ruta cambiando transaccionalmente el estado de la ruta, vehículo, conductor y paquetes.
     * 
     * @param rutaId ID de la ruta a iniciar.
     * @return true si la transacción se completó e hizo commit.
     * @throws DataBaseException Si ocurre un fallo en la base de datos o durante el rollback.
     */
    public boolean iniciarRuta(Integer rutaId) throws DataBaseException {
        // Obtiene la conexión compartida desde el Singleton.
        Connection conn = null;

        try {
            // Obtiene la conexión de la BD.
            conn = DBConnection.getInstance().getConnection();
            // IMPORTANTE: Desactiva el auto-commit para iniciar un bloque transaccional explícito.
            conn.setAutoCommit(false);

            // PASO 1: Busca los datos de la ruta objetivo.
            Ruta ruta = rutaDAO.buscarPorId(rutaId);
            if (ruta == null) {
                throw new IllegalArgumentException("No se encontró la ruta con ID: " + rutaId);
            }

            // PASO 2: Cambia el estado de la ruta a EN_CURSO en la base de datos.
            rutaDAO.iniciarRuta(conn, rutaId);

            // PASO 3: Actualiza el estado del vehículo asignado a EN_RUTA.
            vehiculoDAO.actualizarEstado(conn, ruta.getVehiculo().getId(), EstadoVehiculo.EN_RUTA);

            // PASO 4: Actualiza el estado del conductor asignado a EN_RUTA.
            conductorDAO.actualizarEstado(conn, ruta.getConductor().getId(), EstadoConductor.EN_RUTA);

            // PASO 5: Actualiza el estado de cada paquete asociado a la ruta a EN_TRANSITO.
            List<Paquete> paquetes = paqueteDAO.listarPorRuta(conn, rutaId);
            for (Paquete paquete : paquetes) {
                paqueteDAO.actualizarEstado(conn, paquete.getTrackingId(), EstadoPaquete.EN_TRANSITO);
            }

            // PASO 6: Aplica la transacción confirmando todos los cambios de forma atómica en la BD.
            conn.commit();

            // PASO 7: Registra el evento de inicio en la auditoría del sistema.
            auditoriaService.registrarAccion("UPDATE", "RUTA", "Iniciada: " + rutaId);

            // Retorna verdadero indicando éxito.
            return true;

        } catch (Exception e) {
            // En caso de fallo, revierte de inmediato todos los cambios realizados en el bloque.
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DataBaseException("Error crítico al realizar el rollback: " + ex.getMessage(), ex);
                }
            }
            // Lanza la excepción encapsulada.
            throw new DataBaseException("Error al iniciar la ruta ID " + rutaId + ": " + e.getMessage(), e);

        } finally {
            // Restablece el comportamiento por defecto de la conexión (autoCommit = true).
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    // Cierre seguro de la conexión.
                }
            }
        }
    }

    /**
     * Completa el ciclo de vida de una ruta, liberando transaccionalmente el vehículo y al conductor.
     * 
     * @param rutaId ID de la ruta a finalizar.
     * @return true si la transacción se completó con commit.
     * @throws DataBaseException Si ocurre una falla transaccional.
     */
    public boolean completarRuta(Integer rutaId) throws DataBaseException {
        // Conexión para el manejo explícito de la transacción.
        Connection conn = null;

        try {
            // Obtiene la conexión.
            conn = DBConnection.getInstance().getConnection();
            // Desactiva el autocommit para la transacción.
            conn.setAutoCommit(false);

            // PASO 1: Obtiene la ruta de la base de datos.
            Ruta ruta = rutaDAO.buscarPorId(rutaId);
            if (ruta == null) {
                throw new IllegalArgumentException("No existe la ruta especificada.");
            }

            // PASO 2: Actualiza el estado de la ruta a COMPLETADA.
            rutaDAO.actualizarEstado(conn, rutaId, EstadoRuta.COMPLETADA);

            // PASO 3: Restablece el estado del vehículo a DISPONIBLE.
            vehiculoDAO.actualizarEstado(conn, ruta.getVehiculo().getId(), EstadoVehiculo.DISPONIBLE);

            // PASO 4: Restablece el estado del conductor a ACTIVO.
            conductorDAO.actualizarEstado(conn, ruta.getConductor().getId(), EstadoConductor.ACTIVO);

            // PASO 5: Confirma la transacción en la base de datos.
            conn.commit();

            // PASO 6: Audita la finalización de la ruta.
            auditoriaService.registrarAccion("UPDATE", "RUTA", "Completada: " + rutaId);

            // Confirma la operación.
            return true;

        } catch (Exception e) {
            // Realiza el rollback ante cualquier inconsistencia.
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DataBaseException("Error al ejecutar rollback: " + ex.getMessage(), ex);
                }
            }
            // Propaga la excepción.
            throw new DataBaseException("Error al completar la ruta ID " + rutaId + ": " + e.getMessage(), e);

        } finally {
            // Restablece el estado del autoCommit.
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    // Cierre de recurso.
                }
            }
        }
    }

    /**
     * Obtiene el listado de rutas que están actualmente en proceso de ejecución.
     * 
     * @return Lista de rutas con estado EN_CURSO.
     * @throws DataBaseException Si ocurre un fallo en la consulta SQL.
     */
    public List<Ruta> monitorearRutasActivas() throws DataBaseException {
        // Retorna las rutas activas provistas por la consulta del DAO.
        return rutaDAO.listarActivas();
    }

    /**
     * Actualiza el estado de un paquete dentro de una ruta validando que esté activa y asociada.
     * 
     * @param rutaId ID de la ruta en curso.
     * @param trackingId Código de rastreo del paquete.
     * @param estado Nuevo estado a asignar al paquete.
     * @return true si la actualización del paquete fue exitosa.
     * @throws DataBaseException Si falla la base de datos.
     */
    public boolean actualizarPaqueteEnRuta(Integer rutaId, String trackingId, EstadoPaquete estado) throws DataBaseException {
        // Verifica que la ruta exista.
        Ruta ruta = rutaDAO.buscarPorId(rutaId);
        if (ruta == null) {
            throw new IllegalArgumentException("No se encontró la ruta especificada.");
        }

        // Verifica que la ruta se encuentre actualmente EN_CURSO.
        if (ruta.getEstadoEnum() != EstadoRuta.EN_CURSO) {
            throw new IllegalStateException("Solo se pueden actualizar paquetes de rutas que estén EN_CURSO.");
        }

        // Verifica que el paquete realmente pertenezca a la ruta especificada.
        boolean pertenece = rutaDAO.verificarPaqueteEnRuta(rutaId, trackingId);
        if (!pertenece) {
            throw new IllegalArgumentException("El paquete " + trackingId + " no pertenece a la ruta ID " + rutaId);
        }

        // Actualiza el estado del paquete mediante el paqueteDAO.
        boolean actualizado = paqueteDAO.actualizarEstado(trackingId, estado);

        // Si se actualizó, audita el cambio de estado del paquete dentro de la ruta.
        if (actualizado) {
            auditoriaService.registrarAccion("UPDATE", "PAQUETE_RUTA", "Ruta: " + rutaId + " | Tracking: " + trackingId + " -> " + estado);
        }

        // Retorna el resultado final de la actualización.
        return actualizado;
    }
}