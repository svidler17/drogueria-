import inventario.Inventario;
import inventario.Producto;
import usuarios.SistemaLogin;
import usuarios.GestionUsuarios;
import utils.Colors;
import ventas.HistorialVentas;
import ventas.Venta;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.ArrayList;
import ventas.ItemVenta;
import ventas.Historial;
public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static SistemaLogin sistemaLogin = new SistemaLogin();
    private static GestionUsuarios gestionUsuarios = new GestionUsuarios();
    private static Inventario inventario = new Inventario();
    private static HistorialVentas historial = new HistorialVentas();
    private static DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;

    public static void main(String[] args) {
        inventario.cargarPredeterminado();
        
        boolean salirDelSistema = false;
        
        while (!salirDelSistema) {
            salirDelSistema = menuInicial();
        }
        
        System.out.println(Colors.wrap(Colors.CYAN + Colors.BOLD, "\nGracias por usar el sistema! Hasta pronto."));
        scanner.close();
    }

    private static boolean menuInicial() {
        System.out.println("\n" + Colors.wrap(Colors.BLUE + Colors.BOLD, "╔════════════════════════════════════════╗"));
        System.out.println(Colors.wrap(Colors.BLUE + Colors.BOLD, "║     SISTEMA DE DROGUERIA - INICIO      ║"));
        System.out.println(Colors.wrap(Colors.BLUE + Colors.BOLD, "╚════════════════════════════════════════╝"));
        System.out.println(Colors.wrap(Colors.YELLOW, "1. Iniciar Sesion"));
        System.out.println(Colors.wrap(Colors.YELLOW, "0. Salir del Sistema"));
        System.out.print("Seleccione una opcion: ");
        
        int opcion = leerInt();
        
        switch (opcion) {
            case 1:
                if (sistemaLogin.iniciarSesion(scanner)) {
                    inventario.mostrarAlertas();
                    menuPrincipal();
                }
                return false;
                
            case 0:
                return true;
                
            default:
                System.out.println(Colors.wrap(Colors.RED, "Opcion invalida. Intente nuevamente."));
                return false;
        }
    }

    private static void menuPrincipal() {
        int opc;
        do {
            System.out.println("\n" + Colors.wrap(Colors.GREEN + Colors.BOLD, "╔════════════════════════════════════════╗"));
            System.out.println(Colors.wrap(Colors.GREEN + Colors.BOLD, "║          MENU PRINCIPAL                ║"));
            System.out.println(Colors.wrap(Colors.GREEN + Colors.BOLD, "╚════════════════════════════════════════╝"));
            System.out.println(Colors.wrap(Colors.CYAN, "  Usuario: " + sistemaLogin.obtenerNombreUsuario()));
            System.out.println(Colors.wrap(Colors.CYAN, "  Rol: " + (sistemaLogin.isEsAdmin() ? "Administrador" : "Usuario")));
            System.out.println("──────────────────────────────────────────");
            System.out.println(Colors.wrap(Colors.YELLOW, "1. Inventario"));
            System.out.println(Colors.wrap(Colors.YELLOW, "2. Ventas"));
            
            if (sistemaLogin.isEsAdmin()) {
                System.out.println(Colors.wrap(Colors.YELLOW, "3. Reportes"));
                System.out.println(Colors.wrap(Colors.YELLOW, "4. Gestion de Usuarios"));
            }
            
            System.out.println(Colors.wrap(Colors.YELLOW, "5. Ver Alertas de Inventario"));
            System.out.println("0. Cerrar Sesion");
            System.out.print("Opcion: ");
            opc = leerInt();

            switch (opc) {
                case 1:
                    menuInventario();
                    break;
                    
                case 2:
                    menuVentas();
                    break;
                    
                case 3:
                    if (sistemaLogin.isEsAdmin()) {
                        menuReportes();
                    } else {
                        System.out.println(Colors.wrap(Colors.RED + Colors.BOLD, " Acceso denegado. Solo administradores."));
                    }
                    break;
                    
                case 4:
                    if (sistemaLogin.isEsAdmin()) {
                        menuGestionUsuarios();
                    } else {
                        System.out.println(Colors.wrap(Colors.RED + Colors.BOLD, " Acceso denegado. Solo administradores."));
                    }
                    break;
                    
                case 5:
                    inventario.mostrarAlertas();
                    break;
                    
                case 0:
                    sistemaLogin.cerrarSesion();
                    System.out.println(Colors.wrap(Colors.YELLOW, "\nRegresando al menu inicial..."));
                    break;
                    
                default:
                    System.out.println(Colors.wrap(Colors.RED, "Opcion invalida."));
            }
        } while (opc != 0);
    }

    private static void menuGestionUsuarios() {
        int op;
        do {
            System.out.println("\n" + Colors.wrap(Colors.CYAN + Colors.BOLD, "╔════════════════════════════════════════╗"));
            System.out.println(Colors.wrap(Colors.CYAN + Colors.BOLD, "║      GESTION DE USUARIOS               ║"));
            System.out.println(Colors.wrap(Colors.CYAN + Colors.BOLD, "╚════════════════════════════════════════╝"));
            System.out.println(Colors.wrap(Colors.YELLOW, "1. Listar Usuarios"));
            System.out.println(Colors.wrap(Colors.YELLOW, "2. Registrar Nuevo Usuario"));
            System.out.println(Colors.wrap(Colors.YELLOW, "3. Editar Usuario"));
            System.out.println(Colors.wrap(Colors.YELLOW, "4. Eliminar Usuario"));
            System.out.println(Colors.wrap(Colors.YELLOW, "0. Regresar"));
            System.out.print("Opcion: ");
            op = leerInt();

            switch (op) {
                case 1:
                    gestionUsuarios.listarUsuarios();
                    break;
                    
                case 2:
                    gestionUsuarios.registrarUsuario(scanner, sistemaLogin);
                    break;
                    
                case 3:
                    gestionUsuarios.editarUsuario(scanner);
                    break;
                    
                case 4:
                    System.out.print("Ingrese la cedula del usuario a eliminar: ");
                    String cedula = scanner.nextLine().trim();
                    gestionUsuarios.eliminarUsuario(cedula);
                    break;
                    
                case 0:
                    break;
                    
                default:
                    System.out.println(Colors.wrap(Colors.RED, "Opcion invalida."));
            }
        } while (op != 0);
    }

    private static int leerInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private static void menuInventario() {
        int op;
        do {
            System.out.println("\n" + Colors.wrap(Colors.AQUA + Colors.BOLD, "╔════════════════════════════════════════╗"));
            System.out.println(Colors.wrap(Colors.AQUA + Colors.BOLD, "║          INVENTARIO                    ║"));
            System.out.println(Colors.wrap(Colors.AQUA + Colors.BOLD, "╚════════════════════════════════════════╝"));
            System.out.println(Colors.wrap(Colors.BRIGHT_GREEN, "1. Agregar producto"));
            System.out.println(Colors.wrap(Colors.CYAN, "2. Editar producto"));
            System.out.println(Colors.wrap(Colors.RED, "3. Quitar producto"));
            System.out.println(Colors.wrap(Colors.AQUA, "4. Ver inventario"));
            System.out.println("0. Regresar");
            System.out.print("Opcion: ");
            op = leerInt();

            switch (op) {
                case 1 -> inventario.menuAgregar(scanner);
                case 2 -> inventario.menuEditar(scanner);
                case 3 -> inventario.menuQuitar(scanner);
                case 4 -> inventario.listar();
                case 0 -> {}
                default -> System.out.println(Colors.wrap(Colors.RED, "Opcion invalida."));
            }
        } while (op != 0);
    }

  private static void menuVentas() {
    System.out.println("\n--- VENTAS ---");
    ArrayList<ItemVenta> listaItems = new ArrayList<>();
    boolean continuar = true;

    while (continuar) {
        System.out.print("\nCódigo o nombre del producto: ");
        String query = scanner.nextLine().trim();
        ArrayList<Producto> matches = inventario.buscarPorCodigoONombre(query);

        if (matches.isEmpty()) { 
            System.out.println("Producto no encontrado."); 
            continue; // vuelve al inicio del while
        }

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
            if (sel <= 0 || sel > matches.size()) { 
                System.out.println("Operación cancelada o selección inválida."); 
                continue;
            }
            p = matches.get(sel - 1);
        }

        inventario.mostrarDetallesProducto(p);

        System.out.print("Cantidad a vender: ");
        int cantidad = leerInt();
        if (cantidad <= 0) { 
            System.out.println("Cantidad inválida."); 
            continue;
        }
        if (p.getCantidad() < cantidad) { 
            System.out.println("Stock insuficiente."); 
            continue;
        }

        // Confirmar si quiere agregar otro producto
        System.out.print("¿Desea agregar este producto a la venta? (s/n): ");
        String conf = scanner.nextLine().trim();
        if (!conf.equalsIgnoreCase("s")) { 
            System.out.println("Producto no agregado."); 
        } else {
            // Agregar item a la lista
            listaItems.add(new ItemVenta(
                p.getCodigo(),
                p.getNombre(),
                cantidad,
                cantidad * p.getPrecio()
            ));
            // Restar del inventario
            p.setCantidad(p.getCantidad() - cantidad);
        }

        // Preguntar si desea seguir agregando productos
        System.out.print("¿Desea agregar otro producto a la venta? (s/n): ");
        String seguir = scanner.nextLine().trim();
        if (!seguir.equalsIgnoreCase("s")) {
            continuar = false;
        }
    }

    if (listaItems.isEmpty()) {
        System.out.println("No se registró ninguna venta.");
        return;
    }

    // Calcular total
    double total = 0;
    for (ItemVenta item : listaItems) {
        total += item.getSubtotal(); // asumimos que ItemVenta tiene getTotal()
    }

    // Crear venta
    Venta v = new Venta(java.time.LocalDate.now(), listaItems, total);

    // Registrar venta
    historial.registrarVenta(v);

    System.out.println("\nVenta registrada correctamente:\n" + v);
}



    private static void menuReportes() {
    int op;
    do {
        System.out.println("\n--- REPORTES ---");
        System.out.println("1. Historial de ventas");
        System.out.println("2. Reporte de productos vendidos (barras)");
        System.out.println("0. Regresar");
        System.out.print("Opción: ");
        op = leerInt();

        switch (op) {
            case 1 -> historial.mostrarHistorial();

            case 2 -> {
                // Crear mapa código → nombre
                java.util.Map<String, String> nombres = new java.util.HashMap<>();
                for (Producto p : inventario.getListaProductos()) {
                    nombres.put(p.getCodigo(), p.getNombre());
                }

                historial.mostrarReporteBarras(nombres);
            }

            case 0 -> {}
            default -> System.out.println("Opción inválida.");
        }
    } while (op != 0);
}
}
