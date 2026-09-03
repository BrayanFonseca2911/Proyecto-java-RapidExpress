/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.auditoria;

import rapidexpress.util.DateUtil;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
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
    
    //Nombre del archivo de auditoría
    private static final String ARCHIVO_AUDITORIA = "auditoria.log";
    
    //Instancia unica del logger
    private static AuditLogger instance;
    
    //====CONSTRUCTOR====
    // constructor privado (Evita que se creen multiples instancias desde afuera
    private AuditLogger(){
    }
    
    //=====METODO PRINCIPAL====
    //Obtiene la instancia unidca del AuditLogger
    
    public static AuditLogger getInstance(){
        if (instance == null){
            instance = new AuditLogger();
        }
        return instance;
    }
    
    //=======METODO LOG=======
    //Escribe un registro de auditoria en el archivo
    
    public void log(Auditoria auditoria){
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_AUDITORIA, true))){
            String linea = formatearLinea(auditoria);
            writer.println(linea);
            System.out.println("Auditoria Registrada: "+auditoria.getAccion());
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo de audioria: "+e.getMessage());
            e.printStackTrace();
        }
    }
    
    //Metodo para escribir un registro de auditoría con parametros individuales
    
    public void log(String usuario, String accion, String detalle) {
        Auditoria auditoria = new Auditoria();
        auditoria.setFecha(LocalDateTime.now());
        auditoria.setUsuario(usuario);
        auditoria.setAccion(accion);
        auditoria.setDetalle(detalle);
        auditoria.setTablaAfectada("GENERAL");
        auditoria.setRegistroId("0");
        auditoria.setIp("localhost");
        
        log(auditoria);
    }
    //Metodo para escribir un registro de error en el archivo de auditoria
    public void logError(String usuario, String error, Throwable e){
        Auditoria auditoria = new Auditoria();
        auditoria.setFecha(LocalDateTime.now());
        auditoria.setUsuario(usuario);
        auditoria.setAccion("ERROR");
        auditoria.setDetalle(error + " - " + e.getMessage());
        auditoria.setTablaAfectada("SYSTEM");
        auditoria.setRegistroId("0");
        auditoria.setIp("localhost");
        
        log(auditoria);
        
        //Tambien imprimir el stack trace
        System.out.println("Error registrado en la auditoria: "+error);
        e.printStackTrace();
    }
    
    //Mensaje informativo en el archivo
    public void logInfo(String mensaje){
        log("system", "INFO", mensaje);
    }
    
    //=====METODOS AUXILIARES=====
    /*
    Formatea la linea de l auditoria para escribir en el archivo
    Formato: [FECHA] USUARIO | ACCION | TABLA | ID | DETALLE | IP
    */
    
    private String formatearLinea(Auditoria auditoria) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("[").append(auditoria.getFechaFormateada()).append("] ");
        sb.append("USUARIO: ").append(auditoria.getUsuario()).append(" | ");
        sb.append("ACCION: ").append(auditoria.getAccion()).append(" | ");
        sb.append("TABLA: ").append(auditoria.getTablaAfectada()).append(" | ");
        sb.append("ID: ").append(auditoria.getRegistroId()).append(" | ");
        sb.append("DETALLE: ").append(auditoria.getDetalle()).append(" | ");
        sb.append("IP: ").append(auditoria.getIp());
        
        return sb.toString();
    }
    
    /**
     * Limpia el archivo de auditoría (lo deja vacío)
     * Útil para testing o reiniciar logs
     */
    public void limpiarArchivo() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_AUDITORIA, false))) {
            writer.write("");
            System.out.println("✓ Archivo de auditoria limpiado");
        } catch (IOException e) {
            System.err.println(" Error al limpiar archivo de auditoria: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene la ruta completa del archivo de auditoría
     * 
     * @return Ruta del archivo
     */
    public String getArchivoPath() {
        return System.getProperty("user.dir") + "/" + ARCHIVO_AUDITORIA;
    }
}
