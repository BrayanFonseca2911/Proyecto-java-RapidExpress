package rapidexpress.util;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Clase utilitaria para operaciones con fechas y horas.
 * Usa la API moderna de Java (java.time) en lugar de java.util.Date.
 * 
 * @author User
 */
public class DateUtil {
    
    /** Formateador para fechas cortas (dd/MM/yyyy) */
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    /** Formateador para fechas y horas (dd/MM/yyyy HH:mm:ss) */
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    /** Formateador para fechas ISO (yyyy-MM-dd) */
    private static final DateTimeFormatter FORMATO_ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /** Formateador para horas (HH:mm:ss) */
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    /**
     * Obtiene la fecha y hora actual
     * @return Fecha y hora actual
     */
    public static LocalDateTime ahora() {
        return LocalDateTime.now();
    }
    
    /**
     * Obtiene solo la fecha actual (sin hora)
     * @return Fecha actual
     */
    public static LocalDate hoy() {
        return LocalDate.now();
    }
    
    /**
     * Formatea una fecha en formato corto (dd/MM/yyyy)
     * @param fecha Fecha a formatear
     * @return Fecha en formato string
     */
    public static String formatearFecha(LocalDate fecha) {
        if (fecha == null) {
            return "Sin fecha";
        }
        return fecha.format(FORMATO_FECHA);
    }
    
    /**
     * Formatea una fecha y hora en formato largo (dd/MM/yyyy HH:mm:ss)
     * @param fecha Fecha y hora a formatear
     * @return Fecha y hora en formato string
     */
    public static String formatearFechaHora(LocalDateTime fecha) {
        if (fecha == null) {
            return "Sin fecha";
        }
        return fecha.format(FORMATO_FECHA_HORA);
    }
    
    /**
     * Formatea una fecha en formato ISO (yyyy-MM-dd)
     * @param fecha Fecha a formatear
     * @return Fecha en formato ISO
     */
    public static String formatearISO(LocalDate fecha) {
        if (fecha == null) {
            return "Sin fecha";
        }
        return fecha.format(FORMATO_ISO);
    }
    
    /**
     * Convierte un string en formato "dd/MM/yyyy" a LocalDate
     * @param fechaString String con la fecha
     * @return Objeto LocalDate
     * @throws IllegalArgumentException Si el formato es inválido
     */
    public static LocalDate parsearFecha(String fechaString) {
        if (fechaString == null || fechaString.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha no puede estar vacía");
        }
        
        try {
            return LocalDate.parse(fechaString.trim(), FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use: dd/MM/yyyy");
        }
    }
    
    /**
     * Convierte un string en formato "dd/MM/yyyy HH:mm:ss" a LocalDateTime
     * @param fechaHoraString String con la fecha y hora
     * @return Objeto LocalDateTime
     * @throws IllegalArgumentException Si el formato es inválido
     */
    public static LocalDateTime parsearFechaHora(String fechaHoraString) {
        if (fechaHoraString == null || fechaHoraString.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha y hora no pueden estar vacías");
        }
        
        try {
            return LocalDateTime.parse(fechaHoraString.trim(), FORMATO_FECHA_HORA);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato inválido. Use: dd/MM/yyyy HH:mm:ss");
        }
    }
    
    /**
     * Suma días a una fecha
     * @param fecha Fecha base
     * @param dias Días a sumar (puede ser negativo para restar)
     * @return Nueva fecha con los días sumados
     */
    public static LocalDate sumarDias(LocalDate fecha, int dias) {
        if (fecha == null) {
            return null;
        }
        return fecha.plusDays(dias);
    }
    
    /**
     * Suma horas a una fecha y hora
     * @param fecha Fecha base
     * @param horas Horas a sumar
     * @return Nueva fecha y hora
     */
    public static LocalDateTime sumarHoras(LocalDateTime fecha, long horas) {
        if (fecha == null) {
            return null;
        }
        return fecha.plusHours(horas);
    }
    
    /**
     * Calcula la diferencia en días entre dos fechas
     * @param fechaInicio Fecha de inicio
     * @param fechaFin Fecha de fin
     * @return Número de días entre las fechas (puede ser negativo)
     */
    public static long diasEntre(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(fechaInicio, fechaFin);
    }
    
    /**
     * Verifica si una fecha está entre dos fechas (inclusive)
     * @param fecha Fecha a verificar
     * @param inicio Fecha de inicio del rango
     * @param fin Fecha de fin del rango
     * @return true si la fecha está en el rango
     */
    public static boolean estaEntre(LocalDate fecha, LocalDate inicio, LocalDate fin) {
        if (fecha == null || inicio == null || fin == null) {
            return false;
        }
        return !fecha.isBefore(inicio) && !fecha.isAfter(fin);
    }
    
    /**
     * Verifica si una fecha es anterior a otra
     * @param fecha1 Primera fecha
     * @param fecha2 Segunda fecha
     * @return true si fecha1 es anterior a fecha2
     */
    public static boolean esAnterior(LocalDate fecha1, LocalDate fecha2) {
        if (fecha1 == null || fecha2 == null) {
            return false;
        }
        return fecha1.isBefore(fecha2);
    }
    
    /**
     * Verifica si una fecha es posterior a otra
     * @param fecha1 Primera fecha
     * @param fecha2 Segunda fecha
     * @return true si fecha1 es posterior a fecha2
     */
    public static boolean esPosterior(LocalDate fecha1, LocalDate fecha2) {
        if (fecha1 == null || fecha2 == null) {
            return false;
        }
        return fecha1.isAfter(fecha2);
    }
    
    /**
     * Obtiene el año actual
     * @return Año actual
     */
    public static int anioActual() {
        return LocalDate.now().getYear();
    }
    
    /**
     * Valida si un año es razonable (entre 1900 y año actual + 1)
     * @param anio Año a validar
     * @return true si el año es válido
     */
    public static boolean esAnioValido(int anio) {
        return anio >= 1900 && anio <= anioActual() + 1;
    }
}