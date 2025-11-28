import inventario.Inventario;
import inventario.Producto;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import usuarios.SistemaUsuarios;
import usuarios.Usuario;
import utils.Colors;
import ventas.HistorialVentas;
import ventas.Venta;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static SistemaUsuarios sistemaUsuarios = new SistemaUsuarios();
    private static Inventario inventario = new Inventario();
    private static HistorialVentas historial = new HistorialVentas();
    private static DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;

    public static void main(String[] args) {
        sistemaUsuarios.crearUsuario("admin", "1234", true);
        sistemaUsuarios.crearUsuario("vendedor", "0000", false);

        inventario.cargarPredeterminado();

        System.out.println(Colors.wrap(Colors.BLUE + Colors.BOLD, " ---- DROGUERÍA ---- "));
        System.out.print("Usuario: ");
        String user = scanner.nextLine();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine();

        Usuario actual = sistemaUsuarios.login(user, pass);
        if (actual == null) {
            System.out.println("Credenciales inválidas. Saliendo...");
            return;
        }

        inventario.mostrarAlertas();

        int opc;
        do {
            System.out.println(Colors.wrap(Colors.GREEN + Colors.BOLD, "\nº--- MENÚ PRINCIPAL ---"));
            System.out.println(Colors.wrap(Colors.YELLOW, "1. Inventario"));
            System.out.println(Colors.wrap(Colors.YELLOW, "2. Ventas"));
            if (actual.isAdmin()) System.out.println(Colors.wrap(Colors.YELLOW, "3. Reportes"));
            if (actual.isAdmin()) System.out.println(Colors.wrap(Colors.YELLOW, "4. Usuarios "));
            System.out.println(Colors.wrap(Colors.YELLOW, "5. Ver alertas"));
            System.out.println(Colors.wrap(Colors.YELLOW, "0. Salir"));
            System.out.print("Opción: ");
            opc = leerInt();

            switch (opc) {
                case 1 -> menuInventario();
                case 2 -> menuVentas();
                case 3 -> { if (actual.isAdmin()) menuReportes(); else System.out.println("\u001B[31m\u001B[3mAcceso denegado :(\u001B[0m"); }
                case 4 -> { if (actual.isAdmin()) sistemaUsuarios.menuAdmin(scanner); else System.out.println("\u001B[31m\u001B[3mAcceso denegado :(\u001B[0m"); }
                case 5 -> inventario.mostrarAlertas();
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

    private static void menuInventario() {
        int op;
        do {
            System.out.println(Colors.wrap(Colors.AQUA + Colors.BOLD, "\n--- INVENTARIO ---"));
            System.out.println(Colors.wrap(Colors.BRIGHT_GREEN, "1. Agregar producto"));
            System.out.println(Colors.wrap(Colors.CYAN, "2. Editar producto"));
            System.out.println(Colors.wrap(Colors.RED, "3. Quitar producto"));
            System.out.println(Colors.wrap(Colors.AQUA, "4. Ver inventario"));
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

    private static void menuVentas() {
        System.out.println("\n--- VENTAS ---");
        System.out.print("Código o nombre del producto: ");
        String query = scanner.nextLine().trim();
        java.util.ArrayList<Producto> matches = inventario.buscarPorCodigoONombre(query);
        if (matches.isEmpty()) { System.out.println("Producto no encontrado."); return; }

        Producto p;
        if (matches.size() == 1) {
            p = matches.get(0);
        } else {
            System.out.println("Se encontraron varios productos:");
            for (int i = 0; i < matches.size(); i++) {
                Producto m = matches.get(i);
                System.out.printf("  %d) %s\n", i + 1, inventario.formatoResumenProducto(m));
            }
            System.out.print("Ingrese el número del producto a vender (o 0 para cancelar): ");
            int sel = leerInt();
            if (sel <= 0 || sel > matches.size()) { System.out.println("Operación cancelada o selección inválida."); return; }
            p = matches.get(sel - 1);
        }

        inventario.mostrarDetallesProducto(p);
        System.out.print("¿Desea continuar con la venta? (s/n): ");
        String conf = scanner.nextLine().trim();
        if (!conf.equalsIgnoreCase("s")) { System.out.println("Venta cancelada."); return; }
        System.out.print("Cantidad a vender: ");
        int cantidad = leerInt();
        if (cantidad <= 0) { System.out.println("Cantidad inválida."); return; }
        if (p.getCantidad() < cantidad) { System.out.println("Stock insuficiente."); return; }

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
