package inventario;
import java.time.LocalDate;

public class Producto {
    private String codigo;
    private String nombre;
    private int cantidad;
    private int stockMinimo;
    private LocalDate fechaVencimiento;
    private boolean requiereFrio;
    private double precio;

    public Producto(String codigo, String nombre, int cantidad, int stockMinimo, LocalDate fechaVencimiento, boolean requiereFrio, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.stockMinimo = stockMinimo;
        this.fechaVencimiento = fechaVencimiento;
        this.requiereFrio = requiereFrio;
        this.precio = precio;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    public int getStockMinimo() { return stockMinimo; }
    public java.time.LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public boolean isRequiereFrio() { return requiereFrio; }
    public double getPrecio() { return precio; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }
    public void setFechaVencimiento(LocalDate fecha) { this.fechaVencimiento = fecha; }
    public void setRequiereFrio(boolean requiereFrio) { this.requiereFrio = requiereFrio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public boolean estaVencido() { return LocalDate.now().isAfter(fechaVencimiento); }
    public boolean estaPorVencer() { return !estaVencido() && fechaVencimiento.minusDays(7).isBefore(LocalDate.now().plusDays(1)); }
    public boolean bajoStock() { return cantidad <= stockMinimo; }

    @Override
    public String toString() {
        return String.format("%s | Cant:%d | Min:%d | Vence:%s | Precio: %.2f", codigo, cantidad, stockMinimo, fechaVencimiento.toString(), precio);
    }
}
