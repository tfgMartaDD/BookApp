package com.marta.bookapp;

import java.util.Date;

public class Peticion {

    String id;
    Libro libro;
    String emailUsuario;
    Date fecha;

    public Peticion(String id, String emailUsuario, Libro libro, Date fecha) {
        this.id = id;
        this.emailUsuario = emailUsuario;
        this.libro = libro;
        this.fecha = fecha;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
