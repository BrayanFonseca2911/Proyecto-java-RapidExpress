/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.repositorio;

/**
 *
 * @author User
 */
public class PaqueteDAO {
    /*Propósito: Implementa acceso a datos para Paquete.
Implementa: IDAO<Paquete, Integer>
Métodos:
CRUD básico
public Paquete buscarPorTrackingId(String trackingId):
SQL: SELECT * FROM paquete WHERE tracking_id = ?
public List<Paquete> listarPorEstado(EstadoPaquete estado):
SQL: SELECT * FROM paquete WHERE estado = ?
public List<Paquete> listarEnBodega():
SQL: SELECT * FROM paquete WHERE estado = 'EN_BODEGA'
public List<Paquete> listarPorRangoFechas(Date inicio, Date fin):
SQL: SELECT * FROM paquete WHERE fecha_registro BETWEEN ? AND ?
public List<Paquete> listarEntregasPorConductor(Integer conductorId, Date inicio, Date fin):
SQL complejo con JOIN entre paquete, ruta y conductor
WHERE estado = 'ENTREGADO' AND conductor_id = ? AND fecha_entrega BETWEEN ? AND ?
public boolean actualizarEstado(String trackingId, EstadoPaquete estado):
SQL: UPDATE paquete SET estado = ?, fecha_entrega = ? WHERE tracking_id = ?*/
}
