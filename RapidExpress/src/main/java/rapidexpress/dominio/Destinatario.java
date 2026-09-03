/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rapidexpress.dominio;

/**
 *
 * @author User
 */
public class Destinatario extends Persona {
    public Destinatario(){
        super();
    }
    
    //Constructor con parámetros
    public Destinatario(String nombre, String direccion, String telefono){
        super(nombre, direccion, telefono);
    }

    @Override
    public String toString() {
        return "Destinatario{" +
                "nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }  
}
