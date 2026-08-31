/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.excepciones;

/**
 *
 * @author User
 */

// Excepción para representar violaciones en la lógica operativa de las rutas
// (ejemplo: despachar una ruta sin paquetes o finalizar una ruta ya cerrada).
public class RutaInvalidaException extends Exception {

    // Guarda el identificador único o código de la ruta procesada.
    private final String codigoRuta;

    // Constructor que toma el identificador de la ruta y el motivo del fallo operativo.
    public RutaInvalidaException(String codigoRuta, String motivo) {
        // Entrega la cadena explicativa con la ruta delimitada hacia la clase Exception.
        super("Error de validación en la ruta [" + codigoRuta + "]: " + motivo);
        // Preserva el código de la ruta en la variable interna.
        this.codigoRuta = codigoRuta;
    }

    // Método de acceso para obtener el código de la ruta involucrada.
    public String getCodigoRuta() {
        return codigoRuta;
    }
}