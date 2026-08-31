/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.servicio;

/**
 *
 * @author User
 */
public class VehiculoService {
    /*Propósito: Lógica de negocio para vehículos.
Atributos:
private VehiculoDAO vehiculoDAO
private AuditoriaService auditoriaService
Métodos:
public VehiculoService(): Inicializa DAOs
public boolean registrarVehiculo(Vehiculo v):
Valida que placa no exista (buscarPorPlaca)
Valida datos (peso > 0, año válido)
vehiculoDAO.guardar(v)
auditoriaService.registrarAccion("CREATE", "VEHICULO", "Placa: " + v.getPlaca())
public boolean actualizarVehiculo(Vehiculo v):
Valida que exista
vehiculoDAO.actualizar(v)
auditoriaService.registrarAccion("UPDATE", "VEHICULO", "ID: " + v.getId())
public boolean eliminarVehiculo(String placa):
Busca vehículo
Verifica que no tenga rutas activas
vehiculoDAO.eliminar(id)
public Vehiculo buscarPorPlaca(String placa):
Retorna vehiculoDAO.buscarPorPlaca(placa)
public List<Vehiculo> listarTodos():
Retorna vehiculoDAO.listarTodos()
public List<Vehiculo> listarDisponibles():
Retorna vehiculoDAO.listarDisponibles()
public boolean programarMantenimiento(String placa, Mantenimiento m):
Busca vehículo
Cambia estado a EN_MANTENIMIENTO
mantenimientoDAO.guardar(m)
public boolean validarCapacidad(String placa, double peso):
Busca vehículo
Retorna v.getCapacidadCarga() >= peso*/
}
