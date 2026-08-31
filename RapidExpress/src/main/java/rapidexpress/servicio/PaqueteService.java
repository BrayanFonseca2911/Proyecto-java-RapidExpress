/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.servicio;

/**
 *
 * @author User
 */
public class PaqueteService {
    /*Propósito: Lógica de negocio para paquetes.
Atributos:
private PaqueteDAO paqueteDAO
private AuditoriaService auditoriaService
Métodos:
public boolean registrarPaquete(Paquete p):
p.generarTrackingId()
p.setFechaRegistro(new Date())
p.setEstado(EstadoPaquete.EN_BODEGA)
paqueteDAO.guardar(p)
auditoriaService.registrarAccion("CREATE", "PAQUETE", "Tracking: " + p.getTrackingId())
public Paquete buscarPorTracking(String trackingId):
Retorna paqueteDAO.buscarPorTrackingId(trackingId)
public boolean actualizarEstado(String trackingId, EstadoPaquete estado):
Busca paquete
Valida transición de estados (ej: no puede ir de ENTREGADO a EN_BODEGA)
paqueteDAO.actualizarEstado(trackingId, estado)
Si estado == ENTREGADO, registra fecha
public List<Paquete> listarEnBodega():
Retorna paqueteDAO.listarEnBodega()
public List<Paquete> listarPorEstado(EstadoPaquete estado):
Retorna paqueteDAO.listarPorEstado(estado)
public boolean entregarPaquete(String trackingId):
Busca paquete
actualizaEstado(trackingId, EstadoPaquete.ENTREGADO)
auditoriaService.registrarAccion("UPDATE", "PAQUETE", "Entregado: " + trackingId)*/
}
