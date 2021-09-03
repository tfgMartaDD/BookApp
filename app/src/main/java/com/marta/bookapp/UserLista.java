package com.marta.bookapp;

public class UserLista {

    String id;
    String nombre;
    String apellido;
    int donaciones;
    int prestamos;

    public UserLista(String id, String nombre, String apellido, int donaciones, int prestamos) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.donaciones = donaciones;
        this.prestamos = prestamos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getDonaciones() {
        return donaciones;
    }

    public void setDonaciones(int donaciones) {
        this.donaciones = donaciones;
    }

    public int getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(int prestamos) {
        this.prestamos = prestamos;
    }
}
