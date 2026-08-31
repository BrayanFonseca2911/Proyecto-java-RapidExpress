/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.enums;

/**
 *
 * @author User
 */
public enum EstadoPaquete {
    EN_BODEGA("En Bodega"),
    ASIGNADO_A_RUTA("Asignado a Ruta"),
    EN_TRANSITO("En Tránsito"),
    ENTREGADO("Entregado"),
    DEVUELTO("Devuelto");
    
    private final String descripcion;
    
    EstadoPaquete(String descripcion) {
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
