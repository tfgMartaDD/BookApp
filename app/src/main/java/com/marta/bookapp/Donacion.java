package com.marta.bookapp;

import java.util.Date;

public class Donacion {
    String id;
    String emailUsuario;
    String idLibro;
    //Date fecha;
    String fecha;

    //public Donacion(String id, String emailUsuario, String idLibro, Date fecha) {
    public Donacion(String id, String emailUsuario, String idLibro, String fecha) {
        this.id = id;
        this.emailUsuario = emailUsuario;
        this.idLibro = idLibro;
        this.fecha = fecha;
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

    public String getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(String idLibro) {
        this.idLibro = idLibro;
    }

    /*public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }*/

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
