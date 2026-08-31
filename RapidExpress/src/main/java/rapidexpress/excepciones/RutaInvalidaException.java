/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.excepciones;

/**
 *
 * @author User
 */
public class RutaInvalidaException extends Exception {
    public RutaInvalidaException(String mensaje) {
        super(mensaje);
    }
    
    public RutaInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
