package inventario; //cambiar los colores

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;
import utils.Colors;

public class Inventario {

    private ArrayList<Producto> productos = new ArrayList<>();

    public Inventario() {}

    // ============================================
    //        CARGAR PRODUCTOS PREDETERMINADOS
    // ============================================
    public void cargarPredeterminado() {
        productos.add(new Producto("P001","Paracetamol 500mg",50,10, LocalDate.now().plusMonths(8), false, 1200.0));
        productos.add(new Producto("P002","Ibuprofeno 400mg",30,8, LocalDate.now().plusMonths(6), false, 1500.0));
        productos.add(new Producto("P003","Insulina (Frío)",2,5, LocalDate.now().plusDays(18), true,45000.0));
        productos.add(new Producto("P004","Antibiótico X 250mg",15,5, LocalDate.now().plusDays(16),false,8000.0));
        productos.add(new Producto("P005","Omeprazol 20mg",40,10,LocalDate.now().plusMonths(2),false,1800.0));
   

        productos.add(new Producto("P006","Vitamina C 1g",40,10, LocalDate.now().plusMonths(12), false, 2500.0));
        productos.add(new Producto("P007","Omeprazol 20mg",60,15, LocalDate.now().plusMonths(10), false, 1800.0));
        productos.add(new Producto("P008","Suero Oral",25,5, LocalDate.now().plusMonths(5), false, 3000.0));

      
        productos.add(new Producto("P009","Gel antibacterial 250ml",5,5, LocalDate.now().plusDays(20), false, 5000.0));
        productos.add(new Producto("P010","Jarabe para tos infantil",10,5, LocalDate.now().plusDays(17), false, 7000.0));
    }

    // ============================================
    //        REORDENAR CÓDIGOS AUTOMÁTICAMENTE
    // ============================================
    private void reordenarCodigos() {
        for (int i = 0; i < productos.size(); i++) {
            String codigoNuevo = String.format("P%03d", i + 1);
            productos.get(i).setCodigo(codigoNuevo);
        }

        // reordenó códigos
    }

    // ============================================
    //        GENERAR CÓDIGO AUTOMÁTICO
    // ============================================
    private String generarCodigo() {
        return String.format("P%03d", productos.size() + 1);
    }

    // ============================================
    //        BUSCAR PRODUCTO POR CÓDIGO
    // ============================================
    public Producto buscarPorCodigo(String codigo) {
        for (Producto p : productos)
            if (p.getCodigo().equalsIgnoreCase(codigo))
                return p;

        return null;
    }

    // ============================================
    //        BUSCAR PRODUCTO POR CÓDIGO O NOMBRE
    //        (case-insensitive, substring)
    // ============================================
    public ArrayList<Producto> buscarPorCodigoONombre(String query) {
        ArrayList<Producto> matches = new ArrayList<>();
        if (query == null || query.isBlank()) return matches;

        String q = query.trim().toLowerCase();

        // 1) Si coincide exactamente con un código -> devolver solo ese
        Producto exacto = buscarPorCodigo(q);
        if (exacto != null) {
            matches.add(exacto);
            return matches;
        }

        // 2) Buscar por nombre (substring case-insensitive )
        //(substring sirve para buscar por nombres que contienen el query, por ejemplo "gel" busca "Gel antibacterial")
        //(case-insensitive para que no importe mayúsculas/minúsculas)    
        for (Producto p : productos) {
            if (p.getNombre() != null && p.getNombre().toLowerCase().contains(q)) {
                matches.add(p);
            }
        }

        return matches;
    }

    // ============================================
    //                LISTAR
    // ============================================
    public void listar() {
        System.out.println(Colors.wrap(Colors.BRIGHT_BLUE + Colors.BOLD, "Inventario:"));
        for (Producto p : productos) {
            System.out.println(" - " + Colors.wrap(Colors.AQUA, p.toString()));
        }
    }

