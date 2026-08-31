/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.excepciones;

/**
 *
 * @author User
 */

// Excepción que indica que la búsqueda de un paquete mediante su código
// de seguimiento no retornó ningún resultado en el sistema.
public class PaqueteNotFoundException extends Exception {

    // Almacena el número o código de seguimiento consultado.
    private final String numeroSeguimiento;

    // Constructor que recibe la clave de seguimiento que falló en la búsqueda.
    public PaqueteNotFoundException(String numeroSeguimiento) {
        // Forma el mensaje de notificación de error y lo asigna a la clase base.
        super("No existe ningún paquete registrado con el número de seguimiento: " + numeroSeguimiento);
        // Retiene el código para mostrarlo en los logs o respuestas del CLI.
        this.numeroSeguimiento = numeroSeguimiento;
    }

    // Método de acceso para obtener el número de seguimiento ingresado.
    public String getNumeroSeguimiento() {
        return numeroSeguimiento;
    }
}