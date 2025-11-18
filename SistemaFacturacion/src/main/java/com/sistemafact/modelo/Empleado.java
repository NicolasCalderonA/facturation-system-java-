package com.sistemafact.modelo;

public class Empleado extends Persona {

    private String legajo;

    public Empleado() {
        super();
    }

    public Empleado(int id, String nombre, String dni, String direccion, String legajo) {
        super(id, nombre, dni, direccion);
        this.legajo = legajo;
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }
}