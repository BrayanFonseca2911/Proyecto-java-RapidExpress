/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.controlador;

/**
 *
 * @author User
 */
public class RutaController {
    /*Propósito: Coordina operaciones de rutas (planificación y seguimiento).
Atributos:
private RutaService service
private RutaView view
private VehiculoService vehiculoService
private ConductorService conductorService
private PaqueteService paqueteService
Métodos:
public void gestionarRutas(): Menú de rutas
private void crearRuta():
Obtiene vehículos disponibles (vehiculoService.listarDisponibles())
view.seleccionarVehiculo()
Obtiene conductores disponibles (conductorService.listarDisponibles())
view.seleccionarConductor()
Obtiene paquetes en bodega (paqueteService.listarEnBodega())
view.seleccionarPaquetes() (permite múltiple selección)
service.crearRuta() valida capacidad y crea ruta
private void iniciarRuta():
Pide ID de ruta planificada
service.iniciarRuta() cambia estados de vehículo, conductor y paquetes
Usa transacción
private void monitorearRutas():
service.monitorearRutasActivas()
view.mostrarRutasActivas()
private void actualizarPaqueteEnRuta():
Pide trackingId
Permite cambiar estado (ej: a ENTREGADO)
private void completarRuta():
Marca ruta como COMPLETADA
Libera vehículo y conductor*/
}
