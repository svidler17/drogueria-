package inventario;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Inventario {
    private ArrayList<Producto> productos = new ArrayList<>();

    public Inventario() {}

    public void cargarPredeterminado() {
        productos.add(new Producto("P001","Paracetamol 500mg",50,10,LocalDate.now().plusMonths(8),false,1200.0));
        productos.add(new Producto("P002","Ibuprofeno 400mg",30,8,LocalDate.now().plusMonths(6),false,1500.0));
        productos.add(new Producto("P003","Insulina (Frío)",20,5,LocalDate.now().plusMonths(4),true,45000.0));
        productos.add(new Producto("P004","Antibiótico X 250mg",15,5,LocalDate.now().plusDays(10),false,8000.0));
    }

    public Producto buscarPorCodigo(String codigo) {
        for (Producto p : productos) if (p.getCodigo().equalsIgnoreCase(codigo)) return p;
        return null;
    }

    public void listar() {
        System.out.println("Inventario:");
        for (Producto p : productos) System.out.println(" - " + p);
    }

    public void mostrarAlertas() {
        System.out.println("--- ALERTAS ---");
        boolean any = false;
        for (Producto p : productos) {
            if (p.bajoStock()) { any = true; System.out.println("[BAJO STOCK] " + p.getCodigo() + " " + p.getNombre() + " Cant:" + p.getCantidad()); }
            if (p.estaPorVencer()) { any = true; System.out.println("[POR VENCER] " + p.getCodigo() + " " + p.getNombre() + " Vence:" + p.getFechaVencimiento()); }
            if (p.estaVencido()) { any = true; System.out.println("[VENCIDO] " + p.getCodigo() + " " + p.getNombre()); }
            if (p.isRequiereFrio()) { any = true; System.out.println("[FRIO] " + p.getCodigo() + " " + p.getNombre()); }
        }
        if (!any) System.out.println("Sin alertas.");
    }

    // Menus for web editors using scanner.nextLine()
    public void menuAgregar(Scanner scanner) {
        System.out.print("Código: ");
        String codigo = scanner.nextLine().trim();
        if (buscarPorCodigo(codigo) != null) { System.out.println("Código existente."); return; }
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Cantidad: ");
        int cant = parseIntSafe(scanner.nextLine());
        System.out.print("Stock mínimo: ");
        int min = parseIntSafe(scanner.nextLine());
        System.out.print("Fecha vencimiento (AAAA-MM-DD): ");
        java.time.LocalDate fecha = java.time.LocalDate.parse(scanner.nextLine());
        System.out.print("Requiere frio? (s/n): ");
        boolean frio = scanner.nextLine().equalsIgnoreCase("s");
        System.out.print("Precio unitario: ");
        double precio = parseDoubleSafe(scanner.nextLine());
        productos.add(new Producto(codigo,nombre,cant,min,fecha,frio,precio));
        System.out.println("Producto agregado.");
    }

    public void menuEditar(Scanner scanner) {
        System.out.print("Código a editar: ");
        String codigo = scanner.nextLine();
        Producto p = buscarPorCodigo(codigo);
        if (p == null) { System.out.println("No existe."); return; }
        System.out.println("Dejar vacío para mantener valor.");
        System.out.print("Nuevo nombre: ");
        String n = scanner.nextLine(); if (!n.isBlank()) p.setNombre(n);
        System.out.print("Nueva cantidad: "); String s = scanner.nextLine(); if (!s.isBlank()) p.setCantidad(parseIntSafe(s));
        System.out.print("Nuevo stock mínimo: "); s = scanner.nextLine(); if (!s.isBlank()) p.setStockMinimo(parseIntSafe(s));
        System.out.print("Nueva fecha vencimiento (AAAA-MM-DD): "); s = scanner.nextLine(); if (!s.isBlank()) p.setFechaVencimiento(java.time.LocalDate.parse(s));
        System.out.print("Requiere frio? (s/n): "); s = scanner.nextLine(); if (!s.isBlank()) p.setRequiereFrio(s.equalsIgnoreCase("s"));
        System.out.print("Nuevo precio unitario: "); s = scanner.nextLine(); if (!s.isBlank()) p.setPrecio(parseDoubleSafe(s));
        System.out.println("Producto actualizado.");
    }
    
    public void menuQuitar(Scanner scanner) {
        System.out.print("Código a quitar: ");
        String codigo = scanner.nextLine();
        Iterator<Producto> it = productos.iterator();
        boolean removed = false;
        while (it.hasNext()) {
            if (it.next().getCodigo().equalsIgnoreCase(codigo)) { it.remove(); removed = true; }
        }
        System.out.println(removed ? "Producto eliminado." : "Producto no encontrado.");
    }

    private int parseIntSafe(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }
    private double parseDoubleSafe(String s) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return 0.0; }

    
    }

    public void mostrarAlertas() {
    System.out.println("\n=== ALERTAS DEL INVENTARIO ===");

    boolean hayAlertas = false;

    for (Producto p : productos.values()) {

        // SIN STOCK
        if (p.cantidad == 0) {
            System.out.println("❌ " + p.nombre + " — SIN STOCK");
            hayAlertas = true;
        }

        // STOCK BAJO
        else if (p.cantidad <= p.stock_minimo) {
            System.out.println("⚠️ " + p.nombre + " — Stock bajo (" + p.cantidad + ")");
            hayAlertas = true;
        }

        // PRODUCTO VENCIDO (solo si lo manejas)
        if (p.fechaVencimiento != null && p.estaVencido()) {
            System.out.println("⛔ " + p.nombre + " — PRODUCTO VENCIDO");
            hayAlertas = true;
        }
    }

    if (!hayAlertas) {
        System.out.println("✔ No hay alertas. Todo está en orden.");
    }
}

}

