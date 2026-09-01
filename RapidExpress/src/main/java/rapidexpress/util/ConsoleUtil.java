/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.util;

// Importación de la clase Scanner para capturar las entradas de texto en la consola.
import java.util.Scanner;

/**
 * Propósito: Proporcionar métodos auxiliares para la interacción con el usuario 
 * en la interfaz de consola (CLI), lectura limpia de datos y formateo de texto.
 * 
 * @author User
 */
public class ConsoleUtil {

    // Instancia única de Scanner para evitar abrir y cerrar System.in repetidamente.
    private static final Scanner SCANNER = new Scanner(System.in);

    // Constructor privado para impedir la instanciación de esta clase de utilidad.
    private ConsoleUtil() {}

    // Pausa la ejecución de la consola hasta que el usuario presione la tecla Enter.
    public static void pausar() {
        // Muestra la indicación en la pantalla.
        System.out.println("\nPresione Enter para continuar...");
        // Consume la entrada de la tecla Enter enviada por el usuario.
        SCANNER.nextLine();
    }

    // Imprime un título destacado delimitado por líneas de igualación.
    public static void imprimirTitulo(String titulo) {
        // Imprime la barra superior divisoria.
        System.out.println("\n========================================");
        // Muestra el título centrado visualmente y convertido a mayúsculas.
        System.out.println("  " + titulo.toUpperCase());
        // Imprime la barra inferior divisoria.
        System.out.println("========================================");
    }

    // Solicita un texto al usuario mostrando un mensaje previo personalizado.
    public static String leerCadena(String mensaje) {
        // Muestra la instrucción en consola sin salto de línea.
        System.out.print(mensaje + ": ");
        // Lee y retorna la línea completa ingresada, eliminando espacios iniciales y finales.
        return SCANNER.nextLine().trim();
    }

    // Solicita y retorna un número entero, validando que la entrada no cause una excepción.
    public static int leerEntero(String mensaje) {
        // Variable para controlar la validez del dato ingresado.
        int numero = 0;
        // Bandera de control para mantener el bucle hasta recibir un entero válido.
        boolean valido = false;

        while (!valido) {
            try {
                // Solicita el valor al usuario usando la función auxiliar de lectura.
                String entrada = leerCadena(mensaje);
                // Intenta convertir la cadena ingresada a un tipo numérico primitivo entero.
                numero = Integer.parseInt(entrada);
                // Si la conversión es exitosa, cambia la bandera para salir del bucle.
                valido = true;
            } catch (NumberFormatException e) {
                // Notifica al usuario en caso de haber ingresado letras o caracteres especiales.
                System.out.println(">> Error: Debe ingresar un número entero válido.");
            }
        }
        // Retorna el entero procesado.
        return numero;
    }

    // Imprime un mensaje destacado en caso de error en las operaciones de menú.
    public static void mostrarError(String mensaje) {
        // Muestra el mensaje de error antecedido por un indicador visual.
        System.out.println("[ERROR] " + mensaje);
    }

    // Imprime un mensaje de confirmación de operación exitosa.
    public static void mostrarExito(String mensaje) {
        // Muestra el mensaje de éxito antecedido por un indicador visual positivo.
        System.out.println("[ÉXITO] " + mensaje);
    }
}