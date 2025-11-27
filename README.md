# Proyecto en Java para Gestión de Droguería

Este proyecto implementa un sistema modular en Java para administrar usuarios, productos, inventario y ventas dentro de una droguería.
La arquitectura se organiza por paquetes para mantener un código limpio, escalable y fácil de mantener.

---

##  Estructura del Proyecto

```
src/
 ├── Main.java
 ├── usuarios/
 │     ├── Usuario.java
 │     └── SistemaUsuarios.java
 ├── inventario/
 │     ├── Producto.java
 │     └── Inventario.java
 └── ventas/
       ├── Venta.java
       └── HistorialVentas.java
```

---

##  Funcionalidades

### ✔ Gestión de Usuarios

* Registro y autenticación de usuarios
* Manejo de roles

### ✔ Gestión de Inventario

* Agregar, modificar y eliminar productos
* Control de existencias

### ✔ Registro de Ventas

* Creación de ventas
* Historial y reporte básico

---

## 🛠 Cómo Compilar y Ejecutar

Desde la raíz del proyecto:

### 1. Compilar

```bash
javac src/usuarios/*.java src/inventario/*.java src/ventas/*.java src/Main.java
```

### 2. Ejecutar

```bash
java -cp src Main
```

> Si usas un IDE como IntelliJ o VS Code, basta con abrir la carpeta `src` como proyecto Java y ejecutar `Main.java`.

---

## Requisitos

* Java 17+ (puedes ajustar según tu versión)
* Consola o terminal para compilación manual

---
