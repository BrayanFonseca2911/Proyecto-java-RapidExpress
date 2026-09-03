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
    // Obtiene la descripcion legible del estado
    public String getDescripcion() {
        return descripcion;
    }
    
    //Convierte uin String a EstadoPaquete
    
   public static EstadoPaquete fromString(String texto){
       if(texto == null || texto.trim().isEmpty()){
           throw new IllegalArgumentException("El texto no puede estar vacio");
       }
       
       String textoLimpio = texto.trim().toUpperCase().replace(" ", "_");
       
       for(EstadoPaquete estado : values()){
           if(estado.name().equals(textoLimpio)||estado.descripcion.equalsIgnoreCase(texto.trim())){
               return estado;
           }
       }
       throw new IllegalArgumentException("Estado de paquete no valido: "+texto);
    }
    
    //verifica si el paquete puede cambiat al estado destino
    public boolean puedeTransicionarA(EstadoPaquete estadoDestino) {
        switch (this){
            case EN_BODEGA:
                return estadoDestino == ASIGNADO_A_RUTA || estadoDestino == DEVUELTO;
            case ASIGNADO_A_RUTA:
                return estadoDestino == EN_TRANSITO || estadoDestino == EN_BODEGA;
            case EN_TRANSITO:
                return estadoDestino == ENTREGADO || estadoDestino == DEVUELTO;
            case ENTREGADO:
                return false; //Estado final y no se puede cambiar el paquete
            case DEVUELTO:
                return false; //Estado final y no se puede cambiar el paquete
            default:
                return false;
        }
    }
    
    //Verifica si el paquete está en estado final(NO SE PUEDE CAMBIAR MAS)
    public boolean esEstadoFinal(){
        return this == ENTREGADO || this == DEVUELTO;
    }
    
    //Verifica si el paquete está en movimiento
    public boolean estaEnMovimiento(){
        return this == EN_TRANSITO;
    }
    
    //Verifica si el paquete está disponible para asignar una ruta
    public boolean disponibleParaAsignar(){
        return this == EN_BODEGA;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}
