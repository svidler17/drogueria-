import usuarios.SistemaUsuarios;
import usuarios.Usuario;
import inventario.Inventario;
import inventario.Producto;
import ventas.HistorialVentas;
import ventas.Venta;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

// Main launcher that ties the modules together.
public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static SistemaUsuarios sistemaUsuarios = new SistemaUsuarios();
    private static Inventario inventario = new Inventario();
    private static HistorialVentas historial = new HistorialVentas();
    private static DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;

    public static void main(String[] args) {
        // Seed data
        sistemaUsuarios.crearUsuario("admin", "1234", true);
        sistemaUsuarios.crearUsuario("vendedor", "0000", false);

        // Load sample inventory
        inventario.cargarPredeterminado();

        System.out.println("=== DROGUERÍA MODULAR - MAIN ===");
        System.out.print("Usuario: ");
        String user = scanner.nextLine();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine();

        Usuario actual = sistemaUsuarios.login(user, pass);
        if (actual == null) {
            System.out.println("Credenciales inválidas. Saliendo...");
            return;
        }

        // show alerts on login
        inventario.mostrarAlertas();

        int opc;
        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Inventario");
            System.out.println("2. Ventas");
            System.out.println("3. Reportes");
            if (actual.isAdmin()) System.out.println("4. Usuarios (Admin)");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opc = leerInt();

            switch (opc) {
                case 1 -> menuInventario();
                case 2 -> menuVentas();
                case 3 -> menuReportes();
                case 4 -> { if (actual.isAdmin()) sistemaUsuarios.menuAdmin(scanner); else System.out.println("Acceso denegado."); }
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opc != 0);
    }

    private static int leerInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) { return -1; }
    }

    // Inventory menu delegates to Inventario module
    private static void menuInventario() {
        int op;
        do {
            System.out.println("\n--- INVENTARIO ---");
            System.out.println("1. Agregar producto");
            System.out.println("2. Editar producto");
            System.out.println("3. Quitar producto");
            System.out.println("4. Ver inventario");
            System.out.println("0. Regresar");
            System.out.print("Opción: ");
            op = leerInt();

            switch (op) {
                case 1 -> inventario.menuAgregar(scanner);
                case 2 -> inventario.menuEditar(scanner);
                case 3 -> inventario.menuQuitar(scanner);
                case 4 -> inventario.listar();
                case 0 -> {}
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 0);
    }

    // Sales menu interacts with Inventario and HistorialVentas
    private static void menuVentas() {
        System.out.println("\n--- VENTAS ---");
        System.out.print("Código producto: ");
        String codigo = scanner.nextLine().trim();
        Producto p = inventario.buscarPorCodigo(codigo);
        if (p == null) { System.out.println("Producto no encontrado."); return; }

        System.out.println("Producto: " + p.getNombre() + " | Precio: " + p.getPrecio() + " | Disponible: " + p.getCantidad());
        System.out.print("Cantidad a vender: ");
        int cantidad = leerInt();
        if (cantidad <= 0) { System.out.println("Cantidad inválida."); return; }
        if (p.getCantidad() < cantidad) { System.out.println("Stock insuficiente."); return; }

        // perform sale
        p.setCantidad(p.getCantidad() - cantidad);
        double total = cantidad * p.getPrecio();
        Venta v = new Venta(java.time.LocalDate.now(), p.getCodigo(), p.getNombre(), cantidad, total);
        historial.registrarVenta(v);
        System.out.println("Venta registrada: " + v);
    }

    private static void menuReportes() {
        int op;
        do {
            System.out.println("\n--- REPORTES ---");
            System.out.println("1. Historial de ventas");
            System.out.println("2. Producto más vendido");
            System.out.println("3. Producto menos vendido");
            System.out.println("0. Regresar");
            System.out.print("Opción: ");
            op = leerInt();
            switch (op) {
                case 1 -> historial.mostrarHistorial();
                case 2 -> {
                    String codigo = historial.masVendido();
                    if (codigo == null) System.out.println("No hay ventas.");
                    else System.out.println("Más vendido: " + (inventario.buscarPorCodigo(codigo) != null ? inventario.buscarPorCodigo(codigo).getNombre() : codigo));
                }
                case 3 -> {
                    String codigo = historial.menosVendido();
                    if (codigo == null) System.out.println("No hay ventas.");
                    else System.out.println("Menos vendido: " + (inventario.buscarPorCodigo(codigo) != null ? inventario.buscarPorCodigo(codigo).getNombre() : codigo));
                }
                case 0 -> {}
                default -> System.out.println("Opción inválida.");
            }
        } while (op != 0);
    }
}
