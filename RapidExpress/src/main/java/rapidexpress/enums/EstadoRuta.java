/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.enums;

/**
 *
 * @author User
 */
public enum EstadoRuta {
    PLANIFICADA("Planificada"),
    EN_CURSO("En Curso"),
    COMPLETADA("Completada"),
    CANCELADA("Cancelada");
    
    private final String descripcion;
    
    EstadoRuta(String descripcion) {
        this.descripcion = descripcion;
    }
    
    //Obtiene la descripción legible del estado
    public String getDescripcion() {
        return descripcion;
    }
    
    //Convierte un String a EstadoRuta
    public static EstadoRuta fronString(String texto){
        if(texto == null || texto.trim().isEmpty()){
            throw new IllegalArgumentException("El texto no puede estar vacio ");
        }
        
        String textoLimpio = texto.trim().toUpperCase().replace(" ", "_");
        
        for(EstadoRuta estado : values()){
            if(estado.name().equals(textoLimpio) || estado.descripcion.equalsIgnoreCase(texto.trim())){
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado de ruta no valido: "+texto);
    }
    
    //Verifica si la ruta puede cambiar al estado destino
    public boolean puedeTrnsicionarA(EstadoRuta estadoDestino){
        switch (this) {
            case PLANIFICADA:
                return estadoDestino == EN_CURSO || estadoDestino == CANCELADA;
            case EN_CURSO:
                return estadoDestino == COMPLETADA || estadoDestino == CANCELADA;
            case COMPLETADA:
                return false;//Estado final
            case CANCELADA:
                return false;//Estado final
            default:
                return false;
        }
    }
    
    //Verifica si la ruta está en estado final
    public boolean esEstadoFinal(){
        return this == COMPLETADA || this == CANCELADA;
    }
    
    //Verifica si la ruta está activa
    public boolean estaActiva(){
        return this == EN_CURSO;
    }
    
    //Verifica si la ruta puede ser iniciada
    public boolean puedeIniciarse(){
        return this == PLANIFICADA;
    }
    @Override
    public String toString() {
        return descripcion;
    }
}
