package com.sistemafact.modelo;

import java.time.LocalDate;
import java.util.List;

public class Factura {

    private int id;
    private LocalDate fecha;
    private double total;

    // --- Relaciones con otras clases (Composición) ---
    private Cliente cliente;
    private Empleado empleado;
    private List<DetalleFactura> detalles; // Relación 1 a N

    // Constructores
    public Factura() {
    }

    public Factura(int id, LocalDate fecha, double total, Cliente cliente, Empleado empleado) {
        this.id = id;
        this.fecha = fecha;
        this.total = total;
        this.cliente = cliente;
        this.empleado = empleado;
    }

    // --- Getters y Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles = detalles;
    }
}