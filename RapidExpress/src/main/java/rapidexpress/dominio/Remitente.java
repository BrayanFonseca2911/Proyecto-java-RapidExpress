/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.dominio;

/**
 *
 * @author User
 */
public class Remitente extends Persona{
    
    //Constructor vacio
    public Remitente(){
        super();
    }
    
    //Constructor con parametros
    public Remitente(String nombre, String direccion, String telefono){
        super(nombre, direccion, telefono);
    }

    @Override
    public String toString() {
        return "Remitente{" +
                "nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