    // ============================================
    //        MOSTRAR ALERTAS
    // ============================================
    public void mostrarAlertas() {
        System.out.println("\n" + Colors.wrap(Colors.YELLOW + Colors.BOLD, "=== ALERTAS DEL INVENTARIO ==="));

        boolean hayAlertas = false;
        java.util.HashSet<String> nombresAlertados = new java.util.HashSet<>();

        for (Producto p : productos) {
            String alerta = null;
            String colorAlerta = Colors.YELLOW; 
            if (p.getFechaVencimiento() != null && p.estaVencido()) {
                alerta = "PRODUCTO VENCIDO";
                colorAlerta = Colors.RED + Colors.BOLD;
            } else {
                if (p.isRequiereFrio()) {
                    alerta = "ATENCIÓN: Producto sensible a temperatura == Verificar integridad de la cadena de frío (temperatura de almacenamiento).";
                    colorAlerta = Colors.BRIGHT_BLUE + Colors.BOLD;
                } else {
                    if (p.getFechaVencimiento() != null) {
                        long dias = ChronoUnit.DAYS.between(LocalDate.now(), p.getFechaVencimiento());
                        if (dias >= 15 && dias <= 20) {
                            alerta = "VENCE EN " + dias + " DÍAS == REVISAR";
                            colorAlerta = Colors.YELLOW + Colors.BOLD;
                        }
                    }
                }
            }

            // 3) si no hay alerta por vencimiento, revisar stock bajo
            if (alerta == null && p.getCantidad() <= p.getStockMinimo()) {
                alerta = "Stock bajo (" + p.getCantidad() + ")";
                colorAlerta = Colors.YELLOW;
            }

            // 4) si sigue sin alerta y requiere frío, añadir mensaje técnico
            if (alerta == null && p.isRequiereFrio()) {
                alerta = "ATENCIÓN: Producto sensible a temperatura — Revisar cadena de frío (temperatura y almacenamiento)";
                colorAlerta = Colors.BRIGHT_BLUE + Colors.BOLD;
            }

            if (alerta != null) {
                // Evitar duplicados por nombre — si ya mostramos alerta para este nombre, la omitimos
                String nombreNorm = p.getNombre() == null ? "" : p.getNombre().trim().toLowerCase();
                if (!nombresAlertados.contains(nombreNorm)) {
                        // mostrar el nombre en negrita y la alerta en color asignado
                        String nombreM = Colors.wrap(Colors.BRIGHT_BLUE + Colors.BOLD, p.getNombre());
                        System.out.println(" " + nombreM + " == " + Colors.wrap(colorAlerta, alerta));
                    nombresAlertados.add(nombreNorm);
                    hayAlertas = true;
                }
            }
        }

        if (!hayAlertas) {
            System.out.println(" No hay alertas. Todo está en orden.");
        }
    }

    // ============================================
    //          AGREGAR PRODUCTO
    // ============================================
    public void menuAgregar(Scanner scanner) {
        System.out.println(Colors.wrap(Colors.BRIGHT_GREEN + Colors.BOLD, "\n=== AGREGAR PRODUCTO ==="));

        String codigo = generarCodigo();
        System.out.println("Código asignado automáticamente: " + codigo);

        // Nombre (requerido)
        String nombre = promptNonEmptyString(scanner, "Nombre: ", "El nombre es obligatorio. No puede dejarlo vacío.");
        if (nombre == null) { System.out.println(Colors.wrap(Colors.YELLOW, "Operación cancelada. Volviendo al menú de inventario.")); return; }

        // Cantidad (requerido)
        Integer cant = promptNonEmptyInt(scanner, "Cantidad: ", "La cantidad es obligatoria y debe ser un número entero.");
        if (cant == null) { System.out.println(Colors.wrap(Colors.YELLOW, "Operación cancelada. Volviendo al menú de inventario.")); return; }

        // Stock mínimo (requerido)
        Integer min = promptNonEmptyInt(scanner, "Stock mínimo: ", "El stock mínimo es obligatorio y debe ser un número entero.");
        if (min == null) { System.out.println(Colors.wrap(Colors.YELLOW, "Operación cancelada. Volviendo al menú de inventario.")); return; }

        // Fecha vencimiento (requerido)
        LocalDate fecha = promptNonEmptyDate(scanner, "Fecha vencimiento (AAAA-MM-DD): ", "Fecha inválida — use AAAA-MM-DD.");
        if (fecha == null) { System.out.println(Colors.wrap(Colors.YELLOW, "Operación cancelada. Volviendo al menú de inventario.")); return; }

        // Requiere frío
        System.out.print("¿Requiere frío? (s/n): ");
        boolean frio = scanner.nextLine().equalsIgnoreCase("s");

        // Precio (requerido)
        Double precio = promptNonEmptyDouble(scanner, "Precio unitario: ", "El precio es obligatorio y debe ser numérico (ej. 1200.0).");
        if (precio == null) { System.out.println(Colors.wrap(Colors.YELLOW, "Operación cancelada. Volviendo al menú de inventario.")); return; }

        productos.add(new Producto(codigo, nombre, cant, min, fecha, frio, precio));

        reordenarCodigos(); // REORDENAR AUTOMÁTICO

        System.out.println("Producto agregado correctamente.");
    }

