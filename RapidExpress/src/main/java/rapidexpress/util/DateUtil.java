/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.util;

// Importación para manipular fechas sin hora (Año-Mes-Día).
import java.time.LocalDate;
// Importación para manipular fechas con hora (Año-Mes-Día Hora:Minuto:Segundo).
import java.time.LocalDateTime;
// Importación para aplicar formatos personalizados a objetos de fecha.
import java.time.format.DateTimeFormatter;
// Importación para capturar errores cuando el texto no coincide con el formato esperado.
import java.time.format.DateTimeParseException;

/**
 * Propósito: Ofrecer métodos utilitarios estáticos para la conversión y 
 * formateo de fechas en la interfaz de usuario y las capas de persistencia.
 * 
 * @author User
 */
public class DateUtil {

    // Patrón de formato estándar para fechas cortas (ej. 25/12/2026).
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Patrón de formato para fechas completas con hora (ej. 25/12/2026 14:30:00).
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Constructor privado para impedir la creación de instancias de esta clase de utilidad.
    private DateUtil() {}

    // Transforma un objeto LocalDate a su representación en texto formateado (dd/MM/yyyy).
    public static String formatearFecha(LocalDate fecha) {
        // Retorna una cadena vacía si el objeto de fecha es nulo.
        if (fecha == null) return "";
        // Aplica el formato dd/MM/yyyy sobre la fecha recibida.
        return fecha.format(FORMATO_FECHA);
    }

    // Transforma un objeto LocalDateTime a su representación en texto formateado con hora.
    public static String formatearFechaHora(LocalDateTime fechaHora) {
        // Retorna una cadena vacía si el objeto de fecha y hora es nulo.
        if (fechaHora == null) return "";
        // Aplica el formato dd/MM/yyyy HH:mm:ss sobre el objeto recibido.
        return fechaHora.format(FORMATO_FECHA_HORA);
    }

    // Convierte un texto ingresado por el usuario (dd/MM/yyyy) a un objeto LocalDate.
    public static LocalDate parsearFecha(String textoFecha) {
        // Verifica si la cadena es nula o está vacía para evitar errores de análisis.
        if (textoFecha == null || textoFecha.trim().isEmpty()) {
            return null;
        }
        try {
            // Convierte el texto al objeto LocalDate según el patrón configurado.
            return LocalDate.parse(textoFecha.trim(), FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            // Retorna nulo en caso de que el formato ingresado sea inválido.
            return null;
        }
    }

    // Valida si un texto ingresado cumple exactamente con el formato dd/MM/yyyy.
    public static boolean esFechaValida(String textoFecha) {
        // Retorna verdadero únicamente si el método parsearFecha no produce un valor nulo.
        return parsearFecha(textoFecha) != null;
    }
}