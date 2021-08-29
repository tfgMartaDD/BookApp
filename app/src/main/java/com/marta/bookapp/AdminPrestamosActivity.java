package com.marta.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminPrestamosActivity extends AppCompatActivity {

    PrestAdminAdapter adapter;
    ListView listViewAdminPrestamos;
    List<Prestamo> listaPrestamos = new ArrayList<>();

    Spinner spinner1;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_prestamos);

        listaPrestamos = obtenerPrestamosUsuarios();
        listViewAdminPrestamos = findViewById(R.id.lvPrestamosAdmin);

        spinner1 = findViewById(R.id.spinner1);


        String [] opciones2 = {"PRIMERO PRIMARIA", "SEGUNDO PRIMARIA", "TERCERO PRIMARIA", "CUARTO PRIMARIA", "QUINTO PRIMARIA", "SEXTO PRIMARIA",
        "PRIMERO ESO", "SEGUNDO ESO", "TERCERO ESO", "CUARTO ESO", "PRIMERO BACHILLERATO", "SEGUNDO BACHILLERATO"};


    }


    public List<Prestamo> obtenerPrestamosUsuarios(){
        List<Prestamo> lista = new ArrayList<>();

        return lista;
    }

    public List<Prestamo> obtenerPrestamosClases(){
        List<Prestamo> lista = new ArrayList<>();

        return lista;
    }
}