package rapidexpress.excepciones;

/**
 * Excepción que se lanza cuando se intenta exceder la capacidad máxima 
 * de carga de un vehículo.
 * 
 * @author User
 */
public class CapacidadExcedidaException extends Exception {
    
    private double capacidadMaxima;
    private double pesoIntentado;
    
    /**
     * Constructor con mensaje personalizado
     * @param mensaje Descripción del error
     */
    public CapacidadExcedidaException(String mensaje) {
        super(mensaje);
    }
    
    /**
     * Constructor específico para capacidad excedida
     * @param capacidadMaxima Capacidad máxima del vehículo en kg
     * @param pesoIntentado Peso total que se intentó cargar en kg
     */
    public CapacidadExcedidaException(double capacidadMaxima, double pesoIntentado) {
        super("La capacidad del vehículo ha sido excedida. " +
              "Máximo permitido: " + capacidadMaxima + " kg, " +
              "Peso intentado: " + pesoIntentado + " kg, " +
              "Exceso: " + (pesoIntentado - capacidadMaxima) + " kg");
        this.capacidadMaxima = capacidadMaxima;
        this.pesoIntentado = pesoIntentado;
    }
    
    /**
     * Constructor con mensaje y causa
     * @param mensaje Descripción del error
     * @param causa Excepción original que provocó este error
     */
    public CapacidadExcedidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    /**
     * Constructor solo con causa
     * @param causa Excepción original
     */
    public CapacidadExcedidaException(Throwable causa) {
        super(causa);
    }
    
    // Getters
    public double getCapacidadMaxima() {
        return capacidadMaxima;
    }
    
    public double getPesoIntentado() {
        return pesoIntentado;
    }
    
    /**
     * Calcula el exceso de peso
     * @return Cantidad de kg que exceden la capacidad
     */
    public double getExceso() {
        return pesoIntentado - capacidadMaxima;
    }
}