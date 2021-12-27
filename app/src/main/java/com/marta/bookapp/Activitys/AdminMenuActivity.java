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

public class AdminMenuActivity extends AppCompatActivity {

    Button peticionesBTN, donacionesBTN, prestamosBTN, librosBTN;
    Button cerrarSesion, scannerBTN;
    Button cambiarBTN, listaBTN;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);

        findViewById(R.id.fondoAdminMenu).getBackground().mutate().setAlpha(80);


        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");

        if (!(email.isEmpty())) {
            //Guardado de datos del usuario actual
            prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", email);
            editor.apply();
        }

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


        cerrarSesion = findViewById(R.id.cerraradmin);
        cerrarSesion.setOnClickListener( (View v) -> cerrarSesion(AdminMenuActivity.this));

        scannerBTN = findViewById(R.id.scannerbutton);
        scannerBTN.setOnClickListener( (View v) -> {
            Intent in = new Intent (AdminMenuActivity.this, EscaneoActivity.class);
            startActivity(in);
        });

        cambiarBTN = findViewById(R.id.cambiarBTN);
        cambiarBTN.setOnClickListener( (View v) -> {
            Intent in = new Intent (this, MenuActivity.class);
            in.putExtra("email",email);
            startActivity(in);
        });

        listaBTN = findViewById(R.id.listaUsersBTN);
        listaBTN.setOnClickListener( (View v) -> {
            Intent in = new Intent (AdminMenuActivity.this, AdminUsuariosActivity.class);
            startActivity(in);
        });

    }
}