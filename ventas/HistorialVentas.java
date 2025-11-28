package ventas;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class HistorialVentas {

    private List<Venta> ventas = new ArrayList<>();

    public void registrarVenta(Venta venta) {
        ventas.add(venta);
    }

    public void mostrarHistorial() {
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        for (Venta v : ventas) {
            System.out.println("\n=== VENTA ===");
            System.out.println(v);
        }
    }

    // ============================================================
    //  OBTENER PRODUCTO MÁS VENDIDO (solo código)
    // ============================================================
    public String masVendido() {
        if (ventas.isEmpty()) return null;

        Map<String, Integer> mapa = generarMapaVentas();

        String maxCod = null;
        int maxCant = -1;

        for (var e : mapa.entrySet()) {
            if (e.getValue() > maxCant) {
                maxCant = e.getValue();
                maxCod = e.getKey();
            }
        }

        return maxCod;
    }

    // ============================================================
    //  OBTENER PRODUCTO MENOS VENDIDO (solo código)
    // ============================================================
    public String menosVendido() {
        if (ventas.isEmpty()) return null;

        Map<String, Integer> mapa = generarMapaVentas();

        String minCod = null;
        int minCant = Integer.MAX_VALUE;

        for (var e : mapa.entrySet()) {
            if (e.getValue() < minCant) {
                minCant = e.getValue();
                minCod = e.getKey();
            }
        }

        return minCod;
    }

    // ============================================================
    //   GENERAR MAPA (código → cantidad vendida total)
    // ============================================================
    private Map<String, Integer> generarMapaVentas() {
        Map<String, Integer> mapa = new HashMap<>();

        for (Venta v : ventas) {
            for (ItemVenta i : v.getItems()) {
                mapa.put(i.getCodigo(), mapa.getOrDefault(i.getCodigo(), 0) + i.getCantidad());
            }
        }

        return mapa;
    }

    // ============================================================
    //   REPORTE BONITO EN BARRAS CON COLORES ANSI
    // ============================================================
    public void mostrarReporteBarras(Map<String, String> nombresProductos) {

        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        Map<String, Integer> mapa = generarMapaVentas();

        int max = Collections.max(mapa.values());
        int min = Collections.min(mapa.values());

        System.out.println("\n===== REPORTE DE PRODUCTOS VENDIDOS =====\n");

        for (String codigo : mapa.keySet()) {
            int cantidad = mapa.get(codigo);
            String nombre = nombresProductos.get(codigo);

            if (nombre == null) nombre = "Desconocido";

            // Selección de color
            String color;
            if (cantidad == max) {
                color = "\u001B[32m"; // verde
            } else if (cantidad == min) {
                color = "\u001B[31m"; // rojo
            } else {
                color = "\u001B[33m"; // amarillo
            }

            String barras = "█".repeat(Math.max(1, cantidad));

            System.out.printf(
                "%s%-20s | %3d ventas | %s%s\u001B[0m\n",
                color, nombre, cantidad, color, barras
            );
        }

        System.out.println();
    }
}
