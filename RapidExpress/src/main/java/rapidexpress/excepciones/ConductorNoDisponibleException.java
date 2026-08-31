/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.excepciones;

/**
 *
 * @author User
 */

public class ConductorNoDisponibleException extends Exception {
    
    /**
     * Constructor específico cuando se pasa el nombre del conductor.
     * Genera automáticamente el mensaje de error.
     * 
     * @param nombre Nombre del conductor no disponible
     */
    public ConductorNoDisponibleException(String nombre) {
        super("El conductor '" + nombre + "' no está disponible");
    }
    
    /**
     * Constructor genérico con mensaje personalizado y causa del error.
     * 
     * @param mensaje Mensaje descriptivo del error
     * @param causa Excepción original que provocó este error
     */
    public ConductorNoDisponibleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    /**
     * Constructor que solo recibe la causa del error.
     * 
     * @param causa Excepción original
     */
    public ConductorNoDisponibleException(Throwable causa) {
        super(causa);
    }
}
