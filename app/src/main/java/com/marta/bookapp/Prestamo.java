package com.marta.bookapp;

import java.util.Date;

public class Prestamo {
    String id;
    Libro libro;
    String usuario;
    Date fecha;
    Date fechaDev;

    public Prestamo(String id, Libro libro, String usuario, Date fecha, Date fechaDev) {
        this.id = id;
        this.libro = libro;
        this.usuario = usuario;
        this.fecha = fecha;
        this.fechaDev = fechaDev;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaDev() {
        return fechaDev;
    }

    public void setFechaDev(Date fechaDev) {
        this.fechaDev = fechaDev;
    }
}
