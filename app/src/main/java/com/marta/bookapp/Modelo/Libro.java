package com.marta.bookapp.Modelo;

import java.util.Date;

public class Libro {

    String id, asignatura, clase, curso, donante, editorial, estado;
    String imagen;
    String tipo;
    Date fecha;

    public Libro(String id, String asignatura, String clase, String curso, String donante, String editorial, String estado, String imagen) {
        this.id = id;
        this.asignatura = asignatura;
        this.clase = clase;
        this.curso = curso;
        this.donante = donante;
        this.editorial = editorial;
        this.estado = estado;
        this.imagen = imagen;
    }

    public Libro(String asignatura, String clase, String curso, String donante, String editorial, String estado, String imagen) {
        this.asignatura = asignatura;
        this.clase = clase;
        this.curso = curso;
        this.donante = donante;
        this.editorial = editorial;
        this.estado = estado;
        this.imagen = imagen;
    }

    public Libro(String id, String asignatura, String clase, String curso, String donante, String editorial, String estado, String imagen, String tipo) {
        this.id = id;
        this.asignatura = asignatura;
        this.clase = clase;
        this.curso = curso;
        this.donante = donante;
        this.editorial = editorial;
        this.estado = estado;
        this.imagen = imagen;
        this.tipo = tipo;
    }

    public Libro(String id, String asignatura, String clase, String curso, String donante, String editorial, String estado, String imagen, String tipo, Date fecha) {
        this.id = id;
        this.asignatura = asignatura;
        this.clase = clase;
        this.curso = curso;
        this.donante = donante;
        this.editorial = editorial;
        this.estado = estado;
        this.imagen = imagen;
        this.tipo = tipo;
        this.fecha = fecha;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
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

    public String getDonante() {
        return donante;
    }

    public void setDonante(String donante) {
        this.donante = donante;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
