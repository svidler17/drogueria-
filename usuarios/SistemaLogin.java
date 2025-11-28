package usuarios;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Usuario {
    private String nombre;
    private String cedula;
    private String telefono;
    private String password;
    
    public Usuario(String nombre, String cedula, String telefono, String password) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.password = password;
    }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    @Override
    public String toString() {
        return nombre + " | " + cedula + " | " + telefono;
    }
}

class Cliente {
    private String nombre;
    private String documento;
    private String telefono;
    
    public Cliente(String nombre, String documento, String telefono) {
        this.nombre = nombre;
        this.documento = documento;
        this.telefono = telefono;
    }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    @Override
    public String toString() {
        return nombre + " | " + documento + " | " + telefono;
    }
}

public class SistemaLogin {
    private String usuarioActual;
    private boolean esAdmin;
    private Map<String, Map<String, String>> usuariosSistema;
    
    public SistemaLogin() {
        this.usuarioActual = null;
        this.esAdmin = false;
        this.usuariosSistema = new HashMap<>();
        
        Map<String, String> adminData = new HashMap<>();
        adminData.put("nombre", "Administrador");
        adminData.put("Telefono", "0000000000");
        adminData.put("password", "admin123");
        adminData.put("tipo", "admin");
        usuariosSistema.put("2521", adminData);
    }
    
    public boolean iniciarSesion(Scanner scanner) {
        System.out.println("\n==============================================");
        System.out.println("             INICIO DE SESION");
        System.out.println("==============================================");
        System.out.print("Cedula: ");
        String cedula = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
        
        if (usuariosSistema.containsKey(cedula) && 
            usuariosSistema.get(cedula).get("password").equals(password)) {
            
            usuarioActual = cedula;
            esAdmin = usuariosSistema.get(cedula).get("tipo").equals("admin");
            
            if (esAdmin) {
                System.out.println("Bienvenido Administrador");
            } else {
                System.out.println("Bienvenido Usuario");
            }
            return true;
        } else {
            System.out.println("Cedula o contraseña incorrectos.");
            return false;
        }
    }
    
    public void cerrarSesion() {
        if (usuarioActual != null) {
            System.out.println("Sesion cerrada correctamente.");
            usuarioActual = null;
            esAdmin = false;
        } else {
            System.out.println("No hay sesion activa.");
        }
    }
    
    public boolean estaAutenticado() {
        return usuarioActual != null;
    }
    
    public boolean isEsAdmin() {
        return esAdmin;
    }
    
    public String obtenerNombreUsuario() {
        if (usuarioActual != null && usuariosSistema.containsKey(usuarioActual)) {
            return usuariosSistema.get(usuarioActual).getOrDefault("nombre", "Usuario");
        }
        return "Usuario";
    }
    
    public Map<String, Map<String, String>> getUsuariosSistema() {
        return usuariosSistema;
    }
}
