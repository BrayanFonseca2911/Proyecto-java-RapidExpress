/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.util;

/**
 *
 * @author User
 */
public class ConsoleUtil {
   /*Propósito: Utilidades para la consola.
Métodos estáticos:
public static void clearScreen():
System.out.print("\033[H\033[2J")
System.out.flush()
public static void pause():
System.out.println("\nPresione Enter para continuar...")
new Scanner(System.in).nextLine()
public static void printHeader(String title):
Imprime línea con ╔═══ title ═══╗
public static void printSuccess(String msg):
System.out.println("\u001B[32m✅ " + msg + "\u001B[0m") (verde)
public static void printError(String msg):
System.out.println("\u001B[31m❌ " + msg + "\u001B[0m") (rojo)
public static void printWarning(String msg):
System.out.println("\u001B[33m⚠️ " + msg + "\u001B[0m") (amarillo)
public static int readInt(Scanner sc):
while (!sc.hasNextInt()) { printError("Ingrese un número válido"); sc.next() }
Retorna sc.nextInt()
public static double readDouble(Scanner sc):
while (!sc.hasNextDouble()) { printError("Ingrese un número decimal válido"); sc.next() }
Retorna sc.nextDouble()
public static String readNonEmptyString(Scanner sc):
String str
do { str = sc.nextLine().trim() } while (str.isEmpty())
Retorna str*/ 
}
