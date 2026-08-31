/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.servicio;

/**
 *
 * @author User
 */
public class RutaService {
    /*Propósito: Lógica compleja de planificación y ejecución de rutas.
Atributos:
private RutaDAO rutaDAO
private VehiculoDAO vehiculoDAO
private ConductorDAO conductorDAO
private PaqueteDAO paqueteDAO
private AuditoriaService auditoriaService
Métodos:
public Integer crearRuta(Integer vehiculoId, Integer conductorId, List<Integer> paqueteIds):
Paso 1: Busca vehículo y verifica estado == DISPONIBLE
Paso 2: Busca conductor y verifica estado == ACTIVO
Paso 3: Obtiene todos los paquetes por IDs
Paso 4: Calcula pesoTotal = suma de pesos de paquetes
Paso 5: Verifica pesoTotal <= vehiculo.getCapacidadCarga()
Si excede, lanza CapacidadExcedidaException
Paso 6: Crea objeto Ruta con estado PLANIFICADA
Paso 7: rutaDAO.guardar(ruta) - obtiene ID generado
Paso 8: Por cada paquete, rutaDAO.asignarPaqueteARuta(ruta.getId(), paquete.getId())
Paso 9: auditoriaService.registrarAccion("CREATE", "RUTA", "ID: " + ruta.getId())
Paso 10: Retorna ruta.getId()
public boolean iniciarRuta(Integer rutaId):
IMPORTANTE: Usa transacción (dbConnection.getConnection().setAutoCommit(false))
try {
Paso 1: Busca ruta
Paso 2: rutaDAO.iniciarRuta(rutaId) - cambia a EN_CURSO
Paso 3: vehiculoDAO.actualizarEstado(ruta.getVehiculo().getId(), EstadoVehiculo.EN_RUTA)
Paso 4: conductorDAO.actualizarEstado(ruta.getConductor().getId(), EstadoConductor.EN_RUTA)
Paso 5: Por cada paquete en la ruta:
paqueteDAO.actualizarEstado(paquete.getTrackingId(), EstadoPaquete.EN_TRANSITO)
Paso 6: dbConnection.getConnection().commit()
Paso 7: auditoriaService.registrarAccion("UPDATE", "RUTA", "Iniciada: " + rutaId)
Retorna true
} catch (Exception e) {
dbConnection.getConnection().rollback()
Lanza excepcion
} finally {
dbConnection.getConnection().setAutoCommit(true)
}
public boolean completarRuta(Integer rutaId):
Similar a iniciarRuta pero con commit
Cambia ruta a COMPLETADA
Cambia vehículo a DISPONIBLE
Cambia conductor a ACTIVO
public List<Ruta> monitorearRutasActivas():
Retorna rutaDAO.listarActivas()
public boolean actualizarPaqueteEnRuta(Integer rutaId, String trackingId, EstadoPaquete estado):
Verifica que la ruta esté EN_CURSO
Verifica que el paquete pertenezca a esa ruta
paqueteDAO.actualizarEstado(trackingId, estado)*/
}