    // ============================
    //  HELPERS DE VALIDACIÓN
    // ============================
    private String promptNonEmptyString(Scanner scanner, String prompt, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (!input.isBlank()) return input.trim();

            System.out.println(Colors.wrap(Colors.YELLOW, errorMsg));
            System.out.print(Colors.wrap(Colors.YELLOW, "¿Desea intentar de nuevo? (s/n): "));
            String again = scanner.nextLine();
            if (!again.equalsIgnoreCase("s")) return null; // cancel
        }
    }

    private Integer promptNonEmptyInt(Scanner scanner, String prompt, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (!input.isBlank()) {
                try {
                    return Integer.parseInt(input.trim());
                } catch (Exception e) {
                    System.out.println(Colors.wrap(Colors.YELLOW, errorMsg));
                    System.out.print(Colors.wrap(Colors.YELLOW, "¿Desea intentar de nuevo? (s/n): "));
                    String again = scanner.nextLine();
                    if (!again.equalsIgnoreCase("s")) return null;
                    continue; // retry
                }
            }
            System.out.println(Colors.wrap(Colors.YELLOW, errorMsg));
            System.out.print(Colors.wrap(Colors.YELLOW, "¿Desea intentar de nuevo? (s/n): "));
            String again = scanner.nextLine();
            if (!again.equalsIgnoreCase("s")) return null;
        }
    }

    private Double promptNonEmptyDouble(Scanner scanner, String prompt, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (!input.isBlank()) {
                try {
                    return Double.parseDouble(input.trim());
                } catch (Exception e) {
                    System.out.println(Colors.wrap(Colors.YELLOW, errorMsg));
                    System.out.print(Colors.wrap(Colors.YELLOW, "¿Desea intentar de nuevo? (s/n): "));
                    String again = scanner.nextLine();
                    if (!again.equalsIgnoreCase("s")) return null;
                    continue;
                }
            }
            System.out.println(Colors.wrap(Colors.YELLOW, errorMsg));
            System.out.print(Colors.wrap(Colors.YELLOW, "¿Desea intentar de nuevo? (s/n): "));
            String again = scanner.nextLine();
            if (!again.equalsIgnoreCase("s")) return null;
        }
    }

    private LocalDate promptNonEmptyDate(Scanner scanner, String prompt, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (!input.isBlank()) {
                try {
                    return LocalDate.parse(input.trim());
                } catch (Exception e) {
                    System.out.println(Colors.wrap(Colors.YELLOW, errorMsg));
                    System.out.print(Colors.wrap(Colors.YELLOW, "¿Desea intentar de nuevo? (s/n): "));
                    String again = scanner.nextLine();
                    if (!again.equalsIgnoreCase("s")) return null;
                    continue;
                }
            }
            System.out.println(Colors.wrap(Colors.YELLOW, errorMsg));
            System.out.print(Colors.wrap(Colors.YELLOW, "¿Desea intentar de nuevo? (s/n): "));
            String again = scanner.nextLine();
            if (!again.equalsIgnoreCase("s")) return null;
        }
    }

    // ============================================
    //                  EDITAR
    // ============================================
    public void menuEditar(Scanner scanner) {
        System.out.println(Colors.wrap(Colors.CYAN + Colors.BOLD, "\n=== EDITAR PRODUCTO ==="));
        System.out.print(Colors.wrap(Colors.CYAN, "Código o nombre a editar: "));
        String query = scanner.nextLine();
        ArrayList<Producto> matches = buscarPorCodigoONombre(query);

        if (matches.isEmpty()) {
            System.out.println("No existe.");
            return;
        }

        Producto p;
        if (matches.size() == 1) {
            p = matches.get(0);
        } else {
            System.out.println("Se encontraron varios productos:");
            // Mostrar cada coincidencia en una sola línea horizontal
            for (int i = 0; i < matches.size(); i++) {
                Producto m = matches.get(i);
                System.out.printf("  %d) %s\n", i + 1, Colors.wrap(Colors.GREEN, formatoResumenProducto(m)));
            }
            System.out.print("Ingrese el número del producto a editar (o 0 para cancelar): ");
            String sel = scanner.nextLine();
            int idx = parseIntSafe(sel);
            if (idx <= 0 || idx > matches.size()) {
                System.out.println("Operación cancelada o selección inválida.");
                return;
            }
            p = matches.get(idx - 1);
        }

        // Mostrar detalles completos antes de editar
        mostrarDetallesProducto(p);
        System.out.println("Dejar vacío para mantener valor.");

        System.out.print("Nuevo nombre: ");
        String n = scanner.nextLine();
        if (!n.isBlank()) p.setNombre(n);

        System.out.print("Nueva cantidad: ");
        String s = scanner.nextLine();
        if (!s.isBlank()) p.setCantidad(parseIntSafe(s));

        System.out.print("Nuevo stock mínimo: ");
        s = scanner.nextLine();
        if (!s.isBlank()) p.setStockMinimo(parseIntSafe(s));

        System.out.print("Nueva fecha vencimiento (AAAA-MM-DD): ");
        s = scanner.nextLine();
        if (!s.isBlank()) p.setFechaVencimiento(LocalDate.parse(s));

        System.out.print("¿Requiere frío? (s/n): ");
        s = scanner.nextLine();
        if (!s.isBlank()) p.setRequiereFrio(s.equalsIgnoreCase("s"));

        System.out.print("Nuevo precio unitario: ");
        s = scanner.nextLine();
        if (!s.isBlank()) p.setPrecio(parseDoubleSafe(s));

        System.out.println("Producto actualizado.");
    }

    // ============================================
    //                  QUITAR
    // ============================================
    public void menuQuitar(Scanner scanner) {
        System.out.println(Colors.wrap(Colors.RED + Colors.BOLD, "\n=== QUITAR PRODUCTO ==="));
        System.out.print(Colors.wrap(Colors.RED, "Código o nombre a quitar: "));
        String query = scanner.nextLine();

        ArrayList<Producto> matches = buscarPorCodigoONombre(query);
        if (matches.isEmpty()) {
            System.out.println("Producto no encontrado.");
            return;
        }

        Producto toRemove;
        if (matches.size() == 1) {
            toRemove = matches.get(0);
        } else {
            System.out.println("Se encontraron varios productos:");
            // Mostrar cada coincidencia en una sola línea horizontal
            for (int i = 0; i < matches.size(); i++) {
                Producto m = matches.get(i);
                System.out.printf("  %d) %s\n", i + 1, Colors.wrap(Colors.GREEN, formatoResumenProducto(m)));
            }
            System.out.print("Ingrese el número del producto a quitar (o 0 para cancelar): ");
            String sel = scanner.nextLine();
            int idx = parseIntSafe(sel);
            if (idx <= 0 || idx > matches.size()) {
                System.out.println("Operación cancelada o selección inválida.");
                return;
            }
            toRemove = matches.get(idx - 1);
        }

        // Mostrar detalles antes de confirmar eliminación
        mostrarDetallesProducto(toRemove);
        System.out.print("¿Confirma eliminar este producto? (s/n): ");
        String conf = scanner.nextLine();
        if (!conf.equalsIgnoreCase("s")) { System.out.println("Operación cancelada."); return; }

        boolean removed = productos.remove(toRemove);
        if (removed) {
            reordenarCodigos(); // REORDENAR AUTOMÁTICO AL ELIMINAR
            System.out.println("Producto eliminado: " + toRemove.getNombre());
        } else {
            System.out.println("No fue posible eliminar el producto.");
        }
    }

    // ============================================
    //        MOSTRAR DETALLES DE PRODUCTO
    // ============================================
    // Devuelve un resumen en una sola línea con separadores para impresión horizonal
    public String formatoResumenProducto(Producto p) {
        if (p == null) return "";
        String fechaStr = (p.getFechaVencimiento() == null) ? "No aplica" : p.getFechaVencimiento().toString();
        String vencimientoStatus = "No aplica";
        if (p.getFechaVencimiento() != null) {
            if (p.estaVencido()) {
                vencimientoStatus = "VENCIDO";
            } else {
                long dias = ChronoUnit.DAYS.between(LocalDate.now(), p.getFechaVencimiento());
                vencimientoStatus = "Vence en " + dias + " días";
            }
        }

        return String.format(
            "%s | %s | Cant: %d | Min: %d | Vence: %s (%s) | Frío: %s | Precio: $%.2f",
            p.getCodigo(),
            p.getNombre(),
            p.getCantidad(),
            p.getStockMinimo(),
            fechaStr,
            vencimientoStatus,
            (p.isRequiereFrio() ? "Sí" : "No"),
            p.getPrecio()
        );
    }

    public void mostrarDetallesProducto(Producto p) {
        if (p == null) return;
        // Mostrar en una sola línea: formato compacto para lectura horizontal
        String fechaStr = (p.getFechaVencimiento() == null) ? "No aplica" : p.getFechaVencimiento().toString();
        String stockStatus = (p.getCantidad() == 0) ? "SIN STOCK" : (p.getCantidad() <= p.getStockMinimo() ? "Stock bajo" : "OK");
        String vencimientoStatus = "No aplica";
        if (p.getFechaVencimiento() != null) {
            if (p.estaVencido()) {
                vencimientoStatus = "VENCIDO";
            } else {
                long dias = ChronoUnit.DAYS.between(LocalDate.now(), p.getFechaVencimiento());
                vencimientoStatus = "Vence en " + dias + " días";
            }
        }

        String linea = String.format(
            "%s | %s | Cant: %d | Min: %d | Vence: %s (%s) | Frío: %s | Precio: $%.2f",
            p.getCodigo(),
            p.getNombre(),
            p.getCantidad(),
            p.getStockMinimo(),
            fechaStr,
            vencimientoStatus,
            (p.isRequiereFrio() ? "Sí" : "No"),
            p.getPrecio()
        );

        // Separator para destacar la línea (la línea completa en color verde para producto encontrado)
        System.out.println("\n----------------------------------------");
        // Colorizar secciones: código azul, nombre verde, resto en aqua
        String codigo = Colors.wrap(Colors.BRIGHT_BLUE, p.getCodigo());
        String nombre = Colors.wrap(Colors.BRIGHT_GREEN + Colors.BOLD, p.getNombre());
        String resto = Colors.wrap(Colors.AQUA, String.format("Cant: %d | Min: %d | Vence: %s (%s) | Frío: %s | Precio: $%.2f",
            p.getCantidad(), p.getStockMinimo(), fechaStr, vencimientoStatus, (p.isRequiereFrio() ? "Sí" : "No"), p.getPrecio()));
        System.out.println(Colors.wrap(Colors.GREEN + Colors.BOLD, codigo + " | " + nombre + " | " + resto));
        System.out.println("----------------------------------------");
    }

    // ============================================
    //    MÉTODOS DE PARSEO SEGURO
    // ============================================
    private int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private double parseDoubleSafe(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return 0.0;
        }
    }
}
