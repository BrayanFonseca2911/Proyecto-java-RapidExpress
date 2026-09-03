/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.dominio;

import rapidexpress.enums.EstadoConductor;

/**
 *
 * @author User
 */
public class Conductor {
    private int id;
    private String numeroIdentificacion;
    private String nombre;
    private String tipoLicencia;
    private String contacto;
    private EstadoConductor estado;
    private Vehiculo vehiculoAsignado;
    
    public Conductor(){
        this.estado = EstadoConductor.ACTIVO;
    }
    //Primer constructor con parametros básicos para crear nuevos productores
    public Conductor(String numeroIdentificacion, String nombre, String tipoLicencia, String contacto) {
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombre = nombre;
        this.tipoLicencia = tipoLicencia;
        this.contacto = contacto;
        this.estado = EstadoConductor.ACTIVO;
    }
    //Segundo contructor con todos los atributos para RECUPERAR conductores existentes
    public Conductor(int id, String numeroIdentificacion, String nombre, String tipoLicencia, String contacto, EstadoConductor estado, Vehiculo vehiculoAsignado) {
        this.id = id;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombre = nombre;
        this.tipoLicencia = tipoLicencia;
        this.contacto = contacto;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoLicencia() {
        return tipoLicencia;
    }

    public void setTipoLicencia(String tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public EstadoConductor getEstado() {
        return estado;
    }

    public void setEstado(EstadoConductor estado) {
        this.estado = estado;
    }

    public Vehiculo getVehiculoAsignado() {
        return vehiculoAsignado;
    }

    public void setVehiculoAsignado(Vehiculo vehiculoAsignado) {
        this.vehiculoAsignado = vehiculoAsignado;
    }
    
    //Metodos de negocio
    public boolean disponible(){
        return this.estado == EstadoConductor.ACTIVO && this. vehiculoAsignado == null;
    }
    
    public boolean activo(){
        return this.estado == EstadoConductor.ACTIVO;
    }
    
    public void asignarVehiculo(Vehiculo vehiculo){
        this.vehiculoAsignado = vehiculo;
    }
    
    public void liberarVehiculo(){
        this.vehiculoAsignado = null;
    }
    
    @Override
    public String toString() {
        return "Conductor{" +
                "id=" + id +
                ", numeroIdentificacion='" + numeroIdentificacion + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipoLicencia='" + tipoLicencia + '\'' +
                ", contacto='" + contacto + '\'' +
                ", estado=" + estado +
                '}';
    }
    
}
