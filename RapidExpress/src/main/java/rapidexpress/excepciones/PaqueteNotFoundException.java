package rapidexpress.excepciones;

/**
 * Excepción que se lanza cuando no se encuentra un paquete 
 * con el tracking ID especificado.
 * 
 * @author User
 */
public class PaqueteNotFoundException extends Exception {
    
    private String trackingId;
    
    /**
     * Constructor específico para paquete no encontrado por tracking ID
     * @param trackingId ID de tracking del paquete no encontrado
     */
    public PaqueteNotFoundException(String trackingId) {
        super("No se encontró ningún paquete con el tracking ID: '" + trackingId + "'. " +
              "Verifique que el ID sea correcto.");
        this.trackingId = trackingId;
    }
    
    /**
     * Constructor con mensaje y causa
     * @param mensaje Descripción del error
     * @param causa Excepción original
     */
    public PaqueteNotFoundException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    /**
     * Constructor solo con causa
     * @param causa Excepción original
     */
    public PaqueteNotFoundException(Throwable causa) {
        super(causa);
    }
    
    /**
     * Obtiene el tracking ID del paquete no encontrado
     * @return Tracking ID
     */
    public String getTrackingId() {
        return trackingId;
    }
}