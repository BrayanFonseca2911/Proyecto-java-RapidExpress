/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.excepciones;

/**
 *
 * @author User
 */

// Excepción personalizada para capturar y centralizar los errores técnicos
// relacionados con la base de datos (por ejemplo: fallos de conexión o consultas SQL).
public class DataBaseException extends Exception {

    // Variable inmutable para almacenar la operación técnica donde ocurrió el fallo (ej. "INSERT_VEHICULO").
    private final String operacion;

    // Constructor que recibe el mensaje descriptive, el nombre de la operación y la causa raíz del error.
    public DataBaseException(String mensaje, String operacion, Throwable causa) {
        // Pasa el mensaje descriptivo y el objeto causa (ej. SQLException) a la clase padre Exception.
        super(mensaje, causa);
        // Guarda la operación en el atributo local para su posterior consulta.
        this.operacion = operacion;
    }

    // Método de acceso para obtener el nombre de la operación que causó la excepción.
    public String getOperacion() {
        return operacion;
    }
}