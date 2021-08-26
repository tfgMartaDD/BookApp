package com.marta.bookapp;


import static com.marta.bookapp.BotonesComunes.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CursosActivity extends AppCompatActivity {

    Button pripriBTN, segpriBTN, terpriBTN, cuapriBTN, quipriBTN, sextpriBTN;
    Button priesoBTN, segesoBTN, teresoBTN, cuaesoBTN;
    Button pribachBTN, segbachBTN;

    Button cerrarSesion, menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        pripriBTN = findViewById(R.id.primeroBoton);
        segpriBTN = findViewById(R.id.segundoBoton);
        terpriBTN = findViewById(R.id.terceroBoton);
        cuapriBTN = findViewById(R.id.cuartoBoton);
        quipriBTN = findViewById(R.id.quintoBoton);
        sextpriBTN = findViewById(R.id.sextoBoton);
        priesoBTN = findViewById(R.id.primeroesoBoton);
        segesoBTN = findViewById(R.id.segundoesoBoton);
        teresoBTN = findViewById(R.id.terceroesoBoton);
        cuaesoBTN = findViewById(R.id.cuartoesoBoton);
        pribachBTN = findViewById(R.id.primerobachBoton);
        segbachBTN = findViewById(R.id.segundobachBoton);

        pripriBTN.setOnClickListener( (View v) -> redirigir("primero","primaria"));

        segpriBTN.setOnClickListener( (View v) ->  redirigir("segundo","primaria"));

        terpriBTN.setOnClickListener( (View v) -> redirigir("tercero","primaria"));

        cuapriBTN.setOnClickListener( (View v) -> redirigir("cuarto","primaria") );

        quipriBTN.setOnClickListener( (View v) -> redirigir("quinto","primaria"));

        sextpriBTN.setOnClickListener( (View v) -> redirigir("sexto","primaria"));

        priesoBTN.setOnClickListener( (View v) -> redirigir("primero","eso"));

        segesoBTN.setOnClickListener( (View v) -> redirigir("segundo","eso"));

        teresoBTN.setOnClickListener( (View v) -> redirigir("tercero","eso"));

        cuaesoBTN.setOnClickListener( (View v) -> redirigir("cuarto","eso"));

        pribachBTN.setOnClickListener((View v) ->  redirigir("primero","bachillerato"));

        segbachBTN.setOnClickListener( (View v) ->  redirigir("segundo","bachillerato"));

        cerrarSesion = findViewById(R.id.cerrarCursos);
        cerrarSesion.setOnClickListener( (View v) -> cerrarSesion(CursosActivity.this));

        menu = findViewById(R.id.menuCursos);
        menu.setOnClickListener( (View v) ->  volverAMenu(CursosActivity.this));

    }

    private void redirigir(String clase, String curso){

        Intent in = new Intent (this, ListadoDispActivity.class);
        in.putExtra("clase", clase);
        in.putExtra("curso", curso);
        startActivity(in);

    }
}