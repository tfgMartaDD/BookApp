package com.marta.bookapp.Modelo;

import java.util.Date;

/**
 * CLase correspondiente a las donaciones y las peticiones
 * y los datos que nos interesa almacenar
 *
 * @author Marta Diego u158691@usal.es
 */
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
