/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.excepciones;

/**
 *
 * @author User
 */

// Excepción de regla de negocio que se dispara cuando la carga total
// de paquetes sobrepasa el límite de peso permitido por el vehículo.
public class CapacidadExcedidaException extends Exception {

    // Almacena el límite máximo de kilogramos que soporta el vehículo.
    private final double capacidadMax;
    
    // Almacena el peso total en kilogramos que se intentaba asignar.
    private final double pesoIntentado;

    // Constructor que recibe el límite del vehículo y el peso total que generó el exceso.
    public CapacidadExcedidaException(double capacidadMax, double pesoIntentado) {
        // Invoca al constructor padre formateando un mensaje con los datos calculados.
        super(String.format("Capacidad excedida: El vehículo soporta %.2f kg pero se intentó cargar %.2f kg (Exceso: %.2f kg)", 
                capacidadMax, pesoIntentado, (pesoIntentado - capacidadMax)));
        // Asigna la capacidad máxima al atributo de clase.
        this.capacidadMax = capacidadMax;
        // Asigna el peso intentado al atributo de clase.
        this.pesoIntentado = pesoIntentado;
    }

    // Calcula y retorna la cantidad exacta de kilogramos que sobrepasan el límite.
    public double getExceso() {
        return pesoIntentado - capacidadMax;
    }

    // Método de acceso para obtener la capacidad máxima registrada.
    public double getCapacidadMax() {
        return capacidadMax;
    }

    // Método de acceso para obtener el peso total que se intentó registrar.
    public double getPesoIntentado() {
        return pesoIntentado;
    }
}