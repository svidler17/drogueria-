package usuarios;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class SistemaUsuarios {
    private ArrayList<Usuario> usuarios = new ArrayList<>();

    public SistemaUsuarios() {
        // constructor vacío; se pueden agregar usuarios desde Main
    }

    public void crearUsuario(String nombre, String password, boolean admin) {
        usuarios.add(new Usuario(nombre, password, admin));
    }

    public Usuario login(String nombre, String password) {
        for (Usuario u : usuarios) {
            if (u.getNombre().equals(nombre) && u.getPassword().equals(password)) return u;
        }
        return null;
    }

    public void menuAdmin(Scanner scanner) {
        int op;
        do {
            System.out.println("\n--- GESTIÓN USUARIOS ---");
            System.out.println("1. Crear usuario");
            System.out.println("2. Eliminar usuario");
            System.out.println("3. Listar usuarios");
            System.out.println("0. Regresar");
            System.out.print("Opción: ");
            try { op = Integer.parseInt(scanner.nextLine().trim()); } catch (Exception e) { op = -1; }
            switch (op) {
                case 1 -> {
                    System.out.print("Nombre: ");
                    String n = scanner.nextLine();
                    System.out.print("Contraseña: ");
                    String p = scanner.nextLine();
                    System.out.print("¿Es admin? (s/n): ");
                    boolean adm = scanner.nextLine().equalsIgnoreCase("s");
                    crearUsuario(n, p, adm);
                    System.out.println("Usuario creado.");
                }
                case 2 -> {
                    System.out.print("Nombre a eliminar: ");
                    String nombre = scanner.nextLine();
                    if (nombre.equals("admin")) { System.out.println("No se puede eliminar admin por defecto."); break; }
                    Iterator<Usuario> it = usuarios.iterator();
                    boolean removed = false;
                    while (it.hasNext()) {
                        if (it.next().getNombre().equals(nombre)) { it.remove(); removed = true; }
                    }
                    System.out.println(removed ? "Usuario eliminado." : "Usuario no encontrado.");
                }
                case 3 -> {
                    System.out.println("Usuarios:");
                    for (Usuario u : usuarios) System.out.println(" - " + u);
                }
                case 0 -> {}
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 0);
    }
}
