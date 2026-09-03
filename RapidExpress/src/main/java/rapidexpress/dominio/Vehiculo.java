/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.dominio;

import rapidexpress.enums.EstadoVehiculo;

/**
 *
 * @author User
 */
public class Vehiculo {
    private int id;
    private String placa;
    private String marca;
    private String modelo;
    private int year;
    private double capacidadCarga;
    private EstadoVehiculo estado;
    private Conductor conductorAsignado;
    
    public Vehiculo(){
        this.estado = EstadoVehiculo.DISPONIBLE;
    }

    public Vehiculo(String placa, String marca, String modelo, int year, double capacidadCarga) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.year = year;
        this.capacidadCarga = capacidadCarga;
        this.estado = EstadoVehiculo.DISPONIBLE;
    }

    public Vehiculo(int id, String placa, String marca, String modelo, int year, 
                    double capacidadCarga, EstadoVehiculo estado) {
        this.id = id;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.year = year;
        this.capacidadCarga = capacidadCarga;
        this.estado = estado;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getCapacidadCarga() {
        return capacidadCarga;
    }

    public void setCapacidadCarga(double capacidadCarga) {
        this.capacidadCarga = capacidadCarga;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }

    public Conductor getConductorAsignado() {
        return conductorAsignado;
    }

    public void setConductorAsignado(Conductor conductorAsignado) {
        this.conductorAsignado = conductorAsignado;
    }
    
    //METODOS DE NEGOCIO
    
    public boolean disponible(){
        return this.estado == EstadoVehiculo.DISPONIBLE;
    }
    
    public boolean enRuta(){
        return this.estado == EstadoVehiculo.EN_RUTA;
    }
    
    public boolean enMantenimiento(){
        return this.estado == EstadoVehiculo.EN_MANTENIMIENTO;
    }
    
    public void asignarConductor(Conductor conductor){
        this.conductorAsignado = conductor;
    }
    
    public void liberarConductor() {
        this.conductorAsignado = null;
    }
    
    @Override
    public String toString(){
        return "Vehiculo{" +
                "id=" + id +
                ", placa='" + placa + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", anio=" + year +
                ", capacidadCarga=" + capacidadCarga +
                ", estado=" + estado +
                '}';
    } 
}
