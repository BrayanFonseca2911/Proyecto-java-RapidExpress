/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.dominio;

import java.time.LocalDateTime;

/**
 *
 * @author User
 */
public class Mantenimiento {
    private int id;
    private Vehiculo vehiculo;
    private LocalDateTime fecha;
    private String descripcion;
    private double costo;
    private int kilometraje;
    
    public Mantenimiento(){
        this.fecha = LocalDateTime.now();
    }
    
    // Constructor con parametros basicos
    public Mantenimiento(Vehiculo vehiculo, String descripcion, double costo, int kilometraje) {
        this.vehiculo = vehiculo;
        this.descripcion = descripcion;
        this.costo = costo;
        this.kilometraje = kilometraje;
        this.fecha = LocalDateTime.now();
    }
    
    //Construtor completo

    public Mantenimiento(int id, Vehiculo vehiculo, LocalDateTime fecha, String descripcion, double costo, int kilometraje) {
        this.id = id;
        this.vehiculo = vehiculo;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.costo = costo;
        this.kilometraje = kilometraje;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(int kilometraje) {
        this.kilometraje = kilometraje;
    }
    
    @Override
    public String toString() {
        return "Mantenimiento{" +
                "id=" + id +
                ", vehiculo=" + (vehiculo != null ? vehiculo.getPlaca() : "N/A") +
                ", fecha=" + fecha +
                ", descripcion='" + descripcion + '\'' +
                ", costo=" + costo +
                ", kilometraje=" + kilometraje +
                '}';
    }
}
