/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.servicio;

// Importaciones para el manejo de fechas del sistema y SQL.
import java.util.Date;
// Importación para retornar colecciones de registros de auditoría.
import java.util.List;

// Importación del modelo de dominio Auditoria.
import rapidexpress.dominio.Auditoria;
// Importación de la excepción personalizada de persistencia.
import rapidexpress.excepciones.DataBaseException;
// Importación del repositorio de auditoría.
import rapidexpress.repositorio.AuditoriaDAO;
// Importación del componente de registro en archivo/consola.
import rapidexpress.util.AuditLogger;

/**
 * Propósito: Gestionar el sistema centralizado de auditoría y trazabilidad 
 * de eventos, guardando registros en la base de datos y en los logs del sistema.
 * 
 * @author User
 */
public class AuditoriaService {

    // Repositorio para la persistencia de los registros de auditoría en base de datos.
    private final AuditoriaDAO auditoriaDAO;
    // Componente auxiliar para el registro persistente de logs en archivos de texto o consola.
    private final AuditLogger auditLogger;

    // Constructor que inicializa los componentes de persistencia y registro.
    public AuditoriaService() {
        // Instancia el DAO de auditoría.
        this.auditoriaDAO = new AuditoriaDAO();
        // Obtiene la instancia del registrador de logs.
        this.auditLogger = new AuditLogger();
    }

    /**
     * Registra una acción ejecutada de forma automática por el sistema ("system").
     * 
     * @param accion Operación realizada (ej. CREATE, UPDATE, DELETE).
     * @param tabla Tabla o entidad afectada en el sistema.
     * @param detalle Descripción detallada o identificadores del cambio.
     */
    public void registrarAccion(String accion, String tabla, String detalle) {
        // Invoca el método sobrecargado especificando el usuario por defecto "system".
        registrarAccion("system", accion, tabla, detalle);
    }

    /**
     * Registra una acción especificando el usuario que la ejecutó.
     * 
     * @param usuario Nombre o nombre de usuario que ejecuta la acción.
     * @param accion Tipo de operación efectuada (ej. CREATE, UPDATE, DELETE).
     * @param tabla Tabla o entidad sobre la cual se operó.
     * @param detalle Información complementaria sobre la acción.
     */
    public void registrarAccion(String usuario, String accion, String tabla, String detalle) {
        try {
            // Crea un nuevo objeto de la entidad Auditoria.
            Auditoria auditoria = new Auditoria();
            // Asigna el usuario responsable.
            auditoria.setUsuario(usuario != null ? usuario : "system");
            // Asigna el tipo de acción realizada.
            auditoria.setAccion(accion);
            // Asigna el nombre de la tabla o módulo afectado.
            auditoria.setTabla(tabla);
            // Asigna los detalles descriptivos del evento.
            auditoria.setDetalle(detalle);
            // Asigna la fecha y hora exactas en que ocurre el evento.
            auditoria.setFecha(new Date());

            // Guarda el registro de auditoría en la base de datos a través del DAO.
            auditoriaDAO.guardar(auditoria);

            // Escribe la traza en el log físico o consola mediante el logger.
            auditLogger.log(auditoria);

        } catch (DataBaseException e) {
            // Si ocurre un fallo en la BD, garantiza que el evento quede guardado al menos en el log físico.
            auditLogger.logError("Error al guardar auditoría en BD: " + e.getMessage());
        }
    }

    /**
     * Recupera el listado de logs de auditoría filtrados por una fecha específica.
     * 
     * @param fecha Fecha de consulta.
     * @return Lista de registros de auditoría del día indicado.
     * @throws DataBaseException Si ocurre un error en la consulta SQL.
     */
    public List<Auditoria> getLogsPorFecha(Date fecha) throws DataBaseException {
        // Valida que la fecha no sea nula.
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha para consultar logs no puede ser nula.");
        }
        // Convierte java.util.Date a java.sql.Date para compatibilidad con el DAO JDBC.
        java.sql.Date fechaSql = new java.sql.Date(fecha.getTime());
        // Invoca la consulta parametrizada en el repositorio.
        return auditoriaDAO.listarPorFecha(fechaSql);
    }

    /**
     * Recupera los logs de auditoría filtrados por tipo de acción.
     * 
     * @param accion Tipo de acción a filtrar (ej. "CREATE", "UPDATE", "DELETE").
     * @return Lista de eventos que coinciden con la acción dada.
     * @throws DataBaseException Si falla la consulta en la base de datos.
     */
    public List<Auditoria> getLogsPorAccion(String accion) throws DataBaseException {
        // Valida que el nombre de la acción no esté vacío.
        if (accion == null || accion.trim().isEmpty()) {
            throw new IllegalArgumentException("La acción a consultar no puede estar vacía.");
        }
        // Invoca la consulta por filtro de acción en el DAO.
        return auditoriaDAO.listarPorAccion(accion);
    }
}