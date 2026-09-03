package rapidexpress.excepciones;

/**
 * Excepción que se lanza cuando un conductor no está disponible 
 * para ser asignado a un vehículo o ruta.
 * 
 * @author User
 */
public class ConductorNoDisponibleException extends Exception {
    
    private String numeroIdentificacion;
    
    /**
     * Constructor específico para conductor no disponible por identificación
     * @param numeroIdentificacion Número de identificación del conductor
     */
    public ConductorNoDisponibleException(String numeroIdentificacion) {
        super("El conductor con identificación '" + numeroIdentificacion + 
              "' no está disponible. Verifique que esté ACTIVO y sin vehículo asignado.");
        this.numeroIdentificacion = numeroIdentificacion;
    }
    
    /**
     * Constructor con mensaje y causa
     * @param mensaje Descripción del error
     * @param causa Excepción original
     */
    public ConductorNoDisponibleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    /**
     * Constructor solo con causa
     * @param causa Excepción original
     */
    public ConductorNoDisponibleException(Throwable causa) {
        super(causa);
    }
    
    /**
     * Obtiene el número de identificación del conductor
     * @return Número de identificación
     */
    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }
}