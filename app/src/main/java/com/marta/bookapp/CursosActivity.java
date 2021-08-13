package com.marta.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CursosActivity extends AppCompatActivity {

    Button pripriBTN, segpriBTN, terpriBTN, cuapriBTN, quipriBTN, sextpriBTN;
    Button priesoBTN, segesoBTN, teresoBTN, cuaesoBTN;
    Button pribachBTN, segbachBTN;


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

        pripriBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirigir("primero","primaria");
            }
        });

        segpriBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirigir("segundo","primaria");
            }
        });

        terpriBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirigir("tercero","primaria");
            }
        });

        cuapriBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirigir("cuarto","primaria");
            }
        });

        quipriBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirigir("quinto","primaria");
            }
        });

        sextpriBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirigir("sexto","primaria");
            }
        });

        priesoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirigir("primero","eso");
            }
        });

        segesoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirigir("segundo","eso");
            }
        });

        teresoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirigir("tercero","eso");
            }
        });

        cuaesoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirigir("cuarto","eso");
            }
        });

        pribachBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirigir("primero","bachillerato");
            }
        });

        segbachBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirigir("segundo","bachillerato");
            }
        });


    }


    private void redirigir(String clase, String curso){

        Intent in = new Intent (this, ListadoDispActivity.class);
        in.putExtra("clase", clase);
        in.putExtra("curso", curso);
        startActivity(in);

    }
}