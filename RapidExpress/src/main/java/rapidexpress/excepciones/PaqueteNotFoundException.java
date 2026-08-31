/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.excepciones;

/**
 *
 * @author User
 */
public class PaqueteNotFoundException extends Exception {
    public PaqueteNotFoundException(String trackingId) {
        super("No se encontró el paquete con tracking ID: " + trackingId);
    }
}
