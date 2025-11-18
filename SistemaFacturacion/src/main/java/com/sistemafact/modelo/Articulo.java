package com.sistemafact.modelo;

public class Articulo {

    private int id;
    private String nombre;
    private String descripcion;
    private double precioVenta;
    private int stock;

    // Constructores
    public Articulo() {
    }

    public Articulo(int id, String nombre, String descripcion, double precioVenta, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioVenta = precioVenta;
        this.stock = stock;
    }

    // --- Getters y Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}