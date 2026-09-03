package rapidexpress.excepciones;

/**
 * Excepción que se lanza cuando ocurre un error en las operaciones 
 * de base de datos (conexión, consultas, transacciones, etc.).
 * 
 * @author User
 */
public class DataBaseException extends Exception {
    
    private String operacion;
    private String tabla;
    
    /**
     * Constructor con mensaje personalizado
     * @param mensaje Descripción del error
     */
    public DataBaseException(String mensaje) {
        super(mensaje);
    }
    
    /**
     * Constructor específico para errores de base de datos
     * @param operacion Tipo de operación que falló (INSERT, UPDATE, DELETE, SELECT)
     * @param tabla Nombre de la tabla afectada
     * @param mensaje Descripción detallada del error
     */
    public DataBaseException(String operacion, String tabla, String mensaje) {
        super("Error en " + operacion + " sobre tabla '" + tabla + "': " + mensaje);
        this.operacion = operacion;
        this.tabla = tabla;
    }
    
    /**
     * Constructor con mensaje y causa
     * @param mensaje Descripción del error
     * @param causa Excepción original (generalmente SQLException)
     */
    public DataBaseException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
    /**
     * Constructor específico con operación, tabla y causa
     * @param operacion Tipo de operación que falló
     * @param tabla Nombre de la tabla afectada
     * @param causa Excepción original
     */
    public DataBaseException(String operacion, String tabla, Throwable causa) {
        super("Error en " + operacion + " sobre tabla '" + tabla + "': " + causa.getMessage(), causa);
        this.operacion = operacion;
        this.tabla = tabla;
    }
    
    /**
     * Constructor solo con causa
     * @param causa Excepción original
     */
    public DataBaseException(Throwable causa) {
        super(causa);
    }
    
    // Getters
    public String getOperacion() {
        return operacion;
    }
    
    public String getTabla() {
        return tabla;
    }
}