/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.excepciones;

/**
 *
 * @author User
 */

// Excepción lanzada si el vehículo seleccionado no puede usarse
// (por estar en taller de mantenimiento, de baja o en medio de otra ruta).
public class VehiculoNoDisponibleException extends Exception {

    // Guarda la placa o matrícula única del vehículo.
    private final String placa;

    // Constructor que recibe la placa del vehículo y la razón de la indisponibilidad.
    public VehiculoNoDisponibleException(String placa, String motivo) {
        // Ensambla el mensaje conciso y lo transmite al constructor de la superclase.
        super("El vehículo con placa " + placa + " no está disponible: " + motivo);
        // Almacena la placa en el atributo de clase.
        this.placa = placa;
    }

    // Método de acceso para recuperar la placa del vehículo que generó la alerta.
    public String getPlaca() {
        return placa;
    }
}