/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.repositorio;

/**
 *
 * @author User
 */
public class ConductorDAO {
    /*Propósito: Implementa acceso a datos para Conductor.
Implementa: IDAO<Conductor, Integer>
Métodos:
CRUD básico (buscarPorId, listarTodos, guardar, actualizar, eliminar)
public Conductor buscarPorNumeroIdentificacion(String numeroId):
SQL: SELECT * FROM conductor WHERE numero_identificacion = ?
public List<Conductor> listarPorEstado(EstadoConductor estado):
SQL: SELECT * FROM conductor WHERE estado = ?
public List<Conductor> listarDisponibles():
SQL: SELECT * FROM conductor WHERE estado = 'ACTIVO' AND vehiculo_id IS NULL
public boolean asignarVehiculo(Integer conductorId, Integer vehiculoId):
SQL: UPDATE conductor SET vehiculo_id = ? WHERE id = ?
public boolean liberarVehiculo(Integer conductorId):
SQL: UPDATE conductor SET vehiculo_id = NULL WHERE id = ?*/
}
