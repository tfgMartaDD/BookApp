package com.marta.bookapp;

import static com.marta.bookapp.BotonesComunes.cerrarSesion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminMenuActivity extends AppCompatActivity {

    Button peticionesBTN, donacionesBTN, prestamosBTN, librosBTN;
    Button cerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        librosBTN = findViewById(R.id.listalibros);
        librosBTN.setOnClickListener( (View v) -> {
            Intent in = new Intent (AdminMenuActivity.this, AdminLibrosActivity.class);
            startActivity(in);
        });

        peticionesBTN = findViewById(R.id.peticionesPendientes);
        peticionesBTN.setOnClickListener( (View v) -> {
            Intent in = new Intent (AdminMenuActivity.this, AdminPeticionesActivity.class);
            startActivity(in);
        });

        prestamosBTN = findViewById(R.id.prestamosActuales);
        prestamosBTN.setOnClickListener( (View v) -> {
            Intent in = new Intent (AdminMenuActivity.this, AdminPrestamosActivity.class);
            startActivity(in);
        });

        donacionesBTN = findViewById(R.id.donacionesAprobar);
        donacionesBTN.setOnClickListener( (View v) -> {
            Intent in = new Intent (AdminMenuActivity.this, AdminDonacionesActivity.class);
            startActivity(in);
        });


        cerrarSesion = findViewById(R.id.cerrarbutton);
        cerrarSesion.setOnClickListener( (View v) -> cerrarSesion(AdminMenuActivity.this));

    }
}