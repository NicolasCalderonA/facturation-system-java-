package com.sistemafact.modelo;

import java.time.LocalDate;
import java.util.List;

public class Compra {

    private int id;
    private LocalDate fecha;
    private double totalCosto;

    // Relaciones
    private Proveedor proveedor;
    private List<DetalleCompra> detalles;

    public Compra() {
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public double getTotalCosto() { return totalCosto; }
    public void setTotalCosto(double totalCosto) { this.totalCosto = totalCosto; }
    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }
    public List<DetalleCompra> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleCompra> detalles) { this.detalles = detalles; }
}