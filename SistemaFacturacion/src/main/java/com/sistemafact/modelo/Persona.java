package com.sistemafact.modelo;

public abstract class Persona {

    protected int id;
    protected String nombre;
    protected String dni;
    protected String direccion;

    public Persona() {
    }

    public Persona(int id, String nombre, String dni, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.direccion = direccion;
    }

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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}