# Drogueria Modular - Proyecto
Proyecto modular Java para gestión de droguería.

Estructura:
- Main.java
- usuarios/ (Usuario, SistemaUsuarios)
- inventario/ (Producto, Inventario)
- ventas/ (Venta, HistorialVentas)

Cómo compilar:
from project root:
javac usuarios/*.java inventario/*.java ventas/*.java Main.java
java Main

Recomendación:
- Cada carpeta puede ser colocada en ramas separadas en git (usuarios, inventario, ventas).
