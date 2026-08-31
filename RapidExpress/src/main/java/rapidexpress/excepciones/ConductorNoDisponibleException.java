/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.excepciones;

/**
 *
 * @author User
 */

// Excepción lanzada cuando se intenta asignar a una ruta un conductor
// que está inactivo, en vacaciones o asignado previamente a otra ruta activa.
public class ConductorNoDisponibleException extends Exception {

    // Guarda el número de documento/cédula del conductor no disponible.
    private final String cedulaConductor;

    // Constructor que recibe el documento del conductor y el estado que impide su asignación.
    public ConductorNoDisponibleException(String cedulaConductor, String estadoActual) {
        // Construye y envía el mensaje explicativo a la clase base Exception.
        super("El conductor con cédula " + cedulaConductor + " no se encuentra disponible. Estado actual: " + estadoActual);
        // Guarda la cédula del conductor para identificación precisa en la vista.
        this.cedulaConductor = cedulaConductor;
    }

    // Método de acceso para obtener la cédula del conductor asociado al error.
    public String getCedulaConductor() {
        return cedulaConductor;
    }
}