package com.marta.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class NuevaDonacionActivity extends AppCompatActivity {

    Spinner asigSpin;
    Spinner claseSpin;
    Spinner cursoSpin;
    Spinner editorialSpin;
    TextView libroTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_donacion);

        libroTextView = findViewById(R.id.libroTV);

        asigSpin = findViewById(R.id.asigSpinner);
        claseSpin = findViewById(R.id.claseSpinner);
        cursoSpin = findViewById(R.id.cursoSpinner);
        editorialSpin = findViewById(R.id.editorialSpinner);

        String [] asignaturas = {"MATEMATICAS", "LENGUA", "BIOLOGIA", "SOCIALES", "INGLES"};
        ArrayAdapter<String> asigAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, asignaturas);
        asigSpin.setAdapter(asigAdapter);

        String [] clases = {"primero","segundo","tercero","cuarto", "quinto", "sexto"};
        ArrayAdapter<String> claseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, clases);
        claseSpin.setAdapter(claseAdapter);

        String [] cursos = {"primaria","eso", "bachillerato"};
        ArrayAdapter<String> cursoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cursos);
        cursoSpin.setAdapter(cursoAdapter);

    }
}