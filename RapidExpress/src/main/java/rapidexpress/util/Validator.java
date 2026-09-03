package rapidexpress.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Clase utilitaria para validaciones genéricas.
 * Contiene métodos estáticos para validar diferentes tipos de datos.
 * 
 * @author User
 */
public class Validator {
    
    /** Patrón para validar emails */
    private static final Pattern PATRON_EMAIL = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    /** Patrón para validar teléfonos (10-15 dígitos) */
    private static final Pattern PATRON_TELEFONO = Pattern.compile(
        "^[0-9]{10,15}$"
    );
    
    /** Patrón para validar placas (formato ABC-123 o ABC123) */
    private static final Pattern PATRON_PLACA = Pattern.compile(
        "^[A-Z]{3}-?[0-9]{3}$"
    );
    
    /** Patrón para validar números de identificación (8-15 dígitos) */
    private static final Pattern PATRON_IDENTIFICACION = Pattern.compile(
        "^[0-9]{8,15}$"
    );
    
    /**
     * Valida si un string no es nulo ni vacío
     * @param texto Texto a validar
     * @return true si el texto es válido
     */
    public static boolean noVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }
    
    /**
     * Valida si un string es nulo o vacío
     * @param texto Texto a validar
     * @return true si el texto está vacío
     */
    public static boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
    
    /**
     * Valida un formato de email
     * @param email Email a validar
     * @return true si el email es válido
     */
    public static boolean esEmailValido(String email) {
        if (esVacio(email)) {
            return false;
        }
        Matcher matcher = PATRON_EMAIL.matcher(email.trim());
        return matcher.matches();
    }
    
    /**
     * Valida un formato de teléfono
     * @param telefono Teléfono a validar
     * @return true si el teléfono es válido
     */
    public static boolean esTelefonoValido(String telefono) {
        if (esVacio(telefono)) {
            return false;
        }
        Matcher matcher = PATRON_TELEFONO.matcher(telefono.trim());
        return matcher.matches();
    }
    
    /**
     * Valida un formato de placa de vehículo
     * @param placa Placa a validar
     * @return true si la placa es válida
     */
    public static boolean esPlacaValida(String placa) {
        if (esVacio(placa)) {
            return false;
        }
        Matcher matcher = PATRON_PLACA.matcher(placa.trim().toUpperCase());
        return matcher.matches();
    }
    
    /**
     * Valida un número de identificación
     * @param identificacion Número a validar
     * @return true si es válido
     */
    public static boolean esIdentificacionValida(String identificacion) {
        if (esVacio(identificacion)) {
            return false;
        }
        Matcher matcher = PATRON_IDENTIFICACION.matcher(identificacion.trim());
        return matcher.matches();
    }
    
    /**
     * Valida que un número sea positivo
     * @param numero Número a validar
     * @return true si es positivo
     */
    public static boolean esPositivo(double numero) {
        return numero > 0;
    }
    
    /**
     * Valida que un número sea positivo o cero
     * @param numero Número a validar
     * @return true si es positivo o cero
     */
    public static boolean esPositivoOCero(double numero) {
        return numero >= 0;
    }
    
    /**
     * Valida que un número esté en un rango
     * @param numero Número a validar
     * @param minimo Valor mínimo
     * @param maximo Valor máximo
     * @return true si está en el rango
     */
    public static boolean estaEnRango(double numero, double minimo, double maximo) {
        return numero >= minimo && numero <= maximo;
    }
    
    /**
     * Valida que un año sea razonable
     * @param anio Año a validar
     * @return true si el año es válido
     */
    public static boolean esAnioValido(int anio) {
        return anio >= 1900 && anio <= DateUtil.anioActual() + 1;
    }
    
    /**
     * Valida que un peso sea razonable (entre 0.1 y 10000 kg)
     * @param peso Peso a validar
     * @return true si el peso es válido
     */
    public static boolean esPesoValido(double peso) {
        return peso >= 0.1 && peso <= 10000;
    }
    
    /**
     * Valida que una capacidad de carga sea razonable
     * @param capacidad Capacidad a validar
     * @return true si es válida
     */
    public static boolean esCapacidadValida(double capacidad) {
        return capacidad >= 100 && capacidad <= 50000;
    }
    
    /**
     * Valida un tipo de licencia de conducir
     * @param licencia Tipo de licencia
     * @return true si es válida
     */
    public static boolean esLicenciaValida(String licencia) {
        if (esVacio(licencia)) {
            return false;
        }
        String licenciaMayus = licencia.trim().toUpperCase();
        return licenciaMayus.matches("^[A-E][0-9]?$");
    }
    
    /**
     * Trunca un string a una longitud máxima
     * @param texto Texto a truncar
     * @param longitudMaxima Longitud máxima
     * @return Texto truncado con "..." si excede
     */
    public static String truncar(String texto, int longitudMaxima) {
        if (texto == null) {
            return null;
        }
        if (texto.length() <= longitudMaxima) {
            return texto;
        }
        return texto.substring(0, longitudMaxima - 3) + "...";
    }
}