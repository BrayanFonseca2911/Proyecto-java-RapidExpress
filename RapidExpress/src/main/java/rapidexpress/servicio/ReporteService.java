/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.servicio;

/**
 *
 * @author User
 */
public class ReporteService {
    /*Propósito: Generación de reportes operativos.
Atributos:
private PaqueteDAO paqueteDAO
private RutaDAO rutaDAO
private VehiculoDAO vehiculoDAO
Métodos:
public Map<String, List<String>> getEntregasPorConductor(Integer conductorId, Date inicio, Date fin):
Map<Conductor, List<Paquete>>
paqueteDAO.listarEntregasPorConductor(conductorId, inicio, fin)
Agrupa por conductor
Retorna Map con nombre de conductor y lista de tracking IDs
public List<Ruta> getHistorialRutasVehiculo(String placa):
Busca vehículo por placa
rutaDAO.listarHistorialVehiculo(vehiculo.getId())
public Map<EstadoPaquete, Integer> getResumenPaquetesPorEstado():
Map<EstadoPaquete, Integer>
Por cada estado, cuenta paquetes
Retorna mapa estado -> cantidad
public List<Vehiculo> getVehiculosEnMantenimiento():
vehiculoDAO.listarPorEstado(EstadoVehiculo.EN_MANTENIMIENTO)
public Map<String, Integer> getRendimientoConductores(Date inicio, Date fin):
Map<Conductor, Integer>
Cuenta entregas por conductor en rango de fechas*/
}
