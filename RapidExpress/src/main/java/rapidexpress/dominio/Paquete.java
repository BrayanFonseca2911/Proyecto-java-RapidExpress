/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.dominio;

/**
 *
 * @author User
 */
public class Paquete {
    /*Propósito: Representa un paquete/envío en el sistema.
Atributos:
id (int): Identificador único
trackingId (String): Código único de seguimiento (generado automáticamente)
descripcion (String): Contenido del paquete
peso (double): Peso en kg
dimensiones (String): Dimensiones (ej: "30x20x15")
origen (String): Dirección de origen
destino (String): Dirección de destino
estado (EstadoPaquete): EN_BODEGA, ASIGNADO_A_RUTA, EN_TRANSITO, ENTREGADO, DEVUELTO
remitente (Remitente): Datos del remitente
destinatario (Destinatario): Datos del destinatario
fechaRegistro (Date): Fecha de creación
fechaEntrega (Date): Fecha de entrega (puede ser null)
Métodos:
Constructor completo y vacío
Getters y setters
toString()
generarTrackingId(): String - genera ID único (UUID o formato personalizado)
entregar(): Cambia estado a ENTREGADO y registra fecha
devolver(): Cambia estado a DEVUELTO*/
}
