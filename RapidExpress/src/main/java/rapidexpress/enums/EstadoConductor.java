/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.enums;

/**
 *
 * @author User
 */
public enum EstadoConductor {
    EN_RUTA("En ruta"),
    ACTIVO("Activo"),
    DE_VACACIONES("De Vacaciones"),
    INACTIVO("Inactivo");
    
    private final String descripcion;
    
    EstadoConductor(String descripcion) {
        this.descripcion = descripcion;
    }
    // Obtiene la descripcion legible del estado
    public String getDescripcion() {
        return descripcion;
    }
    
    //Convierte un String a EstadoConductor
    public static EstadoConductor fromString(String texto){
        if(texto == null || texto.trim().isEmpty()){
            throw new IllegalArgumentException("El texto no puede estar vacío");
        }
        
        String textoLimpio = texto.trim().toUpperCase().replace(" ", "_");
        
        for (EstadoConductor estado : values()){
            if (estado.name().equals(textoLimpio)|| estado.descripcion.equalsIgnoreCase(texto.trim())){
                return estado;
            }    
        }
        throw new IllegalArgumentException("Estado de conductor no válido: "+texto);
    }
    
    //Verifica si el conductor puede ser asignado a un vehiculo
    public boolean puedeTrabajar(){
        return this == ACTIVO;
    }
    
    //verifica si el conductor esta disponible para nuevas asignaciones
    public boolean disponible(){
        return this == ACTIVO;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}
