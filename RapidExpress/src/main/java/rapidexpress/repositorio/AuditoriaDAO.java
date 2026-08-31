/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.repositorio;

/**
 *
 * @author User
 */
public class AuditoriaDAO {
    /*Propósito: Implementa acceso a datos para Auditoria.
Métodos:
public boolean guardar(Auditoria a):
SQL: INSERT INTO auditoria (fecha, usuario, accion, tabla_afectada, registro_id, detalle) VALUES (?, ?, ?, ?, ?, ?)
public List<Auditoria> listarPorFecha(Date fecha):
SQL: SELECT * FROM auditoria WHERE DATE(fecha) = DATE(?) ORDER BY fecha DESC
public List<Auditoria> listarPorAccion(String accion):
SQL: SELECT * FROM auditoria WHERE accion = ? ORDER BY fecha DESC*/
}
