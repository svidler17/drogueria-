package ventas;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class HistorialVentas {
    private List<Venta> ventas = new ArrayList<>();
    private HashMap<String,Integer> contador = new HashMap<>();

    public void registrarVenta(Venta v) {
        ventas.add(v);
        contador.putIfAbsent(v.getCodigo(), 0);
        contador.put(v.getCodigo(), contador.get(v.getCodigo()) + v.getCantidad());
    }

    public void mostrarHistorial() {
        if (ventas.isEmpty()) { System.out.println("No hay ventas."); return; }
        System.out.println("Historial de ventas:");
        for (Venta v : ventas) System.out.println(" - " + v);
    }

    public String masVendido() {
        if (contador.isEmpty()) return null;
        String best = null;
        int max = Integer.MIN_VALUE;
        for (var e : contador.entrySet()) {
            if (e.getValue() > max) { max = e.getValue(); best = e.getKey(); }
        }
        return best;
    }

    public String menosVendido() {
        if (contador.isEmpty()) return null;
        String worst = null;
        int min = Integer.MAX_VALUE;
        for (var e : contador.entrySet()) {
            if (e.getValue() < min) { min = e.getValue(); worst = e.getKey(); }
        }
        return worst;
    }
}
