package ventas;
import java.time.LocalDate;
public class Venta {
    private LocalDate fecha;
    private String codigo;
    private String nombre;
    private int cantidad;
    private double total;

    public Venta(LocalDate fecha, String codigo, String nombre, int cantidad, double total) {
        this.fecha = fecha;
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.total = total;
    }

    public LocalDate getFecha() { return fecha; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    public double getTotal() { return total; }

    @Override
    public String toString() {
        return String.format("%s | %s x%d | Total: %.2f", fecha.toString(), nombre, cantidad, total);
    }
}
