/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.auditoria;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author User
 */
public class Auditoria {
    /*Propósito: Representa un registro de auditoría del sistema.
Atributos:
id (int): Identificador único del registro
fecha (Date): Fecha y hora del evento
usuario (String): Usuario que realizó la acción
accion (String): Tipo de acción (CREATE, UPDATE, DELETE, LOGIN, LOGOUT)
tablaAfectada (String): Nombre de la tabla modificada
registroId (String): ID del registro afectado
detalle (String): Descripción detallada del cambio
ip (String): Dirección IP (opcional, puede ser "localhost")
Métodos:
Constructor completo con todos los atributos
Constructor vacío
Getters y setters de todos los atributos
toString(): Retorna string formateado con la información
getFechaFormateada(): String - fecha en formato legible*/
    
    private int id;//identificador de registro
    private LocalDateTime fecha;//fecha del evento
    private String usuario;// usuario que relizó la accion
    private String accion;//tipo de acción (CREATE, UPDATE, DELETE)//
    private String tablaAfectada;//nombre de la tabla modificada
    private String registroId;// id del registro modificado
    private String detalle;//Descripcion detallada del cambio
    private String ip;// Direccion IP (opcional, puede ser en el localhost)

    public Auditoria(){
        
    }

    public Auditoria(int id, LocalDateTime fecha, String usuario, String accion, String tablaAfectada, String registroId, String detalle, String ip) {
        this.id = id;
        this.fecha = fecha;
        this.usuario = usuario;
        this.accion = accion;
        this.tablaAfectada = tablaAfectada;
        this.registroId = registroId;
        this.detalle = detalle;
        this.ip = ip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }
    
    public String getTablaAfectada() {
        return tablaAfectada;
    }

    public void setTablaAfectada(String tablaAfectada) {
        this.tablaAfectada = tablaAfectada;
    }

    public String getRegistroId() {
        return registroId;
    }

    public void setRegistroId(String registroId) {
        this.registroId = registroId;
    }
    
    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
    // =============Metodos adicionales=========================
    
    //Metodo donde da la fecha formateada en formato legible
    
    public String getFechaFormateada(){
        if(fecha == null){
            return "Sin fecha";
        }
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss"));
    }
    
    //Representacion en String del registro de auditoria
    @Override
    public String toString(){
        return "Auditoria{"+
                "id ="+id+
                ", fecha ="+getFechaFormateada()+
                ", usuario ='"+usuario+'\''+
                ", accion ='"+accion+'\''+
                ", tablaAfectada='"+ tablaAfectada+'\''+
                ", registroId= '"+detalle+'\''+
                ", ip= '"+ip+'\''+
                '}';
    }
    
    //Metodo para formatear el registro de auditoria para mostrar en consola
    public String toFormattedString(){
        return String.format("[%s] Usuario: %s | Acción: %s | Tabla: %s | ID: %s | Detalle: %s | IP: %s",
            getFechaFormateada(),
            usuario,
            accion,
            tablaAfectada,
            registroId,
            detalle,
            ip);
    }
}
