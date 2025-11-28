package usuarios;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GestionUsuarios {
    private Map<String, Map<String, String>> usuarios;
    
    public GestionUsuarios() {
        this.usuarios = new HashMap<>();
    }
    
    public boolean registrarUsuario(Scanner scanner, SistemaLogin sistemaLogin) {
        System.out.println("\n==============================================");
        System.out.println("      REGISTRO DE USUARIO DEL SISTEMA");
        System.out.println("==============================================");
        
        System.out.print("Ingrese el nombre: ");
        String nombre = scanner.nextLine();
        
        String cedula;
        while (true) {
            System.out.print("Ingrese la cedula: ");
            cedula = scanner.nextLine();
            if (!cedula.matches("\\d+") || cedula.length() < 7) {
                System.out.println("La cedula debe ser un numero valido");
            } else {
                break;
            }
        }
        
        if (usuarios.containsKey(cedula) || sistemaLogin.getUsuariosSistema().containsKey(cedula)) {
            System.out.println("Ya existe un usuario con esa cedula.");
            return false;
        }
        
        String telefono;
        while (true) {
            System.out.print("Ingrese numero de celular: ");
            telefono = scanner.nextLine();
            if (!telefono.matches("\\d+") || telefono.length() < 10) {
                System.out.println("Debe ser un numero valido");
            } else {
                break;
            }
        }
        
        String password = cedula.substring(cedula.length() - 4);
        System.out.println("Tu contraseña para inicio es " + password);
        
        Map<String, String> datosUsuario = new HashMap<>();
        datosUsuario.put("cedula", cedula);
        datosUsuario.put("nombre", nombre);
        datosUsuario.put("Telefono", telefono);
        datosUsuario.put("password", password);
        datosUsuario.put("tipo", "usuario");
        
        usuarios.put(cedula, datosUsuario);
        sistemaLogin.getUsuariosSistema().put(cedula, datosUsuario);
        
        System.out.println("Usuario registrado correctamente en el sistema.");
        return true;
    }
    
    public boolean editarUsuario(Scanner scanner) {
        System.out.print("Ingrese la cedula del usuario a editar: ");
        String cedula = scanner.nextLine();
        
        if (usuarios.containsKey(cedula)) {
            Map<String, String> usuario = usuarios.get(cedula);
            
            System.out.println("Deje en blanco para no cambiar el dato");
            System.out.print("Nuevo nombre [" + usuario.get("nombre") + "]: ");
            String nuevoNombre = scanner.nextLine();
            System.out.print("Nuevo telefono [" + usuario.get("Telefono") + "]: ");
            String nuevoTelefono = scanner.nextLine();
            
            if (!nuevoNombre.isEmpty()) {
                usuario.put("nombre", nuevoNombre);
            }
            if (!nuevoTelefono.isEmpty()) {
                usuario.put("Telefono", nuevoTelefono);
            }
            
            System.out.println("Usuario actualizado correctamente.");
            return true;
        }
        
        System.out.println("No se encontro un usuario con esa cedula.");
        return false;
    }
    
    public boolean eliminarUsuario(String cedula) {
        if (usuarios.containsKey(cedula)) {
            usuarios.remove(cedula);
            System.out.println("Usuario eliminado correctamente.");
            return true;
        }
        
        System.out.println("No existe usuario con esa cedula.");
        return false;
    }
    
    public void listarUsuarios() {
        if (usuarios.isEmpty()) {
            System.out.println("\n--- LISTA DE USUARIOS ---");
            System.out.println("No hay usuarios registrados.");
            System.out.println("---------------------------");
            return;
        }
        
        System.out.println("\n--- LISTA DE USUARIOS ---");
        
        for (Map.Entry<String, Map<String, String>> entry : usuarios.entrySet()) {
            String cedula = entry.getKey();
            Map<String, String> datos = entry.getValue();
            System.out.println("Nombre: " + datos.get("nombre") + 
                             " | Cedula: " + cedula + 
                             " | Telefono: " + datos.get("Telefono"));
        }
        
        System.out.println("---------------------------");
    }
    
    public Map<String, Map<String, String>> getUsuarios() {
        return usuarios;
    }
}