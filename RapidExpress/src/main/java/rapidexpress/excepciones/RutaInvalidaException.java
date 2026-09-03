package rapidexpress.excepciones;

/**
 * Excepción que se lanza cuando una ruta tiene datos inválidos 
 * o no cumple con las reglas de negocio.
 * 
 * @author User
 */
public class RutaInvalidaException extends Exception {
    
    private Integer rutaId;
    
    /**
     * Constructor específico para ruta inválida por ID
     * @param rutaId ID de la ruta inválida
     * @param razon Razón por la cual la ruta es inválida
     */
    public RutaInvalidaException(Integer rutaId, String razon) {
        super("La ruta con ID " + rutaId + " es invalida: " + razon);
        this.rutaId = rutaId;
    }
    
    /**
     * Constructor con mensaje personalizado
     * @param mensaje Descripción del error
     */
    public RutaInvalidaException(String mensaje) {
        super(mensaje);
    }
    
    /**
     * Constructor con mensaje y causa
     * @param mensaje Descripción del error
     * @param causa Excepción original
     */
    public RutaInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    /**
     * Constructor solo con causa
     * @param causa Excepción original
     */
    public RutaInvalidaException(Throwable causa) {
        super(causa);
    }
    
    /**
     * Obtiene el ID de la ruta inválida
     * @return ID de la ruta
     */
    public Integer getRutaId() {
        return rutaId;
    }
}