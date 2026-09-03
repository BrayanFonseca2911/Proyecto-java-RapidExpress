package rapidexpress.excepciones;

/**
 * Excepción que se lanza cuando un vehículo no está disponible 
 * para ser asignado a una ruta o conductor.
 * 
 * @author User
 */
public class VehiculoNoDisponibleException extends Exception {
    
    private String placa;
    
    /**
     * Constructor específico para vehículo no disponible por placa
     * @param placa Placa del vehículo no disponible
     */
    public VehiculoNoDisponibleException(String placa) {
        super("El vehículo con placa '" + placa + "' no está disponible. " +
              "Verifique que esté en estado DISPONIBLE.");
        this.placa = placa;
    }
    
    /**
     * Constructor con mensaje y causa
     * @param mensaje Descripción del error
     * @param causa Excepción original
     */
    public VehiculoNoDisponibleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    /**
     * Constructor solo con causa
     * @param causa Excepción original
     */
    public VehiculoNoDisponibleException(Throwable causa) {
        super(causa);
    }
    
    /**
     * Obtiene la placa del vehículo
     * @return Placa del vehículo
     */
    public String getPlaca() {
        return placa;
    }
}