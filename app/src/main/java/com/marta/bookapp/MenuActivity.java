package com.marta.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    Button datosBTN, peticionesBTN, prestamosBTN, donacionesBTN ,listaBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        datosBTN = findViewById(R.id.datosButton);
        datosBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent (MenuActivity.this, DatosPersonalesActivity.class);
                startActivity(i);
            }
        });

        listaBTN = findViewById(R.id.listaButton);
        listaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent (MenuActivity.this, CursosActivity.class);
                startActivity(in);
            }
        });

        peticionesBTN = findViewById(R.id.peticionesButton);
        peticionesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent (MenuActivity.this, PeticionesActivity.class);
                startActivity(in);
            }
        });

        prestamosBTN = findViewById(R.id.prestamosButton);
        prestamosBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent (MenuActivity.this, PrestamosActivity.class);
                startActivity(in);
            }
        });

        donacionesBTN = findViewById(R.id.donacionesButton);
        donacionesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent (MenuActivity.this, DonacionesActivity.class);
                startActivity(in);
            }
        });

    }
}