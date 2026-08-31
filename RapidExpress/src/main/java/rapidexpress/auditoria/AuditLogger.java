/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.auditoria;

/**
 *
 * @author User
 */
public class AuditLogger {
    /*Propósito: Escribe registros de auditoría en archivo de texto.
Atributos:
private static final String ARCHIVO_AUDITORIA = "auditoria.log"
private static AuditLogger instance
Métodos:
public static AuditLogger getInstance(): Singleton - retorna instancia única
private AuditLogger(): Constructor privado
public void log(Auditoria auditoria):
Escribe registro en archivo con formato: [FECHA] USUARIO - ACCION en TABLA: DETALLE
Usa FileWriter con append = true
Maneja IOException
public void log(String usuario, String accion, String detalle):
Sobrecarga que crea Auditoria y llama a log()
public void logError(String usuario, String error, Throwable e):
Escribe error con stack trace
private String formatearFecha(Date fecha): String - formato YYYY-MM-DD HH:mm:ss*/
}
