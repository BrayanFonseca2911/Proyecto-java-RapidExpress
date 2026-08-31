/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.servicio;

/**
 *
 * @author User
 */
public class ConductorService {
    /*Propósito: Lógica de negocio para conductores.
Atributos:
private ConductorDAO conductorDAO
private VehiculoDAO vehiculoDAO
Métodos:
public boolean registrarConductor(Conductor c):
Valida número de identificación único
conductorDAO.guardar(c)
public boolean actualizarConductor(Conductor c):
conductorDAO.actualizar(c)
public boolean asignarVehiculo(String numeroId, String placa):
Busca conductor por numeroId
Verifica conductor.estado == ACTIVO
Busca vehículo por placa
Verifica vehículo.estado == DISPONIBLE
conductorDAO.asignarVehiculo(conductor.getId(), vehiculo.getId())
vehiculo.setConductorAsignado(conductor)
vehiculoDAO.actualizar(vehiculo)
Retorna true si todo ok
public boolean liberarVehiculo(String numeroId):
Busca conductor
conductorDAO.liberarVehiculo(conductor.getId())
Libera vehículo asociado
public List<Conductor> listarDisponibles():
Retorna conductorDAO.listarDisponibles()*/
}
