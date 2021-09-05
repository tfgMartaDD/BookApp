package com.marta.bookapp.Modelo;

public class Pendientes {

    String id;
    Boolean esPeticion;
    String asig;
    String clase;
    String curso;
    String estado;
    String usuario;

    public Pendientes(String id, Boolean esPeticion, String asig, String clase, String curso, String estado, String usuario) {
        this.id = id;
        this.esPeticion = esPeticion;
        this.asig = asig;
        this.clase = clase;
        this.curso = curso;
        this.estado = estado;
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getEsPeticion() {
        return esPeticion;
    }

    public void setEsPeticion(Boolean esPeticion) {
        this.esPeticion = esPeticion;
    }

    public String getAsig() {
        return asig;
    }

    public void setAsig(String asig) {
        this.asig = asig;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
