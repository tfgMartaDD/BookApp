package com.marta.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminPrestamosActivity extends AppCompatActivity {

    PrestAdminAdapter adapter;
    ListView listViewAdminPrestamos;
    List<Prestamo> listaPrestamos = new ArrayList<>();

    Spinner spinner1;
    RadioButton rbu, rbc;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_prestamos);

        listViewAdminPrestamos = findViewById(R.id.lvPrestamosAdmin);

        rbu = findViewById(R.id.rbUsuario);
        rbc = findViewById(R.id.rbClase);

        spinner1 = findViewById(R.id.spinner1);

        if(rbu.isChecked()){
            System.out.println("user");
            listaPrestamos = obtenerPrestamosUsuarios();

        }else if(rbc.isChecked()){
            System.out.println("spinner");
            spinner1.setVisibility(View.VISIBLE);

            String [] opciones = {"PRIMERO PRIMARIA", "SEGUNDO PRIMARIA", "TERCERO PRIMARIA", "CUARTO PRIMARIA", "QUINTO PRIMARIA", "SEXTO PRIMARIA",
                    "PRIMERO ESO", "SEGUNDO ESO", "TERCERO ESO", "CUARTO ESO", "PRIMERO BACHILLERATO", "SEGUNDO BACHILLERATO"};

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
            spinner1.setAdapter(arrayAdapter);

            String clasecurso = spinner1.getSelectedItem().toString();

            listaPrestamos = obtenerPrestamosClases(clasecurso);
        }

        adapter = new PrestAdminAdapter(this, listaPrestamos);
        listViewAdminPrestamos.setAdapter(adapter);


    }


    public List<Prestamo> obtenerPrestamosUsuarios(){
        List<Prestamo> listaUsuarios = new ArrayList<>();

        return listaUsuarios;
    }

    public List<Prestamo> obtenerPrestamosClases(String clase){
        List<Prestamo> listaClases = new ArrayList<>();

        return listaClases;
    }
}