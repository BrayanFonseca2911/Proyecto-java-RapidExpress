/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.excepciones;

/**
 *
 * @author User
 */
public class CapacidadExcedidaException extends Exception {
    public CapacidadExcedidaException(String mensaje) {
        super(mensaje);
    }
    
    public CapacidadExcedidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
