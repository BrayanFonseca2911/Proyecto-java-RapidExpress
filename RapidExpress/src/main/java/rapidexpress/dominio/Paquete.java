/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.dominio;

import java.time.LocalDateTime;
import rapidexpress.enums.EstadoPaquete;
import java.util.UUID;

/**
 *
 * @author User
 */
public class Paquete {
    private int id;
    private String trackingId;
    private String descripcion;
    private double peso;
    private String dimensiones;
    private String origen;
    private String destino;
    private EstadoPaquete estado;
    private Remitente remitente;
    private Destinatario destinatario;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaEntregada;
    
    public Paquete(){
        this.estado =EstadoPaquete.EN_BODEGA;
        this.fechaRegistro = LocalDateTime.now();
    }
    //Constructor con parametros basicos
    public Paquete(String descripcion, double peso, String dimensiones, String origen, String destino, Remitente remitente, Destinatario destinatario) {
        this.descripcion = descripcion;
        this.peso = peso;
        this.dimensiones = dimensiones;
        this.origen = origen;
        this.destino = destino;
        this.remitente = remitente;
        this.destinatario = destinatario;
    }
    //Constructor completo
    public Paquete(int id, String trackingId, String descripcion, double peso, String dimensiones, String origen, String destino, EstadoPaquete estado, Remitente remitente, Destinatario destinatario, LocalDateTime fechaRegistro, LocalDateTime fechaEntregada) {
        this.id = id;
        this.trackingId = trackingId;
        this.descripcion = descripcion;
        this.peso = peso;
        this.dimensiones = dimensiones;
        this.origen = origen;
        this.destino = destino;
        this.estado = estado;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.fechaRegistro = fechaRegistro;
        this.fechaEntregada = fechaEntregada;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getDimensiones() {
        return dimensiones;
    }

    public void setDimensiones(String dimensiones) {
        this.dimensiones = dimensiones;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public EstadoPaquete getEstado() {
        return estado;
    }

    public void setEstado(EstadoPaquete estado) {
        this.estado = estado;
    }

    public Remitente getRemitente() {
        return remitente;
    }

    public void setRemitente(Remitente remitente) {
        this.remitente = remitente;
    }

    public Destinatario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Destinatario destinatario) {
        this.destinatario = destinatario;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getFechaEntregada() {
        return fechaEntregada;
    }

    public void setFechaEntregada(LocalDateTime fechaEntregada) {
        this.fechaEntregada = fechaEntregada;
    }
    
    //Metodos de negocio
    public String generarTrackingId(){
        return "RPX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    public void entregar(){
        this.estado = EstadoPaquete.ENTREGADO;
        this.fechaEntregada = LocalDateTime.now();
    }
    
    public void devolver(){
        this.estado = EstadoPaquete.DEVUELTO;
    }
    
    public boolean enBodega(){
        return this.estado == EstadoPaquete.EN_BODEGA;
    }
    
    public boolean enTransito(){
        return this.estado == EstadoPaquete.EN_TRANSITO;
    }
    
    public boolean entregado(){
        return this.estado == EstadoPaquete.ENTREGADO;
    }
    
    @Override
    public String toString() {
        return "Paquete{" +
                "id=" + id +
                ", trackingId='" + trackingId + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", peso=" + peso +
                ", estado=" + estado +
                ", destino='" + destino + '\'' +
                '}';
    }
}
