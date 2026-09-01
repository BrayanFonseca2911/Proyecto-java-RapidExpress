/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.util;

/**
 * Propósito: Proveer validaciones de expresiones regulares y lógica de negocio
 * para verificar la integridad de los datos de entrada en el sistema.
 * 
 * @author User
 */

public class Validator {

    // Patrón Regex para placas vehiculares (ejemplo: ABC123 o ABC-123).
    private static final String REGEX_PLACA = "^[A-Z]{3}-?[0-9]{3}$";

    // Patrón Regex para documentos de identidad / cédulas (de 6 a 10 dígitos numéricos).
    private static final String REGEX_CEDULA = "^[0-9]{6,10}$";

    // Patrón Regex para números de seguimiento de paquetes (ejemplo: PKG-1001 o PKG1001).
    private static final String REGEX_GUIA_SEGUIMIENTO = "^[A-Z]{3}-?[0-9]{4,8}$";

    // Patrón Regex básico para validar formato de correo electrónico.
    private static final String REGEX_EMAIL = "^[A-Za-z0-9+_.-]+@(.+)$";

    // Constructor privado para impedir la instanciación de esta clase de utilidad.
    private Validator() {}

    // Evalúa si una cadena cumple con el formato de placa vehicular registrado en el sistema.
    public static boolean esPlacaValida(String placa) {
        // Retorna verdadero si no es nula y coincide con la regla de tres letras y tres números.
        return placa != null && placa.trim().toUpperCase().matches(REGEX_PLACA);
    }

    // Comprueba si un documento de identidad contiene únicamente entre 6 y 10 dígitos numéricos.
    public static boolean esCedulaValida(String cedula) {
        // Retorna verdadero si no es nulo y cumple con la longitud y formato numérico.
        return cedula != null && cedula.trim().matches(REGEX_CEDULA);
    }

    // Verifica si un número de seguimiento cumple con la nomenclatura estándar del paquete.
    public static boolean esGuiaValida(String guia) {
        // Valida que el código de seguimiento coincida con el patrón de prefijo y números.
        return guia != null && guia.trim().toUpperCase().matches(REGEX_GUIA_SEGUIMIENTO);
    }

    // Evalúa si una dirección de correo electrónico posee un formato válido.
    public static boolean esEmailValido(String email) {
        // Retorna verdadero si no es nulo y estructura adecuadamente el usuario y dominio.
        return email != null && email.trim().matches(REGEX_EMAIL);
    }

    // Comprueba si una cadena ingresada representa un número decimal estrictamente mayor a cero.
    public static boolean esNumeroPositivo(String texto) {
        // Si el texto es nulo o está vacío, descarta la validación inmediatamente.
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        try {
            // Intenta convertir la cadena ingresada a un tipo numérico de punto flotante.
            double valor = Double.parseDouble(texto.trim());
            // Retorna verdadero únicamente si el valor superpasa el cero.
            return valor > 0;
        } catch (NumberFormatException e) {
            // Retorna falso en caso de que la cadena contenga letras o caracteres no válidos.
            return false;
        }
    }
}