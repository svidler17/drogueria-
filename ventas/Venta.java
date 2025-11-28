package ventas;

import java.time.LocalDate;
import java.util.ArrayList;

public class Venta {
    private LocalDate fecha;
    private ArrayList<ItemVenta> items;
    private double total;

    public Venta(LocalDate fecha, ArrayList<ItemVenta> items, double total) {
        this.fecha = fecha;
        this.items = items;
        this.total = total;
    }

    public LocalDate getFecha() { return fecha; }
    public ArrayList<ItemVenta> getItems() { return items; }
    public double getTotal() { return total; }

    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== VENTA ===\n");
        sb.append("Fecha: ").append(fecha).append("\n");
        sb.append("Productos:\n");

        for (ItemVenta item : items) {
            sb.append(item.toString()).append("\n");
        }

        sb.append("-----------------------------\n");
        sb.append(String.format("TOTAL: $%.2f\n", total));

        return sb.toString();
    }
}
