/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.repositorio;

/**
 *
 * @author User
 */
public class RutaDAO {
    /*Propósito: Implementa acceso a datos para Ruta.
Implementa: IDAO<Ruta, Integer>
Métodos:
CRUD básico
public List<Ruta> listarActivas():
SQL: SELECT * FROM ruta WHERE estado = 'EN_CURSO'
public List<Ruta> listarPorVehiculo(Integer vehiculoId):
SQL: SELECT * FROM ruta WHERE vehiculo_id = ? ORDER BY fecha DESC
public List<Ruta> listarPorFecha(Date fecha):
SQL: SELECT * FROM ruta WHERE DATE(fecha) = DATE(?)
public List<Ruta> listarHistorialVehiculo(Integer vehiculoId):
SQL: SELECT * FROM ruta WHERE vehiculo_id = ? AND estado = 'COMPLETADA'
public boolean asignarPaqueteARuta(Integer rutaId, Integer paqueteId):
SQL: INSERT INTO ruta_paquete (ruta_id, paquete_id) VALUES (?, ?)
public List<Paquete> obtenerPaquetesDeRuta(Integer rutaId):
SQL: SELECT p.* FROM paquete p INNER JOIN ruta_paquete rp ON p.id = rp.paquete_id WHERE rp.ruta_id = ?
public boolean iniciarRuta(Integer rutaId):
SQL: UPDATE ruta SET estado = 'EN_CURSO' WHERE id = ?
public boolean completarRuta(Integer rutaId):
SQL: UPDATE ruta SET estado = 'COMPLETADA' WHERE id = ?*/
}
