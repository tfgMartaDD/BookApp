package com.marta.bookapp;

import java.util.Date;

public class DonacionPeticion {
    String id;
    String emailUsuario;
    Libro libro;
    Date fecha;
    String idPendiente;


    public DonacionPeticion(String id, String emailUsuario, Libro libro, Date fecha, String idPendiente) {
        this.id = id;
        this.emailUsuario = emailUsuario;
        this.libro = libro;
        this.fecha = fecha;
        this.idPendiente = idPendiente;
    }

    public String getId() {
        return id;
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

    public String getIdPendiente() {
        return idPendiente;
    }

    public void setIdPendiente(String idPendiente) {
        this.idPendiente = idPendiente;
    }
}
