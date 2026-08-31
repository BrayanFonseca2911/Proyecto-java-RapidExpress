/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.controlador;

/**
 *
 * @author User
 */
public class VehiculoController {
    /*Propósito: Coordina las operaciones de vehículos entre vista y servicio.
Atributos:
private VehiculoService service
private VehiculoView view
private AuditLogger auditLogger
Métodos:
public VehiculoController(): Inicializa service, view y auditLogger
public void gestionarVehiculos():
Bucle while con menú de opciones
Switch para cada opción (1-6)
Llama a métodos específicos según opción
private void registrarVehiculo():
Llama a view.solicitarDatosVehiculo()
Llama a service.registrarVehiculo()
Registra auditoría
Maneja excepciones y muestra mensajes
private void listarVehiculos():
Llama a service.listarTodos()
Llama a view.mostrarListaVehiculos()
private void buscarVehiculo():
Pide placa con view.solicitarPlaca()
Busca con service.buscarPorPlaca()
Muestra con view.mostrarVehiculo()
private void actualizarVehiculo():
Busca vehículo existente
Solicita nuevos datos
Actualiza y registra auditoría
private void programarMantenimiento():
Pide placa y datos de mantenimiento
Llama a service.programarMantenimiento()
Cambia estado a EN_MANTENIMIENTO*/
}
