/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.dominio;

import java.util.ArrayList;
import rapidexpress.enums.EstadoRuta;
import java.time.LocalDateTime;
import java.util.List;
/**
 *
 * @author User
 */
public class Ruta {

    private int id;
    private Vehiculo vehiculo;
    private Conductor conductor;
    private List<Paquete> paquetes;
    private LocalDateTime fecha;
    private EstadoRuta estado;
    private double pesoTotal;
    
    public Ruta(){
        this.paquetes = new ArrayList<>();
        this.estado = EstadoRuta.PLANIFICADA;
        this.fecha = LocalDateTime.now();
        this.pesoTotal = 0.0;
    }

    public Ruta(Vehiculo vehiculo, Conductor conductor) {
        this.vehiculo = vehiculo;
        this.conductor = conductor;;
        this.paquetes = new ArrayList<>();
        this.estado = EstadoRuta.PLANIFICADA;
        this.fecha = LocalDateTime.now();
        this.pesoTotal = 0.0;
                
    }

    public Ruta(int id, Vehiculo vehiculo, Conductor conductor, List<Paquete> paquetes, LocalDateTime fecha, EstadoRuta estado) {
        this.id = id;
        this.vehiculo = vehiculo;
        this.conductor = conductor;
        this.paquetes = paquetes;
        this.fecha = fecha;
        this.estado = estado;
        this.pesoTotal = calcularPesoTotal();
    }

    // Getters y Setters
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
    
    public Conductor getConductor() {
        return conductor;
    }
    
    public void setConductor(Conductor conductor) {
        this.conductor = conductor;
    }
    
    public List<Paquete> getPaquetes() {
        return paquetes;
    }
    
    public void setPaquetes(List<Paquete> paquetes) {
        this.paquetes = paquetes;
        this.pesoTotal = calcularPesoTotal();
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }
    
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
    public EstadoRuta getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoRuta estado) {
        this.estado = estado;
    }
    
    public double getPesoTotal() {
        return pesoTotal;
    }
    
    public void setPesoTotal(double pesoTotal) {
        this.pesoTotal = pesoTotal;
    }
    
    // Métodos de negocio
    public void agregarPaquete(Paquete paquete) {
        this.paquetes.add(paquete);
        this.pesoTotal = calcularPesoTotal();
    }
    
    public void eliminarPaquete(Paquete paquete) {
        this.paquetes.remove(paquete);
        this.pesoTotal = calcularPesoTotal();
    }
    
    public double calcularPesoTotal() {
        this.pesoTotal = paquetes.stream()
                .mapToDouble(Paquete::getPeso)
                .sum();
        return this.pesoTotal;
    }
    
    public void iniciar() {
        this.estado = EstadoRuta.EN_CURSO;
    }
    
    public void completar() {
        this.estado = EstadoRuta.COMPLETADA;
    }
    
    public void cancelar() {
        this.estado = EstadoRuta.CANCELADA;
    }
    
    public boolean enCurso() {
        return this.estado == EstadoRuta.EN_CURSO;
    }
    
    public boolean completada() {
        return this.estado == EstadoRuta.COMPLETADA;
    }
    
    public int cantidadPaquetes() {
        return this.paquetes.size();
    }
    
    @Override
    public String toString() {
        return "Ruta{" +
                "id=" + id +
                ", vehiculo=" + (vehiculo != null ? vehiculo.getPlaca() : "N/A") +
                ", conductor=" + (conductor != null ? conductor.getNombre() : "N/A") +
                ", cantidadPaquetes=" + paquetes.size() +
                ", pesoTotal=" + pesoTotal +
                ", estado=" + estado +
                '}';
    }
    
    
    
}
