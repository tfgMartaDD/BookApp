package com.marta.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {

    Button datosBTN, cerrarSesion;
    Button peticionesBTN, prestamosBTN, donacionesBTN ,listaBTN;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");

        if (!(email.isEmpty())) {
            //Guardado de datos del usuario actual
            prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", email);
            editor.apply();
        }


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

        cerrarSesion = findViewById(R.id.cerrarbutton);
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Borrado de datos del usuario actual
                prefs  = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();

                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MenuActivity.this, "Sesión cerrada correctamente.", Toast.LENGTH_SHORT).show();
                redirigirAhome();
            }
        });

    }

    private void redirigirAhome() {
        Intent i = new Intent (this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}