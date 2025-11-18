package com.sistemafact.modelo;

public class DetalleCompra {

    private int id;
    private int cantidad;
    private double precioCosto;

    // Relaciones
    private Articulo articulo;

    public DetalleCompra() {
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecioCosto() { return precioCosto; }
    public void setPrecioCosto(double precioCosto) { this.precioCosto = precioCosto; }
    public Articulo getArticulo() { return articulo; }
    public void setArticulo(Articulo articulo) { this.articulo = articulo; }
}