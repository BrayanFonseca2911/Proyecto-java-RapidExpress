/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.servicio;

/**
 *
 * @author User
 */
public class MantenimientoService {
    /*Propósito: Gestión de mantenimientos.
Atributos:
private MantenimientoDAO mantenimientoDAO
private VehiculoService vehiculoService
Métodos:
public boolean programarMantenimiento(String placa, Mantenimiento m):
Busca vehículo
vehiculoService.cambiarEstado(placa, EstadoVehiculo.EN_MANTENIMIENTO)
mantenimientoDAO.guardar(m)
public boolean registrarMantenimiento(Mantenimiento m):
mantenimientoDAO.guardar(m)
public List<Mantenimiento> listarHistorialVehiculo(String placa):
Busca vehículo por placa
Retorna mantenimientoDAO.listarPorVehiculo(vehiculo.getId())
public List<Mantenimiento> listarMantenimientosPendientes():
Retorna lista de vehículos EN_MANTENIMIENTO
*/
}
