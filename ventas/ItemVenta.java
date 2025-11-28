package ventas;

public class ItemVenta {

    private String codigo;
    private String nombre;
    private int cantidad;
    private double subtotal;

    public ItemVenta(String codigo, String nombre, int cantidad, double subtotal) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    public double getSubtotal() { return subtotal; }

    
    public String toString() {
        return String.format(
                "  - %s (%s) x%d  →  Subtotal: $%.2f",
                nombre, codigo, cantidad, subtotal
        );
    }
}
