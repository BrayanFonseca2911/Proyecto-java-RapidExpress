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
    INACTIVO("Inactivo"),
    DISPONIBLE("Disponible"),
    EN_RUTA("En Ruta"),
    EN_MANTENIMIENTO("En Mantenimiento");
    
    private final String descripcion;
    
    EstadoVehiculo(String descripcion) {
        this.descripcion = descripcion;
    }
    // Obtiene la descripcion legible del estado
    public String getDescripcion() {
        return descripcion;
    }
    
    //Convierte in string a EstadoVehiculo
    
    public static EstadoVehiculo fromString(String texto){
        if(texto == null || texto.trim().isEmpty()){
            throw new IllegalArgumentException("El texto no puede esatr vacío");
        }
        
        String textoLimpio = texto.trim().toUpperCase().replace(" ", "_");
        
        for(EstadoVehiculo estado : values()){
            if(estado.name().equals(textoLimpio)|| estado.descripcion.equalsIgnoreCase(texto.trim())){
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado ed vehiculo no valido: "+texto);
    }
    
    //Verifica si el estado permite asignar un conductor
    public boolean permiteAsignacion(){
        return this == DISPONIBLE;
    }
    
    //Verifica si el vehiculo está en movimiento
    public boolean estaEnMovimiento(){
        return this == EN_RUTA;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}
