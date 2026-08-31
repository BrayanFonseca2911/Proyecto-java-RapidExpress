/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.servicio;

/**
 *
 * @author User
 */
public class AuditoriaService {
    /*Propósito: Gestiona el sistema de auditoría.
Atributos:
private AuditoriaDAO auditoriaDAO
private AuditLogger auditLogger
Métodos:
public void registrarAccion(String accion, String tabla, String detalle):
Crea objeto Auditoria con fecha actual, usuario "system"
auditoriaDAO.guardar(auditoria)
auditLogger.log(auditoria)
public void registrarAccion(String usuario, String accion, String tabla, String detalle):
Similar pero con usuario específico
public List<Auditoria> getLogsPorFecha(Date fecha):
Retorna auditoriaDAO.listarPorFecha(fecha)
public List<Auditoria> getLogsPorAccion(String accion):
Retorna auditoriaDAO.listarPorAccion(accion)*/
}
