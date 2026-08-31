/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.excepciones;

/**
 *
 * @author User
 */

public class VehiculoNoDisponibleException extends Exception {
    
    // Constructor específico para placa
    public VehiculoNoDisponibleException(String placa) {
        super("El vehículo con placa " + placa + " no está disponible");
    }
    
    // Constructor genérico con mensaje personalizado
    public VehiculoNoDisponibleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    // Constructor solo con causa
    public VehiculoNoDisponibleException(Throwable causa) {
        super(causa);
    }
}
