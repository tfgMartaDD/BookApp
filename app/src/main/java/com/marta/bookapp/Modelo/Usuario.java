package com.marta.bookapp.Modelo;

public class Usuario {

    String id;
    String nombre;
    String apellido;
    String fotoPerfil;
    long donaciones;
    long prestamos;

    public Usuario(String id, String nombre, String apellido, long donaciones, long prestamos) {
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

    public long getDonaciones() {
        return donaciones;
    }

    public void setDonaciones(long donaciones) {
        this.donaciones = donaciones;
    }

    public long getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(long prestamos) {
        this.prestamos = prestamos;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}
