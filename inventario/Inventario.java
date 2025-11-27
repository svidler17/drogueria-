package inventario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Inventario {

    private ArrayList<Producto> productos = new ArrayList<>();

    public Inventario() {}

    // ============================================
    //        CARGAR PRODUCTOS PREDETERMINADOS
    // ============================================
    public void cargarPredeterminado() {
        productos.add(new Producto("P001","Paracetamol 500mg",50,10, LocalDate.now().plusMonths(8), false, 1200.0));
        productos.add(new Producto("P002","Ibuprofeno 400mg",30,8, LocalDate.now().plusMonths(6), false, 1500.0));
        productos.add(new Producto("P003","Insulina (Frío)",0,5, LocalDate.now().plusMonths(4), true,45000.0));
        productos.add(new Producto("P004","Antibiótico X 250mg",15,5, LocalDate.now().plusDays(10),false,8000.0));
        productos.add(new Producto("P005","Omeprazol 20mg",40,10,LocalDate.now().plusMonths(2),false,1800.0));
        // ===============================
        //       NUEVOS PRODUCTOS
        // ===============================

        productos.add(new Producto("P006","Vitamina C 1g",40,10, LocalDate.now().plusMonths(12), false, 2500.0));
        productos.add(new Producto("P007","Omeprazol 20mg",60,15, LocalDate.now().plusMonths(10), false, 1800.0));
        productos.add(new Producto("P008","Suero Oral",25,5, LocalDate.now().plusMonths(5), false, 3000.0));

        productos.add(new Producto("P009","Gel antibacterial 250ml",3,5, LocalDate.now().plusDays(20), false, 5000.0));
        productos.add(new Producto("P010","Jarabe para tos infantil",10,5, LocalDate.now().plusDays(3), false, 7000.0));
        productos.add(new Producto("P011","Amoxicilina 500mg",0,5, LocalDate.now().plusMonths(2), false, 3500.0));
    }

    // ============================================
    //        REORDENAR CÓDIGOS AUTOMÁTICAMENTE
    // ============================================
    private void reordenarCodigos() {
        for (int i = 0; i < productos.size(); i++) {
            String codigoNuevo = String.format("P%03d", i + 1);
            productos.get(i).setCodigo(codigoNuevo);
        }
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
    //                LISTAR
    // ============================================
    public void listar() {
        System.out.println("Inventario:");
        for (Producto p : productos) {
            System.out.println(" - " + p);
        }
    }

    // ============================================
    //        MOSTRAR ALERTAS
    // ============================================
    public void mostrarAlertas() {
        System.out.println("\n=== ALERTAS DEL INVENTARIO ===");

        boolean hayAlertas = false;

        for (Producto p : productos) {

            if (p.getCantidad() == 0) {
                System.out.println(" " + p.getNombre() + " — SIN STOCK");
                hayAlertas = true;
            }

            else if (p.getCantidad() <= p.getStockMinimo()) {
                System.out.println(" " + p.getNombre() + " — Stock bajo (" + p.getCantidad() + ")");
                hayAlertas = true;
            }

            if (p.getFechaVencimiento() != null && p.estaVencido()) {
                System.out.println(" " + p.getNombre() + " — PRODUCTO VENCIDO");
                hayAlertas = true;
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

        System.out.println("\n=== AGREGAR PRODUCTO ===");

        String codigo = generarCodigo();
        System.out.println("Código asignado automáticamente: " + codigo);

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Cantidad: ");
        int cant = parseIntSafe(scanner.nextLine());

        System.out.print("Stock mínimo: ");
        int min = parseIntSafe(scanner.nextLine());

        System.out.print("Fecha vencimiento (AAAA-MM-DD): ");
        LocalDate fecha = LocalDate.parse(scanner.nextLine());

        System.out.print("¿Requiere frío? (s/n): ");
        boolean frio = scanner.nextLine().equalsIgnoreCase("s");

        System.out.print("Precio unitario: ");
        double precio = parseDoubleSafe(scanner.nextLine());

        productos.add(new Producto(codigo, nombre, cant, min, fecha, frio, precio));

        reordenarCodigos(); // REORDENAR AUTOMÁTICO

        System.out.println("Producto agregado correctamente.");
    }

    // ============================================
    //                  EDITAR
    // ============================================
    public void menuEditar(Scanner scanner) {
        System.out.print("Código a editar: ");
        String codigo = scanner.nextLine();
        Producto p = buscarPorCodigo(codigo);

        if (p == null) {
            System.out.println("No existe.");
            return;
        }

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

        System.out.print("Código a quitar: ");
        String codigo = scanner.nextLine();

        Iterator<Producto> it = productos.iterator();
        boolean removed = false;

        while (it.hasNext()) {
            if (it.next().getCodigo().equalsIgnoreCase(codigo)) {
                it.remove();
                removed = true;
            }
        }

        if (removed) {
            reordenarCodigos(); // REORDENAR AUTOMÁTICO AL ELIMINAR
        }

        System.out.println(removed ? "Producto eliminado." : "Producto no encontrado.");
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
