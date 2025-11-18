package com.sistemafact.modelo;

public class Proveedor extends Persona {

    private String cuit;
    private String telefono;

    public Proveedor() {
        super();
    }

    public Proveedor(int id, String nombre, String dni, String direccion, String cuit, String telefono) {
        super(id, nombre, dni, direccion);
        this.cuit = cuit;
        this.telefono = telefono;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}