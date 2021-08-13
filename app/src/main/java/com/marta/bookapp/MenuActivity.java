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

    }
}