package com.marta.bookapp.Activitys;

import static com.marta.bookapp.BotonesComunes.cerrarSesion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.marta.bookapp.R;

public class MenuActivity extends AppCompatActivity {

    Button datosBTN, cerrarSesion, pendientesBTN;
    Button peticionesBTN, prestamosBTN, donacionesBTN ,listaBTN;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

       findViewById(R.id.fondoMenu).getBackground().mutate().setAlpha(80);


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
        datosBTN.setOnClickListener( (View v) -> {
            Intent i = new Intent (MenuActivity.this, DatosPersonalesActivity.class);
            startActivity(i);
        });

        listaBTN = findViewById(R.id.listaButton);
        listaBTN.setOnClickListener( (View v) -> {
            Intent in = new Intent (MenuActivity.this, CursosActivity.class);
            startActivity(in);
        });

        peticionesBTN = findViewById(R.id.peticionesButton);
        peticionesBTN.setOnClickListener( (View v) -> {
            Intent in = new Intent (MenuActivity.this, PeticionesActivity.class);
            startActivity(in);
        });

        prestamosBTN = findViewById(R.id.prestamosButton);
        prestamosBTN.setOnClickListener( (View v) -> {
            Intent in = new Intent (MenuActivity.this, PrestamosActivity.class);
            startActivity(in);
        });

        donacionesBTN = findViewById(R.id.donacionesButton);
        donacionesBTN.setOnClickListener( (View v) -> {
            Intent in = new Intent (MenuActivity.this, DonacionesActivity.class);
            startActivity(in);
        });

        cerrarSesion = findViewById(R.id.cerrarbutton);
        cerrarSesion.setOnClickListener( (View v) -> cerrarSesion(MenuActivity.this));

        pendientesBTN = findViewById(R.id.estadoButton);
        pendientesBTN.setOnClickListener( (View v) -> {
            Intent in = new Intent (MenuActivity.this, EstadoPendientesActivity.class);
            startActivity(in);
        });
    }

}