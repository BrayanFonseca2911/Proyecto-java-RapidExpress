package rapidexpress.util;

import java.util.Scanner;

/**
 * Clase utilitaria para operaciones con la consola.
 * Proporciona métodos para limpiar pantalla, pausar, y leer datos validados.
 * 
 * @author User
 */
public class ConsoleUtil {
    
    /** Scanner compartido para leer entrada del usuario */
    private static final Scanner scanner = new Scanner(System.in);
    
    // Códigos ANSI para colores en consola
    public static final String RESET = "\u001B[0m";
    public static final String ROJO = "\u001B[31m";
    public static final String VERDE = "\u001B[32m";
    public static final String AMARILLO = "\u001B[33m";
    public static final String AZUL = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String BLANCO = "\u001B[37m";
    
    /**
     * Limpia la pantalla de la consola
     * Funciona en Windows, Linux y Mac
     */
    public static void limpiarPantalla() {
        try {
            // Para Windows
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Para Linux/Mac
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Si falla, imprimir líneas en blanco
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    /**
     * Pausa la ejecución hasta que el usuario presione Enter
     */
    public static void pausar() {
        System.out.print("\nPresione ENTER para continuar...");
        scanner.nextLine();
    }
    
    /**
     * Imprime un título con formato decorativo
     * @param titulo Texto del título
     */
    public static void imprimirTitulo(String titulo) {
        int longitud = titulo.length() + 4;
        String linea = "=".repeat(longitud);

        System.out.println("\n+" + linea + "+");
        System.out.println("|   " + titulo + "   |");
        System.out.println("+" + linea + "+");
    }
    
    /**
     * Imprime un separador horizontal
     */
    public static void imprimirSeparador() {
        System.out.println("========================================");
    }
    
    /**
     * Imprime un mensaje de éxito en color verde
     * @param mensaje Mensaje a mostrar
     */
    public static void imprimirExito(String mensaje) {
        System.out.println(VERDE + " " + mensaje + RESET);
    }
    
    /**
     * Imprime un mensaje de error en color rojo
     * @param mensaje Mensaje a mostrar
     */
    public static void imprimirError(String mensaje) {
        System.out.println(ROJO + " " + mensaje + RESET);
    }
    
    /**
     * Imprime un mensaje de advertencia en color amarillo
     * @param mensaje Mensaje a mostrar
     */
    public static void imprimirAdvertencia(String mensaje) {
        System.out.println(AMARILLO + "  " + mensaje + RESET);
    }
    
    /**
     * Imprime un mensaje informativo en color azul
     * @param mensaje Mensaje a mostrar
     */
    public static void imprimirInfo(String mensaje) {
        System.out.println(AZUL + "  " + mensaje + RESET);
    }
    
    /**
     * Lee un número entero válido desde la consola
     * @param mensaje Mensaje a mostrar al usuario
     * @return Número entero ingresado
     */
    public static int leerEntero(String mensaje) {
        int valor;
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            
            try {
                valor = Integer.parseInt(entrada);
                return valor;
            } catch (NumberFormatException e) {
                imprimirError("Por favor, ingrese un numero entero valido");
            }
        }
    }
    
    /**
     * Lee un número entero dentro de un rango
     * @param mensaje Mensaje a mostrar
     * @param minimo Valor mínimo permitido
     * @param maximo Valor máximo permitido
     * @return Número entero dentro del rango
     */
    public static int leerEntero(String mensaje, int minimo, int maximo) {
        int valor;
        while (true) {
            valor = leerEntero(mensaje);
            if (valor >= minimo && valor <= maximo) {
                return valor;
            }
            imprimirError("El valor debe estar entre " + minimo + " y " + maximo);
        }
    }
    
    /**
     * Lee un número decimal válido desde la consola
     * @param mensaje Mensaje a mostrar al usuario
     * @return Número decimal ingresado
     */
    public static double leerDecimal(String mensaje) {
        double valor;
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            
            try {
                valor = Double.parseDouble(entrada);
                return valor;
            } catch (NumberFormatException e) {
                imprimirError("Por favor, ingrese un numero decimal valido");
            }
        }
    }
    
    /**
     * Lee un número decimal positivo
     * @param mensaje Mensaje a mostrar
     * @return Número decimal positivo
     */
    public static double leerDecimalPositivo(String mensaje) {
        double valor;
        while (true) {
            valor = leerDecimal(mensaje);
            if (valor > 0) {
                return valor;
            }
            imprimirError("El valor debe ser mayor a cero");
        }
    }
    
    /**
     * Lee una cadena de texto no vacía
     * @param mensaje Mensaje a mostrar al usuario
     * @return Texto ingresado (sin espacios al inicio/final)
     */
    public static String leerTexto(String mensaje) {
        String texto;
        while (true) {
            System.out.print(mensaje);
            texto = scanner.nextLine().trim();
            
            if (!texto.isEmpty()) {
                return texto;
            }
            imprimirError("El campo no puede estar vacio");
        }
    }
    
    /**
     * Lee una opción del menú (S/N)
     * @param mensaje Mensaje a mostrar
     * @return true si el usuario respondió S, false si respondió N
     */
    public static boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (S/N): ");
            String respuesta = scanner.nextLine().trim().toUpperCase();
            
            if (respuesta.equals("S")) {
                return true;
            } else if (respuesta.equals("N")) {
                return false;
            }
            imprimirError("Por favor, ingrese S o N");
        }
    }
    
    /**
     * Obtiene el Scanner compartido
     * @return Scanner para leer entrada del usuario
     */
    public static Scanner getScanner() {
        return scanner;
    }
}