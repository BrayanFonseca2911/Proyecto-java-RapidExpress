/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.repositorio;

/**
 *
 * @author User
 */
public class VehiculoDAO {
   /*Propósito: Implementa acceso a datos para Vehiculo.
Implementa: IDAO<Vehiculo, Integer>
Atributos:
private DBConnection dbConnection
Métodos:
public VehiculoDAO(): Constructor que inicializa dbConnection
public Vehiculo buscarPorId(Integer id):
SQL: SELECT * FROM vehiculo WHERE id = ?
ResultSet a objeto Vehiculo
public List<Vehiculo> listarTodos():
SQL: SELECT * FROM vehiculo ORDER BY placa
public boolean guardar(Vehiculo v):
SQL: INSERT INTO vehiculo (placa, marca, modelo, anio, capacidad_carga, estado) VALUES (?, ?, ?, ?, ?, ?)
public boolean actualizar(Vehiculo v):
SQL: UPDATE vehiculo SET marca=?, modelo=?, anio=?, capacidad_carga=?, estado=? WHERE id=?
public boolean eliminar(Integer id):
SQL: DELETE FROM vehiculo WHERE id=?
public Vehiculo buscarPorPlaca(String placa):
SQL: SELECT * FROM vehiculo WHERE placa = ?
public List<Vehiculo> listarPorEstado(EstadoVehiculo estado):
SQL: SELECT * FROM vehiculo WHERE estado = ?
public List<Vehiculo> listarDisponibles():
SQL: SELECT * FROM vehiculo WHERE estado = 'DISPONIBLE'
private Vehiculo mapearResultSet(ResultSet rs):
Convierte ResultSet a objeto Vehiculo
Maneja SQLException
*/ 
}
