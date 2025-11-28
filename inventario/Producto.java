package inventario;//acomodar el orden del menu

import java.time.LocalDate;

public class Producto {

    private String codigo;
    private String nombre;
    private int cantidad;
    private int stockMinimo;
    private LocalDate fechaVencimiento;
    private boolean requiereFrio;
    private double precio;

    public Producto(String codigo, String nombre, int cantidad, int stockMinimo,
                    LocalDate fechaVencimiento, boolean requiereFrio, double precio) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.stockMinimo = stockMinimo;
        this.fechaVencimiento = fechaVencimiento;
        this.requiereFrio = requiereFrio;
        this.precio = precio;
    }

    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    public int getStockMinimo() { return stockMinimo; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public boolean isRequiereFrio() { return requiereFrio; }
    public double getPrecio() { return precio; }
    public String getCodigo() { return codigo; }


    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public void setRequiereFrio(boolean requiereFrio) { this.requiereFrio = requiereFrio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public boolean bajoStock() {
        return cantidad <= stockMinimo;
    }

    public boolean estaPorVencer() {
        if (fechaVencimiento == null) return false;
        return fechaVencimiento.minusDays(10).isBefore(LocalDate.now());
    }

    public boolean estaVencido() {
        if (fechaVencimiento == null) return false;
        return fechaVencimiento.isBefore(LocalDate.now());
    }

    // ---------------------------------------------------
    // FORMATO DE IMPRESIÓN
    // ---------------------------------------------------
    
    public String toString() {
        return String.format(
            "%-8s |Nombre: %-25s | Cant: %-4d | Mínimo: %-4d | Vence: %-12s | Frío: %-3s |Precio: $%.2f",
            codigo,
            nombre,
            cantidad,
            stockMinimo,
            fechaVencimiento,
            (requiereFrio ? "Sí" : "No"),
            precio
        );
    }
}
