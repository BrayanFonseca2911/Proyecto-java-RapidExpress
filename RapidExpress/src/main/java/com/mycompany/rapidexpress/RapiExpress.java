package com.mycompany.rapidexpress;

import rapidexpress.vista.MenuPrincipal;
import rapidexpress.util.DBConnection;

/**
 * Clase principal de la aplicación RapidExpress
 * Sistema de Gestión de Flotas y Rutas
 * 
 * @author User
 */
public class RapiExpress {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   RAPIDEXPRESS - Sistema de Gestión   ║");
        System.out.println("║        Bienvenido al sistema           ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        try {
            // Verificar conexión a BD
            DBConnection db = DBConnection.getInstance();
            System.out.println("✅ Conexión a base de datos establecida\n");
            
            // Iniciar menú principal
            MenuPrincipal menu = new MenuPrincipal();
            menu.mostrarMenu();
            
        } catch (Exception e) {
            System.err.println("❌ Error crítico: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}