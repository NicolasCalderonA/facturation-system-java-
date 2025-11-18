package com.sistemafact.modelo;

public class Cliente extends Persona {

    private String email;

    public Cliente() {
        super();
    }

    public Cliente(int id, String nombre, String dni, String direccion, String email) {
        super(id, nombre, dni, direccion);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}