package com.sistemafact.modelo;

public class DetalleFactura {

    private int id;
    private int cantidad;
    private double precioUnitario;

    private Articulo articulo;

    public DetalleFactura() {
    }

    public DetalleFactura(int id, int cantidad, double precioUnitario, Articulo articulo) {
        this.id = id;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.articulo = articulo;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
    public Articulo getArticulo() { return articulo; }
    public void setArticulo(Articulo articulo) { this.articulo = articulo; }
}