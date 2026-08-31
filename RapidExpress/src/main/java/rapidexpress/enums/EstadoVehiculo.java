/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.enums;

/**
 *
 * @author User
 */
public enum EstadoVehiculo {
    DISPONIBLE("Disponible"),
    EN_RUTA("En Ruta"),
    EN_MANTENIMIENTO("En Mantenimiento");
    
    private final String descripcion;
    
    EstadoVehiculo(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}
